import NotificationBell from "../notifications/NotificationBell";
import NotificationDropdown from "../notifications/NotificationDropdown";
import useNotifications from "../notifications/useNotifications";

export default function TopBar({ onLogout }) {

  const {
    notifications,
    unreadCount,
    open,
    setOpen,
    markRead,
    ref
  } = useNotifications();

  return (
    <div className="border-b border-white/10 bg-black/80 sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-6 py-4 flex justify-between items-center">

        <h1 className="text-lg font-semibold">JOB PORTAL</h1>

        <div ref={ref} className="relative flex items-center gap-4">

          <div onClick={() => setOpen(prev => !prev)}>
            <NotificationBell count={unreadCount} />
          </div>

          {open && (
            <NotificationDropdown
              notifications={notifications}
              onRead={markRead}
            />
          )}

          <button
            onClick={onLogout}
            className="px-4 py-1.5 border border-white/20 rounded text-sm"
          >
            Logout
          </button>
        </div>

      </div>
    </div>
  );
}
