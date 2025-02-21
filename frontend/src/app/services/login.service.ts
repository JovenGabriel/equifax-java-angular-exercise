import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {User} from "../models/User";
import {environment} from "../../environments/environment";
import {BehaviorSubject, Observable} from "rxjs";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  localToken: string = 'JWTToken'
  authStatus = new BehaviorSubject<boolean>(this.isLoggedIn())
  _router = inject(Router)
  constructor(private http: HttpClient) { }

  isLoggedIn(): boolean {
    return !!localStorage.getItem(this.localToken)
  }

  login(user: User) {
    return this.http.post(environment.apiUrl + 'users/login', user);
  }

  setToken(token: string): void {
    localStorage.setItem(this.localToken, token)
    this.authStatus.next(true)
  }

  getToken(): string | null {
    return localStorage.getItem(this.localToken)
  }

  getAuthStatus(): Observable<boolean> {
    return this.authStatus.asObservable()
  }

  logout() {
    localStorage.removeItem(this.localToken)
    this.authStatus.next(false)

    this._router.navigate(['login'])
  }
}
