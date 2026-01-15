export default function NotificationDropdown({
  notifications = [],
  onRead
}) {
  const hasNotifications =
    Array.isArray(notifications) && notifications.length > 0;

  return (
    <div
      role="menu"
      aria-label="Notifications"
      className="
        fixed
        top-16 right-4
        w-[20rem] sm:w-[22rem]
        max-w-[calc(100vw-2rem)]
        max-h-[70vh]
        bg-black/95 backdrop-blur
        border border-white/10
        rounded-xl shadow-2xl
        z-50
        overflow-hidden
      "
    >
      {/* HEADER */}
      <div className="px-4 py-3 font-semibold border-b border-white/10">
        Notifications
      </div>

      {/* EMPTY STATE */}
      {!hasNotifications && (
        <div className="p-6 text-center text-sm text-gray-400">
          You’re all caught up 🎉
        </div>
      )}

      {/* LIST */}
      {hasNotifications && (
        <ul
          role="list"
          className="
            overflow-y-auto
            divide-y divide-white/5
            scrollbar-thin
            scrollbar-thumb-white/20
            scrollbar-track-transparent
          "
        >
          {notifications.map((n) => (
            <li key={n.id}>
              <button
                type="button"
                onClick={() => !n.read && onRead(n.id)}
                className={`
                  w-full text-left px-4 py-3
                  transition
                  hover:bg-white/10
                  focus:outline-none focus:bg-white/10
                  ${!n.read ? "bg-white/5" : "opacity-70"}
                `}
              >
                <div className="flex justify-between items-start gap-2">
                  <div className="text-sm font-medium leading-snug">
                    {n.title}
                  </div>

                  {!n.read && (
                    <span
                      aria-hidden
                      className="mt-1 h-2 w-2 rounded-full bg-blue-500 shrink-0"
                    />
                  )}
                </div>

                <div className="text-sm text-gray-400 mt-1 leading-snug">
                  {n.message}
                </div>

                <div className="text-xs text-gray-500 mt-1">
                  {new Date(n.createdAt).toLocaleString()}
                </div>
              </button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
