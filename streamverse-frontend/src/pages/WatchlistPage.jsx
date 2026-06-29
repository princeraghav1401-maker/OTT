import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { Heart, Loader2, Play, Trash2 } from "lucide-react";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import api from "../api/api";

const getImageUrl = (url) => {
  if (!url) return "https://placehold.co/400x600/111827/ffffff?text=StreamVerse";
  if (url.startsWith("http")) return url;
  return `http://localhost:8080${url}`;
};

const WatchlistPage = () => {
  const [watchlist, setWatchlist] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const loadWatchlist = async () => {
    try {
      setLoading(true);
      const res = await api.get("/watchlist/my-list");
      setWatchlist(res.data || []);
    } catch (error) {
      toast.error("Unable to load watchlist");
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const removeFromWatchlist = async (movieId) => {
    try {
      await api.delete(`/watchlist/remove/${movieId}`);
      setWatchlist((prev) =>
        prev.filter((item) => (item.movieId || item.id) !== movieId)
      );
      toast.success("Removed from watchlist");
    } catch {
      toast.error("Unable to remove movie");
    }
  };

  useEffect(() => {
    loadWatchlist();
  }, []);

  if (loading) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-dark">
        <Loader2 className="animate-spin text-primary" size={44} />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-dark px-6 py-28 text-white">
      <div className="mx-auto max-w-7xl">
        <div className="mb-10">
          <h1 className="text-4xl font-black md:text-6xl">My Watchlist</h1>
          <p className="mt-3 text-gray-400">
            Movies and shows you saved for later.
          </p>
        </div>

        {watchlist.length === 0 ? (
          <div className="flex min-h-[400px] flex-col items-center justify-center rounded-3xl border border-white/10 bg-white/5 text-center">
            <Heart className="mb-4 text-primary" size={54} />
            <h2 className="text-2xl font-black">Your watchlist is empty</h2>
            <p className="mt-2 text-gray-400">
              Add movies from details page to watch them later.
            </p>
            <button
              onClick={() => navigate("/")}
              className="mt-6 rounded-full bg-primary px-7 py-3 font-bold hover:bg-primaryDark"
            >
              Browse Movies
            </button>
          </div>
        ) : (
          <div className="grid gap-7 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5">
            {watchlist.map((item, index) => {
              const movie = item.movie || item;
              const movieId = movie.id || item.movieId;

              return (
                <motion.div
                  key={movieId}
                  initial={{ opacity: 0, y: 25 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ delay: index * 0.06 }}
                  className="group overflow-hidden rounded-3xl border border-white/10 bg-white/5 shadow-xl"
                >
                  <div
                    onClick={() => navigate(`/movies/${movieId}`)}
                    className="relative cursor-pointer overflow-hidden"
                  >
                    <img
                      src={getImageUrl(movie.thumbnailUrl || movie.bannerUrl)}
                      alt={movie.title}
                      className="h-[280px] w-full object-cover transition duration-500 group-hover:scale-110"
                    />
                    <div className="absolute inset-0 bg-gradient-to-t from-black via-transparent to-transparent" />
                  </div>

                  <div className="p-4">
                    <h3 className="line-clamp-1 font-black">{movie.title}</h3>
                    <p className="mt-1 text-xs text-gray-400">
                      {movie.releaseYear || "2026"} • {movie.language || "Hindi"}
                    </p>

                    <div className="mt-4 flex gap-2">
                      <button
                        onClick={() => navigate(`/watch/movie/${movieId}`)}
                        className="flex flex-1 items-center justify-center gap-2 rounded-xl bg-primary py-2 text-sm font-bold hover:bg-primaryDark"
                      >
                        <Play size={15} fill="white" />
                        Play
                      </button>

                      <button
                        onClick={() => removeFromWatchlist(movieId)}
                        className="rounded-xl bg-white/10 px-3 hover:bg-red-600"
                      >
                        <Trash2 size={16} />
                      </button>
                    </div>
                  </div>
                </motion.div>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
};

export default WatchlistPage;