// src/stores/posts.js
import { defineStore } from "pinia";
import { ref } from "vue";
import { postService } from "@/services/post";

export const usePostStore = defineStore("posts", () => {
  const posts = ref([]);
  const currentPost = ref(null);
  const loading = ref(false);
  const error = ref(null);

  // Create a new post
  const createPost = async (postData) => {
    loading.value = true;
    error.value = null;

    try {
      const newPost = await postService.createPost(postData);
      posts.value.unshift(newPost); // Add to beginning of list
      return newPost;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // Fetch posts by community
  const fetchPostsByCommunity = async (communityId, page = 0, size = 20) => {
    loading.value = true;
    error.value = null;

    try {
      const communityPosts = await postService.getPostsByCommunity(
        communityId,
        page,
        size
      );
      if (page === 0) {
        posts.value = communityPosts; // Replace for first page
      } else {
        posts.value.push(...communityPosts); // Append for subsequent pages
      }
      return communityPosts;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // Fetch single post
  const fetchPostById = async (id) => {
    loading.value = true;
    error.value = null;

    try {
      const post = await postService.getPostById(id);
      currentPost.value = post;
      return post;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // Update post
  const updatePost = async (id, postData) => {
    loading.value = true;
    error.value = null;

    try {
      const updatedPost = await postService.updatePost(id, postData);

      // Update in posts list if exists
      const index = posts.value.findIndex((post) => post.id === id);
      if (index !== -1) {
        posts.value[index] = updatedPost;
      }

      // Update current post if it's the one being edited
      if (currentPost.value && currentPost.value.id === id) {
        currentPost.value = updatedPost;
      }

      return updatedPost;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // Delete post
  const deletePost = async (id) => {
    loading.value = true;
    error.value = null;

    try {
      await postService.deletePost(id);

      // Remove from posts list
      posts.value = posts.value.filter((post) => post.id !== id);

      // Clear current post if it's the one being deleted
      if (currentPost.value && currentPost.value.id === id) {
        currentPost.value = null;
      }
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // Clear error
  const clearError = () => {
    error.value = null;
  };

  // Clear posts
  const clearPosts = () => {
    posts.value = [];
  };

  return {
    posts,
    currentPost,
    loading,
    error,
    createPost,
    fetchPostsByCommunity,
    fetchPostById,
    updatePost,
    deletePost,
    clearError,
    clearPosts,
  };
});
