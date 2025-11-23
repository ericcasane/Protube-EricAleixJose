export interface Comment {
  author: string;
  text: string;
  timestamp: number;
  likeCount: number;
}

export interface Channel {
  name: string;
  followerCount: number;
}

export interface VideoDetail {
  id: string;
  title: string;
  videoUrl: string;
  thumbnailUrl: string;
  duration: number;
  description: string;
  viewCount: number;
  likeCount: number;
  dislikeCount: number;
  userReaction: 'LIKE' | 'DISLIKE' | null;
  timestamp: number;
  channel: Channel;
  comments: Comment[];
}

export interface VideoReactionResponse {
  likesCount: number;
  dislikesCount: number;
  userReaction: 'LIKE' | 'DISLIKE' | null;
}

export interface VideoListItem {
  id: string;
  title: string;
  channelName: string;
  videoUrl: string;
  thumbnailUrl: string;
  duration: number;
  viewCount: number;
  timestamp: number;
}
