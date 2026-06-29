const MovieCardSkeleton = ({ count = 5 }) => {
  return (
    <div className="flex gap-5 overflow-hidden">
      {Array.from({ length: count }).map((_, index) => (
        <div
          key={index}
          className="min-w-[190px] overflow-hidden rounded-2xl bg-white/5"
        >
          <div className="h-[280px] animate-pulse bg-white/10" />
          <div className="space-y-3 p-4">
            <div className="h-4 w-3/4 animate-pulse rounded bg-white/10" />
            <div className="h-3 w-1/2 animate-pulse rounded bg-white/10" />
          </div>
        </div>
      ))}
    </div>
  );
};

export default MovieCardSkeleton;