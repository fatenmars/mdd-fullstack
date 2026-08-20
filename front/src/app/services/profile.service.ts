import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserProfile } from '../models/userProfile';

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  private apiUrl = 'http://localhost:8080/users/me';

  constructor(private http: HttpClient) {}
  getProfile(): Observable<UserProfile> {
    return this.http.get<UserProfile>(this.apiUrl);
  }
  updateProfile(payload: {
    email: string;
    username: string;
    password: string;
  }): Observable<UserProfile> {
    return this.http.put<UserProfile>(this.apiUrl, payload);
  }
}
