import { Component } from '@angular/core';
import { FeedComponent } from './pages/feed/feed.component';

@Component({
  selector: 'app-root',
  imports: [FeedComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {}
