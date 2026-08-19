import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  provideHttpClientTesting,
  HttpTestingController,
} from '@angular/common/http/testing';
import { provideRouter, Router } from '@angular/router';
import { LoginComponent } from './login.component';

describe('LoginComponent', () => {
  let fixture: ComponentFixture<LoginComponent>;
  let component: LoginComponent;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([]),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('onSubmit connecte, enregistre le token et redirige', () => {
    const navSpy = spyOn(TestBed.inject(Router), 'navigate');
    component.identifier = 'alice';
    component.password = 'Password1!';
    component.onSubmit();

    httpMock.expectOne('http://localhost:8080/auth/login').flush({ token: 'tok' });

    expect(localStorage.getItem('token')).toBe('tok');
    expect(navSpy).toHaveBeenCalledWith(['/']);
  });

  it('onSubmit affiche un message pour un 401', () => {
    component.onSubmit();
    httpMock
      .expectOne('http://localhost:8080/auth/login')
      .flush(null, { status: 401, statusText: 'Unauthorized' });

    expect(component.errorMessage).toBe('Identifiants invalides.');
  });

  it('onSubmit affiche un message générique pour une autre erreur', () => {
    component.onSubmit();
    httpMock
      .expectOne('http://localhost:8080/auth/login')
      .flush(null, { status: 500, statusText: 'Server Error' });

    expect(component.errorMessage).toContain('Réessaie plus tard');
  });
});
