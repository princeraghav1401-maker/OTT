import { useState } from "react";
import { motion } from "framer-motion";
import { Check, Crown, Loader2, ShieldCheck, Sparkles, Zap } from "lucide-react";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import api from "../api/api";
import { useAuth } from "../context/AuthContext";

const plans = [
  {
    id: "MONTHLY",
    name: "Monthly",
    price: 299,
    duration: "1 Month",
    icon: Sparkles,
    popular: false,
    features: [
      "Unlimited movies & series",
      "HD streaming",
      "Watchlist & history",
      "Mobile + desktop access",
    ],
  },
  {
    id: "QUARTERLY",
    name: "Quarterly",
    price: 799,
    duration: "3 Months",
    icon: Zap,
    popular: true,
    features: [
      "Everything in Monthly",
      "Premium content access",
      "Better value plan",
      "Priority playback support",
    ],
  },
  {
    id: "YEARLY",
    name: "Yearly",
    price: 1999,
    duration: "12 Months",
    icon: Crown,
    popular: false,
    features: [
      "Everything in Quarterly",
      "Best yearly savings",
      "Full OTT experience",
      "Early access to new content",
    ],
  },
];

const loadRazorpayScript = () => {
  return new Promise((resolve) => {
    if (window.Razorpay) {
      resolve(true);
      return;
    }

    const script = document.createElement("script");
    script.src = "https://checkout.razorpay.com/v1/checkout.js";
    script.onload = () => resolve(true);
    script.onerror = () => resolve(false);
    document.body.appendChild(script);
  });
};

const SubscriptionPage = () => {
  const navigate = useNavigate();
  const { user } = useAuth();

  const [loadingPlan, setLoadingPlan] = useState(null);

  const buyPlan = async (plan) => {
    try {
      setLoadingPlan(plan.id);

      const isLoaded = await loadRazorpayScript();

      if (!isLoaded) {
        toast.error("Razorpay checkout load nahi hua");
        return;
      }

      const orderRes = await api.post("/payments/create-order", {
        plan: plan.id,
        amount: plan.price,
      });

      const order = orderRes.data;
      console.log("Razorpay order:", order);

      const razorpayOrderId =
        order.razorpayOrderId || order.orderId || order.id;

      if (!razorpayOrderId) {
        toast.error("Backend se Razorpay Order ID nahi aa rahi");
        return;
      }

      const options = {
        key: import.meta.env.VITE_RAZORPAY_KEY_ID,
        amount: order.amount || plan.price * 100,
        currency: order.currency || "INR",
        name: "StreamVerse OTT",
        description: `${plan.name} Premium Subscription`,
        order_id: razorpayOrderId,

        handler: async function (response) {
          try {
            const verifyRes = await api.post("/payments/verify", {
              plan: plan.id,
              amount: plan.price,
              razorpayOrderId: response.razorpay_order_id,
              razorpayPaymentId: response.razorpay_payment_id,
              razorpaySignature: response.razorpay_signature,
            });

            toast.success("Subscription activated successfully");

            navigate("/payment/success", {
              state: {
                plan: plan.id,
                subscription: verifyRes.data,
              },
            });
          } catch (error) {
            toast.error(
              error?.response?.data?.message || "Payment verification failed"
            );

            console.error("Verify failed:", error?.response?.data || error);

            navigate("/payment/failure", {
              state: {
                reason: "Payment verification failed",
              },
            });
          }
        },

        prefill: {
          name: user?.name || "StreamVerse User",
          email: user?.email || "",
          contact: user?.phone || "",
        },

        notes: {
          plan: plan.id,
          app: "StreamVerse OTT",
        },

        theme: {
          color: "#ff6b35",
        },

        modal: {
          ondismiss: function () {
            toast.info("Payment cancelled");
          },
        },
      };

      const razorpay = new window.Razorpay(options);
      razorpay.open();
    } catch (error) {
      toast.error(error?.response?.data?.message || "Unable to start payment");
      console.error("Payment error:", error?.response?.data || error);
    } finally {
      setLoadingPlan(null);
    }
  };

  return (
    <div className="min-h-screen bg-dark px-6 py-28 text-white">
      <div className="mx-auto max-w-7xl">
        <motion.div
          initial={{ opacity: 0, y: 35 }}
          animate={{ opacity: 1, y: 0 }}
          className="mx-auto max-w-3xl text-center"
        >
          <div className="mx-auto mb-5 flex h-16 w-16 items-center justify-center rounded-2xl bg-gradient-to-br from-yellow-400 to-primary text-black shadow-glow">
            <Crown size={34} />
          </div>

          <h1 className="text-4xl font-black md:text-6xl">
            Choose Your Premium Plan
          </h1>

          <p className="mt-4 text-gray-400">
            Unlock premium movies, exclusive series, HD streaming and a complete
            StreamVerse experience.
          </p>
        </motion.div>

        <div className="mt-14 grid gap-7 md:grid-cols-3">
          {plans.map((plan, index) => {
            const Icon = plan.icon;
            const loading = loadingPlan === plan.id;

            return (
              <motion.div
                key={plan.id}
                initial={{ opacity: 0, y: 35 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: index * 0.12 }}
                className={`relative overflow-hidden rounded-3xl border p-7 backdrop-blur-xl ${
                  plan.popular
                    ? "border-primary bg-primary/10 shadow-glow"
                    : "border-white/10 bg-white/5"
                }`}
              >
                {plan.popular && (
                  <div className="absolute right-5 top-5 rounded-full bg-primary px-4 py-1 text-xs font-black">
                    MOST POPULAR
                  </div>
                )}

                <div className="mb-6 flex h-14 w-14 items-center justify-center rounded-2xl bg-white/10">
                  <Icon className="text-primary" size={30} />
                </div>

                <h2 className="text-2xl font-black">{plan.name}</h2>
                <p className="mt-1 text-sm text-gray-400">{plan.duration}</p>

                <div className="mt-6 flex items-end gap-1">
                  <span className="text-5xl font-black">₹{plan.price}</span>
                  <span className="mb-2 text-gray-400">/ plan</span>
                </div>

                <div className="mt-7 space-y-4">
                  {plan.features.map((feature) => (
                    <div key={feature} className="flex items-center gap-3">
                      <span className="flex h-6 w-6 items-center justify-center rounded-full bg-primary/20 text-primary">
                        <Check size={15} />
                      </span>
                      <span className="text-sm text-gray-300">{feature}</span>
                    </div>
                  ))}
                </div>

                <button
                  disabled={loading}
                  onClick={() => buyPlan(plan)}
                  className="mt-8 flex w-full items-center justify-center gap-2 rounded-2xl bg-primary px-5 py-4 font-black text-white transition hover:bg-primaryDark disabled:opacity-60"
                >
                  {loading ? (
                    <>
                      <Loader2 className="animate-spin" size={18} />
                      Opening Razorpay...
                    </>
                  ) : (
                    <>
                      <ShieldCheck size={18} />
                      Subscribe Now
                    </>
                  )}
                </button>
              </motion.div>
            );
          })}
        </div>

        <div className="mt-12 rounded-3xl border border-white/10 bg-white/5 p-6 text-center text-sm text-gray-400">
          Razorpay test checkout enabled. Test card: 4111 1111 1111 1111, any
          future expiry, any CVV.
        </div>
      </div>
    </div>
  );
};

export default SubscriptionPage;