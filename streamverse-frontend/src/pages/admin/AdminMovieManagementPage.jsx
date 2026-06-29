import { useEffect, useState } from "react";
import { Pencil, RefreshCcw, Trash2 } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import api from "../../api/api";

const AdminMovieManagementPage = () => {
  const navigate = useNavigate();
  const [movies, setMovies] = useState([]);

  const loadMovies = async () => {
    try {
      const res = await api.get("/movies");
      setMovies(Array.isArray(res.data) ? res.data : []);
    } catch (error) {
      toast.error("Unable to load movies");
      console.error(error);
    }
  };

  const deleteMovie = async (id) => {
    try {
      await api.delete(`/admin/movies/${id}`);
      toast.success("Movie deleted");
      loadMovies();
    } catch {
      toast.error("Delete failed");
    }
  };

  useEffect(() => {
    loadMovies();
  }, []);

  return (
    <div className="min-h-screen bg-dark p-8 text-white">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-4xl font-black">Manage Movies</h1>
          <p className="mt-2 text-gray-400">
            View, edit and delete movie records.
          </p>
        </div>

        <button
          onClick={loadMovies}
          className="rounded-full bg-white/10 p-3 hover:bg-white/20"
        >
          <RefreshCcw />
        </button>
      </div>

      <div className="mt-8 overflow-hidden rounded-3xl border border-white/10 bg-white/5">
        <table className="w-full">
          <thead className="bg-black/30 text-left">
            <tr>
              <th className="p-4">Title</th>
              <th>Year</th>
              <th>Language</th>
              <th>Premium</th>
              <th>Action</th>
            </tr>
          </thead>

          <tbody>
            {movies.map((movie) => (
              <tr key={movie.id} className="border-t border-white/10">
                <td className="p-4 font-bold">{movie.title}</td>
                <td>{movie.releaseYear}</td>
                <td>{movie.language || "N/A"}</td>
                <td>
                  {movie.premium ? (
                    <span className="rounded-full bg-yellow-500 px-3 py-1 text-xs font-bold text-black">
                      Premium
                    </span>
                  ) : (
                    <span className="rounded-full bg-green-500 px-3 py-1 text-xs font-bold text-black">
                      Free
                    </span>
                  )}
                </td>
                <td>
                  <div className="flex gap-2">
                    <button
                      onClick={() => navigate(`/admin/movies/edit/${movie.id}`)}
                      className="rounded-xl bg-blue-600 p-2 hover:bg-blue-700"
                    >
                      <Pencil size={18} />
                    </button>

                    <button
                      onClick={() => deleteMovie(movie.id)}
                      className="rounded-xl bg-red-600 p-2 hover:bg-red-700"
                    >
                      <Trash2 size={18} />
                    </button>
                  </div>
                </td>
              </tr>
            ))}

            {movies.length === 0 && (
              <tr>
                <td colSpan="5" className="p-8 text-center text-gray-400">
                  No movies found.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default AdminMovieManagementPage;