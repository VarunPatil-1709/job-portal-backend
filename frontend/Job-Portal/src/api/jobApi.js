import axios from "axios";

const jobApi = axios.create({
  baseURL: "http://localhost:8083",
  withCredentials: true, // cookie-based auth
});

export default jobApi;
