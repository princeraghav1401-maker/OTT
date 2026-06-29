import { useEffect, useState } from "react";
import { ArrowLeft, Save } from "lucide-react";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "react-toastify";
import api from "../../api/api";

const EditMoviePage = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    title: "",
    slug: "",
    description: "",
    cast: "",
    director: "",
    releaseYear: "",
    durationMinutes: "",
    ageRating: "",
    thumbnailUrl: "",
    bannerUrl: "",
    trailerUrl: "",
    premium: false,
    categoryId: 1,
    languageId: 1,
    genreIds: [],
  });

  const loadMovie = async () => {
    const res = await api.get(`/movies/${id}`);
    const movie = res.data;

    setForm({
      title: movie.title || "",
      slug: movie.slug || "",
      description: movie.description || "",
      cast: movie.cast || "",
      director: movie.director || "",
      releaseYear: movie.releaseYear || "",
      durationMinutes: movie.durationMinutes || "",
      ageRating: movie.ageRating || "",
      thumbnailUrl: movie.thumbnailUrl || "",
      bannerUrl: movie.bannerUrl || "",
      trailerUrl: movie.trailerUrl || "",
      premium: movie.premium || false,
      categoryId: movie.categoryId || 1,
      languageId: movie.languageId || 1,
      genreIds: movie.genreIds || [],
    });
  };

  useEffect(() => {
    loadMovie();
  }, [id]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    setForm({
      ...form,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const submitHandler = async (e) => {
    e.preventDefault();

    try {
      await api.put(`/admin/movies/${id}`, {
        ...form,
        releaseYear: Number(form.releaseYear),
        durationMinutes: Number(form.durationMinutes),
        categoryId: Number(form.categoryId),
        languageId: Number(form.languageId),
      });

      toast.success("Movie updated successfully");
      navigate("/admin/movies");
    } catch (error) {
      toast.error("Unable to update movie");
      console.error(error);
    }
  };

  return (
    <div className="min-h-screen bg-dark p-8 text-white">
      <button
        onClick={() => navigate("/admin/movies")}
        className="mb-6 flex items-center gap-2 rounded-full bg-white/10 px-5 py-2 hover:bg-white/20"
      >
        <ArrowLeft size={18} />
        Back
      </button>

      <h1 className="text-4xl font-black">Edit Movie</h1>
      <p className="mt-2 text-gray-400">
        Update movie metadata, poster, banner and video URL.
      </p>

      <form
        onSubmit={submitHandler}
        className="mt-8 grid gap-5 rounded-3xl border border-white/10 bg-white/5 p-6 md:grid-cols-2"
      >
        {[
          ["title", "Title"],
          ["slug", "Slug"],
          ["cast", "Cast"],
          ["director", "Director"],
          ["releaseYear", "Release Year"],
          ["durationMinutes", "Duration Minutes"],
          ["ageRating", "Age Rating"],
          ["thumbnailUrl", "Thumbnail URL"],
          ["bannerUrl", "Banner URL"],
          ["trailerUrl", "Trailer / Video URL"],
          ["categoryId", "Category ID"],
          ["languageId", "Language ID"],
        ].map(([name, label]) => (
          <input
            key={name}
            name={name}
            value={form[name]}
            onChange={handleChange}
            placeholder={label}
            className="rounded-2xl border border-white/10 bg-black/30 p-4 outline-none focus:border-primary"
          />
        ))}

        <textarea
          name="description"
          value={form.description}
          onChange={handleChange}
          placeholder="Description"
          className="h-32 rounded-2xl border border-white/10 bg-black/30 p-4 outline-none focus:border-primary md:col-span-2"
        />

        <label className="flex items-center gap-3">
          <input
            type="checkbox"
            name="premium"
            checked={form.premium}
            onChange={handleChange}
          />
          Premium Content
        </label>

        <button className="flex items-center justify-center gap-2 rounded-2xl bg-primary p-4 font-black hover:bg-primaryDark md:col-span-2">
          <Save size={20} />
          Update Movie
        </button>
      </form>
    </div>
  );
};

export default EditMoviePage;