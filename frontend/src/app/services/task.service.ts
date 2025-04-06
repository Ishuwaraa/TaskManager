import { Injectable, signal } from '@angular/core';
import { Task, TaskRequest } from '../models/task.model';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private readonly apiUrl = "http://localhost:8080/api/task";

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
    return this.http.get<Task[]>(`${this.apiUrl}/`);
  }

  getTaskById(id: number) {
    return this.http.get<Task>(`${this.apiUrl}/${id}`);
  }

  createTask(task: TaskRequest) {
    return this.http.post(`${this.apiUrl}/`, task);
  }

  updateTask(id: number, task: TaskRequest) {
    return this.http.put(`${this.apiUrl}/${id}`, task);
  }

  deleteTask(id: number) {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}
