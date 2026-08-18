import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ArticleService } from '../../services/article.service';
import { ThemeService } from '../../services/theme.service';
import { Theme } from '../../models/theme';

@Component({
  selector: 'app-create-article',
  imports: [FormsModule, RouterLink],
  templateUrl: './create-article.component.html',
  styleUrl: './create-article.component.scss',
})
export class CreateArticleComponent implements OnInit {
  themes: Theme[] = [];
  title = '';
  content = '';
  themeId?: number;
  errorMessage = '';

  constructor(
    private articleService: ArticleService,
    private themeService: ThemeService,
    private router: Router,
  ) {}
  ngOnInit(): void {
    this.themeService.getThemes().subscribe((themes) => {
      this.themes = themes;
    });
  }

  onSubmit(): void {
    const payload = {
      title: this.title,
      content: this.content,
      themeId: this.themeId!,
    };
    this.articleService.createArticle(payload).subscribe({
      next: (article) => {
        this.router.navigate(['/articles', article.id]);
      },
      error: (error) => {
        this.errorMessage = "Impossible de créer l'article. Vérifie les champs";
        console.error('Erreur création article', error);
      },
    });
  }
}
