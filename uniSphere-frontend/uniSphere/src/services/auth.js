// src/services/auth.js - UPDATED
import api from "./api";

export const authService = {
  async login(loginData) {
    try {
      console.log("Sending login request:", loginData);
      const response = await api.post("/auth/signin", loginData);
      console.log("Login response received:", response.data);
      return response.data;
    } catch (error) {
      console.error("Login API error:", error);
      const errorMessage =
        error.response?.data?.message ||
        error.response?.data ||
        error.message ||
        "Login failed";
      throw new Error(errorMessage);
    }
  },

  async register(signupData) {
    try {
      const response = await api.post("/auth/signup", signupData);
      return response.data;
    } catch (error) {
      const errorMessage =
        error.response?.data?.message ||
        error.response?.data ||
        error.message ||
        "Registration failed";
      throw new Error(errorMessage);
    }
  },

  logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
  },

  getCurrentUser() {
    return JSON.parse(localStorage.getItem("user"));
  },

  isAuthenticated() {
    return !!localStorage.getItem("token");
  },
};
