// src/services/post.js
import api from "./api";

export const postService = {
  // Create a new post
  async createPost(postData) {
    try {
      const response = await api.post("/api/posts", postData);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.error || "Failed to create post");
    }
  },

  // Get posts by community
  async getPostsByCommunity(
    communityId,
    page = 0,
    size = 20,
    sortBy = "createdAt",
    direction = "desc"
  ) {
    try {
      const response = await api.get(`/api/posts/community/${communityId}`, {
        params: { page, size, sortBy, direction },
      });
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.error || "Failed to fetch posts");
    }
  },

  // Get single post
  async getPostById(id) {
    try {
      const response = await api.get(`/api/posts/${id}`);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.error || "Failed to fetch post");
    }
  },

  // Update post
  async updatePost(id, postData) {
    try {
      const response = await api.put(`/api/posts/${id}`, postData);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.error || "Failed to update post");
    }
  },

  // Delete post
  async deletePost(id) {
    try {
      const response = await api.delete(`/api/posts/${id}`);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.error || "Failed to delete post");
    }
  },

  // Get top posts by community
  async getTopPostsByCommunity(communityId, page = 0, size = 10) {
    try {
      const response = await api.get(
        `/api/posts/community/${communityId}/top`,
        {
          params: { page, size },
        }
      );
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.error || "Failed to fetch top posts"
      );
    }
  },

  // Get hot posts by community
  async getHotPostsByCommunity(communityId, page = 0, size = 10) {
    try {
      const response = await api.get(
        `/api/posts/community/${communityId}/hot`,
        {
          params: { page, size },
        }
      );
      return response.data;
    } catch (error) {
      throw new Error(
        error.response?.data?.error || "Failed to fetch hot posts"
      );
    }
  },
};
