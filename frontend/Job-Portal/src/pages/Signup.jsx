import { useState } from "react";
import { useNavigate } from "react-router-dom";
import authApi from "../api/authApi";

export default function Signup() {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("STUDENT");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      await authApi.post("/auth/signup", {
        email,
        password,
        role,
      });

      navigate("/login");
    } catch {
      setError("Signup failed. Email may already exist.");
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
          <h1 className="text-2xl font-semibold tracking-tight">
            Create account
          </h1>
          <p className="mt-2 text-sm text-gray-400">
            Choose a role and get started in minutes
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

          {/* Role Selection */}
          <div>
            <div className="text-[11px] uppercase tracking-widest text-gray-400 mb-3">
              Select role
            </div>

            <div className="grid grid-cols-2 gap-4">
              <RoleCard
                title="Student"
                description="Discover jobs and apply intelligently"
                active={role === "STUDENT"}
                onClick={() => setRole("STUDENT")}
              />

              <RoleCard
                title="Recruiter"
                description="Post roles and manage candidates"
                active={role === "RECRUITER"}
                onClick={() => setRole("RECRUITER")}
              />
            </div>
          </div>

          {/* Submit */}
          <button
            type="submit"
            disabled={loading}
            className="
              w-full rounded-xl
              bg-white text-black
              py-2.5
              text-sm font-medium
              hover:bg-gray-200 transition
              disabled:opacity-50
            "
          >
            {loading ? "Creating account…" : "Create account"}
          </button>
        </form>

        {/* Footer */}
        <p className="mt-8 text-center text-sm text-gray-400">
          Already have an account?{" "}
          <span
            onClick={() => navigate("/login")}
            className="cursor-pointer text-white hover:underline"
          >
            Sign in
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

/* ================= ROLE CARD ================= */

function RoleCard({ title, description, active, onClick }) {
  return (
    <div
      onClick={onClick}
      className={`
        cursor-pointer rounded-2xl p-4 transition
        border
        ${
          active
            ? "border-white bg-white/10"
            : "border-white/10 hover:border-white/30"
        }
      `}
    >
      <div className="flex items-center justify-between">
        <h3 className="text-sm font-medium text-white">{title}</h3>
        {active && <span className="text-xs font-medium text-white">✓</span>}
      </div>
      <p className="mt-2 text-xs text-gray-400 leading-relaxed">
        {description}
      </p>
    </div>
  );
}
