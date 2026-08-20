import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  provideHttpClientTesting,
  HttpTestingController,
} from '@angular/common/http/testing';
import { provideRouter, Router } from '@angular/router';
import { RegisterComponent } from './register.component';

describe('RegisterComponent', () => {
  let fixture: ComponentFixture<RegisterComponent>;
  let component: RegisterComponent;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([]),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
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

  it('onSubmit inscrit, connecte automatiquement puis redirige', () => {
    const navSpy = spyOn(TestBed.inject(Router), 'navigate');
    component.email = 'a@a.com';
    component.username = 'alice';
    component.password = 'Password1!';
    component.onSubmit();

    httpMock.expectOne('http://localhost:8080/auth/register').flush(null);
    httpMock.expectOne('http://localhost:8080/auth/login').flush({ token: 'tok' });

    expect(localStorage.getItem('token')).toBe('tok');
    expect(navSpy).toHaveBeenCalledWith(['/']);
  });

  it('onSubmit affiche un message si e-mail/username déjà pris (409)', () => {
    component.onSubmit();
    httpMock
      .expectOne('http://localhost:8080/auth/register')
      .flush(null, { status: 409, statusText: 'Conflict' });

    expect(component.errorMessage).toContain('déjà pris');
  });

  it('onSubmit affiche un message pour des champs invalides (400)', () => {
    component.onSubmit();
    httpMock
      .expectOne('http://localhost:8080/auth/register')
      .flush(null, { status: 400, statusText: 'Bad Request' });

    expect(component.errorMessage).toContain('Champs invalides');
  });
});
