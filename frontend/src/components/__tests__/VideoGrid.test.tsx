import { act, render, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';

import VideoGrid from '../VideoGrid';
import fetchMock from 'jest-fetch-mock';

describe('VideoGrid component', () => {
  beforeEach(() => {
    fetchMock.resetMocks();
  });

  it('renders correctly with video data', async () => {
    const mockVideos = [
      {
        title: 'video 1 test',
        user: 'user1',
        videoUrl: 'http://example.com/video1.mp4',
        thumbnailUrl: '/thumbnails/1.webp',
      },
      {
        title: 'video 2 test',
        user: 'user2',
        videoUrl: 'http://example.com/video2.mp4',
        thumbnailUrl: '/thumbnails/2.webp',
      },
    ];

    fetchMock.mockResponseOnce(JSON.stringify(mockVideos));

    const { getByText, asFragment } = await act(async () => {
      return render(<VideoGrid />);
    });

    await waitFor(() => {
      expect(getByText('video 1 test')).toBeInTheDocument();
      expect(getByText('video 2 test')).toBeInTheDocument();
    });

    expect(asFragment()).toMatchSnapshot();
  });

  it('renders loading state initially', () => {
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    fetchMock.mockResponseOnce(JSON.stringify([]), { delay: 100 });
    const { getByText } = render(<VideoGrid />);
    expect(getByText('Loading videos...')).toBeInTheDocument();
  });

  it('renders error state when fetch fails', async () => {
    fetchMock.mockRejectOnce(new Error('Network error'));
    const { getByText } = await act(async () => {
      return render(<VideoGrid />);
    });

    await waitFor(() => {
      expect(getByText(/Error:/)).toBeInTheDocument();
    });
  });

  it('renders no videos message when list is empty', async () => {
    fetchMock.mockResponseOnce(JSON.stringify([]));
    const { getByText } = await act(async () => {
      return render(<VideoGrid />);
    });

    await waitFor(() => {
      expect(getByText('No videos found')).toBeInTheDocument();
    });
  });
});
