// src/services/university.js
import api from "./api";

export const universityService = {
  // Get all universities
  async getAllUniversities() {
    try {
      const response = await api.get("/universities");
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message ||
          error.response?.data ||
          "Failed to fetch universities"
      );
    }
  },

  // Get user's universities
  async getUserUniversities() {
    try {
      const response = await api.get("/universities/my-universities");
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message ||
          error.response?.data ||
          "Failed to fetch user universities"
      );
    }
  },

  // Join a university
  async joinUniversity(universityId) {
    try {
      const response = await api.post(`/universities/${universityId}/join`);
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message ||
          error.response?.data ||
          "Failed to join university"
      );
    }
  },

  // Verify university association
  async verifyUniversity(verificationToken) {
    try {
      const response = await api.post(
        `/universities/verify/${verificationToken}`
      );
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message ||
          error.response?.data ||
          "Failed to verify university"
      );
    }
  },
};
