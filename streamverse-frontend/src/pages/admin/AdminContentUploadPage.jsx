import { useState } from "react";
import { Upload, Save, PlayCircle } from "lucide-react";
import { toast } from "react-toastify";
import api from "../../api/api";

const getYouTubeVideoId = (url = "") => {
  if (!url) return null;

  const patterns = [
    /youtube\.com\/watch\?v=([^&]+)/,
    /youtube\.com\/embed\/([^?&]+)/,
    /youtu\.be\/([^?&]+)/,
    /youtube\.com\/shorts\/([^?&]+)/,
  ];

  for (const pattern of patterns) {
    const match = url.match(pattern);
    if (match?.[1]) return match[1];
  }

  return null;
};

const makeSlug = (text = "") => {
  return text
    .toLowerCase()
    .trim()
    .replace(/[^a-z0-9]+/g, "-")
    .replace(/^-+|-+$/g, "");
};

const AdminContentUploadPage = () => {
  const [form, setForm] = useState({
    title: "",
    slug: "",
    description: "",
    cast: "",
    director: "",
    releaseYear: "",
    durationMinutes: "",
    ageRating: "13+",
    thumbnailUrl: "",
    bannerUrl: "",
    trailerUrl: "",
    premium: false,
    categoryId: 1,
    languageId: 1,
    genreIds: [],
  });

  const [youtubeId, setYoutubeId] = useState("");

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    if (name === "title") {
      setForm((prev) => ({
        ...prev,
        title: value,
        slug: prev.slug ? prev.slug : makeSlug(value),
      }));
      return;
    }

    if (name === "trailerUrl") {
      const videoId = getYouTubeVideoId(value);

      if (videoId) {
        const thumbnail = `https://img.youtube.com/vi/${videoId}/maxresdefault.jpg`;

        setYoutubeId(videoId);
        setForm((prev) => ({
          ...prev,
          trailerUrl: value,
          thumbnailUrl: prev.thumbnailUrl || thumbnail,
          bannerUrl: prev.bannerUrl || thumbnail,
        }));
      } else {
        setYoutubeId("");
        setForm((prev) => ({
          ...prev,
          trailerUrl: value,
        }));
      }

      return;
    }

    setForm((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const resetForm = () => {
    setForm({
      title: "",
      slug: "",
      description: "",
      cast: "",
      director: "",
      releaseYear: "",
      durationMinutes: "",
      ageRating: "13+",
      thumbnailUrl: "",
      bannerUrl: "",
      trailerUrl: "",
      premium: false,
      categoryId: 1,
      languageId: 1,
      genreIds: [],
    });
    setYoutubeId("");
  };

  const submitHandler = async (e) => {
    e.preventDefault();

    if (!form.title.trim()) {
      toast.error("Title required");
      return;
    }

    if (!form.slug.trim()) {
      toast.error("Slug required");
      return;
    }

    if (!form.trailerUrl.trim()) {
      toast.error("YouTube / Video URL required");
      return;
    }

    try {
      await api.post("/admin/movies", {
        ...form,
        releaseYear: Number(form.releaseYear || 2024),
        durationMinutes: Number(form.durationMinutes || 3),
        categoryId: Number(form.categoryId || 1),
        languageId: Number(form.languageId || 1),
      });

      toast.success("Movie created successfully");
      resetForm();
    } catch (error) {
      toast.error(error.response?.data?.message || "Unable to create movie");
      console.error(error.response?.data || error);
    }
  };

  return (
    <div className="min-h-screen bg-dark p-8 text-white">
      <h1 className="text-4xl font-black">Upload Content</h1>

      <p className="mt-2 text-gray-400">
        Add YouTube or local video metadata to StreamVerse.
      </p>

      <form
        onSubmit={submitHandler}
        className="mt-8 grid gap-5 rounded-3xl border border-white/10 bg-white/5 p-6 md:grid-cols-2"
      >
        <input
          name="title"
          value={form.title}
          onChange={handleChange}
          placeholder="Title"
          className="rounded-2xl border border-white/10 bg-black/30 p-4 outline-none focus:border-primary"
        />

        <input
          name="slug"
          value={form.slug}
          onChange={handleChange}
          placeholder="Slug"
          className="rounded-2xl border border-white/10 bg-black/30 p-4 outline-none focus:border-primary"
        />

        <input
          name="cast"
          value={form.cast}
          onChange={handleChange}
          placeholder="Cast"
          className="rounded-2xl border border-white/10 bg-black/30 p-4 outline-none focus:border-primary"
        />

        <input
          name="director"
          value={form.director}
          onChange={handleChange}
          placeholder="Director"
          className="rounded-2xl border border-white/10 bg-black/30 p-4 outline-none focus:border-primary"
        />

        <input
          name="releaseYear"
          value={form.releaseYear}
          onChange={handleChange}
          placeholder="Release Year"
          className="rounded-2xl border border-white/10 bg-black/30 p-4 outline-none focus:border-primary"
        />

        <input
          name="durationMinutes"
          value={form.durationMinutes}
          onChange={handleChange}
          placeholder="Duration Minutes"
          className="rounded-2xl border border-white/10 bg-black/30 p-4 outline-none focus:border-primary"
        />

        <input
          name="ageRating"
          value={form.ageRating}
          onChange={handleChange}
          placeholder="Age Rating"
          className="rounded-2xl border border-white/10 bg-black/30 p-4 outline-none focus:border-primary"
        />

        <input
          name="trailerUrl"
          value={form.trailerUrl}
          onChange={handleChange}
          placeholder="YouTube / Video URL"
          className="rounded-2xl border border-white/10 bg-black/30 p-4 outline-none focus:border-primary"
        />

        <input
          name="thumbnailUrl"
          value={form.thumbnailUrl}
          onChange={handleChange}
          placeholder="Thumbnail URL auto fills from YouTube"
          className="rounded-2xl border border-white/10 bg-black/30 p-4 outline-none focus:border-primary"
        />

        <input
          name="bannerUrl"
          value={form.bannerUrl}
          onChange={handleChange}
          placeholder="Banner URL auto fills from YouTube"
          className="rounded-2xl border border-white/10 bg-black/30 p-4 outline-none focus:border-primary"
        />

        <input
          name="categoryId"
          value={form.categoryId}
          onChange={handleChange}
          placeholder="Category ID"
          className="rounded-2xl border border-white/10 bg-black/30 p-4 outline-none focus:border-primary"
        />

        <input
          name="languageId"
          value={form.languageId}
          onChange={handleChange}
          placeholder="Language ID"
          className="rounded-2xl border border-white/10 bg-black/30 p-4 outline-none focus:border-primary"
        />

        <textarea
          name="description"
          value={form.description}
          onChange={handleChange}
          placeholder="Description"
          className="h-32 rounded-2xl border border-white/10 bg-black/30 p-4 outline-none focus:border-primary md:col-span-2"
        />

        {youtubeId && (
          <div className="rounded-2xl border border-red-500/20 bg-red-500/10 p-4 md:col-span-2">
            <div className="mb-3 flex items-center gap-2 font-bold text-red-300">
              <PlayCircle size={20} />
              YouTube video detected
            </div>

            <div className="grid gap-4 md:grid-cols-2">
              <img
                src={form.thumbnailUrl}
                alt="YouTube thumbnail preview"
                className="h-48 w-full rounded-2xl object-cover"
              />

              <iframe
                className="h-48 w-full rounded-2xl"
                src={`https://www.youtube.com/embed/${youtubeId}`}
                title="YouTube preview"
                allowFullScreen
              />
            </div>
          </div>
        )}

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
          Save Movie
        </button>
      </form>

      <div className="mt-6 rounded-2xl bg-yellow-500/10 p-4 text-sm text-yellow-300">
        <Upload size={18} className="mb-2" />
        YouTube link paste karte hi thumbnail aur banner auto-fill ho jayega.
      </div>
    </div>
  );
};

export default AdminContentUploadPage;