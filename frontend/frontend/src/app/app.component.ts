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

  constructor(private oauthService: OAuthService, private httpClient: HttpClient) { }

  logout() {
    this.oauthService.logOut();
  }

  public get userName() {

    var claims = this.oauthService.getIdentityClaims();
    if (!claims) return null;

    return claims['given_name'];
  }
}
