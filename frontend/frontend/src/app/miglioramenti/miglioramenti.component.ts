import { Component } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Miglioramento } from '../modelli/Miglioramento';
import { CommonModule} from '@angular/common';
import { MiglioramentiService } from '../service/miglioramenti.service';
import { MatDialog } from '@angular/material/dialog';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';
import { UtenteService } from '../service/utente.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-miglioramenti',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './miglioramenti.component.html',
  styleUrl: './miglioramenti.component.css'
})

export class MiglioramentiComponent {

  text: string= ''
  searchTerm: string= ''
  miglioramenti: Miglioramento[] = [];
  constructor(private dialog: MatDialog, private utenteService: UtenteService,  private miglioramentiService: MiglioramentiService) { }

  ngOnInit() {
    this.utenteService.salva().subscribe(error => {
      //this.text = error.message
    })
    this.tutti()
  }

  tutti() {
    this.miglioramentiService.all().subscribe((response)=> {
      if (!Array.isArray(response)) {
        this.text = 'No results!'
        this.miglioramenti = []
      } else {
        this.miglioramenti = response
        this.text = ''
      }
    });
  }

  cerca() {
    this.nome(this.searchTerm)
  }

  onEnter(event: KeyboardEvent) {
    if (event.key === 'Enter') {
      this.cerca();
    }
  }

  nome(nome: string) {
    const params = new HttpParams().set('nome', nome);
    this.miglioramentiService.nome(params).subscribe(response => {
      if (response.message == 'No results!') {
        this.miglioramenti = []
        this.text = 'No results!'
      } else {
        this.text = ''
        this.miglioramenti = [response]
      }
    });
  }

  crediti(crediti: number) {
    const params = new HttpParams().set('crediti', crediti);
    this.miglioramentiService.crediti(params).subscribe((response: Miglioramento[] | undefined )=> {
      if (response != undefined) {
        this.miglioramenti = response
        this.text = ''
      } else {
        this.text = 'No results!'
        this.miglioramenti = []
      }
    });
  }

  acquire(nome: string) {
    const params = new HttpParams().set('nome', nome);
    this.miglioramentiService.acquire(params).subscribe(
      response=> {
        this.openDialog( response.message )
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
