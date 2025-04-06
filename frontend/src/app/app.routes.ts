import { Routes } from '@angular/router';
import { TaskListComponent } from './pages/task-list/task-list.component';
import { AddTaskComponent } from './pages/add-task/add-task.component';
import { LoginComponent } from './auth/login/login.component';
import { SignupComponent } from './auth/signup/signup.component';
import { EditTaskComponent } from './pages/edit-task/edit-task.component';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        component: TaskListComponent
    },
    {
        path: 'add-task',
        component: AddTaskComponent
    },
    {
        path: 'edit-task/:id',
        component: EditTaskComponent
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
