import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import TopBar from "../components/layout/TopBar";
import { useAuth } from "../auth/AuthContext";
import Stomp from "stompjs";

export default function ChatPage() {
  const navigate = useNavigate();
  const { user } = useAuth();

  const [chatCode, setChatCode] = useState("");
  const [senderName, setSenderName] = useState(
    localStorage.getItem("chat_sender_name") || ""
  );
  const [connected, setConnected] = useState(false);
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");

  const stompRef = useRef(null);
  const bottomRef = useRef(null);

  /* ================= CONNECT ================= */

  const connect = (chatId) => {
    const socket = new WebSocket("ws://localhost:8085/ws-chat");
    const stomp = Stomp.over(socket);
    stomp.debug = null;
    stompRef.current = stomp;

    stomp.connect({}, () => {
      setConnected(true);

      stomp.subscribe(`/topic/chat.${chatId}`, (msg) => {
        setMessages((prev) => [...prev, JSON.parse(msg.body)]);
      });
    });
  };

  /* ================= JOIN ================= */

  const joinChat = () => {
    if (!senderName.trim() || chatCode.length !== 8) return;

    localStorage.setItem("chat_sender_name", senderName.trim());
    connect(`CHAT-${chatCode}`);
  };

  /* ================= SEND ================= */

  const sendMessage = () => {
    if (!input.trim()) return;

    stompRef.current.send(
      "/app/chat.send",
      {},
      JSON.stringify({
        chatRoomId: `CHAT-${chatCode}`,
        senderName: senderName,
        content: input,
      })
    );

    setInput("");
  };

  /* ================= AUTO SCROLL ================= */

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  return (
    <div className="min-h-screen bg-black text-white flex flex-col">
      <TopBar user={user} onLogout={() => navigate("/login")} />

      {/* ================= JOIN CARD ================= */}
      {!connected && (
        <div className="flex-1 flex items-center justify-center">
          <div className="border border-white/10 bg-white/5 rounded-2xl p-8 w-[380px]">
            <h2 className="text-xl font-semibold mb-2 text-center">
              Job Portal Chat
            </h2>
            <p className="text-gray-400 text-sm mb-6 text-center">
              Enter your name and Chat ID
            </p>

            <input
              value={senderName}
              onChange={(e) => setSenderName(e.target.value)}
              placeholder="Your Name (Student / Recruiter)"
              className="w-full mb-3 px-3 py-2 bg-black border border-white/20 rounded-md text-sm"
            />

            <input
              value={chatCode}
              onChange={(e) => setChatCode(e.target.value.toUpperCase())}
              maxLength={8}
              placeholder="Chat ID"
              className="w-full mb-5 px-3 py-2 bg-black border border-white/20 rounded-md text-sm"
            />

            <button
              onClick={joinChat}
              className="w-full py-2 bg-white text-black rounded-md font-medium"
            >
              Join Chat
            </button>
          </div>
        </div>
      )}

      {/* ================= CHAT ================= */}
      {connected && (
        <div className="flex-1 max-w-4xl mx-auto w-full flex flex-col px-6 py-6">
          {/* HEADER */}
          <div className="border border-white/10 bg-white/5 rounded-2xl p-4 mb-4 flex items-center justify-between">
            <div>
              <div className="font-semibold">Live Chat</div>
              <div className="text-xs text-gray-400">
                You are chatting as <b>{senderName}</b>
              </div>
            </div>

            <span className="text-sm px-3 py-1 rounded bg-yellow-500/20 text-yellow-400">
              Active
            </span>
          </div>

          {/* MESSAGES */}
          <div className="flex-1 overflow-y-auto space-y-4 mb-4 px-2">
            {messages.map((m, i) => {
              const isMine = m.senderName === senderName;

              return (
                <div
                  key={i}
                  className={`flex ${isMine ? "justify-end" : "justify-start"}`}
                >
                  <div
                    className={`max-w-md px-4 py-2 rounded-xl text-sm border
                      ${
                        isMine
                          ? "bg-blue-600/20 border-blue-500/30 text-blue-100"
                          : "bg-white/5 border-white/10 text-gray-200"
                      }`}
                  >
                    <div className="text-[11px] text-gray-400 mb-1">
                      {m.senderName}
                    </div>
                    {m.content}
                  </div>
                </div>
              );
            })}
            <div ref={bottomRef} />
          </div>

          {/* INPUT */}
          <div className="border border-white/10 bg-white/5 rounded-xl p-3 flex gap-3">
            <input
              value={input}
              onChange={(e) => setInput(e.target.value)}
              placeholder="Type your message…"
              className="flex-1 bg-black border border-white/20 rounded-md px-3 py-2 text-sm"
              onKeyDown={(e) => e.key === "Enter" && sendMessage()}
            />
            <button
              onClick={sendMessage}
              className="px-5 py-2 bg-blue-600/20 text-blue-400 rounded-md hover:bg-blue-600/30"
            >
              Send
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
