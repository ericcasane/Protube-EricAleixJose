export interface RegisterData {
  name: string;
  surname: string;
  email: string;
  username: string;
  password: string;
}

export interface LoginData {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  userId: number;
  username: string;
  email: string;
}

export interface TokenResponse {
  token: string;
}

export interface User {
  userId: number;
  username: string;
  email: string;
}
