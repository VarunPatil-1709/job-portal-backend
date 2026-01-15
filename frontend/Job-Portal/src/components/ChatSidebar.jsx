import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import chatApi from "../api/chatApi"; // axios instance for chat service

export default function ChatSidebar() {
  const [chats, setChats] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const loadChats = async () => {
      try {
        const res = await chatApi.get("/chat/sessions");
        setChats(res.data);
      } catch (e) {
        console.error("Failed to load chats", e);
      } finally {
        setLoading(false);
      }
    };

    loadChats();
  }, []);

  if (loading) {
    return (
      <div className="h-full bg-black text-gray-400 flex items-center justify-center">
        Loading chats…
      </div>
    );
  }

  const activeChats = chats.filter((c) => c.status === "ACTIVE");
  const upcomingChats = chats.filter((c) => c.status === "CREATED");

  return (
    <div className="h-full w-80 bg-black border-r border-white/10 flex flex-col">
      {/* HEADER */}
      <div className="p-4 border-b border-white/10">
        <h2 className="text-white text-lg font-semibold">Chats</h2>
        <p className="text-xs text-gray-400">Active & Scheduled</p>
      </div>

      {/* CHAT LIST */}
      <div className="flex-1 overflow-y-auto">
        {/* ACTIVE */}
        {activeChats.length > 0 && (
          <Section title="Active Chats">
            {activeChats.map((chat) => (
              <ChatItem
                key={chat.chatId}
                chat={chat}
                onJoin={() => navigate(`/chat/${chat.chatId}`)}
                active
              />
            ))}
          </Section>
        )}

        {/* UPCOMING */}
        {upcomingChats.length > 0 && (
          <Section title="Upcoming Chats">
            {upcomingChats.map((chat) => (
              <ChatItem
                key={chat.chatId}
                chat={chat}
                onJoin={() => navigate(`/chat/${chat.chatId}`)}
              />
            ))}
          </Section>
        )}

        {chats.length === 0 && (
          <div className="text-gray-500 text-sm text-center py-10">
            No chats available
          </div>
        )}
      </div>
    </div>
  );
}

/* ================= SUB COMPONENTS ================= */

function Section({ title, children }) {
  return (
    <div className="px-3 py-2">
      <div className="text-xs uppercase text-gray-500 mb-2">{title}</div>
      <div className="space-y-2">{children}</div>
    </div>
  );
}

function ChatItem({ chat, onJoin, active }) {
  return (
    <div
      className={`flex items-center justify-between
                  rounded-lg px-3 py-2
                  border border-white/10
                  ${active ? "bg-green-600/10" : "bg-white/5"}
                  hover:border-white/30`}
    >
      <div>
        <div className="text-sm text-white">Job #{chat.jobId || "—"}</div>

        {chat.startAt && (
          <div className="text-xs text-gray-400">
            {new Date(chat.startAt).toLocaleString()}
          </div>
        )}
      </div>

      <button
        onClick={onJoin}
        className={`text-xs px-3 py-1 rounded
          ${
            active
              ? "bg-green-500/20 text-green-400"
              : "bg-yellow-500/20 text-yellow-400"
          }
          hover:opacity-80`}
      >
        {active ? "Join →" : "View →"}
      </button>
    </div>
  );
}
