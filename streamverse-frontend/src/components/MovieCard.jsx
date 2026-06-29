import { motion } from "framer-motion";
import { Crown, Play, Star } from "lucide-react";
import { useNavigate } from "react-router-dom";

const getImageUrl = (url) => {
  if (!url) return "https://placehold.co/400x600/111827/ffffff?text=StreamVerse";
  if (url.startsWith("http")) return url;
  return `http://localhost:8080${url}`;
};

const MovieCard = ({ movie }) => {
  const navigate = useNavigate();

  const openDetails = () => {
    navigate(`/movies/${movie.id}`);
  };

  return (
    <motion.div
      onClick={openDetails}
      whileHover={{ y: -10, scale: 1.03 }}
      transition={{ duration: 0.25 }}
      className="group relative min-w-[190px] cursor-pointer overflow-hidden rounded-2xl bg-white/5 shadow-xl"
    >
      <img
        src={getImageUrl(movie.thumbnailUrl || movie.bannerUrl)}
        alt={movie.title}
        className="h-[280px] w-full object-cover transition duration-500 group-hover:scale-110"
      />

      <div className="absolute inset-0 bg-gradient-to-t from-black via-black/30 to-transparent opacity-80" />

      {movie.premium && (
        <div className="absolute left-3 top-3 flex items-center gap-1 rounded-full bg-yellow-500 px-3 py-1 text-xs font-bold text-black">
          <Crown size={13} />
          PREMIUM
        </div>
      )}

      <div className="absolute bottom-0 w-full p-4">
        <h3 className="line-clamp-1 text-base font-bold">{movie.title}</h3>

        <div className="mt-2 flex items-center justify-between text-xs text-gray-300">
          <span>{movie.releaseYear || "2026"}</span>
          <span>{movie.language || "Hindi"}</span>
        </div>

        <div className="mt-3 flex items-center justify-between">
          <span className="flex items-center gap-1 text-xs text-yellow-400">
            <Star size={14} fill="currentColor" />
            {movie.averageRating ? movie.averageRating.toFixed(1) : "4.5"}
          </span>

          <button
            onClick={(e) => {
              e.stopPropagation();
              navigate(`/watch/movie/${movie.id}`);
            }}
            className="rounded-full bg-primary p-2 text-white opacity-0 transition group-hover:opacity-100"
          >
            <Play size={15} fill="white" />
          </button>
        </div>
      </div>
    </motion.div>
  );
};

export default MovieCard;