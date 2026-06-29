import { Globe, Mail, PlayCircle, HelpCircle } from "lucide-react";

const Footer = () => {
  return (
    <footer className="border-t border-white/10 bg-[#070b13] px-6 py-10 text-white">
      <div className="mx-auto grid max-w-7xl gap-8 md:grid-cols-4">
        <div>
          <h2 className="text-3xl font-black text-primary">StreamVerse</h2>
          <p className="mt-3 text-sm text-gray-400">
            Movies, shows, originals and premium entertainment in one place.
          </p>
        </div>

        <div>
          <h3 className="font-bold">Company</h3>
          <p className="mt-3 text-sm text-gray-400">About us</p>
          <p className="mt-2 text-sm text-gray-400">Careers</p>
          <p className="mt-2 text-sm text-gray-400">Help</p>
        </div>

        <div>
          <h3 className="font-bold">Watch</h3>
          <p className="mt-3 text-sm text-gray-400">Movies</p>
          <p className="mt-2 text-sm text-gray-400">Web Series</p>
          <p className="mt-2 text-sm text-gray-400">Premium</p>
        </div>

        <div>
          <h3 className="font-bold">Connect</h3>

          <div className="mt-4 flex gap-3">
            <div className="rounded-full bg-white/10 p-3 hover:bg-primary">
              <Globe size={18} />
            </div>

            <div className="rounded-full bg-white/10 p-3 hover:bg-primary">
              <Mail size={18} />
            </div>

            <div className="rounded-full bg-white/10 p-3 hover:bg-primary">
              <PlayCircle size={18} />
            </div>

            <div className="rounded-full bg-white/10 p-3 hover:bg-primary">
              <HelpCircle size={18} />
            </div>
          </div>
        </div>
      </div>

      <p className="mt-10 text-center text-xs text-gray-500">
        © 2026 StreamVerse OTT. All rights reserved.
      </p>
    </footer>
  );
};

export default Footer;