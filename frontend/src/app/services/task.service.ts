import { Injectable, signal } from '@angular/core';
import { Task } from '../models/task.model';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  constructor(private http: HttpClient) { }

  tasks = signal<Task[]>([
    {
      id: 10,
      title: 'task1',
      description: 'my task',
      status: 'todo',
    },
    {
      id: 11,
      title: 'task2',
      description: 'my task',
      status: 'inprogress',
    },
    {
      id: 12,
      title: 'task3',
      description: 'my task',
      status: 'dones',
    },
  ])

  fetchAllTasks() {
    return this.http.get<Task[]>('http://localhost:8080/api/task/');
  }
}
