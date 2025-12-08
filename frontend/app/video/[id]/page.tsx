'use client';

import { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';
import { Spinner, Avatar, Button, Card } from '@heroui/react';
import { VideoDetail, VideoListItem, VideoReactionResponse } from '@/src/types/video';
import { VideoCard } from '@/components/video-card';
import { useAuth } from '@/contexts/AuthContext';
import { AuthService } from '@/utils/authService';
import { useTranslations } from '@/hooks/useTranslations';

// SVG Icons
const ThumbsUpIcon = ({ className }: { className?: string }) => (
  <svg
    className={className}
    fill="none"
    height="20"
    stroke="currentColor"
    strokeLinecap="round"
    strokeLinejoin="round"
    strokeWidth="2"
    viewBox="0 0 24 24"
    width="20"
  >
    <path d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3zM7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3" />
  </svg>
);

const ThumbsDownIcon = ({ className }: { className?: string }) => (
  <svg
    className={className}
    fill="none"
    height="20"
    stroke="currentColor"
    strokeLinecap="round"
    strokeLinejoin="round"
    strokeWidth="2"
    viewBox="0 0 24 24"
    width="20"
  >
    <path d="M10 15v4a3 3 0 0 0 3 3l4-9V2H5.72a2 2 0 0 0-2 1.7l-1.38 9a2 2 0 0 0 2 2.3zm7-13h2.67A2.31 2.31 0 0 1 22 4v7a2.31 2.31 0 0 1-2.33 2H17" />
  </svg>
);

const ShareIcon = ({ className }: { className?: string }) => (
  <svg
    className={className}
    fill="none"
    height="20"
    stroke="currentColor"
    strokeLinecap="round"
    strokeLinejoin="round"
    strokeWidth="2"
    viewBox="0 0 24 24"
    width="20"
  >
    <circle cx="18" cy="5" r="3" />
    <circle cx="6" cy="12" r="3" />
    <circle cx="18" cy="19" r="3" />
    <line x1="8.59" x2="15.42" y1="13.51" y2="17.49" />
    <line x1="15.41" x2="8.59" y1="6.51" y2="10.49" />
  </svg>
);

const DownloadIcon = ({ className }: { className?: string }) => (
  <svg
    className={className}
    fill="none"
    height="20"
    stroke="currentColor"
    strokeLinecap="round"
    strokeLinejoin="round"
    strokeWidth="2"
    viewBox="0 0 24 24"
    width="20"
  >
    <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
    <polyline points="7 10 12 15 17 10" />
    <line x1="12" x2="12" y1="15" y2="3" />
  </svg>
);

const MoreIcon = ({ className }: { className?: string }) => (
  <svg
    className={className}
    fill="none"
    height="20"
    stroke="currentColor"
    strokeLinecap="round"
    strokeLinejoin="round"
    strokeWidth="2"
    viewBox="0 0 24 24"
    width="20"
  >
    <circle cx="12" cy="12" r="1" />
    <circle cx="19" cy="12" r="1" />
    <circle cx="5" cy="12" r="1" />
  </svg>
);

// Función para formatear números grandes
function formatNumber(count: number): string {
  if (count >= 1000000000) {
    return (count / 1000000000).toFixed(1) + 'B';
  }
  if (count >= 1000000) {
    return (count / 1000000).toFixed(1) + 'M';
  }
  if (count >= 1000) {
    return (count / 1000).toFixed(1) + 'K';
  }
  return count.toString();
}

// Función para formatear fecha
function formatDate(timestamp: number): string {
  const date = new Date(timestamp * 1000);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
  });
}

// Función para formatear fecha de comentarios
function formatCommentDate(timestamp: number, t: (key: string, options?: Record<string, string | number>) => string): string {
  const date = new Date(timestamp * 1000);
  const now = new Date();
  const diffMs = now.getTime() - date.getTime();
  const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));

  if (diffDays === 0) {
    const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
    if (diffHours === 0) {
      const diffMins = Math.floor(diffMs / (1000 * 60));
      return diffMins <= 0 ? t('video.timeAgo.justNow') : t('video.timeAgo.minutesAgo', { count: diffMins });
    }
    return t('video.timeAgo.hoursAgo', { count: diffHours });
  }
  if (diffDays < 7) return t('video.timeAgo.daysAgo', { count: diffDays });
  if (diffDays < 30) {
    const weeks = Math.floor(diffDays / 7);
    return t('video.timeAgo.weeksAgo', { count: weeks });
  }
  if (diffDays < 365) {
    const months = Math.floor(diffDays / 30);
    return t('video.timeAgo.monthsAgo', { count: months });
  }
  const years = Math.floor(diffDays / 365);
  return t('video.timeAgo.yearsAgo', { count: years });
}

