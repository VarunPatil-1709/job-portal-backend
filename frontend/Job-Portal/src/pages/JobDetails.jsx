import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import jobApi from "../api/jobApi";

export default function JobDetails() {
  const { jobId } = useParams();

  const [job, setJob] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [file, setFile] = useState(null);
  const [applying, setApplying] = useState(false);

  /* ===============================
     FETCH JOB DETAILS
  =============================== */
  useEffect(() => {
    jobApi
      .get(`/jobs/${jobId}`)
      .then((res) => setJob(res.data))
      .catch(() => setError("Job not found"))
      .finally(() => setLoading(false));
  }, [jobId]);

  /* ===============================
     STEP 1: GET S3 UPLOAD URL
  =============================== */
  const getUploadUrl = async (fileName) => {
    const res = await jobApi.post(
      `/resumes/upload-url?fileName=${encodeURIComponent(fileName)}`
    );
    return res.data;
  };

  /* ===============================
     STEP 2: UPLOAD FILE TO S3
  =============================== */
  const uploadToS3 = async (uploadUrl, file) => {
    await fetch(uploadUrl, {
      method: "PUT",
      headers: { "Content-Type": "application/pdf" },
      body: file,
    });
  };

  /* ===============================
     STEP 3: APPLY JOB
  =============================== */
  const handleApply = async () => {
    if (!file) return;

    setApplying(true);

    try {
      const { uploadUrl, resumeKey } = await getUploadUrl(file.name);
      await uploadToS3(uploadUrl, file);

      await jobApi.post(`/jobs/${jobId}/apply`, {
        resumeRef: resumeKey,
      });

      alert("Applied successfully");
      setFile(null);
    } catch (err) {
      console.error(err);
      alert("Already applied or something went wrong");
    } finally {
      setApplying(false);
    }
  };

  /* ===============================
     UI STATES
  =============================== */
  if (loading)
    return <div className="text-white p-10">Loading…</div>;

  if (error)
    return <div className="text-red-500 p-10">{error}</div>;

  return (
    <div className="min-h-screen bg-black text-white px-6 py-10">
      <div className="max-w-4xl mx-auto space-y-8">

        {/* JOB DETAILS CARD */}
        <div className="border border-white/10 bg-white/5 rounded-2xl p-8">
          <h1 className="text-3xl font-semibold">{job.title}</h1>
          <p className="mt-2 text-gray-400">{job.location}</p>

          <div className="mt-6 space-y-4 text-gray-300">
            <p>{job.description}</p>

            <div className="grid grid-cols-2 gap-4 text-sm">
              <div>Experience: {job.experienceRequired} yrs</div>
              <div>Type: {job.employmentType}</div>
              <div>
                Salary: {job.minSalary} – {job.maxSalary} {job.salaryCurrency}
              </div>
            </div>
          </div>
        </div>

        {/* RESUME UPLOAD */}
        <div className="space-y-2">
          <label className="block text-sm text-gray-300">
            Upload Resume (PDF only)
          </label>

          <div className="flex items-center gap-4">
            <label
              htmlFor="resumeUpload"
              className="
                px-4 py-2 rounded-md
                border border-white/20
                cursor-pointer
                text-gray-300
                hover:text-white
                hover:border-white/40
              "
            >
              Choose File
            </label>

            <input
              id="resumeUpload"
              type="file"
              accept="application/pdf"
              className="hidden"
              onChange={(e) => {
                const selectedFile = e.target.files[0];
                if (!selectedFile) return;

                if (selectedFile.type !== "application/pdf") {
                  alert("Only PDF files are allowed");
                  return;
                }

                setFile(selectedFile);
              }}
            />

            {file && (
              <span className="text-sm text-green-400">
                {file.name}
              </span>
            )}
          </div>
        </div>

        {/* APPLY BUTTON */}
        <div className="flex gap-4">
          <button
            onClick={handleApply}
            disabled={applying || !file}
            className="
              px-6 py-2 rounded-md
              bg-white text-black font-medium
              hover:bg-gray-200
              disabled:opacity-50
              disabled:cursor-not-allowed
            "
          >
            {applying ? "Applying…" : "Apply Now"}
          </button>
        </div>
      </div>
    </div>
  );
}
