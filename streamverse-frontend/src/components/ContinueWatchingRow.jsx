import { motion } from "framer-motion";
import { Play, Clock } from "lucide-react";
import { useNavigate } from "react-router-dom";

const getImageUrl = (url) => {
  if (!url) return "https://placehold.co/500x280/111827/ffffff?text=StreamVerse";
  if (url.startsWith("http")) return url;
  return `http://localhost:8080${url}`;
};

const ContinueWatchingRow = ({ items = [] }) => {
  const navigate = useNavigate();

  if (!items || items.length === 0) return null;

  return (
    <section className="mb-14">
      <div className="mb-5 px-2">
        <h2 className="text-2xl font-black md:text-3xl">▶ Continue Watching</h2>
        <p className="mt-1 text-sm text-gray-400">
          Pick up from where you left off
        </p>
      </div>

      <div className="flex gap-5 overflow-x-auto pb-4">
        {items.map((item, index) => {
          const movie = item.movie || item;
          const movieId = movie.id || item.movieId;

          const progress =
            item.progressPercentage ||
            item.progress ||
            Math.floor(((item.progressSeconds || 0) / (item.durationSeconds || 1)) * 100);

          return (
            <motion.div
              key={movieId}
              initial={{ opacity: 0, y: 25 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: index * 0.06 }}
              onClick={() => navigate(`/watch/movie/${movieId}`)}
              className="group relative min-w-[320px] cursor-pointer overflow-hidden rounded-2xl border border-white/10 bg-white/5"
            >
              <img
                src={getImageUrl(movie.bannerUrl || movie.thumbnailUrl)}
                alt={movie.title}
                className="h-[180px] w-full object-cover transition duration-500 group-hover:scale-110"
              />

              <div className="absolute inset-0 bg-gradient-to-t from-black via-black/40 to-transparent" />

              <button className="absolute left-4 top-4 flex h-11 w-11 items-center justify-center rounded-full bg-primary shadow-glow">
                <Play size={18} fill="white" />
              </button>

              <div className="absolute bottom-0 w-full p-4">
                <h3 className="line-clamp-1 font-black">{movie.title}</h3>

                <div className="mt-2 flex items-center gap-2 text-xs text-gray-300">
                  <Clock size={14} />
                  <span>{Math.min(progress || 0, 100)}% watched</span>
                </div>

                <div className="mt-3 h-1.5 overflow-hidden rounded-full bg-white/20">
                  <div
                    className="h-full rounded-full bg-primary"
                    style={{ width: `${Math.min(progress || 0, 100)}%` }}
                  />
                </div>
              </div>
            </motion.div>
          );
        })}
      </div>
    </section>
  );
};

export default ContinueWatchingRow;