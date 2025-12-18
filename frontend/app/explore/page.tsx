"use client";

import { useEffect, useState } from "react";
import { Spinner, Button } from "@heroui/react";
import { motion } from "framer-motion";
import Image from "next/image";
import Link from "next/link";
import { Icon } from "@iconify/react";

import { VideoCard, type VideoCardProps } from "@/components/video-card";
import { useTranslations } from "@/hooks/useTranslations";

export default function ExplorePage() {
  const [videos, setVideos] = useState<VideoCardProps[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const t = useTranslations();

  useEffect(() => {
    const fetchVideos = async () => {
      try {
        setIsLoading(true);
        const response = await fetch("/api/videos");

        if (response.ok) {
          const data = await response.json();
          const mappedVideos: VideoCardProps[] = Array.isArray(data)
            ? data.map((v: any) => ({
                id: v.videoId || v.id,
                title: v.title,
                channelName: v.channelName,
                videoUrl: v.videoUrl,
                thumbnailUrl: v.thumbnailUrl,
                duration: v.duration,
                viewCount: v.viewCount,
                timestamp: v.timestamp,
              }))
            : [];

          setVideos(mappedVideos);
        }
      } catch (error) {
        console.error("Failed to fetch videos", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchVideos();
  }, []);

  // Compute derived lists
  const sortedByViews = [...videos].sort(
    (a, b) => (b.viewCount || 0) - (a.viewCount || 0),
  );
  const sortedByDate = [...videos].sort(
    (a, b) => (b.timestamp || 0) - (a.timestamp || 0),
  );

  const spotlightVideo = sortedByViews.length > 0 ? sortedByViews[0] : null;
  const trendingVideos = sortedByViews.slice(1, 4); // Next top 3 for 3 columns? Or still 4? Request said "3 videos, not 4 in desktop". I will start with 3 per row visually.
  const recentVideos = sortedByDate.slice(0, 9); // divisible by 3

  // Format Helpers
  const formatViews = (count = 0) => {
    if (count >= 1000000) return (count / 1000000).toFixed(1) + "M";
    if (count >= 1000) return (count / 1000).toFixed(1) + "K";

    return count.toString();
  };

  if (isLoading) {
    return (
      <div className="flex justify-center items-center h-[80vh]">
        <Spinner size="lg" />
      </div>
    );
  }

  return (
    <div className="min-h-screen w-full pb-20">
      {/* Background Ambient Glow */}
      <div className="fixed top-0 left-0 w-full h-[50vh] bg-primary/10 blur-[120px] pointer-events-none -z-10" />

      <main className="max-w-7xl mx-auto px-6 py-8 flex flex-col gap-12">
        {/* Header */}
        <motion.div
          animate={{ opacity: 1, y: 0 }}
          className="flex flex-col gap-2"
          initial={{ opacity: 0, y: -20 }}
          transition={{ duration: 0.6 }}
        >
          <h1 className="text-4xl md:text-6xl font-black tracking-tighter bg-gradient-to-br from-foreground to-foreground/50 bg-clip-text text-transparent">
            {t("explore.title")}
          </h1>
          <p className="text-lg text-default-500 font-medium">
            {t("explore.subtitle")}
          </p>
        </motion.div>

        {/* Spotlight Section */}
        {spotlightVideo && (
          <motion.div
            animate={{ opacity: 1, scale: 1 }}
            className="w-full"
            initial={{ opacity: 0, scale: 0.95 }}
            transition={{ duration: 0.8, delay: 0.2 }}
          >
            <div className="group relative w-full h-[400px] md:h-[500px] rounded-3xl overflow-hidden shadow-2xl border border-white/10">
              <Image
                fill
                priority
                alt={spotlightVideo.title}
                className="object-cover transition-transform duration-700 group-hover:scale-105"
                src={`/media/${spotlightVideo.thumbnailUrl}`}
              />

              {/* Gradient Overlay */}
              <div className="absolute inset-0 bg-gradient-to-t from-black/90 via-black/40 to-transparent" />

              {/* Content */}
              <div className="absolute bottom-0 left-0 p-8 md:p-12 w-full md:w-2/3 flex flex-col gap-4">
                <div className="flex items-center gap-2">
                  <span className="px-3 py-1 rounded-full bg-danger text-white text-xs font-bold tracking-wider uppercase animate-pulse">
                    {t("explore.trendingBadge")}
                  </span>
                </div>

                <h2 className="text-3xl md:text-5xl font-bold text-white leading-tight">
                  {spotlightVideo.title}
                </h2>

                <div className="flex items-center gap-4 text-white/80">
                  <div className="flex items-center gap-2">
                    <Icon className="text-xl" icon="solar:user-circle-bold" />
                    <span className="font-semibold">
                      {spotlightVideo.channelName}
                    </span>
                  </div>
                  <span className="w-1 h-1 rounded-full bg-white/50" />
                  <div className="flex items-center gap-2">
                    <Icon className="text-xl" icon="solar:eye-bold" />
                    <span>
                      {formatViews(spotlightVideo.viewCount)}{" "}
                      {t("video.viewsLabel")}
                    </span>
                  </div>
                </div>

                <div className="mt-4">
                  <Link href={`/video/${spotlightVideo.id}`}>
                    <Button
                      className="font-bold shadow-lg shadow-primary/20 backdrop-blur-md flex items-center gap-2"
                      size="lg"
                    >
                      <Icon className="text-xl" icon="solar:play-bold" />
                      {t("explore.watchNow")}
                    </Button>
                  </Link>
                </div>
              </div>
            </div>
          </motion.div>
        )}

        {/* Trending Strip */}
        {trendingVideos.length > 0 && (
          <motion.div
            animate={{ opacity: 1, y: 0 }}
            className="flex flex-col gap-6"
            initial={{ opacity: 0, y: 30 }}
            transition={{ duration: 0.6, delay: 0.4 }}
          >
            <div className="flex items-center justify-between">
              <h3 className="text-2xl font-bold flex items-center gap-2">
                <Icon className="text-orange-500" icon="solar:fire-bold" />
                {t("explore.trending")}
              </h3>
            </div>

            {/* Updated grid to 3 columns on Desktop */}
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
              {trendingVideos.slice(0, 3).map((video, idx) => (
                <motion.div
                  key={video.id}
                  animate={{ opacity: 1, y: 0 }}
                  initial={{ opacity: 0, y: 20 }}
                  transition={{ duration: 0.5, delay: 0.5 + idx * 0.1 }}
                >
                  <VideoCard {...video} />
                </motion.div>
              ))}
            </div>
          </motion.div>
        )}

        {/* New Arrivals */}
        {recentVideos.length > 0 && (
          <motion.div
            animate={{ opacity: 1, y: 0 }}
            className="flex flex-col gap-6"
            initial={{ opacity: 0, y: 30 }}
            transition={{ duration: 0.6, delay: 0.6 }}
          >
            <div className="flex items-center justify-between">
              <h3 className="text-2xl font-bold flex items-center gap-2">
                <Icon
                  className="text-blue-500"
                  icon="solar:clock-circle-bold"
                />
                {t("explore.freshUploads")}
              </h3>
            </div>

            {/* Updated grid to 3 columns on Desktop */}
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-x-6 gap-y-10">
              {recentVideos.map((video, idx) => (
                <VideoCard key={`recent-${video.id}`} {...video} />
              ))}
            </div>
          </motion.div>
        )}
      </main>
    </div>
  );
}
