import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-header',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent {
  private router = inject(Router);
  private authService = inject(AuthService);
  menuOpen = false;

  get variant(): 'connected' | 'auth' | 'none' {
    if (this.authService.getToken()) return 'connected';
    if (this.router.url === '/login' || this.router.url === '/register')
      return 'auth';
    return 'none';
  }

  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }
  closeMenu(): void {
    this.menuOpen = false;
  }
  logout(): void {
    this.closeMenu();
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
