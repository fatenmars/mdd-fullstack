import { Component, OnInit } from '@angular/core';
import { ProfileService } from '../../services/profile.service';
import { UserProfile } from '../../models/userProfile';
import { ThemeService } from '../../services/theme.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-profile',
  imports: [FormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
})
export class ProfileComponent implements OnInit {
  profile?: UserProfile;
  errorMessage = '';
  unsubscribeErrorMessage = '';
  editEmail = '';
  editUsername = '';
  editPassword = '';
  editErrorMessage = '';

  constructor(
    private profileService: ProfileService,
    private themeService: ThemeService,
  ) {}

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(): void {
    this.profileService.getProfile().subscribe({
      next: (profile) => {
        this.profile = profile;
        this.editEmail = profile.email;
        this.editUsername = profile.username;
      },
      error: (error) => {
        this.errorMessage = 'Utilisateur introuvable ou erreur de chargement.';
        console.error('Erreur chargement :', error);
      },
    });
  }

  unsubscribe(themeId: number): void {
    this.themeService.unsubscribe(themeId).subscribe({
      next: () => {
        this.loadProfile();
      },
      error: (error) => {
        this.unsubscribeErrorMessage =
          'Impossible de se désabonner. Réessaie plus tard.';
        console.error('Erreur désabonnement :', error);
      },
    });
  }

  updateProfile(): void {
    const payload = {
      email: this.editEmail,
      username: this.editUsername,
      password: this.editPassword,
    };
    this.profileService.updateProfile(payload).subscribe({
      next: () => {
        this.editPassword = '';
        this.loadProfile();
      },
      error: (error) => {
        this.editErrorMessage =
          'Impossible de mettre à jour le profil. Vérifie les champs.';
        console.error('Erreur mise à jour profil :', error);
      },
    });
  }
}
