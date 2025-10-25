import React from "react";

interface VideoCardProps {
  title: string;
  imageUrl: string;
}

export const VideoCard: React.FC<VideoCardProps> = ({ title, imageUrl }) => (
  <div className="rounded-lg shadow-sm overflow-hidden flex flex-col transition hover:shadow-md focus:outline-none w-full bg-neutral-800/60">
    {/* CardBody */}
    <div className="overflow-visible p-0">
      <img
        src={imageUrl}
        alt={title}
        className="w-full object-cover rounded-lg shadow-sm"
      />
    </div>
    {/* CardFooter */}
    <div className="flex items-center px-4 py-2 text-sm">
      <b className="truncate">{title}</b>
    </div>
  </div>
);