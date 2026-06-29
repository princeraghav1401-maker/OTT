import { useEffect, useState } from "react";
import api from "../api/api";
import HeroBanner from "../components/HeroBanner";
import ContentRow from "../components/ContentRow";
import ContinueWatchingRow from "../components/ContinueWatchingRow";
import YouTubeRow from "../components/YouTubeRow";
import { useAuth } from "../context/AuthContext";
import PageLoader from "../components/ui/PageLoader";
import ErrorState from "../components/ui/ErrorState";
import EmptyState from "../components/ui/EmptyState";

const HomePage = () => {
  const { token } = useAuth();

  const [trending, setTrending] = useState([]);
  const [recent, setRecent] = useState([]);
  const [premium, setPremium] = useState([]);
  const [latestSeries, setLatestSeries] = useState([]);
  const [continueWatching, setContinueWatching] = useState([]);
  const [watchlist, setWatchlist] = useState([]);

  const [trailers, setTrailers] = useState([]);
  const [trendingYoutube, setTrendingYoutube] = useState([]);
  const [songs, setSongs] = useState([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  const loadHomeData = async () => {
    try {
      setLoading(true);
      setError(false);

      const res = await api.get("/home");
      const data = res.data || {};

      setTrending(data.trendingMovies || []);
      setRecent(data.latestMovies || []);
      setPremium(data.premiumMovies || []);
      setLatestSeries(data.latestSeries || []);
      setContinueWatching(data.continueWatching || []);
      setWatchlist(data.myWatchlist || []);

      setTrailers(data.latestTrailers || []);
      setTrendingYoutube(data.trendingYoutube || []);
      setSongs(data.latestSongs || []);
    } catch (err) {
      console.error("Home API Error:", err);
      setError(true);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadHomeData();
  }, [token]);

  if (loading) return <PageLoader text="Loading StreamVerse..." />;

  if (error) {
    return (
      <div className="min-h-screen bg-[#050812] px-6 py-28">
        <ErrorState
          title="Unable to load homepage"
          message="Backend server ya API issue ho sakta hai."
          onRetry={loadHomeData}
        />
      </div>
    );
  }

  const heroMovie = trending[0] || recent[0] || premium[0];

  return (
    <main className="min-h-screen bg-[#050812] text-white">
      {heroMovie ? (
        <HeroBanner banner={heroMovie} />
      ) : (
        <div className="px-6 pt-28">
          <div className="mx-auto max-w-7xl">
            <EmptyState
              title="No movies available"
              message="Admin panel se movies add karo."
            />
          </div>
        </div>
      )}

      <div className="mx-auto max-w-7xl px-6 pb-16">
        <ContinueWatchingRow items={continueWatching} />

        <ContentRow
          title="Continue Watching"
          subtitle="Resume where you left off"
          items={continueWatching}
        />

        <ContentRow
          title="Trending Now"
          subtitle="Most watched and popular right now"
          items={trending}
        />

        <ContentRow
          title="New on StreamVerse"
          subtitle="Freshly added movies and shows"
          items={recent}
        />

        <ContentRow
          title="Premium Movies"
          subtitle="Exclusive movies for premium members"
          items={premium}
        />

        <ContentRow
          title="Latest Series"
          subtitle="Fresh web series and shows"
          items={latestSeries}
        />

        <YouTubeRow
          title="🔥 Latest Trailers"
          subtitle="Latest trailers from YouTube"
          videos={trailers}
        />

        <YouTubeRow
          title="📺 Trending on YouTube"
          subtitle="Trending movie videos"
          videos={trendingYoutube}
        />

        <YouTubeRow
          title="🎵 Latest Songs"
          subtitle="Trending songs from YouTube"
          videos={songs}
        />

        <ContentRow
          title="My Watchlist"
          subtitle="Movies you saved for later"
          items={watchlist}
        />
      </div>
    </main>
  );
};

export default HomePage;