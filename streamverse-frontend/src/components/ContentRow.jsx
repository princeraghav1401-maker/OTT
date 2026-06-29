import { ChevronRight } from "lucide-react";
import MovieCard from "./MovieCard";

const ContentRow = ({ title, subtitle, items = [] }) => {
  if (!items.length) return null;

  return (
    <section className="mt-12">
      <div className="mb-5 flex items-end justify-between">
        <div>
          <h2 className="text-2xl font-black md:text-3xl">{title}</h2>
          {subtitle && <p className="mt-1 text-sm text-gray-400">{subtitle}</p>}
        </div>

        <button className="hidden items-center gap-1 rounded-full border border-white/10 px-4 py-2 text-sm text-gray-300 hover:bg-white/10 md:flex">
          View All <ChevronRight size={16} />
        </button>
      </div>

      <div className="flex gap-5 overflow-x-auto pb-4 scrollbar-hide">
        {items.map((movie) => (
          <div key={movie.id} className="min-w-[210px]">
            <MovieCard movie={movie} />
          </div>
        ))}
      </div>
    </section>
  );
};

export default ContentRow;