import { TestBed } from '@angular/core/testing';
import { provideRouter, Router } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { authGuard } from './auth.guard';

describe('authGuard', () => {
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([]),
      ],
    });
    router = TestBed.inject(Router);
  });

  afterEach(() => localStorage.clear());

  it('autorise l\'accès quand un token est présent', () => {
    localStorage.setItem('token', 'abc');
    const result = TestBed.runInInjectionContext(() =>
      authGuard({} as any, {} as any),
    );
    expect(result).toBeTrue();
  });

  it('refuse et redirige vers /login sans token', () => {
    const navSpy = spyOn(router, 'navigate');
    const result = TestBed.runInInjectionContext(() =>
      authGuard({} as any, {} as any),
    );
    expect(result).toBeFalse();
    expect(navSpy).toHaveBeenCalledWith(['/login']);
  });
});
