import { Injectable, signal } from '@angular/core';
import { Task, TaskRequest } from '../models/task.model';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private readonly apiUrl = "http://localhost:8080/api/task";

  constructor(private http: HttpClient) { }  

  fetchAllTasks() {
    const accessToken = localStorage.getItem('accessToken');
    const headers = {
      'Authorization': `Bearer ${accessToken}`
    };

    return this.http.get<Task[]>(`${this.apiUrl}/`, { headers });
  }

  getTaskById(id: number) {
    const accessToken = localStorage.getItem('accessToken');
    const headers = {
      'Authorization': `Bearer ${accessToken}`
    };

    return this.http.get<Task>(`${this.apiUrl}/${id}`, { headers });
  }

  createTask(task: TaskRequest) {
    const accessToken = localStorage.getItem('accessToken');
    const headers = {
      'Authorization': `Bearer ${accessToken}`
    };

    return this.http.post(`${this.apiUrl}/`, task, { headers });
  }

  updateTask(id: number, task: TaskRequest) {
    const accessToken = localStorage.getItem('accessToken');
    const headers = {
      'Authorization': `Bearer ${accessToken}`
    };

    return this.http.put(`${this.apiUrl}/${id}`, task, { headers });
  }

  deleteTask(id: number) {
    const accessToken = localStorage.getItem('accessToken');
    const headers = {
      'Authorization': `Bearer ${accessToken}`
    };

    return this.http.delete(`${this.apiUrl}/${id}`, { headers, responseType: 'text' });
  }
}
