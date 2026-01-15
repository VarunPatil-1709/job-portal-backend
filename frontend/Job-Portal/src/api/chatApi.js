import axios from "axios";

/**
 * Chat API
 * Uses same JWT interceptor setup as other APIs
 */
const chatApi = axios.create({
  baseURL: "http://localhost:8085", // chat service base URL
  withCredentials: true,
});

// If you already have a global interceptor file,
// you can REMOVE this and rely on that.
chatApi.interceptors.request.use((config) => {
  const token = localStorage.getItem("accessToken");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default chatApi;
