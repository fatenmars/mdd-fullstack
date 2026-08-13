import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Article } from '../models/article';
import { ArticleDetail } from '../models/articleDetail';
@Injectable({
  providedIn: 'root',
})
export class ArticleService {
  private apiUrl = 'http://localhost:8080/articles';
  constructor(private http: HttpClient) {}
  getArticles(order: string): Observable<Article[]> {
    return this.http.get<Article[]>(this.apiUrl, { params: { order } });
  }
  getArticleById(id: number): Observable<ArticleDetail> {
    return this.http.get<ArticleDetail>(`${this.apiUrl}/${id}`);
  }
  createArticle(payload: {
    title: string;
    content: string;
    themeId: number;
  }): Observable<Article> {
    return this.http.post<Article>(this.apiUrl, payload);
  }
}
