import { AlertTriangle, RefreshCcw } from "lucide-react";

const ErrorState = ({
  title = "Something went wrong",
  message = "Please try again after some time.",
  onRetry,
}) => {
  return (
    <div className="flex min-h-[360px] flex-col items-center justify-center rounded-3xl border border-red-500/20 bg-red-500/10 p-8 text-center text-white">
      <AlertTriangle className="mb-4 text-red-400" size={56} />

      <h2 className="text-2xl font-black">{title}</h2>

      <p className="mt-2 max-w-md text-red-200/80">{message}</p>

      {onRetry && (
        <button
          onClick={onRetry}
          className="mt-6 flex items-center gap-2 rounded-full bg-red-600 px-7 py-3 font-bold hover:bg-red-700"
        >
          <RefreshCcw size={18} />
          Retry
        </button>
      )}
    </div>
  );
};

export default ErrorState;