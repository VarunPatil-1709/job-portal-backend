import { useNavigate } from "react-router-dom";

export default function Landing() {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-black text-white flex flex-col">
      {/* ================= TOP BAR ================= */}
      <div className="border-b border-white/10">
        <div className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
          <h1 className="text-lg font-semibold tracking-wide">
            JOB <span className="text-gray-400">/</span> INTELLIGENCE
          </h1>

          <div className="flex gap-4">
            <button
              onClick={() => navigate("/login")}
              className="
                text-sm px-4 py-1.5 rounded-md
                border border-white/20
                text-gray-300 hover:text-white
                hover:border-white transition
              "
            >
              Sign in
            </button>

            <button
              onClick={() => navigate("/signup")}
              className="
                text-sm px-4 py-1.5 rounded-md
                bg-white text-black
                hover:bg-gray-200 transition
              "
            >
              Get started
            </button>
          </div>
        </div>
      </div>

      {/* ================= HERO ================= */}
      <div className="flex-1 flex items-center">
        <div className="max-w-7xl mx-auto px-6 py-20 grid grid-cols-1 md:grid-cols-2 gap-12 items-center">
          {/* Left */}
          <div>
            <h2 className="text-4xl md:text-5xl font-semibold tracking-tight leading-tight">
              Intelligent hiring.
              <br />
              Smarter careers.
            </h2>

            <p className="mt-6 text-gray-400 max-w-xl">
              A modern job platform where candidates discover meaningful
              opportunities and recruiters hire with clarity — powered by
              intelligent systems, not noise.
            </p>

            <div className="mt-8 flex gap-4">
              <button
                onClick={() => navigate("/signup")}
                className="
                  px-6 py-2.5 rounded-xl
                  bg-white text-black
                  text-sm font-medium
                  hover:bg-gray-200 transition
                "
              >
                Create account
              </button>

              <button
                onClick={() => navigate("/login")}
                className="
                  px-6 py-2.5 rounded-xl
                  border border-white/20
                  text-sm text-gray-300
                  hover:text-white hover:border-white transition
                "
              >
                Sign in
              </button>
            </div>
          </div>

          {/* Right (Visual block, no image yet) */}
          <div
            className="
            relative rounded-2xl
            border border-white/10
            bg-white/5 p-10
            shadow-[0_0_60px_rgba(255,255,255,0.05)]
          "
          >
            <div className="space-y-6">
              <Feature title="For Candidates">
                Discover roles aligned with your skills and career goals.
              </Feature>

              <Feature title="For Recruiters">
                Publish jobs, manage listings, and hire efficiently.
              </Feature>

              <Feature title="Intelligent Platform">
                Built with scalable backend services and secure authentication.
              </Feature>
            </div>

            <div className="absolute -top-24 -right-24 w-64 h-64 rounded-full bg-white/10 blur-3xl" />
          </div>
        </div>
      </div>

      {/* ================= FOOTER ================= */}
      <div className="border-t border-white/10 text-center text-xs text-gray-500 py-6">
        © {new Date().getFullYear()} Job Intelligence. Built for modern hiring.
      </div>
    </div>
  );
}

/* ================= FEATURE ================= */

function Feature({ title, children }) {
  return (
    <div>
      <div className="text-xs uppercase tracking-widest text-gray-400 mb-1">
        {title}
      </div>
      <div className="text-sm text-white">{children}</div>
    </div>
  );
}
