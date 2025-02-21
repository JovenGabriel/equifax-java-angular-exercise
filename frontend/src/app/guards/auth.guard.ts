import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {LoginService} from "../services/login.service";
import {tap} from "rxjs";

export const authGuard: CanActivateFn = (route, state) => {
  const _loginService = inject(LoginService)
  const _router = inject(Router)
  return _loginService.getAuthStatus().pipe(
    tap(isLoggedIn => {
      if (!isLoggedIn) {
        _router.navigate(['/login']);
      }
    })
  );
};
