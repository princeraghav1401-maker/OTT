import { Outlet } from "react-router-dom";
import Navbar from "../components/Navbar";
import MobileBottomNav from "../components/MobileBottomNav";
import Footer from "../components/Footer";

const MainLayout = () => {
  return (
    <div className="min-h-screen bg-[#050812] pb-20 md:pb-0">
      <Navbar />
      <Outlet />
      <Footer />
      <MobileBottomNav />
    </div>
  );
};

export default MainLayout;