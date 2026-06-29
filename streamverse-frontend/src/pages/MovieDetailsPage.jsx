import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { motion } from "framer-motion";
import { ArrowLeft, Crown, Heart, Play, Plus, Star } from "lucide-react";
import { toast } from "react-toastify";
import api from "../api/api";
import ReviewSection from "../components/ReviewSection";
import LikeDislike from "../components/LikeDislike";

const getImageUrl = (url) => {
  if (!url) {
    return "https://placehold.co/1400x700/111827/ffffff?text=StreamVerse";
  }

  if (url.startsWith("http")) {
    return url;
  }

  return `http://localhost:8080${url}`;
};

const MovieDetailsPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [movie, setMovie] = useState(null);
  const [loading, setLoading] = useState(true);

  const loadMovie = async () => {
    try {
      setLoading(true);
      const res = await api.get(`/movies/${id}`);
      setMovie(res.data);
    } catch (error) {
      toast.error("Unable to load movie details");
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const addToWatchlist = async () => {
    try {
      await api.post(`/watchlist/add/${id}`);
      toast.success("Added to watchlist");
    } catch {
      toast.error("Login required or already added");
    }
  };

  const addToFavorites = async () => {
    try {
      await api.post(`/favorites/${id}`);
      toast.success("Added to favorites");
    } catch {
      toast.error("Login required or already added");
    }
  };

  useEffect(() => {
    loadMovie();
  }, [id]);

  if (loading) {
    return (
      <div className="min-h-screen bg-dark px-6 py-28">
        <div className="mx-auto max-w-7xl animate-pulse">
          <div className="h-[520px] rounded-[36px] bg-white/10" />
          <div className="mt-8 h-8 w-80 rounded bg-white/10" />
          <div className="mt-4 h-4 w-[600px] rounded bg-white/10" />
        </div>
      </div>
    );
  }

  if (!movie) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-dark text-white">
        Movie not found
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-dark text-white">
      <section className="relative min-h-[680px] overflow-hidden">
        <img
          src={getImageUrl(movie.bannerUrl || movie.thumbnailUrl)}
          alt={movie.title}
          className="absolute inset-0 h-full w-full object-cover"
        />

        <div className="absolute inset-0 bg-gradient-to-r from-black via-black/75 to-transparent" />
        <div className="absolute inset-0 bg-gradient-to-t from-dark via-transparent to-black/20" />

        <div className="relative z-10 mx-auto flex min-h-[680px] max-w-7xl items-center px-6 pt-20">
          <motion.div
            initial={{ opacity: 0, y: 45 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.55 }}
            className="max-w-3xl"
          >
            <button
              onClick={() => navigate(-1)}
              className="mb-8 flex items-center gap-2 rounded-full bg-white/10 px-4 py-2 text-sm backdrop-blur hover:bg-white/20"
            >
              <ArrowLeft size={17} />
              Back
            </button>

            {movie.premium && (
              <div className="mb-5 inline-flex items-center gap-2 rounded-full bg-yellow-500 px-4 py-2 text-sm font-black text-black">
                <Crown size={16} />
                Premium Content
              </div>
            )}

            <h1 className="text-5xl font-black leading-tight md:text-7xl">
              {movie.title}
            </h1>

            <div className="mt-5 flex flex-wrap items-center gap-4 text-sm text-gray-300">
              <span>{movie.releaseYear || "2026"}</span>

              <span className="rounded-md border border-white/20 px-2 py-1">
                {movie.ageRating || "U/A 16+"}
              </span>

              <span>
                {movie.durationMinutes
                  ? `${movie.durationMinutes} min`
                  : "120 min"}
              </span>

              <span>{movie.language || "Hindi"}</span>

              <span className="flex items-center gap-1 text-yellow-400">
                <Star size={16} fill="currentColor" />
                {movie.averageRating ? movie.averageRating.toFixed(1) : "4.5"}
              </span>
            </div>

            <p className="mt-6 max-w-2xl text-base leading-8 text-gray-300 md:text-lg">
              {movie.description || "No description available."}
            </p>

            <div className="mt-8 flex flex-wrap gap-4">
              <button
                onClick={() => navigate(`/watch/movie/${movie.id}`)}
                className="flex items-center gap-2 rounded-full bg-primary px-8 py-3 font-black text-white shadow-glow hover:bg-primaryDark"
              >
                <Play size={19} fill="white" />
                Watch Now
              </button>

              <button
                onClick={addToWatchlist}
                className="flex items-center gap-2 rounded-full bg-white/10 px-7 py-3 font-bold backdrop-blur hover:bg-white/20"
              >
                <Plus size={18} />
                Watchlist
              </button>

              <button
                onClick={addToFavorites}
                className="flex items-center gap-2 rounded-full bg-white/10 px-7 py-3 font-bold backdrop-blur hover:bg-white/20"
              >
                <Heart size={18} />
                Favorite
              </button>
            </div>

            <LikeDislike movieId={movie.id} />
          </motion.div>
        </div>
      </section>

      <section className="mx-auto max-w-7xl px-6 pb-20">
        <div className="grid gap-8 rounded-3xl border border-white/10 bg-white/5 p-6 backdrop-blur md:grid-cols-3">
          <div>
            <p className="text-sm text-gray-400">Category</p>
            <h3 className="mt-1 font-bold">{movie.category || "Movies"}</h3>
          </div>

          <div>
            <p className="text-sm text-gray-400">Cast</p>
            <h3 className="mt-1 font-bold">{movie.cast || "Not available"}</h3>
          </div>

          <div>
            <p className="text-sm text-gray-400">Director</p>
            <h3 className="mt-1 font-bold">
              {movie.director || "Not available"}
            </h3>
          </div>
        </div>

        <ReviewSection movieId={movie.id} />
      </section>
    </div>
  );
};

export default MovieDetailsPage;