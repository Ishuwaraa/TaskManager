import { HttpErrorResponse, HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';

import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
  
export const authInterceptor: HttpInterceptorFn = (
    req: HttpRequest<any>,
    next: HttpHandlerFn
): Observable<HttpEvent<any>> => {
    const authService = inject(AuthService);
    const router = inject(Router);
  
    //cloning req to send the cookie
    let request = req.clone({
      withCredentials: true
    });
  
    //skipping auth for non protected routes
    if (shouldSkipAuth(request.url)) {
      return next(request);
    }
  
    //adding token to req header
    const accessToken = localStorage.getItem('accessToken');
    if (accessToken) {
      request = addTokenHeader(request, accessToken);
    }
  
    return next(request).pipe(
      catchError((error) => {
        //checking if the error is due to an expired token
        if (error instanceof HttpErrorResponse && error.status === 403) {
          return handleTokenExpired(request, next, authService);
        }
        return throwError(() => error);
      })
    );
};
  
function shouldSkipAuth(url: string): boolean {
    const authEndpoints = [
        '/api/auth/login',
        '/api/auth/register',
        '/api/auth/refresh'
    ];
    return authEndpoints.some(endpoint => url.includes(endpoint));
}

function addTokenHeader(request: HttpRequest<any>, token: string): HttpRequest<any> {
    return request.clone({
        setHeaders: {
        Authorization: `Bearer ${token}`
        }
    });
}

function handleTokenExpired(request: HttpRequest<any>, next: HttpHandlerFn, authService: AuthService): Observable<HttpEvent<any>> {
    return authService.refreshAccessToken().pipe(
        switchMap(() => {
            const newToken = localStorage.getItem('accessToken');
            //retrying the original req w the new token
            return next(addTokenHeader(request, newToken!));
        }),
        catchError((error) => {
            console.log('Error refreshing token:', error);

            //if refresh end point returns 403, logging out due to token tampering or expiration
            if (error instanceof HttpErrorResponse && error.status === 403) {
                authService.logoutDueToCookieExpiration();
            }

            return throwError(() => error);
        })
    );
}
  