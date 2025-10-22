// src/stores/communities.js
import { defineStore } from "pinia";
import { ref, computed } from "vue";
import { communityService } from "@/services/community";

export const useCommunityStore = defineStore("communities", () => {
  // State
  const communities = ref([]);
  const userCommunities = ref([]);
  const currentCommunity = ref(null);
  const loading = ref(false);
  const error = ref(null);

  // Getters
  const joinedCommunityIds = computed(() =>
    userCommunities.value.map((community) => community.id)
  );

  const isUserMember = computed(
    () => (communityId) => joinedCommunityIds.value.includes(communityId)
  );

  const communityMap = computed(() =>
    communities.value.reduce((map, community) => {
      map[community.id] = community;
      return map;
    }, {})
  );

  // Actions
  const fetchCommunities = async () => {
    loading.value = true;
    error.value = null;

    try {
      communities.value = await communityService.getCommunities();
      return communities.value;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const fetchCommunityById = async (id) => {
    loading.value = true;
    error.value = null;

    try {
      currentCommunity.value = await communityService.getCommunityById(id);
      return currentCommunity.value;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const fetchUserCommunities = async () => {
    loading.value = true;
    error.value = null;

    try {
      userCommunities.value = await communityService.getUserCommunities();
      return userCommunities.value;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const createCommunity = async (communityData) => {
    loading.value = true;
    error.value = null;

    try {
      const newCommunity = await communityService.createCommunity(
        communityData
      );
      communities.value.push(newCommunity);
      userCommunities.value.push(newCommunity);
      return newCommunity;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const joinCommunity = async (communityId) => {
    loading.value = true;
    error.value = null;

    try {
      const community = await communityService.joinCommunity(communityId);

      // Add to user communities if not already there
      if (!userCommunities.value.find((c) => c.id === communityId)) {
        const communityDetails =
          communities.value.find((c) => c.id === communityId) || community;
        userCommunities.value.push(communityDetails);
      }

      return community;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const leaveCommunity = async (communityId) => {
    loading.value = true;
    error.value = null;

    try {
      await communityService.leaveCommunity(communityId);

      // Remove from user communities
      userCommunities.value = userCommunities.value.filter(
        (c) => c.id !== communityId
      );
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const searchCommunities = async (query, page = 0, size = 20) => {
    loading.value = true;
    error.value = null;

    try {
      const results = await communityService.searchCommunities(
        query,
        page,
        size
      );
      return results;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const getTrendingCommunities = async (page = 0, size = 10) => {
    loading.value = true;
    error.value = null;

    try {
      const trending = await communityService.getTrendingCommunities(
        page,
        size
      );
      return trending;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const clearError = () => {
    error.value = null;
  };

  const clearCurrentCommunity = () => {
    currentCommunity.value = null;
  };

  return {
    // State
    communities,
    userCommunities,
    currentCommunity,
    loading,
    error,

    // Getters
    joinedCommunityIds,
    isUserMember,
    communityMap,

    // Actions
    fetchCommunities,
    fetchCommunityById,
    fetchUserCommunities,
    createCommunity,
    joinCommunity,
    leaveCommunity,
    searchCommunities,
    getTrendingCommunities,
    clearError,
    clearCurrentCommunity,
  };
});
