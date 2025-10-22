// src/stores/auth.js - UPDATED
import { defineStore } from "pinia";
import { authService } from "@/services/auth";
import { universityService } from "@/services/university";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: JSON.parse(localStorage.getItem("user")) || null,
    token: localStorage.getItem("token") || null,
    isLoading: false,
    error: null,
    userUniversity: JSON.parse(localStorage.getItem("userUniversity")) || null,
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,
    currentUser: (state) => state.user,
    currentUniversity: (state) => state.userUniversity,
  },

  actions: {
    async login(loginData) {
      this.isLoading = true;
      this.error = null;

      try {
        const response = await authService.login(loginData);

        // DEBUG: Log the actual response structure
        console.log("Login response:", response);

        // Use the correct field name from backend - it's "token" not "jwt"
        const token = response.token;
        if (!token) {
          throw new Error("No token received in response");
        }

        this.token = token;
        this.user = {
          id: response.id,
          username: response.username,
          email: response.email,
        };

        // Store in localStorage
        localStorage.setItem("token", token);
        localStorage.setItem("user", JSON.stringify(this.user));

        // Fetch user's university data after successful login
        await this.fetchUserUniversity();

        console.log(
          "Login successful, token stored:",
          token.substring(0, 20) + "..."
        );
        return response;
      } catch (error) {
        console.error("Login error:", error);
        this.error =
          error.response?.data?.message || error.message || "Login failed";
        throw error;
      } finally {
        this.isLoading = false;
      }
    },

    async register(signupData) {
      this.isLoading = true;
      this.error = null;

      try {
        const response = await authService.register(signupData);
        return response;
      } catch (error) {
        this.error =
          error.response?.data?.message ||
          error.message ||
          "Registration failed";
        throw error;
      } finally {
        this.isLoading = false;
      }
    },

    async fetchUserUniversity() {
      try {
        // Fetch user's university data from the backend - FIXED METHOD NAME
        const universityData = await universityService.getUserUniversities();

        console.log("University API response:", universityData);

        if (universityData && universityData.length > 0) {
          // Assuming the first university is the primary one
          this.userUniversity = universityData[0];
          localStorage.setItem(
            "userUniversity",
            JSON.stringify(this.userUniversity)
          );
          console.log("User university data loaded:", this.userUniversity);
        } else {
          this.userUniversity = null;
          localStorage.removeItem("userUniversity");
          console.log("No university data found for user");
        }
      } catch (error) {
        console.error("Error fetching user university:", error);
        this.userUniversity = null;
        localStorage.removeItem("userUniversity");
      }
    },

    async updateUserUniversity(universityData) {
      try {
        this.userUniversity = universityData;
        if (universityData) {
          localStorage.setItem(
            "userUniversity",
            JSON.stringify(universityData)
          );
        } else {
          localStorage.removeItem("userUniversity");
        }
      } catch (error) {
        console.error("Error updating user university:", error);
        throw error;
      }
    },

    logout() {
      this.user = null;
      this.token = null;
      this.userUniversity = null;
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      localStorage.removeItem("userUniversity");
    },
  },
});
