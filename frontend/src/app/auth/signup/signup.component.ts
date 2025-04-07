import { Component, inject } from '@angular/core';
import { FormControl, ReactiveFormsModule, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AuthService } from '../../services/auth.service';
import { Router, RouterLink } from '@angular/router';
import { UserRegisterRequest } from '../../models/user.model';

@Component({
  selector: 'app-signup',
  imports: [ReactiveFormsModule, MatSnackBarModule, RouterLink],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {
  authService = inject(AuthService);

  constructor(private snackBar: MatSnackBar, private router: Router) {}

  signUpForm: FormGroup = new FormGroup({
    name: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required])
  })

  onFormSubmit() {
    if (this.signUpForm.valid) {
      const signUpData: UserRegisterRequest = this.signUpForm.value;
      
      this.authService.signup(signUpData).subscribe({
        next: (response) => {
          this.router.navigate(['/login']);
          this.resetForm();
          this.snackBar.open('Account created successfully!', 'Close', {
            duration: 3000
          });
        },
        error: (error) => {
          if (error.status === 409) {
            this.snackBar.open('Email already exists. Please use a different email.', 'Close', {
              duration: 3000
            });
          } else {
            this.snackBar.open('Error occurred while creating your account. Try again later.', 'Close', {
              duration: 3000
            });
          }
        }
      })
    }
  }

  resetForm() {
    this.signUpForm.reset({
      name: '',
      email: '',
      password: ''
    });
  }
}
