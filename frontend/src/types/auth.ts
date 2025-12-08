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
  userId?: number; // Optional because profile response might not have ID
  username: string;
  name: string;
  surname: string;
  email: string;
  description?: string;
  profilePictureUrl?: string;
  bannerUrl?: string;
}
