import { Routes } from '@angular/router';
import { FeedComponent } from './pages/feed/feed.component';
import { ArticleDetailComponent } from './pages/article-detail/article-detail.component';
import { CreateArticleComponent } from './pages/create-article/create-article.component';
import { ThemesComponent } from './pages/themes/themes.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { RegisterComponent } from './pages/register/register.component';
import { LoginComponent } from './pages/login/login.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', component: FeedComponent, canActivate: [authGuard] },
  {
    path: 'articles/new',
    component: CreateArticleComponent,
    canActivate: [authGuard],
  },
  {
    path: 'articles/:id',
    component: ArticleDetailComponent,
    canActivate: [authGuard],
  },
  { path: 'themes', component: ThemesComponent, canActivate: [authGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [authGuard] },
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
];
