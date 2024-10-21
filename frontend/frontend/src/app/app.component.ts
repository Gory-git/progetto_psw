import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'psw_front'
  helloText = '';

  constructor(private oauthService: OAuthService, private httpClient: HttpClient) { }

  login() {
    this.oauthService.initImplicitFlow();
  }

  logout() {
    this.oauthService.logOut();
  }

  public get userName() {

    var claims = this.oauthService.getIdentityClaims();
    if (!claims) return null;

    return claims['given_name'];
}

  getHelloText() {
    this.httpClient.get<{ message: string }>('http://localhost:8080/hello', {
      headers: {
        'Authorization': `Bearer ${this.oauthService.getAccessToken()}`
      }
    }).subscribe(result => {
      this.helloText = result.message;
    });
  }
}
