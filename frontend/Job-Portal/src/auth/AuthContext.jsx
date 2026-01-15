import { createContext, useContext, useEffect, useState } from "react";
import userApi from "../api/userApi";

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [checkingAuth, setCheckingAuth] = useState(true);

  useEffect(() => {
    const restoreAuth = async () => {
      try {
        const res = await userApi.get("/profile/status");
        setUser(res.data);
      } catch {
        setUser(null);
      } finally {
        setCheckingAuth(false);
      }
    };

    restoreAuth();
  }, []);

  return (
    <AuthContext.Provider value={{ user, setUser, checkingAuth }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);
