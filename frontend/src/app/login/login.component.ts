import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  username = '';
  password = '';

  errorMessage = '';
  loading = false;

  constructor(private authService: AuthService, private router: Router) { }

  onSubmit(): void {
    this.errorMessage = '';
    this.loading = true;

    this.authService.login(this.username, this.password).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/tasks']);
      },
      error: (error) => {
        this.loading = false;
        // the backend puts the reason in a field called "message"
        this.errorMessage = error.error?.message || 'Something went wrong, please try again';
      }
    });
  }
}
