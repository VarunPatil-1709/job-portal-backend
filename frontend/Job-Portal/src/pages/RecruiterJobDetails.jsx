import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import jobApi from "../api/jobApi";

export default function RecruiterJobDetails() {
  const { jobId } = useParams();

  const [job, setJob] = useState(null);
  const [applicants, setApplicants] = useState([]);
  const [loading, setLoading] = useState(true);

  // 🔥 Chat session state
  const [chatUserId, setChatUserId] = useState("");
  const [chatStartTime, setChatStartTime] = useState("");
  const [creatingChat, setCreatingChat] = useState(false);

  /* ================= LOAD DATA ================= */

  useEffect(() => {
    const loadData = async () => {
      try {
        const [jobRes, applicantsRes] = await Promise.all([
          jobApi.get(`/jobs/${jobId}`),
          jobApi.get(`/jobs/${jobId}/applications`),
        ]);

        setJob(jobRes.data);
        setApplicants(applicantsRes.data);
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, [jobId]);

  /* ================= STATUS UPDATE ================= */

  const updateStatus = async (applicationId, status) => {
    try {
      const applicant = applicants.find((a) => a.id === applicationId);

      if (!applicant) {
        alert("Applicant not found");
        return;
      }

      await jobApi.patch(`/application/status`, null, {
        params: {
          jobId: job.id,
          userAuthId: applicant.userAuthId,
          status,
        },
      });

      setApplicants((prev) =>
        prev.map((a) => (a.id === applicationId ? { ...a, status } : a))
      );
    } catch (err) {
      console.error(err);
      alert(err.response?.data || "Failed to update status");
    }
  };

  /* ================= RESUME DOWNLOAD ================= */

  const downloadResume = async (applicationId) => {
    try {
      const res = await jobApi.get(`/applications/${applicationId}/resume`);
      window.open(res.data.downloadUrl, "_blank");
    } catch {
      alert("Resume not available");
    }
  };

  /* ================= CREATE CHAT SESSION ================= */

  const createChatSession = async () => {
    if (!chatUserId) {
      alert("Select a valid shortlisted applicant");
      return;
    }

    if (!chatStartTime) {
      alert("Select chat start time");
      return;
    }

    try {
      setCreatingChat(true);

      await jobApi.post(`/jobs/${jobId}/chat-session`, {
        candidateAuthId: Number(chatUserId),
        startAt: `${chatStartTime}:00`, // ✅ add seconds
      });

      alert("✅ Chat session created successfully");
      setChatUserId("");
      setChatStartTime("");
    } catch (err) {
      console.error(err);
      alert(err.response?.data?.message || "❌ Failed to create chat session");
    } finally {
      setCreatingChat(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-black text-gray-400 flex items-center justify-center">
        Loading…
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-black text-white px-6 py-10">
      <div className="max-w-5xl mx-auto space-y-10">
        {/* ================= JOB INFO ================= */}
        <div className="rounded-2xl border border-white/10 bg-white/5 p-8">
          <h1 className="text-3xl font-semibold">{job.title}</h1>
          <p className="mt-2 text-gray-400">
            {job.location} · {job.employmentType}
          </p>
          <p className="mt-6 text-gray-300 leading-relaxed">
            {job.description}
          </p>
        </div>

        {/* ================= APPLICANTS ================= */}
        <div className="rounded-2xl border border-white/10 bg-white/5 p-8">
          <h2 className="text-xl font-semibold mb-6">Applicants</h2>

          {applicants.length === 0 ? (
            <div className="text-gray-500 text-center py-10">
              No applicants yet
            </div>
          ) : (
            <div className="space-y-4">
              {applicants.map((a) => (
                <ApplicantCard
                  key={a.id}
                  applicant={a}
                  onShortlist={() => updateStatus(a.id, "SHORTLISTED")}
                  onReject={() => updateStatus(a.id, "REJECTED")}
                  onDownload={() => downloadResume(a.id)}
                />
              ))}
            </div>
          )}
        </div>

        {/* ================= CREATE CHAT SESSION ================= */}
        <div className="rounded-2xl border border-white/10 bg-white/5 p-8">
          <h2 className="text-xl font-semibold mb-2">Create Chat Session</h2>

          <p className="text-gray-400 text-sm mb-6">
            Schedule a chat with a shortlisted applicant
          </p>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 items-end">
            {/* Applicant */}
            <div>
              <label className="text-xs text-gray-400">
                Shortlisted Applicant
              </label>
              <select
                value={chatUserId}
                onChange={(e) => setChatUserId(e.target.value)}
                className="w-full mt-1 px-3 py-2
                           bg-black border border-white/20
                           rounded-md text-sm"
              >
                <option value="">Select applicant</option>

                {applicants
                  .filter(
                    (a) =>
                      a.status === "SHORTLISTED" &&
                      a.userAuthId !== null &&
                      a.userAuthId !== undefined
                  )
                  .map((a) => (
                    <option key={a.id} value={a.userAuthId}>
                      User ID: {a.userAuthId}
                    </option>
                  ))}
              </select>
            </div>

            {/* Start time */}
            <div>
              <label className="text-xs text-gray-400">Chat Start Time</label>
              <input
                type="datetime-local"
                value={chatStartTime}
                onChange={(e) => setChatStartTime(e.target.value)}
                className="w-full mt-1 px-3 py-2
                           bg-black border border-white/20
                           rounded-md text-sm"
              />
            </div>

            {/* Button */}
            <button
              onClick={createChatSession}
              disabled={creatingChat}
              className="px-6 py-2 bg-white text-black
                         rounded-md font-medium
                         disabled:opacity-50"
            >
              {creatingChat ? "Creating…" : "Create Chat"}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

/* ================= APPLICANT CARD ================= */

function ApplicantCard({ applicant, onShortlist, onReject, onDownload }) {
  return (
    <div
      className="flex items-center justify-between
                    rounded-xl border border-white/10
                    bg-black/40 p-5"
    >
      <div>
        <div className="font-medium">
          User ID: {applicant.userAuthId ?? "—"}
        </div>
        <div className="text-sm text-gray-400">
          Applied at: {new Date(applicant.appliedAt).toLocaleString()}
        </div>
      </div>

      <div className="flex items-center gap-3">
        <StatusBadge status={applicant.status} />

        <button
          onClick={onDownload}
          className="px-4 py-1.5 rounded-md
                     bg-blue-600/20 text-blue-400
                     hover:bg-blue-600/30"
        >
          Download Resume
        </button>

        {applicant.status === "APPLIED" && (
          <>
            <button
              onClick={onShortlist}
              className="px-4 py-1.5 rounded-md
                         bg-green-600/20 text-green-400
                         hover:bg-green-600/30"
            >
              Shortlist
            </button>

            <button
              onClick={onReject}
              className="px-4 py-1.5 rounded-md
                         bg-red-600/20 text-red-400
                         hover:bg-red-600/30"
            >
              Reject
            </button>
          </>
        )}
      </div>
    </div>
  );
}

/* ================= STATUS BADGE ================= */

function StatusBadge({ status }) {
  const styles = {
    APPLIED: "bg-blue-600/20 text-blue-400",
    SHORTLISTED: "bg-yellow-600/20 text-yellow-400",
    REJECTED: "bg-red-600/20 text-red-400",
    SELECTED: "bg-green-600/20 text-green-400",
  };

  return (
    <span
      className={`px-3 py-1 rounded-full text-xs font-medium ${styles[status]}`}
    >
      {status}
    </span>
  );
}
