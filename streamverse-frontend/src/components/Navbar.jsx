import { Link, NavLink } from "react-router-dom";
import { LogOut, Search, User } from "lucide-react";
import { useAuth } from "../context/AuthContext";

const Navbar = () => {
  const { user, logout } = useAuth();

  const linkClass = ({ isActive }) =>
    isActive
      ? "text-primary font-bold"
      : "text-white/80 hover:text-primary transition";

  return (
    <nav className="fixed top-0 z-50 w-full border-b border-white/10 bg-[#080b14]/90 backdrop-blur-xl">
      <div className="mx-auto flex h-20 max-w-7xl items-center justify-between px-4 md:px-6">

        {/* LOGO */}
        <Link
          to="/"
          className="text-2xl font-black text-primary md:text-3xl"
        >
          StreamVerse
        </Link>

        {/* DESKTOP MENU */}
        <div className="hidden items-center gap-9 md:flex">
          <NavLink to="/" className={linkClass}>
            Home
          </NavLink>

          <NavLink to="/browse" className={linkClass}>
            Browse
          </NavLink>

          <NavLink to="/search" className={linkClass}>
            Search
          </NavLink>

          <NavLink to="/watchlist" className={linkClass}>
            Watchlist
          </NavLink>

          <NavLink to="/subscription" className={linkClass}>
            Plans
          </NavLink>

          <NavLink to="/history" className={linkClass}>
            History
          </NavLink>
        </div>

        {/* RIGHT SIDE */}
        <div className="hidden items-center gap-4 md:flex">

          <Link
            to="/search"
            className="rounded-full bg-white/10 p-3 transition hover:bg-primary"
          >
            <Search size={20} />
          </Link>

          {user ? (
            <>
              <Link
                to="/profile"
                className="rounded-full bg-white/10 p-3 transition hover:bg-primary"
              >
                <User size={20} />
              </Link>

             {user?.role === "ADMIN" && (
               <Link
                 to="/admin"
                 className="rounded-full bg-primary px-5 py-2 font-bold text-white"
               >
                 Admin
               </Link>
             )}

              <button
                onClick={logout}
                className="rounded-full bg-red-600 p-3 transition hover:bg-red-700"
              >
                <LogOut size={20} />
              </button>
            </>
          ) : (
            <Link
              to="/login"
              className="rounded-full bg-primary px-6 py-3 font-bold transition hover:bg-primaryDark"
            >
              Login
            </Link>
          )}
        </div>

      </div>
    </nav>
  );
};

export default Navbar;