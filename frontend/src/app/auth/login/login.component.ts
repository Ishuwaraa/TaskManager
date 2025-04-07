import { Component, inject } from '@angular/core';
import { FormControl, ReactiveFormsModule, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AuthService } from '../../services/auth.service';
import { Router, RouterLink } from '@angular/router';
import { UserLoginRequest } from '../../models/user.model';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, MatSnackBarModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  authService = inject(AuthService);
  errorMessage: string = '';

  constructor(private snackBar: MatSnackBar, private router: Router) {}

  loginForm: FormGroup = new FormGroup({
    email: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required])
  })

  onFormSubmit() {
    if (this.loginForm.valid) {
      console.log(this.loginForm.value)

      const loginData: UserLoginRequest = this.loginForm.value;
      this.authService.login(loginData).subscribe({
        next: () => {          
          this.router.navigate(['/']);
          this.resetForm();
        },
        error: (err) => {
          if (err.status === 401) {
            this.errorMessage = 'Invalid email or password';
          } else {
            this.errorMessage = 'Something went wrong. Please try again.';
          }
        }
      });
    }
  }

  resetForm() {
    this.loginForm.reset({
      email: '',
      password: ''
    });
  }
}
