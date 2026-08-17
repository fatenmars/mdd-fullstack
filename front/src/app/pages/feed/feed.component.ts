import { Component, OnInit } from '@angular/core';
import { ArticleService } from '../../services/article.service';
import { Article } from '../../models/article';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-feed',
  imports: [RouterLink],
  templateUrl: './feed.component.html',
  styleUrl: './feed.component.scss',
})
export class FeedComponent implements OnInit {
  articles: Article[] = [];
  order: string = 'desc';
  errorMessage: string = '';

  constructor(
    private articleService: ArticleService,
    private authService: AuthService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.loadFeed();
  }

  loadFeed(): void {
    this.articleService.getArticles(this.order).subscribe({
      next: (articles) => {
        this.articles = articles;
        this.errorMessage = '';
      },
      error: (error) => {
        this.errorMessage =
          "Impossible de charger le fil d'actualité. Réessaie plus tard.";
        console.error('Erreur chargement du fil :', error);
      },
    });
  }

  toggleOrder(): void {
    this.order = this.order === 'desc' ? 'asc' : 'desc';
    this.loadFeed();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
