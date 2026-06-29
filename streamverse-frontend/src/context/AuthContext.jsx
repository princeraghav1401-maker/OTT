import { createContext, useContext, useEffect, useState } from "react";
import api from "../api/api";
import { toast } from "react-toastify";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem("streamverse_token"));

  useEffect(() => {
    const savedUser = localStorage.getItem("streamverse_user");
    if (savedUser) setUser(JSON.parse(savedUser));
  }, []);

  const login = async (email, password) => {
    const res = await api.post("/auth/login", { email, password });

    const jwt = res.data.token;
    localStorage.setItem("streamverse_token", jwt);
    localStorage.setItem("streamverse_user", JSON.stringify(res.data));

    setToken(jwt);
    setUser(res.data);

    toast.success("Login successful");
    return res.data;
  };

  const register = async (payload) => {
    const res = await api.post("/auth/register", payload);
    toast.success(res.data.message || "OTP sent to your Gmail");
    return res.data;
  };

  const verifyEmail = async (email, otp) => {
    const res = await api.post("/auth/verify-email", { email, otp });
    toast.success(res.data.message || "Email verified successfully");
    return res.data;
  };

  const resendOtp = async (email) => {
    const res = await api.post("/auth/resend-otp", { email });
    toast.success(res.data.message || "OTP resent successfully");
    return res.data;
  };

  const logout = () => {
    localStorage.removeItem("streamverse_token");
    localStorage.removeItem("streamverse_user");
    setUser(null);
    setToken(null);
    toast.info("Logged out");
  };

const googleLogin = async (credential) => {
  const res = await api.post("/auth/google", { credential });

  const jwt = res.data.token;
  localStorage.setItem("streamverse_token", jwt);
  localStorage.setItem("streamverse_user", JSON.stringify(res.data));

  setToken(jwt);
  setUser(res.data);

  toast.success("Google login successful");
  return res.data;
};

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        login,
        register,
        googleLogin,
        verifyEmail,
        resendOtp,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);