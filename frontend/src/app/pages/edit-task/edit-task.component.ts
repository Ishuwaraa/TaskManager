import { Component, inject, signal } from '@angular/core';
import { TaskService } from '../../services/task.service';
import { FormControl, ReactiveFormsModule, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { TaskRequest } from '../../models/task.model';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-edit-task',
  imports: [ReactiveFormsModule, MatSnackBarModule],
  templateUrl: './edit-task.component.html',
  styleUrl: './edit-task.component.css'
})
export class EditTaskComponent {
  taskService = inject(TaskService);
  statusOptions: string[] = ['TODO', 'IN PROGRESS', 'DONE'];
  taskId: number = 0;

  constructor(private route: ActivatedRoute, private snackBar: MatSnackBar) {}

  taskForm: FormGroup = new FormGroup({
    title: new FormControl('', [Validators.required, Validators.maxLength(50)]),
    description: new FormControl('', [Validators.maxLength(100)]),
    status: new FormControl('', [Validators.required]) //TODO, IN PROGRESS, DONE
  })

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.taskService.getTaskById(parseInt(id!)).subscribe(response => {
      console.log(response);
      this.taskId = response.id;
      this.taskForm = new FormGroup({
        title: new FormControl(response.title),
        description: new FormControl(response.description),
        status: new FormControl(response.status)
      });
    })
  }

  onEditTask() {
    if (this.taskForm.valid) {
      console.log(this.taskForm.value);
      console.log(this.taskId);

      const taskData: TaskRequest = this.taskForm.value;
      this.taskService.updateTask(this.taskId, taskData).subscribe(response => {
        console.log(response);
        this.snackBar.open('Task updated successfully!', 'Close', {
          duration: 3000
        });
      })
    }
  }
}
