// these must stay the same as the DTO classes in the backend

export type Status = 'TODO' | 'IN_PROGRESS' | 'DONE';

export type Priority = 'LOW' | 'MEDIUM' | 'HIGH';

export interface Task {
  id: number;
  title: string;
  description: string;
  status: Status;
  priority: Priority;
  createdAt: string;
}

export interface TaskRequest {
  title: string;
  description: string;
  status: Status;
  priority: Priority;
}

export interface AuthResponse {
  token: string;
  username: string;
}
