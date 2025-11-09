'use client';

import { useEffect, useState } from 'react';
import { Spinner } from '@heroui/react';
import { VideoCard, type VideoCardProps } from '@/components/video-card';
import { title } from '@/components/primitives';

export default function Home() {
  const [videos, setVideos] = useState<VideoCardProps[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchVideos = async () => {
      try {
        setIsLoading(true);
        const response = await fetch('/api/videos');

        if (!response.ok) {
          throw new Error('Failed to fetch videos');
        }

        const data = await response.json();
        setVideos(Array.isArray(data) ? data : []);
        setError(null);
      } catch (err) {
        console.error('Error fetching videos:', err);
        setError(
          err instanceof Error ? err.message : 'Failed to fetch videos'
        );
        setVideos([]);
      } finally {
        setIsLoading(false);
      }
    };

    fetchVideos();
  }, []);

  return (
    <section className="flex flex-col gap-8 py-8">
      <div className="flex flex-col items-center justify-center gap-4">
        <div className="inline-block max-w-lg text-center justify-center">
          <span className={title()}>The&nbsp;</span>
          <span className={title({ color: 'blue' })}>ultimate&nbsp;</span>
          <br />
          <span className={title()}>
            platform for video enthusiasts.
          </span>
        </div>
      </div>

      <div className="w-full">
        {isLoading ? (
          <div className="flex justify-center items-center min-h-[400px]">
            <Spinner size="xl" />
          </div>
        ) : error ? (
          <div className="text-center text-danger min-h-[400px] flex items-center justify-center">
            <div>
              <p className="text-lg font-semibold">Error loading videos</p>
              <p className="text-sm mt-2">{error}</p>
            </div>
          </div>
        ) : videos.length === 0 ? (
          <div className="text-center text-foreground-500 min-h-[400px] flex items-center justify-center">
            <p className="text-lg">No videos available</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {videos.map((video, index) => (
              <VideoCard key={`${video.title}-${index}`} {...video} />
            ))}
          </div>
        )}
      </div>
    </section>
  );
}
