import { ArrowLeft } from "lucide-react";
import { useNavigate, useParams } from "react-router-dom";

const YouTubePlayerPage = () => {
  const { videoId } = useParams();
  const navigate = useNavigate();

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
          StreamVerse YouTube Player
        </h1>

        <div />
      </div>

      <iframe
        className="h-screen w-full bg-black"
        src={`https://www.youtube.com/embed/${videoId}?autoplay=1&rel=0&modestbranding=1`}
        title="StreamVerse YouTube Player"
        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
        allowFullScreen
      />
    </div>
  );
};

export default YouTubePlayerPage;