import { Component, CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { UtenteService } from './service/utente.service';
import { MatDialog } from '@angular/material/dialog';
import { ErrorDialogComponent } from './error-dialog/error-dialog.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'psw_front'
  text = ''
  constructor(private dialog: MatDialog, private oauthService: OAuthService, private httpClient: HttpClient, private utenteService: UtenteService) { }

  ngOnInit() {

  }

  logout() {
    this.oauthService.logOut()
  }

  public get userName() {

    var claims = this.oauthService.getIdentityClaims();
    if (!claims) return 'null';

    return claims['given_name'];
  }
}
