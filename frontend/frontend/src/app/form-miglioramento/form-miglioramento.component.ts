import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatDialog } from '@angular/material/dialog';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { MatInput } from '@angular/material/input';
import { MiglioramentiService } from '../service/miglioramenti.service';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';

@Component({
  selector: 'app-form-miglioramento',
  standalone: true,
  imports: [MatFormField, MatLabel, MatInput, FormsModule],
  templateUrl: './form-miglioramento.component.html',
  styleUrl: './form-miglioramento.component.css'
})
export class FormMiglioramentoComponent {

  crediti: number = 0
  descrizione: string = ''
  nome: string = ''
  quantitaMassima: number = 0
  tipologia: string = ''

  constructor(private dialog: MatDialog, private dialogRef: MatDialogRef<FormMiglioramentoComponent>, private miglioramentiService: MiglioramentiService) {}

  onSubmit(): void {

    const params = {
        'nome': this.nome,
        'crediti': this.crediti,
        'descrizione': this.descrizione,
        'quantitaMassima': this.quantitaMassima,
        'tipologia': this.tipologia
      };
    this.miglioramentiService.add(params).subscribe(
      response=> {
        this.openDialog( response.message )
    }, error => {
      this.openDialog( error.error.message )
    })

    this.dialogRef.close();
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
