import { Film } from "lucide-react";

const EmptyState = ({
  icon: Icon = Film,
  title = "No data found",
  message = "There is nothing to show here yet.",
  actionText,
  onAction,
}) => {
  return (
    <div className="flex min-h-[360px] flex-col items-center justify-center rounded-3xl border border-white/10 bg-white/5 p-8 text-center text-white">
      <Icon className="mb-4 text-primary" size={56} />

      <h2 className="text-2xl font-black">{title}</h2>

      <p className="mt-2 max-w-md text-gray-400">{message}</p>

      {actionText && (
        <button
          onClick={onAction}
          className="mt-6 rounded-full bg-primary px-7 py-3 font-bold hover:bg-primaryDark"
        >
          {actionText}
        </button>
      )}
    </div>
  );
};

export default EmptyState;