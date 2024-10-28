import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-loading',
  standalone: true,
  imports: [],
  templateUrl: './loading.component.html',
  styleUrl: './loading.component.css'
})
export class LoadingComponent {

  constructor(private router: Router) {}

  ngOnInit() {
    setTimeout(()=>{
      this.router.navigate(['/user'])
    }, 1000);
    
  }
}
