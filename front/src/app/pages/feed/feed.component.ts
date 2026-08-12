import { Component, OnInit } from '@angular/core';
import { ArticleService } from '../../services/article.service';
import { Article } from '../../models/article';

@Component({
  selector: 'app-feed',
  imports: [],
  templateUrl: './feed.component.html',
  styleUrl: './feed.component.scss',
})
export class FeedComponent implements OnInit {
  articles: Article[] = [];
  order: string = 'desc';
  constructor(private articleService: ArticleService) {}

  ngOnInit(): void {
    this.loadFeed();
  }

  loadFeed(): void {
    this.articleService.getArticles(this.order).subscribe((articles) => {
      this.articles = articles;
    });
  }

  toggleOrder(): void {
    this.order = this.order === 'desc' ? 'asc' : 'desc';
    this.loadFeed();
  }
}
