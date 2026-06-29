import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { motion } from "framer-motion";
import {
  Mail,
  Phone,
  Crown,
  LogOut,
  Film,
  Heart,
  Clock,
  Camera,
  Save,
  Loader2,
} from "lucide-react";
import api from "../api/api";
import { toast } from "react-toastify";

const getImageUrl = (url) => {
  if (!url) return "";
  if (url.startsWith("http")) return url;
  return `http://localhost:8080${url}`;
};

const ProfilePage = () => {
  const { user, logout } = useAuth();

  const [profile, setProfile] = useState(null);
  const [form, setForm] = useState({
    name: "",
    phone: "",
  });
  const [profileImage, setProfileImage] = useState(null);
  const [preview, setPreview] = useState("");
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  const stats = [
    { title: "Watchlist", value: "12", icon: Heart },
    { title: "Watched", value: "48", icon: Film },
    { title: "Hours", value: "126", icon: Clock },
  ];

  const loadProfile = async () => {
    try {
      setLoading(true);
      const res = await api.get("/users/profile");

      setProfile(res.data);
      setForm({
        name: res.data.name || "",
        phone: res.data.phone || "",
      });

      if (res.data.profileImage) {
        setPreview(getImageUrl(res.data.profileImage));
      }
    } catch (error) {
      toast.error("Unable to load profile");
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadProfile();
  }, []);

  const imageChangeHandler = (e) => {
    const file = e.target.files[0];
    if (!file) return;

    setProfileImage(file);
    setPreview(URL.createObjectURL(file));
  };

  const submitHandler = async (e) => {
    e.preventDefault();

    try {
      setSaving(true);

      const data = new FormData();
      data.append("name", form.name);
      data.append("phone", form.phone);

      if (profileImage) {
        data.append("profileImage", profileImage);
      }

      const res = await api.put("/users/profile", data, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      setProfile(res.data);
      setPreview(getImageUrl(res.data.profileImage));
      setProfileImage(null);

      toast.success("Profile updated successfully");
    } catch (error) {
      toast.error(error?.response?.data?.message || "Unable to update profile");
      console.error(error);
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-dark text-white">
        <Loader2 className="animate-spin text-primary" size={44} />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-dark px-6 py-28 text-white">
      <div className="mx-auto max-w-7xl">
        <motion.div
          initial={{ opacity: 0, y: 30 }}
          animate={{ opacity: 1, y: 0 }}
          className="mb-10"
        >
          <h1 className="text-5xl font-black">My Profile</h1>
          <p className="mt-2 text-gray-400">
            Manage your StreamVerse account
          </p>
        </motion.div>

        <div className="grid gap-8 lg:grid-cols-3">
          <motion.div
            initial={{ opacity: 0, x: -30 }}
            animate={{ opacity: 1, x: 0 }}
            className="rounded-3xl border border-white/10 bg-card p-8"
          >
            <div className="flex flex-col items-center">
              <div className="relative">
                {preview ? (
                  <img
                    src={preview}
                    alt="Profile"
                    className="h-32 w-32 rounded-full border-4 border-primary object-cover"
                  />
                ) : (
                  <div className="flex h-32 w-32 items-center justify-center rounded-full bg-primary text-5xl font-black">
                    {profile?.name?.charAt(0) || user?.email?.charAt(0) || "U"}
                  </div>
                )}

                <label className="absolute bottom-1 right-1 flex h-10 w-10 cursor-pointer items-center justify-center rounded-full bg-primary text-white shadow-lg hover:bg-primaryDark">
                  <Camera size={18} />
                  <input
                    type="file"
                    accept="image/*"
                    onChange={imageChangeHandler}
                    className="hidden"
                  />
                </label>
              </div>

              <h2 className="mt-5 text-2xl font-bold">
                {profile?.name || "StreamVerse User"}
              </h2>

              <p className="text-gray-400">
                {profile?.role === "ADMIN" ? "Administrator" : "Member"}
              </p>
            </div>

            <div className="mt-8 space-y-5">
              <div className="flex items-center gap-3">
                <Mail size={18} className="text-primary" />
                <span className="break-all">{profile?.email}</span>
              </div>

              <div className="flex items-center gap-3">
                <Phone size={18} className="text-primary" />
                <span>{profile?.phone || "Not Available"}</span>
              </div>

              <div className="flex items-center gap-3">
                <Crown size={18} className="text-yellow-400" />
                <span>Premium Plan Active</span>
              </div>
            </div>

            <button
              onClick={logout}
              className="mt-8 flex w-full items-center justify-center gap-2 rounded-xl bg-red-600 py-3 font-bold transition hover:bg-red-700"
            >
              <LogOut size={18} />
              Logout
            </button>
          </motion.div>

          <div className="lg:col-span-2">
            <motion.form
              onSubmit={submitHandler}
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              className="rounded-3xl border border-white/10 bg-card p-8"
            >
              <h3 className="text-2xl font-bold">Edit Profile</h3>
              <p className="mt-2 text-gray-400">
                Update your name, phone number and profile picture.
              </p>

              <div className="mt-7 grid gap-5 md:grid-cols-2">
                <div>
                  <label className="mb-2 block text-sm text-gray-400">
                    Full Name
                  </label>
                  <input
                    value={form.name}
                    onChange={(e) =>
                      setForm({ ...form, name: e.target.value })
                    }
                    className="w-full rounded-2xl border border-white/10 bg-black/30 p-4 outline-none focus:border-primary"
                    placeholder="Your name"
                  />
                </div>

                <div>
                  <label className="mb-2 block text-sm text-gray-400">
                    Phone
                  </label>
                  <input
                    value={form.phone}
                    onChange={(e) =>
                      setForm({ ...form, phone: e.target.value })
                    }
                    className="w-full rounded-2xl border border-white/10 bg-black/30 p-4 outline-none focus:border-primary"
                    placeholder="Phone number"
                  />
                </div>
              </div>

              <button
                disabled={saving}
                className="mt-7 flex items-center justify-center gap-2 rounded-2xl bg-primary px-8 py-4 font-black hover:bg-primaryDark disabled:opacity-60"
              >
                {saving ? (
                  <>
                    <Loader2 className="animate-spin" size={18} />
                    Saving...
                  </>
                ) : (
                  <>
                    <Save size={18} />
                    Save Profile
                  </>
                )}
              </button>
            </motion.form>

            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              className="mt-8 rounded-3xl border border-primary/20 bg-card p-8"
            >
              <div className="flex items-center justify-between">
                <div>
                  <h3 className="text-2xl font-bold">
                    Premium Subscription
                  </h3>
                  <p className="mt-2 text-gray-400">
                    Unlimited movies, web series and premium content.
                  </p>
                </div>

                <span className="rounded-full bg-green-500/20 px-5 py-2 font-semibold text-green-400">
                  Active
                </span>
              </div>
            </motion.div>

            <div className="mt-8 grid gap-6 md:grid-cols-3">
              {stats.map((item) => (
                <motion.div
                  key={item.title}
                  whileHover={{ scale: 1.03 }}
                  className="rounded-3xl border border-white/10 bg-card p-6"
                >
                  <item.icon size={30} className="mb-4 text-primary" />
                  <h4 className="text-3xl font-black">{item.value}</h4>
                  <p className="text-gray-400">{item.title}</p>
                </motion.div>
              ))}
            </div>

            <div className="mt-8 rounded-3xl border border-white/10 bg-card p-8">
              <h3 className="mb-6 text-2xl font-bold">Recent Activity</h3>

              <div className="space-y-4">
                <div className="rounded-xl bg-white/5 p-4">
                  🎬 Watched Shadow Mission
                </div>
                <div className="rounded-xl bg-white/5 p-4">
                  ❤️ Added Cyber Force to Watchlist
                </div>
                <div className="rounded-xl bg-white/5 p-4">
                  👑 Upgraded to Premium Plan
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProfilePage;