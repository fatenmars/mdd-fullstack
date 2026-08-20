import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  provideHttpClientTesting,
  HttpTestingController,
} from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { ArticleDetailComponent } from './article-detail.component';

describe('ArticleDetailComponent', () => {
  let fixture: ComponentFixture<ArticleDetailComponent>;
  let component: ArticleDetailComponent;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArticleDetailComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([]),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ArticleDetailComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should create', () => {
    fixture.detectChanges();
    httpMock.expectOne('http://localhost:8080/articles/0').flush({});
    expect(component).toBeTruthy();
  });

  it('charge l\'article au démarrage', () => {
    fixture.detectChanges();
    httpMock
      .expectOne('http://localhost:8080/articles/0')
      .flush({ id: 1, title: 'A', comments: [] });
    expect(component.article).toBeTruthy();
  });

  it('addComment poste le commentaire, vide le champ et recharge', () => {
    fixture.detectChanges();
    httpMock
      .expectOne('http://localhost:8080/articles/0')
      .flush({ id: 1, title: 'A', comments: [] });

    component.newComment = 'super';
    component.addComment();
    httpMock.expectOne('http://localhost:8080/articles/0/comments').flush({});
    httpMock
      .expectOne('http://localhost:8080/articles/0')
      .flush({ id: 1, title: 'A', comments: [] });

    expect(component.newComment).toBe('');
  });
});
