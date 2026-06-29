import { useEffect, useState } from "react";
import { Filter, Loader2, Search, X } from "lucide-react";
import { motion } from "framer-motion";
import api from "../api/api";
import MovieCard from "../components/MovieCard";

const years = ["", "2026", "2025", "2024", "2023", "2022", "2021", "2020"];

const premiumOptions = [
  { label: "All", value: "" },
  { label: "Premium", value: "true" },
  { label: "Free", value: "false" },
];

const normalizeList = (data) => {
  if (Array.isArray(data)) return data;
  if (Array.isArray(data?.content)) return data.content;
  if (Array.isArray(data?.movies)) return data.movies;
  if (Array.isArray(data?.data)) return data.data;
  if (Array.isArray(data?.results)) return data.results;
  return [];
};

const BrowsePage = () => {
  const [filters, setFilters] = useState({
    keyword: "",
    releaseYear: "",
    premium: "",
    category: "",
    language: "",
  });

  const [movies, setMovies] = useState([]);
  const [categories, setCategories] = useState([]);
  const [languages, setLanguages] = useState([]);
  const [loading, setLoading] = useState(false);

  const updateFilter = (key, value) => {
    setFilters((prev) => ({ ...prev, [key]: value }));
  };

  const loadMeta = async () => {
    try {
      const [catRes, langRes] = await Promise.all([
        api.get("/categories"),
        api.get("/languages"),
      ]);

      setCategories(normalizeList(catRes.data));
      setLanguages(normalizeList(langRes.data));
    } catch (error) {
      console.error("Meta load failed:", error);
      setCategories([]);
      setLanguages([]);
    }
  };

  const loadMovies = async () => {
    try {
      setLoading(true);

      const params = {};
      Object.entries(filters).forEach(([key, value]) => {
        if (value !== "") params[key] = value;
      });

      const res = await api.get("/search", { params });
      console.log("Browse response:", res.data);

      setMovies(normalizeList(res.data));
    } catch (error) {
      console.error("Browse failed:", error);
      setMovies([]);
    } finally {
      setLoading(false);
    }
  };

  const resetFilters = () => {
    const emptyFilters = {
      keyword: "",
      releaseYear: "",
      premium: "",
      category: "",
      language: "",
    };

    setFilters(emptyFilters);
    setTimeout(() => loadMovies(), 0);
  };

  useEffect(() => {
    loadMeta();
    loadMovies();
  }, []);

  return (
    <div className="min-h-screen bg-dark px-6 py-28 text-white">
      <div className="mx-auto max-w-7xl">
        <motion.div
          initial={{ opacity: 0, y: 28 }}
          animate={{ opacity: 1, y: 0 }}
          className="mb-9"
        >
          <h1 className="text-4xl font-black md:text-6xl">Browse Movies</h1>
          <p className="mt-3 text-gray-400">
            Filter movies by title, year, category, language and premium access.
          </p>
        </motion.div>

        <div className="rounded-3xl border border-white/10 bg-white/5 p-5 backdrop-blur-xl">
          <div className="grid gap-4 md:grid-cols-6">
            <div className="relative md:col-span-2">
              <Search
                size={18}
                className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400"
              />

              <input
                value={filters.keyword}
                onChange={(e) => updateFilter("keyword", e.target.value)}
                placeholder="Search title, cast..."
                className="w-full rounded-2xl border border-white/10 bg-black/30 py-3 pl-11 pr-4 outline-none focus:border-primary"
              />
            </div>

            <select
              value={filters.releaseYear}
              onChange={(e) => updateFilter("releaseYear", e.target.value)}
              className="rounded-2xl border border-white/10 bg-black/30 p-3 outline-none focus:border-primary"
            >
              {years.map((year) => (
                <option key={year || "all-years"} value={year} className="bg-dark">
                  {year || "All Years"}
                </option>
              ))}
            </select>

            <select
              value={filters.premium}
              onChange={(e) => updateFilter("premium", e.target.value)}
              className="rounded-2xl border border-white/10 bg-black/30 p-3 outline-none focus:border-primary"
            >
              {premiumOptions.map((item) => (
                <option key={item.label} value={item.value} className="bg-dark">
                  {item.label}
                </option>
              ))}
            </select>

            <select
              value={filters.category}
              onChange={(e) => updateFilter("category", e.target.value)}
              className="rounded-2xl border border-white/10 bg-black/30 p-3 outline-none focus:border-primary"
            >
              <option value="" className="bg-dark">
                All Categories
              </option>

              {categories.map((cat) => {
                const value = cat.name || cat.title || cat;
                return (
                  <option key={cat.id || value} value={value} className="bg-dark">
                    {value}
                  </option>
                );
              })}
            </select>

            <select
              value={filters.language}
              onChange={(e) => updateFilter("language", e.target.value)}
              className="rounded-2xl border border-white/10 bg-black/30 p-3 outline-none focus:border-primary"
            >
              <option value="" className="bg-dark">
                All Languages
              </option>

              {languages.map((lang) => {
                const value = lang.name || lang.title || lang;
                return (
                  <option key={lang.id || value} value={value} className="bg-dark">
                    {value}
                  </option>
                );
              })}
            </select>
          </div>

          <div className="mt-5 flex flex-wrap gap-3">
            <button
              onClick={loadMovies}
              disabled={loading}
              className="flex items-center gap-2 rounded-full bg-primary px-7 py-3 font-black hover:bg-primaryDark disabled:opacity-60"
            >
              {loading ? <Loader2 className="animate-spin" size={18} /> : <Filter size={18} />}
              Apply Filters
            </button>

            <button
              onClick={resetFilters}
              className="flex items-center gap-2 rounded-full bg-white/10 px-7 py-3 font-bold hover:bg-white/20"
            >
              <X size={18} />
              Reset
            </button>
          </div>
        </div>

        <div className="mt-10">
          {loading ? (
            <div className="flex min-h-[300px] items-center justify-center">
              <Loader2 className="animate-spin text-primary" size={44} />
            </div>
          ) : movies.length === 0 ? (
            <div className="flex min-h-[350px] flex-col items-center justify-center rounded-3xl border border-white/10 bg-white/5 text-center">
              <Search className="mb-4 text-primary" size={54} />
              <h2 className="text-2xl font-black">No movies found</h2>
              <p className="mt-2 text-gray-400">
                Try changing your filters or search keyword.
              </p>
            </div>
          ) : (
            <div className="grid gap-7 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5">
              {movies.map((movie, index) => (
                <motion.div
                  key={movie.id || `${movie.title}-${index}`}
                  initial={{ opacity: 0, y: 22 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ delay: index * 0.04 }}
                >
                  <MovieCard movie={movie} />
                </motion.div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default BrowsePage;