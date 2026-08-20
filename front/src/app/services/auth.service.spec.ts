import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  provideHttpClientTesting,
  HttpTestingController,
} from '@angular/common/http/testing';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('register poste vers /auth/register', () => {
    const payload = { email: 'a@a.com', username: 'alice', password: 'Password1!' };
    service.register(payload).subscribe();

    const req = httpMock.expectOne('http://localhost:8080/auth/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush(null);
  });

  it('login poste vers /auth/login et renvoie un token', () => {
    let result: { token: string } | undefined;
    service.login({ identifier: 'alice', password: 'Password1!' }).subscribe(
      (r) => (result = r),
    );

    const req = httpMock.expectOne('http://localhost:8080/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush({ token: 'abc' });

    expect(result).toEqual({ token: 'abc' });
  });

  it('saveToken / getToken / logout gèrent le localStorage', () => {
    service.saveToken('mon-token');
    expect(service.getToken()).toBe('mon-token');

    service.logout();
    expect(service.getToken()).toBeNull();
  });
});
