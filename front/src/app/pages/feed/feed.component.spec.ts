import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  provideHttpClientTesting,
  HttpTestingController,
} from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { FeedComponent } from './feed.component';

describe('FeedComponent', () => {
  let fixture: ComponentFixture<FeedComponent>;
  let component: FeedComponent;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FeedComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([]),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(FeedComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should create', () => {
    fixture.detectChanges();
    httpMock.expectOne((r) => r.url === 'http://localhost:8080/articles').flush([]);
    expect(component).toBeTruthy();
  });

  it('charge les articles au démarrage', () => {
    fixture.detectChanges();
    httpMock
      .expectOne((r) => r.url === 'http://localhost:8080/articles')
      .flush([{ id: 1, title: 'A' }]);
    expect(component.articles.length).toBe(1);
  });

  it('toggleOrder inverse le tri et recharge', () => {
    fixture.detectChanges();
    httpMock.expectOne((r) => r.url === 'http://localhost:8080/articles').flush([]);

    component.toggleOrder();
    expect(component.order).toBe('asc');
    httpMock.expectOne((r) => r.url === 'http://localhost:8080/articles').flush([]);
  });

  it('affiche un message en cas d\'erreur de chargement', () => {
    fixture.detectChanges();
    httpMock
      .expectOne((r) => r.url === 'http://localhost:8080/articles')
      .flush(null, { status: 500, statusText: 'Server Error' });
    expect(component.errorMessage).toContain('Impossible');
  });
});
