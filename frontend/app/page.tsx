"use client";

import { useEffect, useState } from "react";
import { Spinner } from "@heroui/react";
import { motion } from "framer-motion";

import { VideoCard, type VideoCardProps } from "@/components/video-card";
import { title } from "@/components/primitives";
import { useTranslations } from "@/hooks/useTranslations";

export default function Home() {
  const [videos, setVideos] = useState<VideoCardProps[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const t = useTranslations();

  useEffect(() => {
    const fetchVideos = async () => {
      try {
        setIsLoading(true);
        const response = await fetch("/api/videos");

        if (!response.ok) {
          throw new Error("Failed to fetch videos");
        }

        const data = await response.json();

        setVideos(Array.isArray(data) ? data : []);
        setError(null);
      } catch (err) {
        console.error("Error fetching videos:", err);
        setError(err instanceof Error ? err.message : "Failed to fetch videos");
        setVideos([]);
      } finally {
        setIsLoading(false);
      }
    };

    fetchVideos();
  }, []);

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.2,
        delayChildren: 0.3,
      },
    },
  };

  const itemVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: {
      opacity: 1,
      y: 0,
      transition: {
        duration: 0.8,
        ease: "easeOut",
      },
    },
  };

  const wordVariants = {
    hidden: { opacity: 0, scale: 0.8, rotateZ: -10 },
    visible: {
      opacity: 1,
      scale: 1,
      rotateZ: 0,
      transition: {
        duration: 0.6,
        ease: "easeOut",
      },
    },
  };

  const underlineVariants = {
    hidden: { scaleX: 0 },
    visible: {
      scaleX: 1,
      transition: {
        duration: 0.8,
        delay: 0.8,
        ease: "easeOut",
      },
    },
  };

  return (
    <section className="flex flex-col gap-8 py-4">
      <motion.div
        animate="visible"
        className="flex flex-col items-center justify-center gap-4"
        initial="hidden"
        variants={containerVariants}
      >
        <div className="inline-block max-w-lg text-center justify-center">
          <div className="flex items-center justify-center flex-wrap gap-2">
            <motion.span
              className={`${title()} inline-block`}
              variants={wordVariants}
            >
              {t("home.title.the")}
            </motion.span>
            <motion.span
              className={`${title({ color: "blue" })} inline-block bg-gradient-to-r from-blue-400 to-cyan-400 bg-clip-text text-transparent`}
              variants={wordVariants}
            >
              {t("home.title.ultimate")}
            </motion.span>
          </div>

          <motion.div className="relative mt-2" variants={itemVariants}>
            <span className={title()}>{t("home.title.platform")}</span>
            <motion.div
              className="h-1 bg-gradient-to-r from-cyan-500 via-blue-500 to-cyan-500 rounded-full mt-2 origin-left"
              style={{ transformOrigin: "left" }}
              variants={underlineVariants}
            />
          </motion.div>
        </div>

        <motion.div
          animate={{ opacity: 1, y: 0 }}
          className="mt-4"
          initial={{ opacity: 0, y: 10 }}
          transition={{ duration: 0.6, delay: 1.2 }}
        >
          <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-gradient-to-r from-cyan-500/10 to-blue-500/10 border border-cyan-500/30 backdrop-blur-md">
            <motion.span
              animate={{ rotate: 360 }}
              className="inline-block w-2 h-2 rounded-full bg-cyan-400"
              transition={{ duration: 2, repeat: Infinity, ease: "linear" }}
            />
            <span className="text-xs font-medium">{t("home.badge")}</span>
          </div>
        </motion.div>
      </motion.div>

      <div className="w-full">
        {isLoading ? (
          <div className="flex justify-center items-center min-h-[400px]">
            <Spinner size="xl" />
          </div>
        ) : error ? (
          <div className="text-center text-danger min-h-[400px] flex items-center justify-center">
            <div>
              <p className="text-lg font-semibold">{t("home.errorLoading")}</p>
              <p className="text-sm mt-2">{error}</p>
            </div>
          </div>
        ) : videos.length === 0 ? (
          <div className="text-center text-foreground-500 min-h-[400px] flex items-center justify-center">
            <p className="text-lg">{t("home.noVideos")}</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-3 gap-4">
            {videos.map((video, index) => (
              <VideoCard key={`${video.title}-${index}`} {...video} />
            ))}
          </div>
        )}
      </div>
    </section>
  );
}
