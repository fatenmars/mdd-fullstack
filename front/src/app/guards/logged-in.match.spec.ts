import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { loggedInMatch } from './logged-in.match';

describe('loggedInMatch', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
  });

  afterEach(() => localStorage.clear());

  it('renvoie true quand un token est présent', () => {
    localStorage.setItem('token', 'abc');
    expect(TestBed.runInInjectionContext(() => loggedInMatch({} as any, []))).toBeTrue();
  });

  it('renvoie false sans token', () => {
    expect(TestBed.runInInjectionContext(() => loggedInMatch({} as any, []))).toBeFalse();
  });
});
