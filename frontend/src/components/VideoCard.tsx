import React from "react";

interface VideoCardProps {
  title: string;
  imageUrl: string;
}

export const VideoCard: React.FC<VideoCardProps> = ({ title, imageUrl }) => (
  <div className="rounded-lg shadow-sm p-2 overflow-hidden flex flex-col transition hover:shadow-md focus:outline-none w-full bg-slate-800/60">
    <div className="overflow-visible p-0">
      <img
        alt={title}
        className="w-full h-[200px] object-cover rounded-lg shadow-sm"
        src={imageUrl}
      />
    </div>

    <div className="flex items-center px-4 py-2 text-sm">
      <b className="truncate">{title}</b>
    </div>
  </div>
);
