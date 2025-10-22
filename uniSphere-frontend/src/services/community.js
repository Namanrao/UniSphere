// src/services/community.js - CORRECT SERVICE FILE
import api from "./api";

export const communityService = {
  // Get user's communities
  async getUserCommunities() {
    try {
      const response = await api.get("/communities/my-communities");
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message ||
          error.response?.data ||
          "Failed to fetch user communities"
      );
    }
  },

  // Get all communities for a university
  async getUniversityCommunities(universityId) {
    try {
      const response = await api.get(`/communities/university/${universityId}`);
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message ||
          error.response?.data ||
          "Failed to fetch university communities"
      );
    }
  },

  // Get community by ID
  async getCommunityById(id) {
    try {
      const response = await api.get(`/communities/${id}`);
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message ||
          error.response?.data ||
          "Failed to fetch community"
      );
    }
  },

  // Create a new community
  async createCommunity(communityData) {
    try {
      const response = await api.post("/communities", communityData);
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message ||
          error.response?.data ||
          "Failed to create community"
      );
    }
  },

  // Update community
  async updateCommunity(communityId, communityData) {
    try {
      const response = await api.put(
        `/communities/${communityId}`,
        communityData
      );
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message ||
          error.response?.data ||
          "Failed to update community"
      );
    }
  },

  // Delete community
  async deleteCommunity(communityId) {
    try {
      const response = await api.delete(`/communities/${communityId}`);
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message ||
          error.response?.data ||
          "Failed to delete community"
      );
    }
  },

  // Join a community
  async joinCommunity(communityId) {
    try {
      const response = await api.post(`/communities/${communityId}/join`);
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message ||
          error.response?.data ||
          "Failed to join community"
      );
    }
  },

  // Leave a community
  async leaveCommunity(communityId) {
    try {
      const response = await api.post(`/communities/${communityId}/leave`);
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message ||
          error.response?.data ||
          "Failed to leave community"
      );
    }
  },

  // Get community members
  async getCommunityMembers(communityId) {
    try {
      const response = await api.get(`/communities/${communityId}/members`);
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message ||
          error.response?.data ||
          "Failed to fetch community members"
      );
    }
  },

  // Search communities
  async searchCommunities(query) {
    try {
      const response = await api.get("/communities/search", {
        params: { q: query },
      });
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message ||
          error.response?.data ||
          "Failed to search communities"
      );
    }
  },

  // Get trending communities
  async getTrendingCommunities(limit = 10) {
    try {
      const response = await api.get("/communities/trending", {
        params: { limit },
      });
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.message ||
          error.response?.data ||
          "Failed to fetch trending communities"
      );
    }
  },
};
