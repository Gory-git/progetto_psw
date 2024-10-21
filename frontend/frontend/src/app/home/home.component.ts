import { Component } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { PartitaService } from '../service/partita.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  title = 'home'
  text: string = ''

  constructor(private oauthService: OAuthService, private httpClient: HttpClient, private partitaService: PartitaService) { }

  play() {
    const punti_acquisiti = Math.floor(Math.random() * 101);
    this.text = "hai acquisito " + punti_acquisiti + " crediti!!!"
    const params = new HttpParams().set('punti_acquisiti', punti_acquisiti);
    this.partitaService.salva(params).subscribe(error => {
      this.text = error.message;
    });
  }
}

