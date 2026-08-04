import { Component, OnInit } from '@angular/core';
import { ThemeService } from './services/theme.service';

@Component({
  selector: 'app-root',
  imports: [],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {
  themes: any[] = [];

  constructor(private themeService: ThemeService) {}

  ngOnInit(): void {
    this.themeService.getThemes().subscribe((data) => {
      this.themes = data;
    });
  }
}
