import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  provideHttpClientTesting,
  HttpTestingController,
} from '@angular/common/http/testing';
import { ThemeService } from './theme.service';

describe('ThemeService', () => {
  let service: ThemeService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ThemeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('getThemes fait un GET sur /themes', () => {
    service.getThemes().subscribe();

    const req = httpMock.expectOne('http://localhost:8080/themes');
    expect(req.request.method).toBe('GET');
    req.flush([]);
  });

  it('subscribe fait un POST sur /users/me/themes/:id', () => {
    service.subscribe(3).subscribe();

    const req = httpMock.expectOne('http://localhost:8080/users/me/themes/3');
    expect(req.request.method).toBe('POST');
    req.flush(null);
  });

  it('unsubscribe fait un DELETE sur /users/me/themes/:id', () => {
    service.unsubscribe(3).subscribe();

    const req = httpMock.expectOne('http://localhost:8080/users/me/themes/3');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
