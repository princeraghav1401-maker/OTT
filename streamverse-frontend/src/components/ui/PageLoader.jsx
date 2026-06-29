import { Loader2 } from "lucide-react";

const PageLoader = ({ text = "Loading StreamVerse..." }) => {
  return (
    <div className="flex min-h-screen items-center justify-center bg-dark text-white">
      <div className="flex flex-col items-center gap-4">
        <Loader2 className="animate-spin text-primary" size={48} />
        <p className="text-sm text-gray-400">{text}</p>
      </div>
    </div>
  );
};

export default PageLoader;