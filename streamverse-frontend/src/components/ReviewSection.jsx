import { useEffect, useState } from "react";
import { Star, Send } from "lucide-react";
import { toast } from "react-toastify";
import api from "../api/api";

const ReviewSection = ({ movieId }) => {
  const [reviews, setReviews] = useState([]);
  const [rating, setRating] = useState(5);
  const [comment, setComment] = useState("");

  const loadReviews = async () => {
    try {
      const res = await api.get(`/reviews/movie/${movieId}`);
      setReviews(res.data || []);
    } catch {
      setReviews([]);
    }
  };

  const submitReview = async (e) => {
    e.preventDefault();

    try {
      await api.post(`/reviews/movie/${movieId}`, {
        rating,
        comment,
      });

      toast.success("Review submitted");
      setComment("");
      setRating(5);
      loadReviews();
    } catch {
      toast.error("Unable to submit review");
    }
  };

  useEffect(() => {
    loadReviews();
  }, [movieId]);

  return (
    <section className="mt-12 rounded-3xl border border-white/10 bg-white/5 p-6">
      <h2 className="text-2xl font-black">Reviews & Ratings</h2>

      <form onSubmit={submitReview} className="mt-6">
        <div className="mb-4 flex gap-2">
          {[1, 2, 3, 4, 5].map((num) => (
            <button
              type="button"
              key={num}
              onClick={() => setRating(num)}
              className={num <= rating ? "text-yellow-400" : "text-gray-600"}
            >
              <Star fill="currentColor" />
            </button>
          ))}
        </div>

        <textarea
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          placeholder="Write your review..."
          className="h-28 w-full rounded-2xl border border-white/10 bg-black/30 p-4 outline-none focus:border-primary"
        />

        <button className="mt-4 flex items-center gap-2 rounded-full bg-primary px-6 py-3 font-bold">
          <Send size={17} />
          Submit Review
        </button>
      </form>

      <div className="mt-8 space-y-4">
        {reviews.length === 0 ? (
          <p className="text-gray-400">No reviews yet.</p>
        ) : (
          reviews.map((review, index) => (
            <div key={index} className="rounded-2xl bg-black/30 p-4">
              <div className="mb-2 flex gap-1 text-yellow-400">
                {[1, 2, 3, 4, 5].map((num) => (
                  <Star
                    key={num}
                    size={16}
                    fill={num <= review.rating ? "currentColor" : "none"}
                  />
                ))}
              </div>
              <p className="text-gray-300">{review.comment}</p>
            </div>
          ))
        )}
      </div>
    </section>
  );
};

export default ReviewSection;