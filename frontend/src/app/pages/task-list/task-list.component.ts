import { Component, inject, signal } from '@angular/core';
import { TaskService } from '../../services/task.service';
import { Task } from '../../models/task.model';
import { RouterLink } from '@angular/router';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-task-list',
  imports: [RouterLink, MatSnackBarModule],
  templateUrl: './task-list.component.html',
  styleUrl: './task-list.component.css'
})
export class TaskListComponent {
  taskService = inject(TaskService);

  constructor(private snackBar: MatSnackBar) {}

  ngOnInit() {
    this.fetchAllTasks();
  }

  tasks = signal<Task[]>([]);

  fetchAllTasks() {
    this.taskService.fetchAllTasks().subscribe(response => {
      this.tasks.set(response);
      console.log(response);
    })
  }

  onDeleteTask(id: number) {
    if (window.confirm('Are you sure you want to delete this task?')) {
      this.taskService.deleteTask(id).subscribe(response => {        
        this.snackBar.open('Task deleted successfully!', 'Close', {
          duration: 3000
        });
        this.fetchAllTasks();
      })
    }
  }
}
