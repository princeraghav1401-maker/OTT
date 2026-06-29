import { useEffect, useState } from "react";
import {
  Film,
  Users,
  Crown,
  IndianRupee,
  Upload,
  Video,
  UserCog,
} from "lucide-react";
import { Link } from "react-router-dom";
import api from "../../api/api";

const StatCard = ({ title, value, icon }) => (
  <div className="rounded-3xl border border-white/10 bg-white/5 p-6 backdrop-blur">
    <div className="flex items-center justify-between">
      <div>
        <p className="text-sm text-gray-400">{title}</p>
        <h2 className="mt-2 text-3xl font-black text-white">
          {value}
        </h2>
      </div>

      <div className="rounded-2xl bg-primary/20 p-4 text-primary">
        {icon}
      </div>
    </div>
  </div>
);

const AdminDashboard = () => {
  const [stats, setStats] = useState({
    movies: 0,
    users: 0,
    subscriptions: 0,
    revenue: 0,
  });

  const [recentMovies, setRecentMovies] = useState([]);

  useEffect(() => {
    loadDashboard();
  }, []);

  const loadDashboard = async () => {
    try {
      const moviesRes = await api.get("/movies");

      setStats({
        movies: moviesRes.data.length || 0,
        users: 120,
        subscriptions: 54,
        revenue: 24999,
      });

      setRecentMovies(moviesRes.data.slice(0, 5));
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className="min-h-screen bg-dark p-6 text-white">
      <div className="mx-auto max-w-7xl">

        <div className="mb-10">
          <h1 className="text-4xl font-black">
            Admin Dashboard
          </h1>

          <p className="mt-2 text-gray-400">
            Manage content, users and platform analytics
          </p>
        </div>

        {/* STATS */}

        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">

          <StatCard
            title="Total Movies"
            value={stats.movies}
            icon={<Film size={28} />}
          />

          <StatCard
            title="Total Users"
            value={stats.users}
            icon={<Users size={28} />}
          />

          <StatCard
            title="Subscriptions"
            value={stats.subscriptions}
            icon={<Crown size={28} />}
          />

          <StatCard
            title="Revenue"
            value={`₹${stats.revenue}`}
            icon={<IndianRupee size={28} />}
          />
        </div>

        {/* QUICK ACTIONS */}

        <div className="mt-10 rounded-3xl border border-white/10 bg-white/5 p-6">

          <h2 className="mb-6 text-2xl font-bold">
            Quick Actions
          </h2>

          <div className="grid gap-4 md:grid-cols-3">

            <Link
              to="/admin/upload"
              className="flex items-center gap-3 rounded-2xl bg-primary p-4 font-bold"
            >
              <Upload size={20} />
              Upload Content
            </Link>

            <Link
              to="/admin/movies"
              className="flex items-center gap-3 rounded-2xl bg-white/10 p-4 font-bold"
            >
              <Video size={20} />
              Manage Movies
            </Link>

            <Link
              to="/admin/users"
              className="flex items-center gap-3 rounded-2xl bg-white/10 p-4 font-bold"
            >
              <UserCog size={20} />
              Manage Users
            </Link>

          </div>
        </div>

        {/* RECENT MOVIES */}

        <div className="mt-10 rounded-3xl border border-white/10 bg-white/5 p-6">

          <h2 className="mb-6 text-2xl font-bold">
            Recently Added
          </h2>

          <div className="overflow-x-auto">

            <table className="w-full">

              <thead>
                <tr className="border-b border-white/10 text-left">
                  <th className="pb-4">Title</th>
                  <th className="pb-4">Year</th>
                  <th className="pb-4">Language</th>
                  <th className="pb-4">Premium</th>
                </tr>
              </thead>

              <tbody>
                {recentMovies.map((movie) => (
                  <tr
                    key={movie.id}
                    className="border-b border-white/5"
                  >
                    <td className="py-4">{movie.title}</td>

                    <td>{movie.releaseYear}</td>

                    <td>{movie.language}</td>

                    <td>
                      {movie.premium ? (
                        <span className="rounded-full bg-yellow-500 px-3 py-1 text-xs font-bold text-black">
                          PREMIUM
                        </span>
                      ) : (
                        <span className="rounded-full bg-green-500 px-3 py-1 text-xs font-bold text-black">
                          FREE
                        </span>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>

            </table>

          </div>
        </div>

      </div>
    </div>
  );
};

export default AdminDashboard;