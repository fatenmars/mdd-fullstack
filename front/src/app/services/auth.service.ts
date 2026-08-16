import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';
  constructor(private http: HttpClient) {}
  register(payload: {
    email: string;
    username: string;
    password: string;
  }): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/register`, payload);
  }
}
