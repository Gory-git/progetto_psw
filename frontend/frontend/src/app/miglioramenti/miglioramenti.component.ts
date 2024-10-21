import { Component } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Miglioramento } from '../modelli/Miglioramento';
import { CommonModule} from '@angular/common';
import { MiglioramentiService } from '../service/miglioramenti.service';

@Component({
  selector: 'app-miglioramenti',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './miglioramenti.component.html',
  styleUrl: './miglioramenti.component.css'
})

export class MiglioramentiComponent {

  text: string= ''
  miglioramenti: Miglioramento[] = [];
  constructor(private miglioramentiService: MiglioramentiService) { }

  ngOnInit() {
    this.tutti()
    //this.nome('a')
    //this.crediti(1)
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

  nome(nome: string) {
    const params = new HttpParams().set('nome', nome);
    this.miglioramentiService.nome(params).subscribe((response)=> {
      if (!Array.isArray(response)) {
        this.text = 'No results!'
        this.miglioramenti = []
      } else {
        this.miglioramenti = response
        this.text = ''
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

  acquire(miglioramento: string) {
    const params = new HttpParams().set('miglioramento', miglioramento);
    this.miglioramentiService.acquire(params).subscribe(
      response=> {
      this.text = response
    }, error => {
      this.text = error.message
    });
  }

}
