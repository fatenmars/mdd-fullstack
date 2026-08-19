import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter, Router } from '@angular/router';
import { HeaderComponent } from './header.component';

describe('HeaderComponent', () => {
  let fixture: ComponentFixture<HeaderComponent>;
  let component: HeaderComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([]),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => localStorage.clear());

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('variant vaut "connected" quand un token est présent', () => {
    localStorage.setItem('token', 'abc');
    expect(component.variant).toBe('connected');
  });

  it('variant vaut "auth" sur /login sans token', () => {
    spyOnProperty(TestBed.inject(Router), 'url', 'get').and.returnValue('/login');
    localStorage.removeItem('token');
    expect(component.variant).toBe('auth');
  });

  it('variant vaut "none" sur la landing sans token', () => {
    localStorage.removeItem('token');
    expect(component.variant).toBe('none');
  });

  it('toggleMenu / closeMenu gèrent l\'ouverture du menu', () => {
    expect(component.menuOpen).toBeFalse();
    component.toggleMenu();
    expect(component.menuOpen).toBeTrue();
    component.closeMenu();
    expect(component.menuOpen).toBeFalse();
  });

  it('logout efface le token et redirige vers la landing', () => {
    const navSpy = spyOn(TestBed.inject(Router), 'navigate');
    localStorage.setItem('token', 'abc');
    component.logout();
    expect(localStorage.getItem('token')).toBeNull();
    expect(navSpy).toHaveBeenCalledWith(['/']);
  });
});
