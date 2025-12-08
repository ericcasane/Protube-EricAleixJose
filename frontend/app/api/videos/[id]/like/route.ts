import { NextResponse } from 'next/server';

export async function POST(
  request: Request,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id } = await params;
    const backendUrl = process.env.BACKEND_URL || 'http://localhost:8080';

    // Get the authorization token from the request headers
    const authHeader = request.headers.get('authorization');
    
    if (!authHeader) {
      return NextResponse.json(
        { error: 'Unauthorized' },
        { status: 401 }
      );
    }

    console.log('[Like Route] Sending request to:', `${backendUrl}/api/videos/${id}/like`);
    console.log('[Like Route] Authorization header:', authHeader ? 'Present' : 'Missing');
    
    const response = await fetch(`${backendUrl}/api/videos/${id}/like`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': authHeader,
      },
    });

    console.log('[Like Route] Backend response status:', response.status);

    if (!response.ok) {
      const errorText = await response.text();
      console.error('[Like Route] Backend error:', errorText);
      return NextResponse.json(
        { error: 'Failed to like video', details: errorText },
        { status: response.status }
      );
    }

    const data = await response.json();
    console.log('[Like Route] Success, returning data:', data);
    return NextResponse.json(data);
  } catch (error) {
    console.error('[Like Route] Exception:', error);
    return NextResponse.json(
      { error: 'Failed to like video', details: error instanceof Error ? error.message : 'Unknown error' },
      { status: 500 }
    );
  }
}
