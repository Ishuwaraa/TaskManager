import { Component, inject, signal } from '@angular/core';
import { TaskService } from '../../services/task.service';
import { Task } from '../../models/task.model';

@Component({
  selector: 'app-task-list',
  imports: [],
  templateUrl: './task-list.component.html',
  styleUrl: './task-list.component.css'
})
export class TaskListComponent {
  taskService = inject(TaskService);

  ngOnInit() {
    this.taskService.fetchAllTasks().subscribe(response => {
      this.tasks.set(response);
      console.log(response);
    })
  }

  tasks = signal<Task[]>([]);
}
