import { createBrowserRouter } from "react-router-dom";

import MainLayout from "../layouts/MainLayout";
import AdminLayout from "../layouts/AdminLayout";

import HomePage from "../pages/HomePage";
import LoginPage from "../pages/LoginPage";
import RegisterPage from "../pages/RegisterPage";
import MovieDetailsPage from "../pages/MovieDetailsPage";
import VideoPlayerPage from "../pages/VideoPlayerPage";
import SubscriptionPage from "../pages/SubscriptionPage";
import WatchlistPage from "../pages/WatchlistPage";
import SearchPage from "../pages/SearchPage";
import ProfilePage from "../pages/ProfilePage";
import BrowsePage from "../pages/BrowsePage";
import WatchHistoryPage from "../pages/WatchHistoryPage";
import PaymentSuccessPage from "../pages/PaymentSuccessPage";
import PaymentFailurePage from "../pages/PaymentFailurePage";
import YouTubePlayerPage from "../pages/YouTubePlayerPage";

import AdminDashboard from "../pages/admin/AdminDashboard";
import AdminContentUploadPage from "../pages/admin/AdminContentUploadPage";
import AdminMovieManagementPage from "../pages/admin/AdminMovieManagementPage";
import AdminUserManagementPage from "../pages/admin/AdminUserManagementPage";
import EditMoviePage from "../pages/admin/EditMoviePage";
import AdminRoute from "./AdminRoute";
import ProtectedRoute from "./ProtectedRoute";
import VerifyEmailPage from "../pages/VerifyEmailPage";

const AppRoutes = createBrowserRouter([
  {
    path: "/",
    element: <MainLayout />,
    children: [
      { index: true, element: <HomePage /> },
      { path: "movies/:id", element: <MovieDetailsPage /> },
      { path: "browse", element: <BrowsePage /> },
      { path: "search", element: <SearchPage /> },

      {
        path: "subscription",
        element: (
          <ProtectedRoute>
            <SubscriptionPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "watchlist",
        element: (
          <ProtectedRoute>
            <WatchlistPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "history",
        element: (
          <ProtectedRoute>
            <WatchHistoryPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "profile",
        element: (
          <ProtectedRoute>
            <ProfilePage />
          </ProtectedRoute>
        ),
      },
      {
        path: "payment/success",
        element: (
          <ProtectedRoute>
            <PaymentSuccessPage />
          </ProtectedRoute>
        ),
      },
  {
    path: "/watch/youtube/:videoId",
    element: <YouTubePlayerPage />,
  },
      {
        path: "payment/failure",
        element: (
          <ProtectedRoute>
            <PaymentFailurePage />
          </ProtectedRoute>
        ),
      },
    ],
  },

  {
    path: "/watch/movie/:id",
    element: (
      <ProtectedRoute>
        <VideoPlayerPage />
      </ProtectedRoute>
    ),
  },

{
  path: "/admin",
  element: (
    <AdminRoute>
      <AdminLayout />
    </AdminRoute>
  ),
  children: [
    { index: true, element: <AdminDashboard /> },
    { path: "upload", element: <AdminContentUploadPage /> },
    { path: "movies", element: <AdminMovieManagementPage /> },
    { path: "movies/edit/:id", element: <EditMoviePage /> },
    { path: "users", element: <AdminUserManagementPage /> },
  ],
},

  { path: "/login", element: <LoginPage /> },
  { path: "/register", element: <RegisterPage /> },
  { path: "/verify-email", element: <VerifyEmailPage /> },
]);

export default AppRoutes;