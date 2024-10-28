import { Component } from '@angular/core';
import { UtenteService } from '../service/utente.service';
import { Utente } from '../modelli/Utente';
import { OAuthService } from 'angular-oauth2-oidc';
import { NgForm, NgModel } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { FormMiglioramentoComponent } from '../form-miglioramento/form-miglioramento.component';
import { CommonModule } from '@angular/common';
import { FormSkinComponent } from '../form-skin/form-skin.component';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css'
})
export class UserComponent {
   
  utente: Utente = {
    id: 0,
    nome: 'nome',
    cognome: 'cognome',
    email: 'email@email.email',
    crediti: 0,
    ruolo: false
  }
  text: String = '';
  constructor(private dialog: MatDialog, private utenteService: UtenteService, private oauthService: OAuthService,) { }

  ngOnInit() {
    this.utenteService.salva().subscribe(error => {
      //this.text = error.message
    })
    this.getInfo()
    if (this.utente.email == 'email@email.email') {
      //window.location.reload();
    }
  }

  getInfo() {
    this.utenteService.getInfo().subscribe((response: Utente)=> {
      this.utente = response
    }, (error) => {
      this.text = error.message
    })
  }

  openFormMiglioramento() {
    const dialogRef = this.dialog.open(FormMiglioramentoComponent, { });

    dialogRef.afterClosed().subscribe(result => {
      console.log('Il form è stato chiuso');
    });
  }

  openFormSkin() {
    const dialogRef = this.dialog.open(FormSkinComponent, { });

    dialogRef.afterClosed().subscribe(result => {
      console.log('Il form è stato chiuso');
    });
  }
}
