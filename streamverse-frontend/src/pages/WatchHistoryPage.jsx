import { useEffect, useState } from "react";
import { Clock, Loader2, Play } from "lucide-react";
import { useNavigate } from "react-router-dom";
import api from "../api/api";

const getImageUrl = (url) => {
  if (!url) return "https://placehold.co/500x280/111827/ffffff?text=StreamVerse";
  if (url.startsWith("http")) return url;
  return `http://localhost:8080${url}`;
};

const WatchHistoryPage = () => {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const loadHistory = async () => {
    try {
      const res = await api.get("/watch-history/continue");
      setItems(res.data || []);
    } catch (error) {
      console.error(error);
      setItems([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadHistory();
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
        <h1 className="text-5xl font-black">Watch History</h1>
        <p className="mt-2 text-gray-400">Continue from where you left off.</p>

        {items.length === 0 ? (
          <div className="mt-10 flex min-h-[380px] flex-col items-center justify-center rounded-3xl border border-white/10 bg-white/5 text-center">
            <Clock className="mb-4 text-primary" size={56} />
            <h2 className="text-2xl font-black">No watch history yet</h2>
            <p className="mt-2 text-gray-400">Start watching any movie to see it here.</p>
            <button
              onClick={() => navigate("/")}
              className="mt-6 rounded-full bg-primary px-7 py-3 font-bold hover:bg-primaryDark"
            >
              Browse Movies
            </button>
          </div>
        ) : (
          <div className="mt-10 grid gap-7 sm:grid-cols-2 lg:grid-cols-3">
            {items.map((item) => {
              const movie = item.movie || item;
              const movieId = movie.id || item.movieId;

              const progress =
                item.progressPercentage ||
                item.progress ||
                Math.floor(((item.currentSecond || item.progressSeconds || 0) / (item.durationSeconds || 1)) * 100);

              return (
                <div key={movieId} className="overflow-hidden rounded-3xl border border-white/10 bg-white/5">
                  <img
                    src={getImageUrl(movie.bannerUrl || movie.thumbnailUrl)}
                    alt={movie.title}
                    className="h-56 w-full object-cover"
                  />

                  <div className="p-5">
                    <h3 className="text-xl font-black">{movie.title}</h3>
                    <p className="mt-1 text-sm text-gray-400">
                      {Math.min(progress || 0, 100)}% watched
                    </p>

                    <div className="mt-3 h-2 overflow-hidden rounded-full bg-white/10">
                      <div
                        className="h-full rounded-full bg-primary"
                        style={{ width: `${Math.min(progress || 0, 100)}%` }}
                      />
                    </div>

                    <button
                      onClick={() => navigate(`/watch/movie/${movieId}`)}
                      className="mt-5 flex items-center gap-2 rounded-full bg-primary px-6 py-3 font-bold hover:bg-primaryDark"
                    >
                      <Play size={17} fill="white" />
                      Continue Watching
                    </button>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
};

export default WatchHistoryPage;