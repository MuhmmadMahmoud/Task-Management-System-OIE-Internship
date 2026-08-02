import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { AuthResponse } from '../models/task.model';

// localStorage and not a variable, so the login stays after a refresh
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { }

  register(username: string, email: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.apiUrl + '/register', { username, email, password })
      .pipe(tap(response => this.saveToken(response)));
  }

  login(username: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.apiUrl + '/login', { username, password })
      .pipe(tap(response => this.saveToken(response)));
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUsername(): string | null {
    return localStorage.getItem('username');
  }

  isLoggedIn(): boolean {
    return this.getToken() != null;
  }

  private saveToken(response: AuthResponse): void {
    localStorage.setItem('token', response.token);
    localStorage.setItem('username', response.username);
  }
}
