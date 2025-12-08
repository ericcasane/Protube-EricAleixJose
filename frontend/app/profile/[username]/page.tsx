'use client';

import { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';
import { Image } from '@heroui/image';
import { Button } from '@heroui/button';
import { Card, CardBody } from '@heroui/card';
import { Modal, ModalContent, ModalHeader, ModalBody, ModalFooter, useDisclosure } from '@heroui/modal';
import { Input, Textarea } from '@heroui/input';
import { useAuth } from '@/contexts/AuthContext';
import { UserService } from '@/src/services/userService';
import type { User } from '@/types/auth';

export default function ProfilePage() {
  const params = useParams();
  const { user: currentUser } = useAuth();
  const [profile, setProfile] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { isOpen, onOpen, onOpenChange } = useDisclosure();
  
  // Edit form state
  const [editForm, setEditForm] = useState({
    description: '',
    profilePictureUrl: '',
    bannerUrl: ''
  });
  const [isSaving, setIsSaving] = useState(false);

  const username = Array.isArray(params.username) ? params.username[0] : params.username;

  useEffect(() => {
    if (username) {
      fetchProfile();
    }
  }, [username]);

  const fetchProfile = async () => {
    try {
      setLoading(true);
      const data = await UserService.getUserProfile(username);
      setProfile(data);
      setEditForm({
        description: data.description || '',
        profilePictureUrl: data.profilePictureUrl || '',
        bannerUrl: data.bannerUrl || ''
      });
    } catch (err) {
      setError('Failed to load profile');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateProfile = async (onClose: () => void) => {
    try {
      setIsSaving(true);
      const updatedProfile = await UserService.updateMyProfile(editForm);
      setProfile(prev => prev ? { ...prev, ...updatedProfile } : updatedProfile);
      onClose();
    } catch (err) {
      console.error('Failed to update profile', err);
      // Show error toast or message
    } finally {
      setIsSaving(false);
    }
  };

  const isOwnProfile = currentUser?.username === username;

  if (loading) return <div className="flex justify-center p-10">Loading...</div>;
  if (error || !profile) return <div className="flex justify-center p-10 text-danger">{error || 'User not found'}</div>;

  return (
    <div className="w-full flex flex-col items-center">
      {/* Banner */}
      <div className="w-full h-48 md:h-64 relative bg-gray-200 overflow-hidden">
        {profile.bannerUrl ? (
            <Image
                src={profile.bannerUrl}
                alt="Banner"
                className="w-full h-full object-cover"
                width={1920}
                height={300}
            />
        ) : (
            <div className="w-full h-full bg-gradient-to-r from-blue-400 to-purple-500" />
        )}
      </div>

      <div className="w-full max-w-6xl px-4">
        <div className="flex flex-col md:flex-row items-start gap-6 -mt-16 mb-8 relative z-10">
          {/* Avatar */}
          <div className="rounded-full p-1 bg-background">
             <Image
                src={profile.profilePictureUrl || `https://ui-avatars.com/api/?name=${profile.name}+${profile.surname}&background=random`}
                alt={profile.username}
                className="w-32 h-32 md:w-40 md:h-40 rounded-full object-cover"
                width={160}
                height={160}
            />
          </div>

          {/* Info */}
          <div className="flex-1 pt-16 md:pt-20">
            <div className="flex justify-between items-start">
              <div>
                <h1 className="text-2xl md:text-3xl font-bold">{profile.name} {profile.surname}</h1>
                <p className="text-default-500">@{profile.username}</p>
              </div>
              {isOwnProfile && (
                <Button color="primary" variant="flat" onPress={onOpen}>
                  Edit Profile
                </Button>
              )}
            </div>
            
            {profile.description && (
              <div className="mt-4 text-default-700 max-w-2xl">
                {profile.description}
              </div>
            )}
          </div>
        </div>

        {/* Content Tabs/Section (Placeholder) */}
        <div className="mt-8 border-t border-divider pt-4">
          <h2 className="text-xl font-semibold mb-4">Videos</h2>
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
            <Card>
                <CardBody className="p-4">
                    <p className="text-center text-default-500">No videos yet</p>
                </CardBody>
            </Card>
          </div>
        </div>
      </div>

      {/* Edit Modal */}
      <Modal isOpen={isOpen} onOpenChange={onOpenChange} placement="center">
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="flex flex-col gap-1">Edit Profile</ModalHeader>
              <ModalBody>
                <Textarea
                  label="Description"
                  placeholder="Tell viewers about your channel"
                  value={editForm.description}
                  onChange={(e) => setEditForm({ ...editForm, description: e.target.value })}
                />
                <Input
                  label="Profile Picture URL"
                  placeholder="https://..."
                  value={editForm.profilePictureUrl}
                  onChange={(e) => setEditForm({ ...editForm, profilePictureUrl: e.target.value })}
                />
                <Input
                  label="Banner URL"
                  placeholder="https://..."
                  value={editForm.bannerUrl}
                  onChange={(e) => setEditForm({ ...editForm, bannerUrl: e.target.value })}
                />
              </ModalBody>
              <ModalFooter>
                <Button color="danger" variant="light" onPress={onClose}>
                  Cancel
                </Button>
                <Button color="primary" onPress={() => handleUpdateProfile(onClose)} isLoading={isSaving}>
                  Save
                </Button>
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </div>
  );
}

