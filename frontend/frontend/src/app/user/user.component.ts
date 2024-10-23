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
   
  utente: Utente = {
    id: 0,
    nome: 'a',
    cognome: 'a',
    email: 'a',
    crediti: 0
  }
  text: String = '';
  constructor(private utenteService: UtenteService) { }

  ngOnInit() {
    this.getInfo()
  }

  getInfo() {
    this.utenteService.getInfo().subscribe((response: Utente)=> {
      this.utente = response
    }, (error) => {
      this.text = error.message
    })
  }
}
