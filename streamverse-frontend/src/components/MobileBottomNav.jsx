import { NavLink } from "react-router-dom";
import { Home, Search, Heart, Crown, User } from "lucide-react";

const links = [
  { to: "/", label: "Home", icon: Home },
  { to: "/search", label: "Search", icon: Search },
  { to: "/watchlist", label: "Watchlist", icon: Heart },
  { to: "/subscription", label: "Plans", icon: Crown },
  { to: "/profile", label: "Profile", icon: User },
];

const MobileBottomNav = () => {
  return (
    <div className="fixed bottom-0 left-0 z-50 w-full border-t border-white/10 bg-[#080b13]/95 px-3 py-2 backdrop-blur-xl md:hidden">
      <div className="flex items-center justify-between">
        {links.map((item) => {
          const Icon = item.icon;

          return (
            <NavLink
              key={item.to}
              to={item.to}
              className={({ isActive }) =>
                `flex flex-col items-center gap-1 rounded-2xl px-3 py-2 text-xs transition ${
                  isActive ? "text-primary" : "text-gray-400"
                }`
              }
            >
              <Icon size={20} />
              <span>{item.label}</span>
            </NavLink>
          );
        })}
      </div>
    </div>
  );
};

export default MobileBottomNav;