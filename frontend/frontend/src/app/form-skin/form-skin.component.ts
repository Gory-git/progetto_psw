import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatDialog } from '@angular/material/dialog';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { MatInput } from '@angular/material/input';
import { SkinsService } from '../service/skins.service';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';

@Component({
  selector: 'app-form-miglioramento',
  standalone: true,
  imports: [MatFormField, MatLabel, MatInput, FormsModule],
  templateUrl: './form-skin.component.html',
  styleUrl: './form-skin.component.css'
})
export class FormSkinComponent {

  crediti: number = 0
  nome: string = ''

  constructor(private dialog: MatDialog, private dialogRef: MatDialogRef<FormSkinComponent>, private skinsService: SkinsService) {}

  onSubmit(): void {

    const params = {
        'nome': this.nome,
        'crediti': this.crediti
      };
    this.skinsService.add(params).subscribe(
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
