import axios from "axios";

const API_URL = process.env.REACT_APP_API_URL || "http://localhost:8080/api";

const api = axios.create({
  baseURL: API_URL
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      const hadToken = localStorage.getItem("token");
      localStorage.removeItem("token");
      localStorage.removeItem("username");
      localStorage.removeItem("role");
      if (hadToken && !window.location.pathname.includes("/login")) {
        window.location.href = "/login";
      }
    }
    return Promise.reject(error);
  }
);

export const registerUser = (payload) => api.post("/auth/register", payload);
export const loginUser = (payload) => api.post("/auth/login", payload);
export const getDashboardSummary = () => api.get("/admin/summary");
export const getLoginLogs = () => api.get("/admin/logs");
export const getBlockedUsers = () => api.get("/admin/blocked-users");
export const unblockUser = (username) => api.put(`/admin/unblock/${username}`);

export default api;
