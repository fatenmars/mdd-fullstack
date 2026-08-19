import { TestBed } from '@angular/core/testing';
import {
  HttpClient,
  provideHttpClient,
  withInterceptors,
} from '@angular/common/http';
import {
  provideHttpClientTesting,
  HttpTestingController,
} from '@angular/common/http/testing';
import { provideRouter, Router } from '@angular/router';
import { authInterceptor } from './auth.interceptor';

describe('authInterceptor', () => {
  let http: HttpClient;
  let httpMock: HttpTestingController;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting(),
        provideRouter([]),
      ],
    });
    http = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('ajoute l\'en-tête Authorization quand un token est présent', () => {
    localStorage.setItem('token', 'tok');
    http.get('/data').subscribe();

    const req = httpMock.expectOne('/data');
    expect(req.request.headers.get('Authorization')).toBe('Bearer tok');
    req.flush({});
  });

  it('n\'ajoute pas d\'en-tête quand il n\'y a pas de token', () => {
    http.get('/data').subscribe();

    const req = httpMock.expectOne('/data');
    expect(req.request.headers.has('Authorization')).toBeFalse();
    req.flush({});
  });

  it('sur un 401, efface le token et redirige vers /login', () => {
    localStorage.setItem('token', 'tok');
    const navSpy = spyOn(router, 'navigate');

    http.get('/data').subscribe({ next: () => {}, error: () => {} });
    httpMock
      .expectOne('/data')
      .flush(null, { status: 401, statusText: 'Unauthorized' });

    expect(localStorage.getItem('token')).toBeNull();
    expect(navSpy).toHaveBeenCalledWith(['/login']);
  });

  it('laisse passer les erreurs non-401 sans déconnecter', () => {
    const navSpy = spyOn(router, 'navigate');

    http.get('/data').subscribe({ next: () => {}, error: () => {} });
    httpMock
      .expectOne('/data')
      .flush(null, { status: 500, statusText: 'Server Error' });

    expect(navSpy).not.toHaveBeenCalled();
  });
});
