import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  provideHttpClientTesting,
  HttpTestingController,
} from '@angular/common/http/testing';
import { ProfileService } from './profile.service';

describe('ProfileService', () => {
  let service: ProfileService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ProfileService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('getProfile fait un GET sur /users/me', () => {
    service.getProfile().subscribe();

    const req = httpMock.expectOne('http://localhost:8080/users/me');
    expect(req.request.method).toBe('GET');
    req.flush({ email: 'a@a.com', username: 'alice', subscriptions: [] });
  });

  it('updateProfile fait un PUT sur /users/me', () => {
    const payload = { email: 'new@a.com', username: 'alice2', password: '' };
    service.updateProfile(payload).subscribe();

    const req = httpMock.expectOne('http://localhost:8080/users/me');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(payload);
    req.flush({ email: 'new@a.com', username: 'alice2', subscriptions: [] });
  });
});
