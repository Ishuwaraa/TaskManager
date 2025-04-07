import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, catchError, Observable, of, tap, throwError } from 'rxjs';
import { UserLoginRequest, UserLoginResponse, UserRegisterRequest, UserRegisterResponse } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly apiUrl = "http://localhost:8080/api/auth";
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasToken()); 

  constructor(private http: HttpClient, private router: Router) { }

  private hasToken(): boolean {
    return !!localStorage.getItem('accessToken');
  }

  signup(userData: UserRegisterRequest): Observable<any> {  
    return this.http.post<UserRegisterResponse>(`${this.apiUrl}/register`, userData).pipe(
        tap((response: UserRegisterResponse) => {
            console.log('Signup successful, response:', response.email);
        }),
        catchError((error) => {
          return this.handleError(error);
        })
    );
  }

  login(credentials: UserLoginRequest): Observable<any> {
    return this.http.post<UserLoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap((response: UserLoginResponse) => {
          if (response?.accessToken) {
              localStorage.setItem('accessToken', response.accessToken);
              this.isAuthenticatedSubject.next(true);
          }
      }),
      catchError((error) => {
          console.log(error);
          return this.handleError(error);
      })
    );
  }

  refreshAccessToken(): Observable<any> {        
    return this.http.post<any>(`${this.apiUrl}/refresh`, {}, { withCredentials: true }).pipe(
        tap((response: any) => {
            if (response?.accessToken) {
                localStorage.setItem('accessToken', response.accessToken);
            }
        })
    );
  }

  logout(): Observable<any> {
    return this.http.post(`${this.apiUrl}/logout`, {}, { withCredentials: true }).pipe(
        tap(() => {
            localStorage.removeItem('accessToken');
            this.isAuthenticatedSubject.next(false);
            this.router.navigate(['/login']);
        }),
        catchError((error) => {
            return this.handleError(error);
        })
    );        
  }

  logoutDueToCookieExpiration(): Observable<any> {
    localStorage.removeItem('accessToken');
    this.isAuthenticatedSubject.next(false);
    this.router.navigate(['/login']);
    return of(null);
  }

  isLoggedIn(): Observable<boolean> {
    return this.isAuthenticatedSubject.asObservable();
  }

  private handleError(error: HttpErrorResponse) {
    return throwError(() => new Error(error.message));        
  }
}
