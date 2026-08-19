import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  provideHttpClientTesting,
  HttpTestingController,
} from '@angular/common/http/testing';
import { ThemesComponent } from './themes.component';

describe('ThemesComponent', () => {
  let fixture: ComponentFixture<ThemesComponent>;
  let component: ThemesComponent;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ThemesComponent],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    fixture = TestBed.createComponent(ThemesComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('should create', () => {
    fixture.detectChanges();
    httpMock.expectOne('http://localhost:8080/themes').flush([]);
    expect(component).toBeTruthy();
  });

  it('charge les thèmes au démarrage', () => {
    fixture.detectChanges();
    httpMock
      .expectOne('http://localhost:8080/themes')
      .flush([{ id: 1, title: 'JS', description: 'desc', subscribed: false }]);
    expect(component.themes.length).toBe(1);
  });

  it('subscribeToTheme envoie un POST puis recharge la liste', () => {
    fixture.detectChanges();
    httpMock.expectOne('http://localhost:8080/themes').flush([]);

    component.subscribeToTheme(3);
    httpMock.expectOne('http://localhost:8080/users/me/themes/3').flush(null);
    httpMock.expectOne('http://localhost:8080/themes').flush([]);
  });
});
