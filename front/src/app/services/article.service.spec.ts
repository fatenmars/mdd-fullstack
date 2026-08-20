import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  provideHttpClientTesting,
  HttpTestingController,
} from '@angular/common/http/testing';
import { ArticleService } from './article.service';

describe('ArticleService', () => {
  let service: ArticleService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ArticleService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('getArticles fait un GET avec le paramètre order', () => {
    service.getArticles('desc').subscribe();

    const req = httpMock.expectOne(
      (r) =>
        r.url === 'http://localhost:8080/articles' &&
        r.params.get('order') === 'desc',
    );
    expect(req.request.method).toBe('GET');
    req.flush([]);
  });

  it('getArticleById fait un GET sur /articles/:id', () => {
    service.getArticleById(42).subscribe();

    const req = httpMock.expectOne('http://localhost:8080/articles/42');
    expect(req.request.method).toBe('GET');
    req.flush({});
  });

  it('createArticle fait un POST sur /articles', () => {
    const payload = { title: 'Titre', content: 'Contenu', themeId: 1 };
    service.createArticle(payload).subscribe();

    const req = httpMock.expectOne('http://localhost:8080/articles');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush({});
  });

  it('addComment fait un POST sur /articles/:id/comments', () => {
    service.addComment(42, 'super article').subscribe();

    const req = httpMock.expectOne('http://localhost:8080/articles/42/comments');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ content: 'super article' });
    req.flush({});
  });
});
