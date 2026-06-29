import { Play, Plus } from "lucide-react";
import { useNavigate } from "react-router-dom";

const getImageUrl = (url) => {
  if (!url) return "https://placehold.co/1600x800/080b14/ffffff?text=StreamVerse";
  if (url.startsWith("http")) return url;
  return `http://localhost:8080${url}`;
};

const HeroBanner = ({ banner }) => {
  const navigate = useNavigate();

  if (!banner) return null;

  return (
    <section className="relative min-h-[720px] overflow-hidden bg-[#050812] pt-20">
      <img
        src={getImageUrl(banner.bannerUrl || banner.thumbnailUrl)}
        alt={banner.title}
        className="absolute inset-0 h-full w-full object-cover opacity-55"
      />

      <div className="absolute inset-0 bg-gradient-to-r from-[#050812] via-[#050812]/80 to-transparent" />
      <div className="absolute inset-0 bg-gradient-to-t from-[#050812] via-transparent to-[#050812]/70" />

      <div className="relative z-10 mx-auto flex min-h-[650px] max-w-7xl items-center px-6">
        <div className="max-w-2xl">
          <h1 className="text-5xl font-black leading-tight md:text-7xl">
            {banner.title}
          </h1>

          <p className="mt-5 line-clamp-3 max-w-xl text-lg text-gray-300">
            {banner.description ||
              "Watch premium movies, shows and exclusive entertainment on StreamVerse."}
          </p>

          <div className="mt-6 flex flex-wrap gap-3 text-sm font-bold text-gray-200">
            <span>{banner.releaseYear || "2026"}</span>
            <span>|</span>
            <span>{banner.language || "Hindi"}</span>
            <span>|</span>
            <span>{banner.ageRating || "13+"}</span>
          </div>

          <div className="mt-8 flex gap-4">
            <button
              onClick={() => navigate(`/watch/movie/${banner.id}`)}
              className="flex items-center gap-2 rounded-xl bg-gradient-to-r from-blue-500 to-pink-600 px-9 py-4 font-black shadow-xl hover:scale-105"
            >
              <Play size={20} fill="white" />
              Watch Now
            </button>

            <button className="rounded-xl bg-white/15 px-5 py-4 backdrop-blur hover:bg-white/25">
              <Plus />
            </button>
          </div>
        </div>
      </div>
    </section>
  );
};

export default HeroBanner;