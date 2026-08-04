import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  private apiUrl = 'http://localhost:8080/themes';

  constructor(private http: HttpClient) {}
  getThemes(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
}
