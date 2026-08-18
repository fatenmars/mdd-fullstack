import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthLayoutComponent } from '../../components/auth-layout/auth-layout.component';

@Component({
  selector: 'app-login',
  imports: [FormsModule, AuthLayoutComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  identifier = '';
  password = '';
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}
  onSubmit(): void {
    const payload = { identifier: this.identifier, password: this.password };
    this.authService.login(payload).subscribe({
      next: (response) => {
        this.authService.saveToken(response.token);
        this.router.navigate(['/']);
      },
      error: (error) => {
        this.errorMessage =
          error.status === 401
            ? 'Identifiants invalides.'
            : 'Une erreur est survenue. Réessaie plus tard.';
        console.error('Erreur connexion :', error);
      },
    });
  }
}
