import axios from "axios";

const notificationApi = axios.create({
  baseURL: "http://localhost:8084",
  withCredentials: true,
});

export const fetchMyNotifications = async () => {
  const res = await notificationApi.get("/notifications/me");
  return res.data;
};

export const markNotificationRead = async (id) => {
  await notificationApi.post(`/notifications/${id}/read`);
};
