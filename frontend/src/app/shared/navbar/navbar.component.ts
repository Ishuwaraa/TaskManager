import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  authService = inject(AuthService);
  shouldShowAuthLinks: boolean = true;

  //constructor(private router: Router) {}

  // ngOnInit() {
  //   this.authService.isLoggedIn().subscribe(response => {
  //     this.shouldShowAuthLinks = false;
  //   })
  // }

  logout() {
    this.authService.logout().subscribe(response => {
      console.log(response);
    })
    console.log('clicked');
  }
}
