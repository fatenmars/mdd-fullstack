import { Component, OnInit } from '@angular/core';
import { Theme } from '../../models/theme';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-themes',
  imports: [],
  templateUrl: './themes.component.html',
  styleUrl: './themes.component.scss',
})
export class ThemesComponent implements OnInit {
  themes: Theme[] = [];
  errorMessage: string = '';
  subscribeErrorMessage = '';

  constructor(private themeService: ThemeService) {}

  ngOnInit(): void {
    this.loadThemes();
  }

  loadThemes(): void {
    this.themeService.getThemes().subscribe({
      next: (themes) => {
        this.themes = themes;
        this.errorMessage = '';
      },
      error: (error) => {
        this.errorMessage =
          'Impossible de charger la liste des thèmes. Réessaie plus tard.';
        console.error('Erreur chargement de la liste des thèmes', error);
      },
    });
  }

  subscribeToTheme(themeId: number): void {
    this.themeService.subscribe(themeId).subscribe({
      next: () => {
        this.loadThemes();
      },
      error: (error) => {
        this.subscribeErrorMessage =
          "Impossible de s'abonner. Réessaie plus tard.";
        console.error('Erreur abonnement :', error);
      },
    });
  }
}
