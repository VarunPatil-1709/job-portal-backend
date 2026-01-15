export default function NotificationBell({ count }) {
  return (
    <div className="relative cursor-pointer select-none">
      <span className="text-xl">🔔</span>

      {count > 0 && (
        <span className="
          absolute -top-1 -right-1
          bg-red-600 text-white
          text-[10px] font-semibold
          px-1.5 py-0.5
          rounded-full
        ">
          {count}
        </span>
      )}
    </div>
  );
}
