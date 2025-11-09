'use client';

import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { AuthService } from '../utils/authService';
import type { RegisterData, LoginData, User } from '../types/auth';

interface AuthContextType {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (data: LoginData) => Promise<void>;
  register: (data: RegisterData) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // Verificar si hay un usuario autenticado al cargar
    const userData = AuthService.getUserData();
    if (userData) {
      setUser(userData);
    }
    setIsLoading(false);
  }, []);

  const login = async (data: LoginData) => {
    const response = await AuthService.login(data);
    setUser({
      userId: response.userId,
      username: response.username,
      email: response.email,
    });
  };

  const register = async (data: RegisterData) => {
    const response = await AuthService.register(data);
    setUser({
      userId: response.userId,
      username: response.username,
      email: response.email,
    });
  };

  const logout = () => {
    AuthService.logout();
    setUser(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated: !!user,
        isLoading,
        login,
        register,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}

