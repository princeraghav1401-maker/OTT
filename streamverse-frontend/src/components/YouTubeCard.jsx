import { motion } from "framer-motion";
import { Play, PlayCircle } from "lucide-react";
import { useNavigate } from "react-router-dom";

const YouTubeCard = ({ video }) => {
  const navigate = useNavigate();

  return (
    <motion.div
      whileHover={{ y: -8, scale: 1.03 }}
      transition={{ duration: 0.25 }}
      onClick={() => navigate(`/watch/youtube/${video.videoId}`)}
      className="group relative min-w-[220px] cursor-pointer overflow-hidden rounded-2xl bg-white/5 shadow-xl"
    >
      <img
        src={video.thumbnailUrl}
        alt={video.title}
        className="h-[280px] w-full object-cover transition duration-500 group-hover:scale-110"
      />

      {/* Overlay */}
      <div className="absolute inset-0 bg-gradient-to-t from-black via-black/30 to-transparent" />

      {/* YouTube Badge */}
      <div className="absolute left-3 top-3 flex items-center gap-1 rounded-full bg-red-600 px-3 py-1 text-xs font-bold text-white">
        <PlayCircle size={14} />
        YouTube
      </div>

      {/* Play Button */}
      <div className="absolute inset-0 flex items-center justify-center opacity-0 transition duration-300 group-hover:opacity-100">
        <div className="rounded-full bg-red-600 p-4 shadow-xl">
          <Play size={28} fill="white" className="text-white" />
        </div>
      </div>

      {/* Bottom */}
      <div className="absolute bottom-0 w-full p-4">

        <h3 className="line-clamp-2 text-sm font-bold text-white">
          {video.title}
        </h3>

        <p className="mt-2 line-clamp-1 text-xs text-gray-300">
          {video.channelTitle}
        </p>

      </div>
    </motion.div>
  );
};

export default YouTubeCard;