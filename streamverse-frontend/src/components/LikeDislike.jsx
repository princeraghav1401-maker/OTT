import { ThumbsDown, ThumbsUp } from "lucide-react";
import { toast } from "react-toastify";
import api from "../api/api";

const LikeDislike = ({ movieId }) => {
  const react = async (type) => {
    try {
      await api.post(`/reactions/movie/${movieId}`, { type });
      toast.success(type === "LIKE" ? "Liked" : "Disliked");
    } catch {
      toast.error("Unable to react");
    }
  };

  return (
    <div className="mt-6 flex gap-3">
      <button
        onClick={() => react("LIKE")}
        className="flex items-center gap-2 rounded-full bg-white/10 px-5 py-3 font-bold hover:bg-primary"
      >
        <ThumbsUp size={18} />
        Like
      </button>

      <button
        onClick={() => react("DISLIKE")}
        className="flex items-center gap-2 rounded-full bg-white/10 px-5 py-3 font-bold hover:bg-red-600"
      >
        <ThumbsDown size={18} />
        Dislike
      </button>
    </div>
  );
};

export default LikeDislike;