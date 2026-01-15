import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "./AuthContext";

export default function PrivateRoute({ children }) {
  const { user, checkingAuth } = useAuth();
  const location = useLocation();

  if (checkingAuth) return null;

  // Not logged in
  if (!user) {
    return <Navigate to="/login" replace />;
  }

  // Profile NOT completed → force complete-profile
  if (
    location.pathname === "/home" &&
    !user.profileCompleted
  ) {
    return <Navigate to="/complete-profile" replace />;
  }

  // Profile already completed → block complete-profile
  if (
    location.pathname === "/complete-profile" &&
    user.profileCompleted
  ) {
    return <Navigate to="/home" replace />;
  }

  return children;
}
