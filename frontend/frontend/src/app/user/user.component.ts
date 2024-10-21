import { Component } from '@angular/core';
import { UtenteService } from '../service/utente.service';
import { Utente } from '../modelli/Utente';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css'
})
export class UserComponent {
   
  utente!: Utente;
  text: String = '';
  constructor(private utenteService: UtenteService) { }

  getInfo() {
    this.utenteService.getInfo().subscribe(response=> {
      this.utente = response
    }, error => {
      this.text = error.message
    })
  }
}
