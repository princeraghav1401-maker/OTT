import { RouterProvider } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import { AuthProvider } from "./context/AuthContext";
import AppRoutes from "./routes/AppRoutes";

import "react-toastify/dist/ReactToastify.css";

function App() {
  return (
    <AuthProvider>
      <RouterProvider router={AppRoutes} />

      <ToastContainer
        position="top-right"
        theme="dark"
        autoClose={2500}
      />
    </AuthProvider>
  );
}

export default App;