import { NextResponse } from 'next/server';

export async function GET(
  request: Request,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id } = await params;
    const backendUrl = process.env.BACKEND_URL || 'http://localhost:8080';

    console.log('[API Route] Fetching video with ID:', id);
    console.log('[API Route] Backend URL:', `${backendUrl}/api/videos/${id}`);

    const response = await fetch(`${backendUrl}/api/videos/${id}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    console.log('[API Route] Backend response status:', response.status);

    if (!response.ok) {
      const errorText = await response.text();
      console.error('[API Route] Backend error:', errorText);
      return NextResponse.json(
        { error: `Failed to fetch video from backend: ${response.status}`, details: errorText },
        { status: response.status }
      );
    }

    const video = await response.json();
    console.log('[API Route] Video data:', video);
    return NextResponse.json(video);
  } catch (error) {
    console.error('[API Route] Error fetching video:', error);
    return NextResponse.json(
      { error: 'Failed to fetch video', details: error instanceof Error ? error.message : 'Unknown error' },
      { status: 500 }
    );
  }
}
