import { useState, useEffect } from 'react';
import { getEnv } from '../utils/Env';
import { VideoCard } from './VideoCard.tsx';

interface Video {
  title: string;
  user: string;
  videoUrl: string;
  thumbnailUrl: string;
}

const VideoGrid = () => {
  const [videos, setVideos] = useState<Video[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetch(getEnv().API_BASE_URL + '/videos')
      .then((res) => {
        if (!res.ok) throw new Error('Failed to fetch videos');
        return res.json();
      })
      .then((data) => {
        setVideos(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, []);

  if (loading) return <div className="text-center py-8">Loading videos...</div>;
  if (error) return <div className="text-center py-8 text-red-600">Error: {error}</div>;
  if (!videos.length) return <div className="text-center py-8">No videos found</div>;

  return (
    <ul className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 p-4">
      {videos.map((video) => (
        <li key={video.videoUrl}>
          <VideoCard title={video.title} imageUrl={`${getEnv().MEDIA_BASE_URL}${video.thumbnailUrl}`} />
        </li>
      ))}
    </ul>
  );
};

export default VideoGrid;
