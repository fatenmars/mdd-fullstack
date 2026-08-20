import { CanMatchFn } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const loggedInMatch: CanMatchFn = () => !!inject(AuthService).getToken();
