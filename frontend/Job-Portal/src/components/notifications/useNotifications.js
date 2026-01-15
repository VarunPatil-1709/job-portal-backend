import { useEffect, useRef, useState } from "react";
import {
  fetchMyNotifications,
  markNotificationRead
} from "./notificationApi";

export default function useNotifications() {
  const [notifications, setNotifications] = useState([]);
  const [open, setOpen] = useState(false);
  const ref = useRef(null);

  // ===============================
  // FETCH NOTIFICATIONS
  // ===============================
  useEffect(() => {
    fetchMyNotifications()
      .then((data) => {
        setNotifications(Array.isArray(data) ? data : []);
      })
      .catch(() => setNotifications([]));
  }, []);

  // ===============================
  // CLOSE DROPDOWN ON OUTSIDE CLICK
  // ===============================
  useEffect(() => {
    const handleClickOutside = (e) => {
      if (ref.current && !ref.current.contains(e.target)) {
        setOpen(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () =>
      document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  // ===============================
  // UNREAD COUNT
  // ===============================
  const unreadCount = notifications.filter((n) => !n.read).length;

  // ===============================
  // MARK AS READ
  // ===============================
  const markRead = async (id) => {
    try {
      await markNotificationRead(id);
      setNotifications((prev) =>
        prev.map((n) =>
          n.id === id ? { ...n, read: true } : n
        )
      );
    } catch {
      // silent fail — UI stability first
    }
  };

  return {
    notifications,
    unreadCount,
    open,
    setOpen,
    markRead,
    ref,
  };
}
