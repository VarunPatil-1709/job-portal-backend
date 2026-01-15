import { useState } from "react";
import { useNavigate } from "react-router-dom";
import userApi from "../api/userApi";
import authApi from "../api/authApi";
import { useAuth } from "../auth/AuthContext";

export default function CompleteProfile() {
  const navigate = useNavigate();
  const { user, setUser } = useAuth();

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  // STUDENT
  const [student, setStudent] = useState({
    firstName: "",
    lastName: "",
    dob: "",
    gender: "",
    city: "",
    experience: "",
    university: "",
    education: "",
    currentlyWorking: false,
  });

  // RECRUITER
  const [recruiter, setRecruiter] = useState({
    companyName: "",
    companyWebsite: "",
    companySize: "",
    companyIndustry: "",
  });

  const logout = async () => {
    await authApi.post("/auth/logout");
    setUser(null);
    navigate("/login");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      if (user.role === "STUDENT") {
        await userApi.post("/profile/user", {
          ...student,
          experience: student.experience ? Number(student.experience) : null,
        });
      } else {
        await userApi.post("/profile/recruiter", recruiter);
      }

      setUser({ ...user, profileCompleted: true });
      navigate("/home");
    } catch {
      setError("Failed to complete profile. Please verify all fields.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 px-6 py-6">
      {/* HEADER */}
      <div className="max-w-5xl mx-auto flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-semibold text-gray-900">
            Complete Profile
          </h1>
          <p className="text-sm text-gray-500">
            {user.role === "STUDENT"
              ? "Tell us about yourself"
              : "Tell us about your company"}
          </p>
        </div>

        <button
          onClick={logout}
          className="
            rounded-md border border-gray-300
            px-4 py-1.5 text-sm font-medium
            text-gray-700 hover:bg-gray-100
          "
        >
          Logout
        </button>
      </div>

      {/* CARD */}
      <div className="max-w-5xl mx-auto bg-white border border-gray-200 rounded-xl shadow-sm p-8">
        {error && (
          <div className="mb-6 rounded-md border border-red-200 bg-red-50 px-4 py-2 text-sm text-red-600">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-8">
          {/* STUDENT */}
          {user.role === "STUDENT" && (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <Input
                label="First name"
                value={student.firstName}
                onChange={(v) => setStudent({ ...student, firstName: v })}
              />

              <Input
                label="Last name"
                value={student.lastName}
                onChange={(v) => setStudent({ ...student, lastName: v })}
              />

              <Input
                type="date"
                label="Date of birth"
                value={student.dob}
                onChange={(v) => setStudent({ ...student, dob: v })}
              />

              <Input
                label="Gender"
                value={student.gender}
                onChange={(v) => setStudent({ ...student, gender: v })}
              />

              <Input
                label="City"
                value={student.city}
                onChange={(v) => setStudent({ ...student, city: v })}
              />

              <Input
                type="number"
                label="Experience (years)"
                value={student.experience}
                onChange={(v) => setStudent({ ...student, experience: v })}
              />

              <Input
                label="University"
                value={student.university}
                onChange={(v) => setStudent({ ...student, university: v })}
              />

              <Input
                label="Education"
                value={student.education}
                onChange={(v) => setStudent({ ...student, education: v })}
              />

              <div className="flex items-center gap-2 mt-2">
                <input
                  type="checkbox"
                  checked={student.currentlyWorking}
                  onChange={(e) =>
                    setStudent({
                      ...student,
                      currentlyWorking: e.target.checked,
                    })
                  }
                />
                <span className="text-sm text-gray-700">Currently working</span>
              </div>
            </div>
          )}

          {/* RECRUITER */}
          {user.role === "RECRUITER" && (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <Input
                label="Company name"
                value={recruiter.companyName}
                onChange={(v) => setRecruiter({ ...recruiter, companyName: v })}
              />

              <Input
                label="Company website"
                value={recruiter.companyWebsite}
                onChange={(v) =>
                  setRecruiter({ ...recruiter, companyWebsite: v })
                }
              />

              <Input
                label="Company size"
                value={recruiter.companySize}
                onChange={(v) => setRecruiter({ ...recruiter, companySize: v })}
              />

              <Input
                label="Industry"
                value={recruiter.companyIndustry}
                onChange={(v) =>
                  setRecruiter({ ...recruiter, companyIndustry: v })
                }
              />
            </div>
          )}

          {/* SUBMIT */}
          <div className="pt-4">
            <button
              disabled={loading}
              className="
                w-full md:w-auto
                rounded-md bg-gray-900 px-8 py-2.5
                text-sm font-medium text-white
                hover:bg-gray-800
                disabled:opacity-50
              "
            >
              {loading ? "Saving..." : "Complete profile"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

/* ===== INPUT ===== */

function Input({ label, value, onChange, type = "text" }) {
  return (
    <div>
      <label className="block text-sm font-medium text-gray-700 mb-1">
        {label}
      </label>
      <input
        type={type}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        required
        className="
          w-full rounded-md border border-gray-300
          px-3 py-2 text-sm
          focus:outline-none focus:ring-2
          focus:ring-gray-900 focus:border-gray-900
        "
      />
    </div>
  );
}
