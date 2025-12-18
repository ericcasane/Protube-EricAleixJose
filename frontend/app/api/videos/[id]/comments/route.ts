import { NextRequest, NextResponse } from "next/server";

const backendUrl =
  process.env.NEXT_PUBLIC_BACKEND_URL || "http://localhost:8080";

export async function POST(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> },
) {
  try {
    const { id } = await params;
    const body = await request.json();
    const authHeader = request.headers.get("authorization");

    console.log("[Comment Route] Adding comment to video:", id);
    console.log(
      "[Comment Route] Authorization header:",
      authHeader ? "Present" : "Missing",
    );

    const response = await fetch(`${backendUrl}/api/videos/${id}/comments`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        ...(authHeader && { Authorization: authHeader }),
      },
      body: JSON.stringify(body),
    });

    console.log("[Comment Route] Backend response status:", response.status);

    if (!response.ok) {
      const errorText = await response.text();

      console.error("[Comment Route] Backend error:", errorText);

      return NextResponse.json(
        { error: "Failed to add comment" },
        { status: response.status },
      );
    }

    const data = await response.json();

    console.log("[Comment Route] Comment added successfully:", data);

    return NextResponse.json(data);
  } catch (error) {
    console.error("[Comment Route] Error:", error);

    return NextResponse.json(
      { error: "Internal server error" },
      { status: 500 },
    );
  }
}

export async function GET(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> },
) {
  try {
    const { id } = await params;

    console.log("[Comment Route] Fetching comments for video:", id);

    const response = await fetch(`${backendUrl}/api/videos/${id}/comments`, {
      method: "GET",
    });

    console.log("[Comment Route] Backend response status:", response.status);

    if (!response.ok) {
      const errorText = await response.text();

      console.error("[Comment Route] Backend error:", errorText);

      return NextResponse.json(
        { error: "Failed to fetch comments" },
        { status: response.status },
      );
    }

    const data = await response.json();

    console.log("[Comment Route] Comments fetched successfully");

    return NextResponse.json(data);
  } catch (error) {
    console.error("[Comment Route] Error:", error);

    return NextResponse.json(
      { error: "Internal server error" },
      { status: 500 },
    );
  }
}
