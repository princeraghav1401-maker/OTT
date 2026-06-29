import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import { MailCheck } from "lucide-react";
import { useAuth } from "../context/AuthContext";

const VerifyEmailPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { verifyEmail, resendOtp } = useAuth();

  const [email, setEmail] = useState(location.state?.email || "");
  const [otp, setOtp] = useState("");
  const [loading, setLoading] = useState(false);
  const [resending, setResending] = useState(false);

  const submitHandler = async (e) => {
    e.preventDefault();

    try {
      setLoading(true);
      await verifyEmail(email, otp);
      navigate("/login");
    } finally {
      setLoading(false);
    }
  };

  const resendHandler = async () => {
    try {
      setResending(true);
      await resendOtp(email);
    } finally {
      setResending(false);
    }
  };

  return (
    <div className="relative flex min-h-screen items-center justify-center overflow-hidden bg-[#0A0A0F] px-5">
      <div className="absolute -top-40 -right-40 h-96 w-96 rounded-full bg-violet-700/20 blur-[120px]" />
      <div className="absolute -bottom-40 -left-40 h-96 w-96 rounded-full bg-cyan-500/15 blur-[120px]" />

      <motion.div
        initial={{ opacity: 0, y: 35, scale: 0.96 }}
        animate={{ opacity: 1, y: 0, scale: 1 }}
        className="relative w-full max-w-md rounded-3xl bg-gradient-to-br from-violet-500/30 via-transparent to-cyan-500/20 p-[1px]"
      >
        <div className="rounded-3xl bg-[#0E0E1A]/90 p-8 text-white backdrop-blur-xl">
          <div className="mx-auto mb-6 flex h-14 w-14 items-center justify-center rounded-2xl bg-gradient-to-br from-cyan-500 to-violet-600">
            <MailCheck size={30} />
          </div>

          <h1 className="text-center text-2xl font-bold">Verify your Gmail</h1>
          <p className="mt-2 text-center text-sm text-gray-500">
            Enter the 6-digit OTP sent to your Gmail account.
          </p>

          <form onSubmit={submitHandler} className="mt-7">
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Email address"
              className="mb-4 w-full rounded-xl border border-white/10 bg-white/5 px-4 py-4 text-sm outline-none focus:border-violet-500"
              required
            />

            <input
              type="text"
              value={otp}
              onChange={(e) => setOtp(e.target.value)}
              placeholder="Enter OTP"
              maxLength={6}
              className="mb-5 w-full rounded-xl border border-white/10 bg-white/5 px-4 py-4 text-center text-xl tracking-[10px] outline-none focus:border-violet-500"
              required
            />

            <button
              disabled={loading}
              className="w-full rounded-xl bg-gradient-to-r from-cyan-500 to-violet-600 py-3 font-semibold disabled:opacity-60"
            >
              {loading ? "Verifying..." : "Verify Email"}
            </button>
          </form>

          <button
            onClick={resendHandler}
            disabled={!email || resending}
            className="mt-4 w-full rounded-xl bg-white/10 py-3 text-sm font-semibold hover:bg-white/15 disabled:opacity-50"
          >
            {resending ? "Sending..." : "Resend OTP"}
          </button>

          <p className="mt-6 text-center text-sm text-gray-500">
            Already verified?{" "}
            <Link to="/login" className="text-violet-400 hover:text-violet-300">
              Login
            </Link>
          </p>
        </div>
      </motion.div>
    </div>
  );
};

export default VerifyEmailPage;