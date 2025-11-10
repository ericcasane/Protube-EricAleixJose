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
  timestamp: number;
  channel: Channel;
  comments: Comment[];
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
