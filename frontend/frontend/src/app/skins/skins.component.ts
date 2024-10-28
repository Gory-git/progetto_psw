import { Component } from '@angular/core';
import { Skin } from '../modelli/Skin';
import { SkinsService } from '../service/skins.service';
import { HttpParams } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';
import { UtenteService } from '../service/utente.service';

@Component({
  selector: 'app-skins',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './skins.component.html',
  styleUrl: './skins.component.css'
})
export class SkinsComponent {
  text: string= ''
  nonPosseduteV: boolean = false;
  skins: Skin[] = [];
  constructor(private dialog: MatDialog, private utenteService: UtenteService, private skinsService: SkinsService) { }

  ngOnInit() {
    this.utenteService.salva().subscribe(error => {
      //this.text = error.message
    })
  }

  possedute() {
    this.skinsService.possedute().subscribe((response)=> {
      if (!Array.isArray(response)) {
        this.text = 'No results!'
        this.skins = []
      } else {
        this.skins = response
        this.text = 'Possedute'
        this.nonPosseduteV = false
      }
    })
  }

  nonPossedute() {
    this.skinsService.nonPossedute().subscribe((response)=> {
      if (!Array.isArray(response)) {
        this.text = 'No results!'
        this.skins = []
      } else {
        this.skins = response
        this.text = 'Non Possedute'
        this.nonPosseduteV = true
      }
    })
  }

  acquire(nome: string) {
    this.text = 'acquisto ' + nome
    const params = new HttpParams().set('nome', nome);
    this.skinsService.acquire(params).subscribe(response=> {
      this.openDialog( response.message )
    }, error => {
      this.openDialog( error.error.message )
    })
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
