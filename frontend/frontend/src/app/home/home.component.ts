import { Component } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { PartitaService } from '../service/partita.service';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { UtenteService } from '../service/utente.service';

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

  constructor(private dialog: MatDialog, private utenteService: UtenteService, private partitaService: PartitaService) { }

  ngOnInit() {
    this.utenteService.salva().subscribe(error => {
      //this.text = error.message
    })
  }

  play() {
    const punti_acquisiti: number = Math.floor(Math.random() * 101);
    this.openDialog( "hai acquisito " + punti_acquisiti + " crediti!!!" )
    const params = new HttpParams().set('punti', punti_acquisiti);
    this.partitaService.salva(params).subscribe(response => {
      this.text= response.message;
    }, error => {
      this.openDialog( error.error.message )
    });
  }

  openDialog(message: String) {
    const dialogRef = this.dialog.open(ErrorDialogComponent, {
      data: { message: message }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('Il dialogo è stato chiuso');
    });
  }
}

