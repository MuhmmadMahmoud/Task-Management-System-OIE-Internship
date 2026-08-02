import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Priority, Status, TaskRequest } from '../models/task.model';
import { TaskService } from '../services/task.service';

// one component for create and edit, an id in the url means edit
@Component({
  selector: 'app-task-form',
  imports: [FormsModule],
  templateUrl: './task-form.component.html',
  styleUrl: './task-form.component.css'
})
export class TaskFormComponent implements OnInit {

  task: TaskRequest = {
    title: '',
    description: '',
    status: 'TODO',
    priority: 'MEDIUM'
  };

  statusList: Status[] = ['TODO', 'IN_PROGRESS', 'DONE'];
  priorityList: Priority[] = ['LOW', 'MEDIUM', 'HIGH'];

  taskId: number | null = null;
  isEditMode = false;

  errorMessage = '';
  loading = false;

  constructor(private taskService: TaskService,
              private route: ActivatedRoute,
              private router: Router) { }

  ngOnInit(): void {
    const idFromUrl = this.route.snapshot.paramMap.get('id');

    if (idFromUrl) {
      this.isEditMode = true;
      this.taskId = Number(idFromUrl);
      this.loadTheTask();
    }
  }

  loadTheTask(): void {
    this.loading = true;

    this.taskService.getTaskById(this.taskId!).subscribe({
      next: (task) => {
        this.task = {
          title: task.title,
          description: task.description,
          status: task.status,
          priority: task.priority
        };
        this.loading = false;
      },
      error: (error) => {
        this.loading = false;
        this.errorMessage = error.error?.message || 'Could not find this task';
      }
    });
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.loading = true;

    if (this.isEditMode) {
      this.taskService.updateTask(this.taskId!, this.task).subscribe({
        next: () => this.goBackToTheList(),
        error: (error) => this.showError(error)
      });
    } else {
      this.taskService.createTask(this.task).subscribe({
        next: () => this.goBackToTheList(),
        error: (error) => this.showError(error)
      });
    }
  }

  goBackToTheList(): void {
    this.router.navigate(['/tasks']);
  }

  private showError(error: any): void {
    this.loading = false;
    this.errorMessage = error.error?.message || 'Could not save the task';
  }
}
