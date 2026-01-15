import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import userApi from "../api/userApi";
import chatApi from "../api/chatApi"; // you will create this
import { useAuth } from "../auth/AuthContext";

export default function ChatGround() {
  const { jobId } = useParams();
  const { user } = useAuth();

  const [receiverId, setReceiverId] = useState(null);
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");

  /* 1️⃣ Open chat + get receiver */
  useEffect(() => {
    const openChat = async () => {
      const res = await chatApi.post("/chat/open", {
        jobId: Number(jobId),
      });

      setReceiverId(res.data.receiverId);
    };

    openChat();
  }, [jobId]);

  /* 2️⃣ Load old messages */
  useEffect(() => {
    if (!receiverId) return;

    const loadHistory = async () => {
      const res = await chatApi.get(
        `/chat/history?jobId=${jobId}&receiverId=${receiverId}`
      );
      setMessages(res.data);
    };

    loadHistory();
  }, [receiverId, jobId]);

  /* 3️⃣ WebSocket connection */
  useEffect(() => {
    if (!receiverId) return;

    const socket = new WebSocket("ws://localhost:9091/ws-chat");

    socket.onopen = () => {
      socket.send("CONNECT\naccept-version:1.2\nhost:localhost\n\n\0");

      setTimeout(() => {
        socket.send(
          `SUBSCRIBE\nid:sub-1\ndestination:/queue/chat/${user.id}\n\n\0`
        );
      }, 200);
    };

    socket.onmessage = (e) => {
      if (!e.data.startsWith("MESSAGE")) return;

      const body = e.data.split("\n\n")[1];
      const msg = JSON.parse(body.replace("\0", ""));

      setMessages((prev) => [...prev, msg]);
    };

    return () => socket.close();
  }, [receiverId, user.id]);

  /* 4️⃣ Send message */
  const sendMessage = () => {
    if (!input.trim()) return;

    const socket = new WebSocket("ws://localhost:9091/ws-chat");

    socket.onopen = () => {
      socket.send("CONNECT\naccept-version:1.2\nhost:localhost\n\n\0");

      setTimeout(() => {
        socket.send(
          "SEND\ndestination:/app/chat.send\ncontent-type:application/json\n\n" +
            JSON.stringify({
              receiverId,
              jobId: Number(jobId),
              content: input,
            }) +
            "\0"
        );
        setInput("");
        socket.close();
      }, 200);
    };
  };

  return (
    <div className="min-h-screen bg-black text-white p-6">
      <h2 className="text-xl mb-4">Chat</h2>

      <div className="border border-white/10 rounded-lg p-4 h-[400px] overflow-y-auto bg-white/5">
        {messages.map((m, i) => (
          <div
            key={i}
            className={`mb-2 flex ${
              m.senderId === user.id ? "justify-end" : "justify-start"
            }`}
          >
            <span
              className={`px-3 py-1 rounded text-sm max-w-[60%]
                ${
                  m.senderId === user.id
                    ? "bg-blue-600/30 text-blue-200"
                    : "bg-gray-600/30 text-gray-200"
                }`}
            >
              {m.content}
            </span>
          </div>
        ))}
      </div>

      <div className="flex mt-4 gap-2">
        <input
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="Type message..."
          className="flex-1 bg-black border border-white/10 rounded px-3 py-2"
        />
        <button onClick={sendMessage} className="px-4 py-2 bg-blue-600 rounded">
          Send
        </button>
      </div>
    </div>
  );
}
