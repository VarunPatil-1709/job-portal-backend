import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Home from "./pages/Home";
import CompleteProfile from "./pages/CompleteProfile";
import PrivateRoute from "./auth/PrivateRoute";
import { AuthProvider } from "./auth/AuthContext";
import Landing from "./pages/Landing";
import CreateJob from "./pages/CreateJob";
import JobDetails from "./pages/JobDetails";
import RecruiterJobDetails from "./pages/RecruiterJobDetails";
import ChatPage from "./pages/ChatPage";

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          {/* PUBLIC ROUTES */}
          <Route path="/" element={<Landing />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />

          {/* PROTECTED ROUTES */}
          <Route
            path="/home"
            element={
              <PrivateRoute>
                <Home />
              </PrivateRoute>
            }
          />

          <Route
            path="/complete-profile"
            element={
              <PrivateRoute>
                <CompleteProfile />
              </PrivateRoute>
            }
          />

          <Route
            path="/recruiter/jobs/create"
            element={
              <PrivateRoute>
                <CreateJob />
              </PrivateRoute>
            }
          />

          <Route
            path="/jobs/:jobId"
            element={
              <PrivateRoute>
                <JobDetails />
              </PrivateRoute>
            }
          />

          <Route
            path="/recruiter/jobs/:jobId"
            element={
              <PrivateRoute>
                <RecruiterJobDetails />
              </PrivateRoute>
            }
          />

          {/* ✅ CHAT ROUTES (CORRECT) */}

          {/* Entry page → ask Chat ID */}
          <Route
            path="/chat"
            element={
              <PrivateRoute>
                <ChatPage />
              </PrivateRoute>
            }
          />

          {/* Actual chat session */}
          <Route
            path="/chat/:chatId"
            element={
              <PrivateRoute>
                <ChatPage />
              </PrivateRoute>
            }
          />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}
