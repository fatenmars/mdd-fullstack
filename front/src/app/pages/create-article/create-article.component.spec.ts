import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  provideHttpClientTesting,
  HttpTestingController,
} from '@angular/common/http/testing';
import { provideRouter, Router } from '@angular/router';
import { CreateArticleComponent } from './create-article.component';

describe('CreateArticleComponent', () => {
  let fixture: ComponentFixture<CreateArticleComponent>;
  let component: CreateArticleComponent;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateArticleComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([]),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(CreateArticleComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should create', () => {
    fixture.detectChanges();
    httpMock.expectOne('http://localhost:8080/themes').flush([]);
    expect(component).toBeTruthy();
  });

  it('charge la liste des thèmes au démarrage', () => {
    fixture.detectChanges();
    httpMock
      .expectOne('http://localhost:8080/themes')
      .flush([{ id: 1, title: 'JS', description: 'desc' }]);
    expect(component.themes.length).toBe(1);
  });

  it('onSubmit crée l\'article et redirige vers son détail', () => {
    const navSpy = spyOn(TestBed.inject(Router), 'navigate');
    fixture.detectChanges();
    httpMock.expectOne('http://localhost:8080/themes').flush([]);

    component.title = 'Titre';
    component.content = 'Contenu';
    component.themeId = 1;
    component.onSubmit();

    httpMock.expectOne('http://localhost:8080/articles').flush({ id: 5 });
    expect(navSpy).toHaveBeenCalledWith(['/articles', 5]);
  });
});
