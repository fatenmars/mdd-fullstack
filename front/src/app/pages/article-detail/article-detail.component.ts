import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ArticleService } from '../../services/article.service';
import { ArticleDetail } from '../../models/articleDetail';
import { Comment } from '../../models/comment';

@Component({
  selector: 'app-article-detail',
  imports: [],
  templateUrl: './article-detail.component.html',
  styleUrl: './article-detail.component.scss',
})
export class ArticleDetailComponent implements OnInit {
  article?: ArticleDetail;
  errorMessage: string = '';
  constructor(
    private route: ActivatedRoute,
    private articleService: ArticleService,
  ) {}

  ngOnInit(): void {
    const articleId = Number(this.route.snapshot.paramMap.get('id'));
    this.articleService.getArticleById(articleId).subscribe({
      next: (article) => {
        this.article = article;
      },
      error: (error) => {
        this.errorMessage = 'Article introuvable ou erreur de chargement.';
        console.error("Erreur chargement de l'article :", error);
      },
    });
  }
}
