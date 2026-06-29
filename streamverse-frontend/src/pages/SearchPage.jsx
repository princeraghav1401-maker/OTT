import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { Loader2, Play, Search, X } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import api from "../api/api";
import MovieCard from "../components/MovieCard";

const normalizeList = (data) => {
  if (Array.isArray(data)) return data;
  if (Array.isArray(data?.content)) return data.content;
  if (Array.isArray(data?.movies)) return data.movies;
  if (Array.isArray(data?.data)) return data.data;
  return [];
};

const SearchPage = () => {
  const navigate = useNavigate();

  const [keyword, setKeyword] = useState("");
  const [results, setResults] = useState([]);
  const [youtubeResults, setYoutubeResults] = useState([]);
  const [loading, setLoading] = useState(false);

  const searchAll = async () => {
    const q = keyword.trim();

    if (!q) {
      setResults([]);
      setYoutubeResults([]);
      return;
    }

    try {
      setLoading(true);

      const [dbRes, ytRes] = await Promise.allSettled([
        api.get("/search", { params: { keyword: q } }),
        api.get("/youtube/search", { params: { query: q } }),
      ]);

      if (dbRes.status === "fulfilled") {
        setResults(normalizeList(dbRes.value.data));
      } else {
        setResults([]);
        console.error("DB search failed:", dbRes.reason);
      }

      if (ytRes.status === "fulfilled") {
        setYoutubeResults(normalizeList(ytRes.value.data));
      } else {
        setYoutubeResults([]);
        console.error("YouTube search failed:", ytRes.reason);
      }
    } catch (error) {
      toast.error("Search failed");
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const timer = setTimeout(searchAll, 500);
    return () => clearTimeout(timer);
  }, [keyword]);

  const clearSearch = () => {
    setKeyword("");
    setResults([]);
    setYoutubeResults([]);
  };

  return (
    <div className="min-h-screen bg-dark px-6 py-28 text-white">
      <div className="mx-auto max-w-7xl">
        <motion.div
          initial={{ opacity: 0, y: 35 }}
          animate={{ opacity: 1, y: 0 }}
          className="mb-10"
        >
          <h1 className="text-4xl font-black md:text-6xl">
            Search StreamVerse
          </h1>
          <p className="mt-3 text-gray-400">
            Search movies from StreamVerse and YouTube.
          </p>
        </motion.div>

        <div className="relative mb-10 max-w-3xl">
          <Search
            className="absolute left-5 top-1/2 -translate-y-1/2 text-gray-400"
            size={22}
          />

          <input
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            placeholder="Search movies, songs, trailers..."
            className="w-full rounded-2xl border border-white/10 bg-white/5 py-5 pl-14 pr-14 text-lg text-white outline-none backdrop-blur focus:border-primary focus:ring-4 focus:ring-primary/20"
            autoFocus
          />

          {keyword && (
            <button
              onClick={clearSearch}
              className="absolute right-5 top-1/2 -translate-y-1/2 rounded-full bg-white/10 p-2 hover:bg-primary"
            >
              <X size={18} />
            </button>
          )}
        </div>

        {loading ? (
          <div className="flex min-h-[300px] items-center justify-center">
            <Loader2 className="animate-spin text-primary" size={44} />
          </div>
        ) : !keyword.trim() ? (
          <div className="flex min-h-[350px] flex-col items-center justify-center rounded-3xl border border-white/10 bg-white/5 text-center">
            <Search className="mb-4 text-primary" size={54} />
            <h2 className="text-2xl font-black">Start searching</h2>
            <p className="mt-2 text-gray-400">
              Find movies from StreamVerse and videos from YouTube.
            </p>
          </div>
        ) : results.length === 0 && youtubeResults.length === 0 ? (
          <div className="flex min-h-[350px] flex-col items-center justify-center rounded-3xl border border-white/10 bg-white/5 text-center">
            <Search className="mb-4 text-primary" size={54} />
            <h2 className="text-2xl font-black">No results found</h2>
            <p className="mt-2 text-gray-400">
              Try another title, song, movie or keyword.
            </p>
          </div>
        ) : (
          <div className="space-y-14">
            {results.length > 0 && (
              <section>
                <h2 className="mb-6 text-2xl font-black">
                  StreamVerse Results for{" "}
                  <span className="text-primary">"{keyword}"</span>
                </h2>

                <div className="grid gap-7 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5">
                  {results.map((movie, index) => (
                    <motion.div
                      key={movie.id}
                      initial={{ opacity: 0, y: 25 }}
                      animate={{ opacity: 1, y: 0 }}
                      transition={{ delay: index * 0.04 }}
                    >
                      <MovieCard movie={movie} />
                    </motion.div>
                  ))}
                </div>
              </section>
            )}

            {youtubeResults.length > 0 && (
              <section>
                <h2 className="mb-6 text-2xl font-black">
                  YouTube Results
                </h2>

                <div className="grid gap-7 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
                  {youtubeResults.map((video, index) => (
                    <motion.button
                      type="button"
                      key={video.videoId}
                      initial={{ opacity: 0, y: 25 }}
                      animate={{ opacity: 1, y: 0 }}
                      transition={{ delay: index * 0.04 }}
                      onClick={() => navigate(`/watch/youtube/${video.videoId}`)}
                      className="overflow-hidden rounded-3xl border border-white/10 bg-white/5 text-left transition hover:-translate-y-1 hover:bg-white/10"
                    >
                      <div className="relative h-48 overflow-hidden bg-black">
                        <img
                          src={video.thumbnailUrl}
                          alt={video.title}
                          className="h-full w-full object-cover"
                        />

                        <div className="absolute inset-0 bg-gradient-to-t from-black/80 via-transparent to-transparent" />

                        <div className="absolute left-4 top-4 rounded-full bg-red-600 px-3 py-1 text-xs font-black">
                          YouTube
                        </div>

                        <div className="absolute bottom-4 left-4 rounded-full bg-primary p-3">
                          <Play size={18} fill="white" />
                        </div>
                      </div>

                      <div className="p-4">
                        <h3 className="line-clamp-2 font-black">
                          {video.title}
                        </h3>

                        <p className="mt-2 line-clamp-1 text-sm text-gray-400">
                          {video.channelTitle}
                        </p>

                        <p className="mt-3 line-clamp-2 text-sm text-gray-500">
                          {video.description}
                        </p>
                      </div>
                    </motion.button>
                  ))}
                </div>
              </section>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default SearchPage;