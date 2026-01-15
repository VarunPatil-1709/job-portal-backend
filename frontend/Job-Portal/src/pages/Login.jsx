import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import authApi from "../api/authApi";
import userApi from "../api/userApi";
import { useAuth } from "../auth/AuthContext";

export default function Login() {
  const navigate = useNavigate();
  const { user, setUser, checkingAuth } = useAuth();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  // 🚨 Auto redirect if already logged in
  useEffect(() => {
    if (!checkingAuth && user) {
      navigate(user.profileCompleted ? "/home" : "/complete-profile");
    }
  }, [user, checkingAuth, navigate]);

  if (checkingAuth) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      await authApi.post("/auth/login", { email, password });

      const res = await userApi.get("/profile/status");
      setUser(res.data);

      navigate(res.data.profileCompleted ? "/home" : "/complete-profile");
    } catch {
      setError("Invalid email or password");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-black flex items-center justify-center px-4 text-white">
      <div
        className="
          w-full max-w-md rounded-2xl
          border border-white/10
          bg-white/5
          p-8
          shadow-[0_0_40px_rgba(255,255,255,0.05)]
        "
      >
        {/* Header */}
        <div className="mb-10 text-center">
          <h1 className="text-2xl font-semibold tracking-tight">Sign in</h1>
          <p className="mt-2 text-sm text-gray-400">
            Access your intelligent workspace
          </p>
        </div>

        {/* Error */}
        {error && (
          <div
            className="
            mb-6 rounded-lg
            border border-red-500/20
            bg-red-500/10
            px-4 py-2
            text-sm text-red-400
          "
          >
            {error}
          </div>
        )}

        {/* Form */}
        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Email */}
          <Field
            label="Email address"
            type="email"
            value={email}
            onChange={setEmail}
          />

          {/* Password */}
          <Field
            label="Password"
            type="password"
            value={password}
            onChange={setPassword}
          />

          {/* Submit */}
          <button
            type="submit"
            disabled={loading}
            className="
              w-full rounded-xl
              bg-white text-black
              py-2.5
              text-sm font-medium
              hover:bg-gray-200
              transition
              disabled:opacity-50
            "
          >
            {loading ? "Signing in…" : "Sign in"}
          </button>
        </form>

        {/* Footer */}
        <p className="mt-8 text-center text-sm text-gray-400">
          Don’t have an account?{" "}
          <span
            onClick={() => navigate("/signup")}
            className="cursor-pointer text-white hover:underline"
          >
            Create one
          </span>
        </p>
      </div>
    </div>
  );
}

/* ================= FIELD ================= */

function Field({ label, type, value, onChange }) {
  return (
    <div>
      <div className="text-[11px] uppercase tracking-widest text-gray-400 mb-2">
        {label}
      </div>
      <input
        type={type}
        required
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className="
          w-full rounded-xl
          bg-black border border-white/15
          px-4 py-2.5
          text-sm text-white
          placeholder-gray-500
          focus:outline-none
          focus:border-white
        "
      />
    </div>
  );
}
