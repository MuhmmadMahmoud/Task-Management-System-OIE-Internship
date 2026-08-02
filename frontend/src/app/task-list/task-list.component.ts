import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Priority, Status, Task } from '../models/task.model';
import { AuthService } from '../services/auth.service';
import { TaskService } from '../services/task.service';

@Component({
  selector: 'app-task-list',
  imports: [FormsModule, RouterLink],
  templateUrl: './task-list.component.html',
  styleUrl: './task-list.component.css'
})
export class TaskListComponent implements OnInit {

  tasks: Task[] = [];

  // empty means "All"
  selectedStatus: Status | '' = '';
  selectedPriority: Priority | '' = '';

  statusList: Status[] = ['TODO', 'IN_PROGRESS', 'DONE'];
  priorityList: Priority[] = ['LOW', 'MEDIUM', 'HIGH'];

  username = '';
  errorMessage = '';
  loading = false;

  constructor(private taskService: TaskService,
              private authService: AuthService,
              private router: Router) { }

  ngOnInit(): void {
    this.username = this.authService.getUsername() || '';
    this.loadTasks();
  }

  loadTasks(): void {
    this.loading = true;
    this.errorMessage = '';

    this.taskService.getTasks(this.selectedStatus, this.selectedPriority).subscribe({
      next: (tasks) => {
        this.tasks = tasks;
        this.loading = false;
      },
      error: (error) => {
        this.loading = false;
        this.errorMessage = error.error?.message || 'Could not load the tasks';
      }
    });
  }

  onFilterChange(): void {
    this.loadTasks();
  }

  clearFilters(): void {
    this.selectedStatus = '';
    this.selectedPriority = '';
    this.loadTasks();
  }

  deleteTask(task: Task): void {
    const sure = confirm('Are you sure you want to delete the task "' + task.title + '" ?');
    if (!sure) {
      return;
    }

    this.taskService.deleteTask(task.id).subscribe({
      next: () => this.loadTasks(),
      error: (error) => this.errorMessage = error.error?.message || 'Could not delete the task'
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  // gives every status and priority its own colour class
  statusClass(status: Status): string {
    return 'badge status-' + status.toLowerCase();
  }

  priorityClass(priority: Priority): string {
    return 'badge priority-' + priority.toLowerCase();
  }
}
