import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';

// puts the token in every request, so the services stay clean
export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  // the register and the login do not need a token
  if (token && !request.url.includes('/api/auth/')) {
    const requestWithToken = request.clone({
      setHeaders: {
        Authorization: 'Bearer ' + token
      }
    });
    return next(requestWithToken);
  }

  return next(request);
};