export default function VideoPage() {
  const params = useParams();
  const videoId = params.id as string;
  const t = useTranslations();
  const { isAuthenticated, isLoading: isAuthLoading } = useAuth();

  const [video, setVideo] = useState<VideoDetail | null>(null);
  const [recommendedVideos, setRecommendedVideos] = useState<VideoListItem[]>(
    []
  );
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showFullDescription, setShowFullDescription] = useState(false);
  const [isLikeLoading, setIsLikeLoading] = useState(false);
  const [isDislikeLoading, setIsDislikeLoading] = useState(false);
  const [commentText, setCommentText] = useState('');
  const [isCommentSubmitting, setIsCommentSubmitting] = useState(false);

  useEffect(() => {
    const fetchVideoDetail = async () => {
      try {
        setIsLoading(true);

        // Fetch video detail
        console.log('Fetching video with ID:', videoId);
        const videoResponse = await fetch(`/api/videos/${videoId}`);
        console.log('Video response status:', videoResponse.status);

        if (!videoResponse.ok) {
          const errorText = await videoResponse.text();
          console.error('Video fetch error:', errorText);
          throw new Error(`Failed to fetch video: ${videoResponse.status} ${errorText}`);
        }

        const videoData = await videoResponse.json();
        console.log('Video data received:', videoData);
        setVideo(videoData);

        // Fetch all videos for recommendations
        const videosResponse = await fetch('/api/videos');
        if (videosResponse.ok) {
          const allVideos = await videosResponse.json();
          // Filter out current video and take first 10
          const filtered = allVideos
            .filter((v: VideoListItem) => v.id !== videoId)
            .slice(0, 10);
          setRecommendedVideos(filtered);
        }

        setError(null);
      } catch (err) {
        console.error('Error fetching video:', err);
        setError(err instanceof Error ? err.message : 'Failed to fetch video');
      } finally {
        setIsLoading(false);
      }
    };

    if (videoId) {
      fetchVideoDetail();
    }
  }, [videoId]);

  const handleLike = async () => {
    const token = AuthService.getToken();
    const userData = AuthService.getUserData();

    console.log('Like button clicked:');
    console.log('- isAuthenticated:', isAuthenticated);
    console.log('- token exists:', !!token);
    console.log('- userData:', userData);

    if (!isAuthenticated || !token) {
      alert(t('video.loginRequired') || 'Please login to like videos');
      return;
    }

    try {
      setIsLikeLoading(true);

      const response = await fetch(`/api/videos/${videoId}/like`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
      });

      if (response.status === 401) {
        alert(t('video.loginRequired') || 'Please login to like videos');
        return;
      }

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        console.error('Like error:', errorData);
        throw new Error('Failed to like video');
      }

      const reactionData: VideoReactionResponse = await response.json();

      // Update video state with new counts and user reaction
      setVideo(prev => prev ? {
        ...prev,
        likeCount: reactionData.likesCount,
        dislikeCount: reactionData.dislikesCount,
        userReaction: reactionData.userReaction,
      } : null);
    } catch (error) {
      console.error('Error liking video:', error);
      alert(t('video.errorLiking') || 'Failed to like video. Please try again.');
    } finally {
      setIsLikeLoading(false);
    }
  };

  const handleDislike = async () => {
    const token = AuthService.getToken();
    const userData = AuthService.getUserData();

    console.log('Dislike button clicked:');
    console.log('- isAuthenticated:', isAuthenticated);
    console.log('- token exists:', !!token);
    console.log('- userData:', userData);

    if (!isAuthenticated || !token) {
      alert(t('video.loginRequired') || 'Please login to dislike videos');
      return;
    }

    try {
      setIsDislikeLoading(true);

      const response = await fetch(`/api/videos/${videoId}/dislike`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
      });

      if (response.status === 401) {
        alert(t('video.loginRequired') || 'Please login to dislike videos');
        return;
      }

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        console.error('Dislike error:', errorData);
        throw new Error('Failed to dislike video');
      }

      const reactionData: VideoReactionResponse = await response.json();

      // Update video state with new counts and user reaction
      setVideo(prev => prev ? {
        ...prev,
        likeCount: reactionData.likesCount,
        dislikeCount: reactionData.dislikesCount,
        userReaction: reactionData.userReaction,
      } : null);
    } catch (error) {
      console.error('Error disliking video:', error);
      alert(t('video.errorDisliking') || 'Failed to dislike video. Please try again.');
    } finally {
      setIsDislikeLoading(false);
    }
  };

  const handleAddComment = async (e: React.FormEvent) => {
    e.preventDefault();

    const token = AuthService.getToken();
    const userData = AuthService.getUserData();

    if (!isAuthenticated || !token) {
      alert(t('video.loginRequired') || 'Please login to comment');
      return;
    }

    if (!commentText.trim()) {
      return;
    }

    try {
      setIsCommentSubmitting(true);

      const response = await fetch(`/api/videos/${videoId}/comments`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({ text: commentText }),
      });

      if (response.status === 401) {
        alert(t('video.loginRequired') || 'Please login to comment');
        return;
      }

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        console.error('Comment error:', errorData);
        throw new Error('Failed to add comment');
      }

      const newComment = await response.json();

      // Add new comment to the beginning of the comments list
      setVideo(prev => prev ? {
        ...prev,
        comments: [newComment, ...(prev.comments || [])],
      } : null);

      // Clear the input
      setCommentText('');
    } catch (error) {
      console.error('Error adding comment:', error);
      alert(t('video.errorComment') || 'Failed to add comment. Please try again.');
    } finally {
      setIsCommentSubmitting(false);
    }
  };

  if (isLoading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <Spinner size="xl" />
      </div>
    );
  }

  if (error || !video) {
    return (
      <div className="text-center text-danger min-h-screen flex items-center justify-center">
        <div>
          <p className="text-lg font-semibold">{t('video.errorLoading')}</p>
          <p className="text-sm mt-2">{error || t('video.notFound')}</p>
        </div>
      </div>
    );
  }

  const videoUrl = `/media/${video.videoUrl}`;

  return (
    <div className="flex flex-col lg:flex-row gap-6 py-4">
      {/* Main Video Section */}
      <div className="flex-1">
        {/* Video Player */}
        <div className="w-full aspect-video bg-black rounded-2xl overflow-hidden mb-4">
          <video className="w-full h-full" controls autoPlay>
            <source src={videoUrl} type="video/mp4" />
            {t('video.browserNotSupported')}
          </video>
        </div>

        {/* Video Title */}
        <h1 className="text-2xl font-bold mb-3">{video.title}</h1>

        {/* Video Stats and Actions */}
        <div className="flex flex-wrap items-center justify-between gap-4 mb-4">
          <div className="flex items-center gap-4">
            {/* Channel Info */}
            <div className="flex items-center gap-3">
              <Avatar size="lg" className="rounded-full">
                <Avatar.Fallback>
                  {video.channel?.name?.charAt(0).toUpperCase() || 'C'}
                </Avatar.Fallback>
              </Avatar>
              <div>
                <p className="font-semibold">{video.channel?.name}</p>
                {video.channel?.followerCount && (
                  <p className="text-sm text-foreground-500">
                    {formatNumber(video.channel.followerCount)} {t('video.subscribers')}
                  </p>
                )}
              </div>
            </div>
            <Button className="px-6 rounded-full bg-primary text-white">
              {t('video.subscribe')}
            </Button>
          </div>

          {/* Action Buttons */}
          <div className="flex items-center gap-2">
            <div className="flex items-center bg-default-100 rounded-full overflow-hidden">
              <button
                onClick={handleLike}
                disabled={isLikeLoading}
                className={`flex items-center gap-2 px-4 py-2 hover:bg-default-200 transition-colors ${video.userReaction === 'LIKE' ? 'bg-blue-500/20 text-blue-500' : ''
                  }`}
              >
                <ThumbsUpIcon />
                <span className="text-sm font-medium">
                  {formatNumber(video.likeCount)}
                </span>
              </button>
              <div className="w-px h-6 bg-default-300" />
              <button
                onClick={handleDislike}
                disabled={isDislikeLoading}
                className={`flex items-center gap-2 px-4 py-2 hover:bg-default-200 transition-colors ${video.userReaction === 'DISLIKE' ? 'bg-red-500/20 text-red-500' : ''
                  }`}
              >
                <ThumbsDownIcon />
                <span className="text-sm font-medium">
                  {formatNumber(video.dislikeCount)}
                </span>
              </button>
            </div>
            <Button className="rounded-full bg-secondary text-white">
              <ShareIcon />
              <span className="ml-2">{t('video.share')}</span>
            </Button>
            <Button className="rounded-full bg-secondary text-white">
              <DownloadIcon />
              <span className="ml-2">{t('video.download')}</span>
            </Button>
            <Button className="rounded-full px-3 bg-secondary text-white">
              <MoreIcon />
            </Button>
          </div>
        </div>

        {/* Video Description */}
        <Card className="p-4 bg-default-50">
          <div className="flex gap-3 text-sm font-semibold mb-2">
            <span>{formatNumber(video.viewCount)} {t('video.viewsLabel')}</span>
            <span>{formatDate(video.timestamp)}</span>
          </div>
          <div
            className={`text-sm whitespace-pre-wrap ${!showFullDescription ? 'line-clamp-3' : ''}`}
          >
            {video.description}
          </div>
          {video.description && video.description.length > 200 && (
            <button
              onClick={() => setShowFullDescription(!showFullDescription)}
              className="text-sm font-semibold mt-2 hover:text-primary transition-colors"
            >
              {showFullDescription ? t('video.showLess') : t('video.showMore')}
            </button>
          )}
        </Card>

        {/* Comments Section */}
        <div className="mt-6">
          <h2 className="text-xl font-bold mb-4">
            {t('video.comments', { count: video.comments?.length || 0 })}
          </h2>

          {/* Comment Input */}
          <form onSubmit={handleAddComment} className="flex gap-3 mb-6">
            <Avatar size="md" className="rounded-full">
              <Avatar.Fallback>
                {AuthService.getUserData()?.username?.charAt(0).toUpperCase() || 'U'}
              </Avatar.Fallback>
            </Avatar>
            <div className="flex-1">
              <input
                type="text"
                placeholder={t('video.addComment')}
                value={commentText}
                onChange={(e) => setCommentText(e.target.value)}
                disabled={isCommentSubmitting || !isAuthenticated}
                className="w-full bg-transparent border-b-2 border-default-200 focus:border-primary outline-none pb-2 transition-colors disabled:opacity-50"
              />
              {commentText.trim() && (
                <div className="flex justify-end gap-2 mt-2">
                  <Button
                    type="button"
                    onClick={() => setCommentText('')}
                    isDisabled={isCommentSubmitting}
                  >
                    {t('video.cancel')}
                  </Button>
                  <Button
                    type="submit"
                    className="bg-primary text-white"
                    size="sm"
                    isDisabled={isCommentSubmitting}
                  >
                    {isCommentSubmitting ? t('video.commenting') : t('video.comment')}
                  </Button>
                </div>
              )}
            </div>
          </form>

          {/* Comments List */}
          <div className="space-y-4">
            {video.comments?.map((comment, index) => (
              <div key={index} className="flex gap-3">
                <Avatar size="md" className="rounded-full">
                  <Avatar.Fallback>
                    {comment.author?.charAt(0).toUpperCase() || 'U'}
                  </Avatar.Fallback>
                </Avatar>
                <div className="flex-1">
                  <div className="flex items-center gap-2 mb-1">
                    <span className="font-semibold text-sm">
                      @{comment.author}
                    </span>
                    <span className="text-xs text-foreground-500">
                      {formatCommentDate(comment.timestamp, t)}
                    </span>
                  </div>
                  <p className="text-sm mb-2">{comment.text}</p>
                  <div className="flex items-center gap-4">
                    <button className="flex items-center gap-2 text-sm hover:bg-default-100 px-3 py-1 rounded-full transition-colors">
                      <ThumbsUpIcon className="w-4 h-4" />
                      {comment.likeCount > 0 && (
                        <span>{formatNumber(comment.likeCount)}</span>
                      )}
                    </button>
                    <button className="flex items-center gap-2 text-sm hover:bg-default-100 px-3 py-1 rounded-full transition-colors">
                      <ThumbsDownIcon className="w-4 h-4" />
                    </button>
                    <button className="text-sm font-medium hover:bg-default-100 px-3 py-1 rounded-full transition-colors">
                      {t('video.reply')}
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Recommended Videos Sidebar */}
      <div className="lg:w-[400px]">
        <h2 className="text-lg font-bold mb-4">{t('video.recommended')}</h2>
        <div className="space-y-2">
          {recommendedVideos.map((recVideo) => (
            <VideoCard
              key={recVideo.id}
              id={recVideo.id}
              title={recVideo.title}
              channelName={recVideo.channelName}
              videoUrl={recVideo.videoUrl}
              thumbnailUrl={recVideo.thumbnailUrl}
              duration={recVideo.duration}
              viewCount={recVideo.viewCount}
              timestamp={recVideo.timestamp}
            />
          ))}
        </div>
      </div>
    </div>
  );
}
