import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { UserComponent } from './user/user.component';
import { MiglioramentiComponent } from './miglioramenti/miglioramenti.component';
import { SkinsComponent } from './skins/skins.component';
import { LoadingComponent } from './loading/loading.component';

export const routes: Routes = [
  { path: '', component: LoadingComponent },
  { path: 'user', component: UserComponent },
  { path: 'home', component: HomeComponent },
  { path: 'miglioramenti', component: MiglioramentiComponent },
  { path: 'skin', component: SkinsComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
