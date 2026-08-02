import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  username = '';
  email = '';
  password = '';

  errorMessage = '';
  loading = false;

  constructor(private authService: AuthService, private router: Router) { }

  onSubmit(): void {
    this.errorMessage = '';
    this.loading = true;

    this.authService.register(this.username, this.email, this.password).subscribe({
      next: () => {
        this.loading = false;
        // the backend gives a token also after the register, so we go directly to the tasks
        this.router.navigate(['/tasks']);
      },
      error: (error) => {
        this.loading = false;
        this.errorMessage = error.error?.message || 'Something went wrong, please try again';
      }
    });
  }
}
