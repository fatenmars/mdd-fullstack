import { Component, Input } from '@angular/core';
import { Location } from '@angular/common';

@Component({
  selector: 'app-auth-layout',
  imports: [],
  templateUrl: './auth-layout.component.html',
  styleUrl: './auth-layout.component.scss',
})
export class AuthLayoutComponent {
  @Input() title = '';
  constructor(private location: Location) {}
  goBack(): void {
    this.location.back();
  }
}
