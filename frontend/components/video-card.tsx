"use client";

import { Card, Avatar, Chip } from "@heroui/react";
import Image from "next/image";
import Link from "next/link";
import { useState, useEffect, useRef } from "react";

import { useTranslations } from "@/hooks/useTranslations";

export interface VideoCardProps {
  id?: string;
  title: string;
  channelName: string;
  videoUrl: string;
  thumbnailUrl: string;
  duration?: number; // en segundos
  viewCount?: number;
  timestamp?: number; // unix timestamp
}

// Función para formatear números grandes
function formatViewCount(count: number): string {
  if (count >= 1000000000) {
    return (count / 1000000000).toFixed(1) + "B";
  }
  if (count >= 1000000) {
    return (count / 1000000).toFixed(1) + "M";
  }
  if (count >= 1000) {
    return (count / 1000).toFixed(1) + "K";
  }

  return count.toString();
}

// Función para formatear duración a minutos:segundos
function formatDuration(seconds: number): string {
  const mins = Math.floor(seconds / 60);
  const secs = seconds % 60;

  return `${mins}:${secs.toString().padStart(2, "0")}`;
}

// Función para formatear fecha con soporte para traducción
function formatDate(
  timestamp: number,
  t: (key: string, options?: Record<string, string | number>) => string,
): string {
  const date = new Date(timestamp * 1000);
  const now = new Date();
  const diffMs = now.getTime() - date.getTime();
  const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));

  if (diffDays === 0) {
    const diffHours = Math.floor(diffMs / (1000 * 60 * 60));

    if (diffHours === 0) {
      const diffMins = Math.floor(diffMs / (1000 * 60));

      return diffMins <= 0
        ? t("video.timeAgo.justNow")
        : t("video.timeAgo.minutesAgo", { count: diffMins });
    }

    return t("video.timeAgo.hoursAgo", { count: diffHours });
  }
  if (diffDays < 7) return t("video.timeAgo.daysAgo", { count: diffDays });
  if (diffDays < 30) {
    const weeks = Math.floor(diffDays / 7);

    return t("video.timeAgo.weeksAgo", { count: weeks });
  }
  if (diffDays < 365) {
    const months = Math.floor(diffDays / 30);

    return t("video.timeAgo.monthsAgo", { count: months });
  }
  const years = Math.floor(diffDays / 365);

  return t("video.timeAgo.yearsAgo", { count: years });
}

export function VideoCard({
  id,
  title,
  channelName,
  videoUrl,
  thumbnailUrl,
  duration = 0,
  viewCount = 0,
  timestamp = Math.floor(Date.now() / 1000),
}: VideoCardProps) {
  const [isHovered, setIsHovered] = useState(false);
  const [shouldPlayVideo, setShouldPlayVideo] = useState(false);
  const videoRef = useRef<HTMLVideoElement>(null);
  const hoverTimeoutRef = useRef<NodeJS.Timeout | null>(null);
  const t = useTranslations();

  const mediaUrl = `/media/${thumbnailUrl}`;
  const videoMediaUrl = `/media/${videoUrl}`;
  const formattedDuration = formatDuration(duration);
  const formattedViews = formatViewCount(viewCount);
  const formattedDate = formatDate(timestamp, t);

  useEffect(() => {
    if (isHovered) {
      hoverTimeoutRef.current = setTimeout(() => {
        setShouldPlayVideo(true);
      }, 1000);
    } else {
      if (hoverTimeoutRef.current) {
        clearTimeout(hoverTimeoutRef.current);
      }
      setShouldPlayVideo(false);
      if (videoRef.current) {
        videoRef.current.pause();
        videoRef.current.currentTime = 0;
      }
    }

    return () => {
      if (hoverTimeoutRef.current) {
        clearTimeout(hoverTimeoutRef.current);
      }
    };
  }, [isHovered]);

  return (
    <Link className="block" href={`/video/${id}`}>
      <Card className="w-full overflow-hidden transition-all duration-200 rounded-2xl hover:shadow-lg hover:-translate-y-1 p-0">
        <Card.Header className="flex flex-col items-start p-2 pb-0 m-0">
          <div
            className="relative w-full aspect-video overflow-hidden m-0 rounded-xl cursor-pointer"
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
          >
            {shouldPlayVideo ? (
              <video
                ref={videoRef}
                autoPlay
                loop
                muted
                playsInline
                className="w-full h-full object-cover"
                preload="auto"
              >
                <source src={videoMediaUrl} type="video/mp4" />
              </video>
            ) : (
              <Image
                fill
                priority
                alt={title}
                className="object-cover transition-transform duration-300 group-hover:scale-105"
                src={mediaUrl}
              />
            )}

            {duration > 0 && (
              <Chip
                className="absolute bottom-2 right-2 bg-black/80 text-white text-xs font-semibold"
                size="sm"
                variant="soft"
              >
                {formattedDuration}
              </Chip>
            )}
          </div>
        </Card.Header>

        <Card.Content className="flex flex-col gap-3 px-3 py-3 pt-0">
          <h3 className="font-semibold text-sm line-clamp-2 hover:text-accent transition-colors cursor-pointer leading-tight">
            {title}
          </h3>

          <div className="flex items-center gap-2.5">
            <Avatar className="rounded-lg" size="sm">
              <Avatar.Fallback>
                {channelName.charAt(0).toUpperCase()}
              </Avatar.Fallback>
            </Avatar>
            <div className="flex flex-col gap-0.5 min-w-0">
              <p className="text-xs font-medium text-foreground truncate hover:text-accent transition-colors cursor-pointer">
                {channelName}
              </p>
              <p className="text-xs text-neutral-500">
                {formattedViews} {t("video.viewsLabel")} • {formattedDate}
              </p>
            </div>
          </div>
        </Card.Content>
      </Card>
    </Link>
  );
}
