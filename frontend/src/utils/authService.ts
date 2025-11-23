import type { RegisterData, LoginData, AuthResponse, TokenResponse } from '../types/auth';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

export class AuthService {
  private static TOKEN_KEY = 'authToken';
  private static USER_KEY = 'userData';

  static async register(data: RegisterData): Promise<AuthResponse> {
    const response = await fetch(`${API_BASE_URL}/api/auth/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({
        message: response.statusText,
      }));
      throw new Error(errorData.message || 'Error en el registre');
    }

    const authResponse: AuthResponse = await response.json();
    this.saveAuthData(authResponse);
    return authResponse;
  }

  static async login(data: LoginData): Promise<AuthResponse> {
    console.log('[AuthService] Login started for user:', data.username);
    
    const response = await fetch(`${API_BASE_URL}/api/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({
        message: response.statusText,
      }));
      throw new Error(errorData.message || 'Error en l\'inici de sessió');
    }

    const tokenResponse: TokenResponse = await response.json();
    console.log('[AuthService] Token received, length:', tokenResponse.token.length);

    // Decodificar JWT para obtener userId
    const payload = this.decodeToken(tokenResponse.token);
    console.log('[AuthService] Decoded payload:', payload);
    
    const userId = payload?.sub ? parseInt(payload.sub) : 0;
    console.log('[AuthService] Extracted userId:', userId);

    // Guardar token y datos del usuario
    const authResponse: AuthResponse = {
      token: tokenResponse.token,
      userId: userId,
      username: data.username,
      email: payload?.email || '',
    };

    console.log('[AuthService] Saving auth data:', { userId, username: data.username, email: authResponse.email });
    this.saveAuthData(authResponse);
    
    // Verificar que se guardó correctamente
    const savedUserData = this.getUserData();
    console.log('[AuthService] Verified saved data:', savedUserData);
    
    return authResponse;
  }

  static logout(): void {
    if (typeof window !== 'undefined') {
      localStorage.removeItem(this.TOKEN_KEY);
      localStorage.removeItem(this.USER_KEY);
    }
  }

  static getToken(): string | null {
    if (typeof window === 'undefined') return null;
    return localStorage.getItem(this.TOKEN_KEY);
  }

  static isAuthenticated(): boolean {
    return !!this.getToken();
  }

  static getUserData(): { userId: number; username: string; email: string } | null {
    if (typeof window === 'undefined') return null;
    const userData = localStorage.getItem(this.USER_KEY);
    return userData ? JSON.parse(userData) : null;
  }

  private static saveAuthData(authResponse: AuthResponse): void {
    if (typeof window === 'undefined') return;
    localStorage.setItem(this.TOKEN_KEY, authResponse.token);
    localStorage.setItem(
      this.USER_KEY,
      JSON.stringify({
        userId: authResponse.userId,
        username: authResponse.username,
        email: authResponse.email,
      })
    );
  }

  private static saveToken(token: string): void {
    if (typeof window === 'undefined') return;
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  static async authFetch(input: RequestInfo, init?: RequestInit): Promise<Response> {
    const token = this.getToken();
    const headers = new Headers(init?.headers || {});

    if (token) {
      headers.set('Authorization', `Bearer ${token}`);
    }

    return fetch(input, { ...init, headers });
  }

  private static decodeToken(token: string): any {
    try {
      // Decodificar JWT (solo el payload, no valida la firma)
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );
      return JSON.parse(jsonPayload);
    } catch (error) {
      console.error('Error decoding token:', error);
      return null;
    }
  }
}

