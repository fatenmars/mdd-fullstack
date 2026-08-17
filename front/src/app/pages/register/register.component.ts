import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent {
  email = '';
  username = '';
  password = '';
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  onSubmit(): void {
    const payload = {
      email: this.email,
      username: this.username,
      password: this.password,
    };
    this.authService.register(payload).subscribe({
      next: () => {
        this.authService
          .login({ identifier: this.email, password: this.password })
          .subscribe({
            next: (response) => {
              this.authService.saveToken(response.token);
              this.router.navigate(['/']);
            },
            error: () => {
              this.router.navigate(['/login']);
            },
          });
      },
      error: (error) => {
        if (error.status === 409) {
          this.errorMessage =
            "Cet e-mail ou ce nom d'utilisateur est déjà pris.";
        } else if (error.status === 400) {
          this.errorMessage =
            "Champs invalides. Vérifie : l'e-mail (format valide), le nom d'utilisateur (non vide), et le mot de passe (8+ caractères, avec majuscule, minuscule, chiffre et caractère spécial).";
        } else {
          this.errorMessage = 'Une erreur est survenue. Réessaie plus tard.';
        }
        console.error('Erreur inscription :', error);
      },
    });
  }
}
