import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ArticleService } from '../../services/article.service';
import { ArticleDetail } from '../../models/articleDetail';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-article-detail',
  imports: [FormsModule, RouterLink, DatePipe],
  templateUrl: './article-detail.component.html',
  styleUrl: './article-detail.component.scss',
})
export class ArticleDetailComponent implements OnInit {
  article?: ArticleDetail;
  articleId!: number;
  newComment = '';
  errorMessage = '';
  commentError = '';

  constructor(
    private route: ActivatedRoute,
    private articleService: ArticleService,
  ) {}

  ngOnInit(): void {
    this.articleId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadArticle();
  }

  loadArticle(): void {
    this.articleService.getArticleById(this.articleId).subscribe({
      next: (article) => {
        this.article = article;
      },
      error: (error) => {
        this.errorMessage = 'Article introuvable ou erreur de chargement.';
        console.error('Erreur chargement article :', error);
      },
    });
  }

  addComment(): void {
    this.articleService.addComment(this.articleId, this.newComment).subscribe({
      next: () => {
        this.newComment = '';
        this.loadArticle();
      },
      error: (error) => {
        this.commentError = "Impossible d'ajouter le commentaire.";
        console.error('Erreur ajout commentaire :', error);
      },
    });
  }
}
