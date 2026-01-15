import { useState } from "react";
import { useNavigate } from "react-router-dom";
import jobApi from "../api/jobApi";

export default function CreateJob() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    title: "",
    description: "",
    location: "",
    employmentType: "FULL_TIME",
    experienceRequired: "",
    minSalary: "",
    maxSalary: "",
    salaryCurrency: "INR",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      await jobApi.post("/jobs", {
        ...form,
        experienceRequired: Number(form.experienceRequired),
        minSalary: Number(form.minSalary),
        maxSalary: Number(form.maxSalary),
      });

      navigate("/home");
    } catch (err) {
      setError("Failed to create job");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-black text-white px-6 py-10">
      <div className="max-w-3xl mx-auto">
        <h1 className="text-2xl font-semibold mb-8">Create Job Opening</h1>

        {error && <div className="mb-6 text-sm text-red-500">{error}</div>}

        <form
          onSubmit={handleSubmit}
          className="
            space-y-6
            rounded-2xl
            border border-white/10
            bg-white/5
            p-8
          "
        >
          <Input
            label="Job Title"
            name="title"
            value={form.title}
            onChange={handleChange}
          />
          <Textarea
            label="Description"
            name="description"
            value={form.description}
            onChange={handleChange}
          />
          <Input
            label="Location"
            name="location"
            value={form.location}
            onChange={handleChange}
          />

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <Select
              label="Employment Type"
              name="employmentType"
              value={form.employmentType}
              onChange={handleChange}
              options={["FULL_TIME", "PART_TIME", "INTERNSHIP", "CONTRACT"]}
            />

            <Input
              label="Experience Required (Years)"
              name="experienceRequired"
              type="number"
              value={form.experienceRequired}
              onChange={handleChange}
            />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <Input
              label="Min Salary"
              name="minSalary"
              type="number"
              value={form.minSalary}
              onChange={handleChange}
            />
            <Input
              label="Max Salary"
              name="maxSalary"
              type="number"
              value={form.maxSalary}
              onChange={handleChange}
            />
            <Select
              label="Currency"
              name="salaryCurrency"
              value={form.salaryCurrency}
              onChange={handleChange}
              options={["INR", "USD", "EUR"]}
            />
          </div>

          <div className="flex justify-end pt-4">
            <button
              disabled={loading}
              className="
                px-6 py-2 rounded-md
                bg-white text-black
                font-medium
                hover:bg-gray-200
                disabled:opacity-50
              "
            >
              {loading ? "Posting..." : "Post Job"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

/* ================= REUSABLE UI ================= */

function Input({ label, ...props }) {
  return (
    <div>
      <div className="text-xs uppercase tracking-widest text-gray-400 mb-1">
        {label}
      </div>
      <input
        {...props}
        required
        className="
          w-full bg-black/40
          border border-white/10
          rounded-md px-3 py-2
          text-sm text-white
          focus:outline-none focus:border-white/40
        "
      />
    </div>
  );
}

function Textarea({ label, ...props }) {
  return (
    <div>
      <div className="text-xs uppercase tracking-widest text-gray-400 mb-1">
        {label}
      </div>
      <textarea
        {...props}
        required
        rows={4}
        className="
          w-full bg-black/40
          border border-white/10
          rounded-md px-3 py-2
          text-sm text-white
          focus:outline-none focus:border-white/40
        "
      />
    </div>
  );
}

function Select({ label, options, ...props }) {
  return (
    <div>
      <div className="text-xs uppercase tracking-widest text-gray-400 mb-1">
        {label}
      </div>
      <select
        {...props}
        className="
          w-full bg-black/40
          border border-white/10
          rounded-md px-3 py-2
          text-sm text-white
          focus:outline-none focus:border-white/40
        "
      >
        {options.map((o) => (
          <option key={o} value={o}>
            {o}
          </option>
        ))}
      </select>
    </div>
  );
}
