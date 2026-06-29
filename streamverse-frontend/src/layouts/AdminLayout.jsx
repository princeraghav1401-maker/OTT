import { Outlet, NavLink } from "react-router-dom";

const AdminLayout = () => {
  return (
    <div className="flex min-h-screen bg-dark text-white">

      <aside className="w-72 border-r border-white/10 bg-black/30">

        <div className="p-6">
          <h1 className="text-3xl font-black text-primary">
            StreamVerse
          </h1>

          <p className="text-sm text-gray-400">
            Admin Panel
          </p>
        </div>

        <nav className="space-y-2 p-4">

          <NavLink
            to="/admin"
            end
            className="block rounded-xl px-4 py-3 hover:bg-white/10"
          >
            Dashboard
          </NavLink>

          <NavLink
            to="/admin/upload"
            className="block rounded-xl px-4 py-3 hover:bg-white/10"
          >
            Upload Content
          </NavLink>

          <NavLink
            to="/admin/movies"
            className="block rounded-xl px-4 py-3 hover:bg-white/10"
          >
            Manage Movies
          </NavLink>

          <NavLink
            to="/admin/users"
            className="block rounded-xl px-4 py-3 hover:bg-white/10"
          >
            Manage Users
          </NavLink>

        </nav>
      </aside>

      <main className="flex-1">
        <Outlet />
      </main>

    </div>
  );
};

export default AdminLayout;