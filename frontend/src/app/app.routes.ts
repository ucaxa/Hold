import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'pessoas', pathMatch: 'full' },
  { path: 'pessoas', loadComponent: () => import('./pessoas/pessoas-list/pessoas-list.component').then(m => m.PessoasListComponent) },
  { path: '**', redirectTo: 'pessoas' },
];
