import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  provideHttpClientTesting,
  HttpTestingController,
} from '@angular/common/http/testing';
import { ProfileComponent } from './profile.component';

const PROFILE = { email: 'a@a.com', username: 'alice', subscriptions: [] };

describe('ProfileComponent', () => {
  let fixture: ComponentFixture<ProfileComponent>;
  let component: ProfileComponent;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileComponent],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should create', () => {
    fixture.detectChanges();
    httpMock.expectOne('http://localhost:8080/users/me').flush(PROFILE);
    expect(component).toBeTruthy();
  });

  it('charge le profil au démarrage', () => {
    fixture.detectChanges();
    httpMock.expectOne('http://localhost:8080/users/me').flush(PROFILE);
    expect(component.profile?.username).toBe('alice');
    expect(component.editEmail).toBe('a@a.com');
  });

  it('updateProfile envoie un PUT puis recharge', () => {
    fixture.detectChanges();
    httpMock.expectOne('http://localhost:8080/users/me').flush(PROFILE);

    component.updateProfile();
    httpMock
      .expectOne((r) => r.method === 'PUT' && r.url === 'http://localhost:8080/users/me')
      .flush(PROFILE);
    httpMock
      .expectOne((r) => r.method === 'GET' && r.url === 'http://localhost:8080/users/me')
      .flush(PROFILE);
  });

  it('unsubscribe supprime l\'abonnement puis recharge', () => {
    fixture.detectChanges();
    httpMock.expectOne('http://localhost:8080/users/me').flush(PROFILE);

    component.unsubscribe(3);
    httpMock.expectOne('http://localhost:8080/users/me/themes/3').flush(null);
    httpMock
      .expectOne((r) => r.method === 'GET' && r.url === 'http://localhost:8080/users/me')
      .flush(PROFILE);
  });
});
