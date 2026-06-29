import { XCircle } from "lucide-react";
import { Link } from "react-router-dom";

const PaymentFailurePage = () => {
  return (
    <div className="flex min-h-screen items-center justify-center bg-dark px-6 text-white">
      <div className="max-w-md rounded-3xl border border-white/10 bg-white/5 p-8 text-center">
        <XCircle className="mx-auto mb-5 text-red-500" size={70} />
        <h1 className="text-4xl font-black">Payment Failed</h1>
        <p className="mt-3 text-gray-400">Something went wrong. Please try again.</p>
        <Link
          to="/subscription"
          className="mt-7 inline-block rounded-full bg-primary px-8 py-3 font-bold"
        >
          Try Again
        </Link>
      </div>
    </div>
  );
};

export default PaymentFailurePage;