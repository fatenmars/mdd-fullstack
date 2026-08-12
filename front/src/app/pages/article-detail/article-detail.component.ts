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
  constructor(
    private route: ActivatedRoute,
    private articleService: ArticleService,
  ) {}

  ngOnInit(): void {
    const articleId = Number(this.route.snapshot.paramMap.get('id'));
    this.articleService.getArticleById(articleId).subscribe((article) => {
      this.article = article;
    });
  }
}
