import { Routes } from '@angular/router';
import { FeedComponent } from './pages/feed/feed.component';
import { ArticleDetailComponent } from './pages/article-detail/article-detail.component';

export const routes: Routes = [
  { path: '', component: FeedComponent },
  { path: 'articles/:id', component: ArticleDetailComponent },
];
