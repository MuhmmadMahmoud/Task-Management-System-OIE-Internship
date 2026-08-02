import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Priority, Status, Task, TaskRequest } from '../models/task.model';

// I never send the username, the backend takes it from the token
@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private apiUrl = 'http://localhost:8080/api/tasks';

  constructor(private http: HttpClient) { }

  // a filter goes in the url only if the user chose one
  getTasks(status?: Status | '', priority?: Priority | ''): Observable<Task[]> {
    let params = new HttpParams();
    if (status) {
      params = params.set('status', status);
    }
    if (priority) {
      params = params.set('priority', priority);
    }
    return this.http.get<Task[]>(this.apiUrl, { params });
  }

  getTaskById(id: number): Observable<Task> {
    return this.http.get<Task>(this.apiUrl + '/' + id);
  }

  createTask(task: TaskRequest): Observable<Task> {
    return this.http.post<Task>(this.apiUrl, task);
  }

  updateTask(id: number, task: TaskRequest): Observable<Task> {
    return this.http.put<Task>(this.apiUrl + '/' + id, task);
  }

  deleteTask(id: number): Observable<void> {
    return this.http.delete<void>(this.apiUrl + '/' + id);
  }
}
