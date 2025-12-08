export interface Video {
    id: string;
    title: string;
    channelName: string;
    videoUrl: string;
    thumbnailUrl: string;
    duration: number;
    viewCount: number;
    timestamp: number;
}

export interface Page<T> {
    content: T[];
    totalPages: number;
    totalElements: number;
    size: number;
    number: number;
}
