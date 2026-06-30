import { createContext, useContext, useEffect, useState } from "react";
import api from "../api/api";
import { toast } from "react-toastify";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem("streamverse_token"));

  useEffect(() => {
    const savedUser = localStorage.getItem("streamverse_user");

    if (savedUser) {
      try {
        setUser(JSON.parse(savedUser));
      } catch (err) {
        localStorage.removeItem("streamverse_user");
      }
    }
  }, []);

  const getErrorMessage = (error) => {
    return (
      error?.response?.data?.message ||
      error?.response?.data?.error ||
      error?.response?.data ||
      error?.message ||
      "Something went wrong"
    );
  };

  const saveLogin = (data) => {
    const jwt = data.token;

    localStorage.setItem("streamverse_token", jwt);
    localStorage.setItem("streamverse_user", JSON.stringify(data));

    setToken(jwt);
    setUser(data);
  };

  // ---------------- LOGIN ----------------

  const login = async (email, password) => {
    try {
      const res = await api.post("/auth/login", {
        email,
        password,
      });

      saveLogin(res.data);

      toast.success("Login successful");

      return res.data;
    } catch (error) {
      toast.error(getErrorMessage(error));
      throw error;
    }
  };

  // ---------------- REGISTER ----------------

  const register = async (payload) => {
    try {
      const res = await api.post("/auth/register", payload);

      toast.success(
        res.data.message || "OTP sent successfully."
      );

      return res.data;
    } catch (error) {
      toast.error(getErrorMessage(error));
      throw error;
    }
  };

  // ---------------- VERIFY EMAIL ----------------

  const verifyEmail = async (email, otp) => {
    try {
      const res = await api.post("/auth/verify-email", {
        email,
        otp,
      });

      toast.success(
        res.data.message || "Email verified successfully."
      );

      return res.data;
    } catch (error) {
      toast.error(getErrorMessage(error));
      throw error;
    }
  };

  // ---------------- RESEND OTP ----------------

  const resendOtp = async (email) => {
    try {
      const res = await api.post("/auth/resend-otp", {
        email,
      });

      toast.success(
        res.data.message || "OTP sent successfully."
      );

      return res.data;
    } catch (error) {
      toast.error(getErrorMessage(error));
      throw error;
    }
  };

  // ---------------- GOOGLE LOGIN ----------------

  const googleLogin = async (credential) => {
    try {
      const res = await api.post("/auth/google", {
        credential,
      });

      saveLogin(res.data);

      toast.success("Google Login Successful");

      return res.data;
    } catch (error) {
      toast.error(getErrorMessage(error));
      throw error;
    }
  };

  // ---------------- LOGOUT ----------------

  const logout = () => {
    localStorage.removeItem("streamverse_token");
    localStorage.removeItem("streamverse_user");

    setToken(null);
    setUser(null);

    toast.info("Logged out");
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        login,
        register,
        verifyEmail,
        resendOtp,
        googleLogin,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);