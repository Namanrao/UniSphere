// src/stores/universities.js
import { defineStore } from "pinia";
import { ref, computed } from "vue";
import { universityService } from "@/services/university";

export const useUniversityStore = defineStore("universities", () => {
  // State
  const universities = ref([]);
  const userUniversities = ref([]);
  const loading = ref(false);
  const error = ref(null);

  // Getters
  const verifiedUniversities = computed(() =>
    userUniversities.value.filter(
      (uni) => uni.verificationStatus === "VERIFIED"
    )
  );

  const hasVerifiedUniversity = computed(
    () => verifiedUniversities.value.length > 0
  );

  const isUserVerifiedForUniversity = computed(
    () => (universityId) =>
      verifiedUniversities.value.some(
        (uni) => uni.university.id === universityId
      )
  );

  // Actions
  const fetchAllUniversities = async () => {
    loading.value = true;
    error.value = null;

    try {
      universities.value = await universityService.getAllUniversities();
      return universities.value;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const fetchUserUniversities = async () => {
    loading.value = true;
    error.value = null;

    try {
      userUniversities.value = await universityService.getUserUniversities();
      return userUniversities.value;
    } catch (err) {
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const joinUniversity = async (universityId) => {
    loading.value = true;
    error.value = null;

    try {
      const userUniversity = await universityService.joinUniversity(
        universityId
      );
      userUniversities.value.push(userUniversity);
      return userUniversity;
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

  return {
    // State
    universities,
    userUniversities,
    loading,
    error,

    // Getters
    verifiedUniversities,
    hasVerifiedUniversity,
    isUserVerifiedForUniversity,

    // Actions
    fetchAllUniversities,
    fetchUserUniversities,
    joinUniversity,
    clearError,
  };
});
