import { useEffect, useRef, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ArrowLeft, Crown, Loader2, Lock, Play } from "lucide-react";
import { toast } from "react-toastify";
import api from "../api/api";

const getMediaUrl = (url) => {
  if (!url) return "";
  if (url.startsWith("http")) return url;
  return `http://localhost:8080${url}`;
};

const getYouTubeVideoId = (url = "") => {
  if (!url) return null;

  const patterns = [
    /youtube\.com\/watch\?v=([^&]+)/,
    /youtube\.com\/embed\/([^?&]+)/,
    /youtu\.be\/([^?&]+)/,
    /youtube\.com\/shorts\/([^?&]+)/,
  ];

  for (const pattern of patterns) {
    const match = url.match(pattern);
    if (match?.[1]) return match[1];
  }

  return null;
};

const isYouTubeUrl = (url = "") => {
  return url.includes("youtube.com") || url.includes("youtu.be");
};

const VideoPlayerPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const videoRef = useRef(null);

  const [movie, setMovie] = useState(null);
  const [loading, setLoading] = useState(true);
  const [savingProgress, setSavingProgress] = useState(false);

  const loadMovie = async () => {
    try {
      setLoading(true);
      const res = await api.get(`/movies/${id}`);
      setMovie(res.data);
    } catch (error) {
      toast.error("Unable to load video");
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const saveProgress = async () => {
    if (!videoRef.current || savingProgress) return;

    const currentSecond = Math.floor(videoRef.current.currentTime || 0);
    const durationSeconds = Math.floor(videoRef.current.duration || 0);

    if (currentSecond <= 0 || durationSeconds <= 0) return;

    try {
      setSavingProgress(true);

      await api.post("/watch-history/progress", {
        movieId: Number(id),
        currentSecond,
        durationSeconds,
      });
    } catch (error) {
      console.error("Progress save failed", error);
    } finally {
      setSavingProgress(false);
    }
  };

  useEffect(() => {
    loadMovie();
  }, [id]);

  useEffect(() => {
    const interval = setInterval(saveProgress, 15000);
    return () => clearInterval(interval);
  }, [movie, savingProgress]);

  if (loading) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-black text-white">
        <Loader2 className="animate-spin text-primary" size={44} />
      </div>
    );
  }

  if (!movie) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-black text-white">
        Movie not found
      </div>
    );
  }

  const videoUrl =
    movie.videoUrl ||
    movie.fullVideoUrl ||
    movie.videoFileUrl ||
    movie.trailerUrl;

  const youtubeVideoId = isYouTubeUrl(videoUrl) ? getYouTubeVideoId(videoUrl) : null;

  const isPremiumLocked = movie.premium && !movie.subscriptionActive;

  return (
    <div className="min-h-screen bg-black text-white">
      <div className="fixed left-0 top-0 z-50 flex w-full items-center justify-between bg-gradient-to-b from-black/90 to-transparent px-6 py-5">
        <button
          onClick={() => navigate(-1)}
          className="flex items-center gap-2 rounded-full bg-white/10 px-4 py-2 backdrop-blur hover:bg-white/20"
        >
          <ArrowLeft size={18} />
          Back
        </button>

        <h1 className="max-w-[60vw] truncate text-lg font-bold">
          {movie.title}
        </h1>

        {movie.premium && (
          <span className="flex items-center gap-1 rounded-full bg-yellow-500 px-3 py-1 text-xs font-black text-black">
            <Crown size={14} />
            Premium
          </span>
        )}
      </div>

      {isPremiumLocked ? (
        <div className="flex min-h-screen items-center justify-center px-5">
          <div className="max-w-md rounded-3xl border border-white/10 bg-white/5 p-8 text-center backdrop-blur">
            <Lock className="mx-auto mb-4 text-yellow-400" size={52} />

            <h2 className="text-3xl font-black">Premium Content Locked</h2>

            <p className="mt-3 text-gray-400">
              Subscribe to StreamVerse Premium to watch this movie.
            </p>

            <button
              onClick={() => navigate("/subscription")}
              className="mt-6 rounded-full bg-primary px-8 py-3 font-bold hover:bg-primaryDark"
            >
              View Plans
            </button>
          </div>
        </div>
      ) : videoUrl ? (
        <div className="flex min-h-screen items-center justify-center bg-black">
          {youtubeVideoId ? (
            <iframe
              className="h-screen w-full bg-black"
              src={`https://www.youtube.com/embed/${youtubeVideoId}?autoplay=1&rel=0&modestbranding=1`}
              title={movie.title || "StreamVerse YouTube Player"}
              allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
              allowFullScreen
            />
          ) : (
            <video
              ref={videoRef}
              src={getMediaUrl(videoUrl)}
              controls
              autoPlay
              onPause={saveProgress}
              onEnded={saveProgress}
              className="h-screen w-full bg-black object-contain"
            />
          )}
        </div>
      ) : (
        <div className="flex min-h-screen items-center justify-center px-5 text-center">
          <div>
            <Play className="mx-auto mb-4 text-primary" size={56} />

            <h2 className="text-3xl font-black">Video not available</h2>

            <p className="mt-2 text-gray-400">
              No video file/trailer URL found for this movie.
            </p>
          </div>
        </div>
      )}
    </div>
  );
};

export default VideoPlayerPage;