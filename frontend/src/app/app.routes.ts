import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { SignupComponent } from './auth/signup/signup.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        loadComponent: () => import('./pages/task-list/task-list.component')
            .then(m => m.TaskListComponent),
        canActivate: [AuthGuard]
    },
    {
        path: 'add-task',
        loadComponent: () => import('./pages/add-task/add-task.component')
            .then(m => m.AddTaskComponent),
        canActivate: [AuthGuard]
    },
    {
        path: 'edit-task/:id',
        loadComponent: () => import('./pages/edit-task/edit-task.component')
            .then(m => m.EditTaskComponent),
        canActivate: [AuthGuard]
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'signup',
        component: SignupComponent
    }
];
