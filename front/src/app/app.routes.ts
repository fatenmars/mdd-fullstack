import { Routes } from '@angular/router';
import { FeedComponent } from './pages/feed/feed.component';
import { ArticleDetailComponent } from './pages/article-detail/article-detail.component';
import { CreateArticleComponent } from './pages/create-article/create-article.component';

export const routes: Routes = [
  { path: '', component: FeedComponent },
  { path: 'articles/new', component: CreateArticleComponent },
  { path: 'articles/:id', component: ArticleDetailComponent },
];
