// src/services/api.js - ENHANCED DEBUGGING
import axios from "axios";

const API_BASE_URL = "http://localhost:8080";

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Add token to requests
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    console.log(
      "API Request Interceptor - Token:",
      token ? token.substring(0, 20) + "..." : "No token"
    );

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
      console.log(
        "Authorization header set:",
        config.headers.Authorization.substring(0, 30) + "..."
      );
    }
    return config;
  },
  (error) => {
    console.error("Request interceptor error:", error);
    return Promise.reject(error);
  }
);

// Handle token expiration
api.interceptors.response.use(
  (response) => {
    console.log("API Response success:", response.config.url, response.status);
    return response;
  },
  (error) => {
    console.error(
      "API Response error:",
      error.config?.url,
      error.response?.status,
      error.response?.data
    );

    if (error.response?.status === 401) {
      console.log("Unauthorized - clearing token and redirecting to login");
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export default api;
