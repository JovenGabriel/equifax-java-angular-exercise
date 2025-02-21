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

  /**
   * Checks if a user is currently logged in by verifying the presence of a valid token in local storage.
   *
   * @return {boolean} Returns true if the user is logged in, false otherwise.
   */
  isLoggedIn(): boolean {
    return !!localStorage.getItem(this.localToken)
  }

  /**
   * Authenticates the user by sending login credentials to the server.
   *
   * @param {User} user - The user object containing login credentials.
   * @return {Observable<any>} An observable that emits the server response.
   */
  login(user: User) {
    return this.http.post(environment.apiUrl + 'users/login', user);
  }

  /**
   * Sets the authentication token in the local storage and updates the authentication status.
   *
   * @param {string} token - The authentication token to be stored.
   * @return {void} This method does not return a value.
   */
  setToken(token: string): void {
    localStorage.setItem(this.localToken, token)
    this.authStatus.next(true)
  }

  /**
   * Retrieves a token stored in the local storage.
   *
   * @return {string | null} The token from local storage if it exists, otherwise null.
   */
  getToken(): string | null {
    return localStorage.getItem(this.localToken)
  }

  /**
   * Retrieves the authentication status as an observable.
   *
   * @return {Observable<boolean>} An observable that emits the current authentication status as a boolean.
   */
  getAuthStatus(): Observable<boolean> {
    return this.authStatus.asObservable()
  }

  /**
   * Logs the user out by removing the authentication token from local storage,
   * updating the authentication status to "logged out", and navigating to the login page.
   *
   * @return {void} This method does not return a value.
   */
  logout() {
    localStorage.removeItem(this.localToken)
    this.authStatus.next(false)
    this._router.navigate(['login'])
  }
}
