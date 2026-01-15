import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import authApi from "../api/authApi";
import userApi from "../api/userApi";
import jobApi from "../api/jobApi";
import chatApi from "../api/chatApi";
import { useAuth } from "../auth/AuthContext";
import TopBar from "../components/layout/TopBar";
import useNotifications from "../components/notifications/useNotifications";

export default function Home() {
  const { notifications } = useNotifications();
  const navigate = useNavigate();
  const { setUser } = useAuth();

  const [profile, setProfile] = useState(null);
  const [jobs, setJobs] = useState([]);
  const [stats, setStats] = useState(null);
  const [chats, setChats] = useState([]);
  const [loading, setLoading] = useState(true);

  /* ================= LOAD DATA ================= */

  useEffect(() => {
    const loadData = async () => {
      try {
        const profileRes = await userApi.get("/profile/me");
        const user = profileRes.data;
        if (!user) throw new Error("No profile");

        setProfile(user);

        // STUDENT FLOW
        if (user.role === "STUDENT") {
          const [
            jobsRes,
            appliedRes,
            rejectedRes,
            shortlistedRes,
            statsRes,
          ] = await Promise.all([
            jobApi.get("/jobs"),
            jobApi.get("/my/job/applied"),
            jobApi.get("/my/job/rejected"),
            jobApi.get("/my/job/shortlisted"),
            jobApi.get("/stats"),
          ]);

          const appliedSet = new Set(appliedRes.data.map(j => j.jobId));
          const rejectedSet = new Set(rejectedRes.data.map(j => j.jobId));
          const shortlistedSet = new Set(shortlistedRes.data.map(j => j.jobId));

          setJobs(
            jobsRes.data.map(job => ({
              ...job,
              applied: appliedSet.has(job.id),
              rejected: rejectedSet.has(job.id),
              shortlisted: shortlistedSet.has(job.id),
            }))
          );

          setStats(statsRes.data);
        }

        // RECRUITER FLOW
        else {
          const jobsRes = await jobApi.get("/jobs/my");
          setJobs(jobsRes.data);
        }

        // CHAT SESSIONS
        const chatRes = await chatApi.get("/chat/sessions");
        setChats(chatRes.data);
      } catch {
        navigate("/login");
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, [navigate]);

  const logout = async () => {
    try {
      await authApi.post("/auth/logout");
    } finally {
      setUser(null);
      navigate("/login");
    }
  };

  if (loading || !profile) {
    return (
      <div className="min-h-screen bg-black text-gray-400 flex items-center justify-center">
        Loading…
      </div>
    );
  }

  /* ================= RECRUITER STATS (frontend calc) ================= */

  const recruiterStats =
    profile.role === "RECRUITER"
      ? {
          jobsPosted: jobs.length,
          applicants: jobs.reduce(
            (sum, j) => sum + (j.applicantCount || 0),
            0
          ),
          shortlisted: jobs.reduce(
            (sum, j) => sum + (j.shortlistedCount || 0),
            0
          ),
          hired: jobs.reduce(
            (sum, j) => sum + (j.hiredCount || 0),
            0
          ),
        }
      : null;

  return (
    <div className="min-h-screen bg-black text-white">
      <TopBar user={profile} onLogout={logout} />

      <div className="max-w-7xl mx-auto px-6 py-8 flex gap-8">
        {/* ================= MAIN ================= */}
        <div className="flex-1 space-y-10">
          {/* HERO */}
          <div className="border border-white/10 bg-white/5 rounded-2xl p-8">
            <h2 className="text-3xl font-semibold">
              Welcome {profile.firstName || profile.companyName}
            </h2>
            <p className="text-gray-400 mt-2">
              {profile.role === "STUDENT"
                ? "Browse and track job opportunities."
                : "Manage job postings and applicants."}
            </p>
          </div>

          {/* PROFILE + STATS */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <ProfileCard
              title={profile.role === "STUDENT" ? "Your Profile" : "Organization"}
              fields={
                profile.role === "STUDENT"
                  ? [
                      ["Name", `${profile.firstName} ${profile.lastName}`],
                      ["Email", profile.email],
                    ]
                  : [
                      ["Company", profile.companyName],
                      ["Email", profile.email],
                    ]
              }
            />

            {/* STUDENT STATS */}
            {profile.role === "STUDENT" && (
              <div className="md:col-span-2 grid grid-cols-2 sm:grid-cols-4 gap-6">
                <Stat title="Applied" value={stats?.totalApplied} />
                <Stat title="Shortlisted" value={stats?.shortlistedCount} />
                <Stat title="Rejected" value={stats?.rejectedCount} />
                <Stat title="Selected" value={stats?.selectedCount} />
              </div>
            )}

            {/* RECRUITER STATS */}
            {profile.role === "RECRUITER" && (
              <div className="md:col-span-2 grid grid-cols-2 sm:grid-cols-4 gap-6">
                <Stat title="Jobs Posted" value={recruiterStats.jobsPosted} />
                <Stat title="Applicants" value={recruiterStats.applicants} />
                <Stat title="Shortlisted" value={recruiterStats.shortlisted} />
                <Stat title="Hired" value={recruiterStats.hired} />
              </div>
            )}
          </div>

          {/* 🔥 RECRUITER JOB POST BUTTON (RESTORED) */}
          {profile.role === "RECRUITER" && (
            <div className="flex justify-end">
              <button
                onClick={() => navigate("/recruiter/jobs/create")}
                className="px-6 py-2 bg-white text-black rounded-md font-medium"
              >
                + Post New Job
              </button>
            </div>
          )}

          {/* JOB LIST */}
          <JobsSection
            title={
              profile.role === "STUDENT"
                ? "Job Opportunities"
                : "My Job Posts"
            }
            description={
              profile.role === "STUDENT"
                ? "Browse all active job openings."
                : "Jobs posted by your organization."
            }
            jobs={jobs}
            role={profile.role}
            onJobClick={id =>
              profile.role === "STUDENT"
                ? navigate(`/jobs/${id}`)
                : navigate(`/recruiter/jobs/${id}`)
            }
          />
        </div>

        {/* ================= CHAT SIDEBAR ================= */}
        <ChatSidebar
          chats={chats}
          onJoin={chatId => navigate(`/chat/${chatId}`)}
        />
      </div>
    </div>
  );
}

/* ================= CHAT SIDEBAR ================= */

function ChatSidebar({ chats, onJoin }) {
  const active = chats.filter(c => c.status === "ACTIVE");
  const upcoming = chats.filter(c => c.status === "CREATED");

  return (
    <div className="w-80 border border-white/10 bg-white/5 rounded-2xl p-4 h-fit sticky top-24">
      <h3 className="text-lg font-semibold mb-1">Chats</h3>
      <p className="text-xs text-gray-400 mb-4">Active & Upcoming</p>

      {active.length > 0 && (
        <ChatSection title="Active">
          {active.map(chat => (
            <ChatItem key={chat.chatId} chat={chat} onJoin={onJoin} active />
          ))}
        </ChatSection>
      )}

      {upcoming.length > 0 && (
        <ChatSection title="Upcoming">
          {upcoming.map(chat => (
            <ChatItem key={chat.chatId} chat={chat} onJoin={onJoin} />
          ))}
        </ChatSection>
      )}

      {chats.length === 0 && (
        <div className="text-gray-500 text-sm text-center py-6">
          No chats scheduled
        </div>
      )}
    </div>
  );
}

function ChatSection({ title, children }) {
  return (
    <div className="mb-4">
      <div className="text-xs uppercase text-gray-500 mb-2">{title}</div>
      <div className="space-y-2">{children}</div>
    </div>
  );
}

function ChatItem({ chat, onJoin, active }) {
  return (
    <div
      className={`border rounded-lg p-3 ${
        active
          ? "border-green-500/40 bg-green-500/10"
          : "border-white/10 bg-black/40"
      }`}
    >
      <div className="text-[11px] text-gray-400">Job ID: {chat.jobId}</div>
      <div className="text-sm font-medium truncate">{chat.chatId}</div>

      {chat.startAt && (
        <div className="text-xs text-gray-400 mt-0.5">
          Starts at: {new Date(chat.startAt).toLocaleString()}
        </div>
      )}

      <button
        onClick={() => onJoin(chat.chatId)}
        className="mt-2 text-xs px-3 py-1 rounded
                   bg-yellow-500/20 text-yellow-400
                   hover:bg-yellow-500/30"
      >
        {active ? "Join Chat" : "View"}
      </button>
    </div>
  );
}

/* ================= REUSABLE ================= */

function ProfileCard({ title, fields }) {
  return (
    <div className="border border-white/10 bg-white/5 rounded-2xl p-6">
      <h3 className="text-xs uppercase text-gray-400 mb-4">{title}</h3>
      {fields.map(([k, v]) => (
        <div key={k} className="mb-3">
          <div className="text-[11px] uppercase text-gray-400">{k}</div>
          <div className="text-sm">{v}</div>
        </div>
      ))}
    </div>
  );
}

function JobsSection({ title, description, jobs, role, onJobClick }) {
  return (
    <div className="border border-white/10 bg-white/5 rounded-2xl p-8">
      <h3 className="text-xl font-semibold">{title}</h3>
      <p className="text-gray-400 mb-6">{description}</p>

      {jobs.length === 0 ? (
        <div className="text-gray-500 text-center py-10">No jobs available</div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
          {jobs.map(job => (
            <JobCard
              key={job.id}
              job={job}
              role={role}
              onClick={() => onJobClick(job.id)}
            />
          ))}
        </div>
      )}
    </div>
  );
}

function JobCard({ job, role, onClick }) {
  const disabled = role === "STUDENT" && (job.applied || job.rejected);

  const statusBadge = job.applied
    ? { text: "APPLIED", color: "green" }
    : job.rejected
    ? { text: "REJECTED", color: "red" }
    : job.shortlisted
    ? { text: "SHORTLISTED", color: "yellow" }
    : null;

  return (
    <div
      onClick={disabled ? undefined : onClick}
      className={`border rounded-xl p-5 relative
        ${
          job.applied
            ? "border-green-500/40 bg-green-500/10"
            : job.rejected
            ? "border-red-500/40 bg-red-500/10"
            : job.shortlisted
            ? "border-yellow-500/40 bg-yellow-500/10"
            : "border-white/10 bg-black/40 hover:border-white/30 cursor-pointer"
        }
        ${disabled ? "cursor-not-allowed opacity-90" : ""}
      `}
    >
      {/* STATUS BADGE (RESTORED) */}
      {statusBadge && (
        <span
          className={`absolute top-3 right-3 text-xs px-2 py-0.5 rounded
            ${
              statusBadge.color === "green"
                ? "bg-green-600/20 text-green-400"
                : statusBadge.color === "red"
                ? "bg-red-600/20 text-red-400"
                : "bg-yellow-600/20 text-yellow-400"
            }`}
        >
          {statusBadge.text}
        </span>
      )}

      <h4 className="text-lg mb-1">{job.title}</h4>
      <p className="text-sm text-gray-400">{job.description}</p>

      {/* OPTIONAL helper text */}
      {disabled && role === "STUDENT" && (
        <p className="mt-3 text-xs text-gray-500">
          {job.applied
            ? "You have already applied"
            : "Application rejected"}
        </p>
      )}
    </div>
  );
}


function Stat({ title, value }) {
  return (
    <div className="border border-white/10 bg-white/5 rounded-xl p-6 text-center">
      <div className="text-2xl font-semibold">{value ?? "—"}</div>
      <div className="text-xs text-gray-400 mt-1">{title}</div>
    </div>
  );
}
