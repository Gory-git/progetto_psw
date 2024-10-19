import { Component } from '@angular/core';
import { Skin } from '../modelli/Skin';
import { SkinsService } from '../service/skins.service';
import { HttpParams } from '@angular/common/http';

@Component({
  selector: 'app-skins',
  standalone: true,
  imports: [],
  templateUrl: './skins.component.html',
  styleUrl: './skins.component.css'
})
export class SkinsComponent {
  text: string= ''
  skins: Skin[] = [];
  constructor(private skinsService: SkinsService) { }

  ngOnInit() {
    
  }

  possedute() {
    this.skinsService.possedute().subscribe((response)=> {
      if (!Array.isArray(response)) {
        this.text = 'No results!'
        this.skins = []
      } else {
        this.skins = response
        this.text = ''
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
        this.text = ''
      }
    })
  }

  acqire(nome: string) {
    const params = new HttpParams().set('nome', nome);
    this.skinsService.acquire(params).subscribe((response)=> {
      if (!Array.isArray(response)) {
        this.text = 'No results!'
        this.skins = []
      } else {
        this.skins = response
        this.text = ''
      }
    })
  }

  add() {
    
  }
}
