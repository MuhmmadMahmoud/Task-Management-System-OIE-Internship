import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { TaskFormComponent } from './task-form/task-form.component';
import { TaskListComponent } from './task-list/task-list.component';
import { authGuard } from './services/auth.guard';

// the task pages have canActivate, so nobody opens them without a login
export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  { path: 'tasks', component: TaskListComponent, canActivate: [authGuard] },
  { path: 'tasks/new', component: TaskFormComponent, canActivate: [authGuard] },
  { path: 'tasks/edit/:id', component: TaskFormComponent, canActivate: [authGuard] },

  { path: '**', redirectTo: 'login' }
];
