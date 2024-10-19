import { Component } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  title = 'home'

  constructor(private oauthService: OAuthService, private httpClient: HttpClient) { }

  putGame() {
    this.httpClient.get<{ message: string }>('http://localhost:8080/game', {
      headers: {
        'Authorization': `Bearer ${this.oauthService.getAccessToken()}`
      }
    }).subscribe(result => {
      // prendo i punti guadagnati durante la partita
    });
  }
}

