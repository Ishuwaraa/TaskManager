import { Component, computed, inject, signal } from '@angular/core';
import { TaskService } from '../../services/task.service';
import { Task } from '../../models/task.model';
import { RouterLink } from '@angular/router';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';

@Component({
  selector: 'app-task-list',
  imports: [RouterLink, MatSnackBarModule, MatIconModule, MatTableModule, MatButtonModule, CommonModule, MatSelectModule, MatFormFieldModule],
  templateUrl: './task-list.component.html',
  styleUrl: './task-list.component.css'
})
export class TaskListComponent {
  taskService = inject(TaskService);
  displayedColumns: string[] = ['title', 'description', 'date', 'status', 'action'];

  constructor(private snackBar: MatSnackBar) {}

  ngOnInit() {
    this.fetchAllTasks();
  }

  tasks = signal<Task[]>([]);
  filteredTasks = computed(() => {
    if (this.selectedStatus() === 'ALL') {
      return this.tasks();
    }
    return this.tasks().filter(task => task.status === this.selectedStatus());
  });
  selectedStatus = signal<string>('ALL');

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

  filterTasks(status: string) {
    this.selectedStatus.set(status);
  }
}
