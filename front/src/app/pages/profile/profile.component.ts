import { Component, OnInit } from '@angular/core';
import { ProfileService } from '../../services/profile.service';
import { UserProfile } from '../../models/userProfile';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-profile',
  imports: [],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
})
export class ProfileComponent implements OnInit {
  profile?: UserProfile;
  errorMessage = '';
  unsubscribeErrorMessage = '';

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
}
