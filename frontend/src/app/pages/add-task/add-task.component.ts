import { Component, inject } from '@angular/core';
import { FormControl, ReactiveFormsModule, FormGroup, Validators } from '@angular/forms';
import { TaskService } from '../../services/task.service';
import { TaskRequest } from '../../models/task.model';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-add-task',
  imports: [ReactiveFormsModule, MatSnackBarModule],
  templateUrl: './add-task.component.html',
  styleUrl: './add-task.component.css'
})
export class AddTaskComponent {
  taskService = inject(TaskService);

  constructor(private snackBar: MatSnackBar) {}

  taskForm: FormGroup = new FormGroup({
    title: new FormControl('', [Validators.required, Validators.maxLength(50)]),
    description: new FormControl('', [Validators.maxLength(100)]),
    status: new FormControl('TODO') //TODO, IN PROGRESS, DONE
  })

  onAddTask() {
    if (this.taskForm.valid) {
      console.log(this.taskForm.value);
      
      const taskData: TaskRequest = this.taskForm.value;
      this.taskService.createTask(taskData).subscribe(response => {
        console.log('task created ', response);
        this.snackBar.open('Task created!', 'Close', {
          duration: 3000
        });
        this.resetForm();
      })
    }
  }

  resetForm() {
    this.taskForm.reset({
      title: '',
      description: '',
      status: 'TODO'
    });
  }
}
