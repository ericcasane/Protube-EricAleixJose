'use client';

import { Card, Avatar } from '@heroui/react';
import Image from 'next/image';

export interface VideoCardProps {
  id?: string;
  title: string;
  user: string;
  videoUrl: string;
  thumbnailUrl: string;
}

export function VideoCard({
  id,
  title,
  user,
  videoUrl,
  thumbnailUrl,
}: VideoCardProps) {
  const mediaUrl = `/media${thumbnailUrl}`;

  return (
    <Card className="w-full">
      <Card.Header className="flex flex-col items-start px-0 py-0">
        <div className="relative w-full aspect-video overflow-hidden rounded-t-lg">
          <Image
            src={mediaUrl}
            alt={title}
            fill
            className="object-cover"
          />
          <div className="absolute inset-0 bg-black/20 hover:bg-black/10 transition-colors" />
        </div>
      </Card.Header>

      <Card.Content className="flex flex-col gap-3 py-4">
        <h3 className="font-semibold text-lg line-clamp-2 hover:text-accent transition-colors">
          {title}
        </h3>

        <div className="flex items-center gap-3">
          <Avatar size="sm">
            <Avatar.Fallback className="bg-accent text-white">
              {user.charAt(0).toUpperCase()}
            </Avatar.Fallback>
          </Avatar>
          <div className="flex flex-col gap-1">
            <p className="text-sm font-medium text-foreground">{user}</p>
          </div>
        </div>
      </Card.Content>
    </Card>
  );
}
