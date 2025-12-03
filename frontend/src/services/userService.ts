import { AuthService } from '../utils/authService';
import type { User } from '../types/auth';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

export interface UpdateProfileData {
  description?: string;
  profilePictureUrl?: string;
  bannerUrl?: string;
}

export class UserService {
  static async getUserProfile(username: string): Promise<User> {
    const response = await fetch(`${API_BASE_URL}/api/users/${username}`);
    
    if (!response.ok) {
      throw new Error('Error fetching user profile');
    }
    
    return response.json();
  }

  static async updateMyProfile(data: UpdateProfileData): Promise<User> {
    const response = await AuthService.authFetch(`${API_BASE_URL}/api/users/me`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      throw new Error('Error updating profile');
    }

    return response.json();
  }
}

