<template>
  <div class="communities-page">
    <!-- Header Section -->
    <div class="page-header glass-morphism">
      <div class="header-content">
        <h1>Discover Communities</h1>
        <p class="subtitle">
          Join course-specific communities and connect with peers from your
          university
        </p>

        <!-- Search and Create -->
        <div class="header-actions">
          <div class="search-container">
            <input
              v-model="searchQuery"
              type="text"
              placeholder="Search communities..."
              class="search-input"
              @input="handleSearch"
            />
            <span class="search-icon">🔍</span>
          </div>
          <button
            @click="showCreateModal = true"
            class="create-community-btn orange-gradient"
          >
            <span class="btn-icon">➕</span>
            Create Community
          </button>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="communities-content">
      <!-- Loading State -->
      <div
        v-if="communityStore.loading && !communityStore.communities.length"
        class="loading-section"
      >
        <div class="loading-spinner-large"></div>
        <p>Loading communities...</p>
      </div>

      <!-- Error State -->
      <div v-else-if="communityStore.error" class="error-section">
        <div class="error-icon">⚠️</div>
        <h3>Failed to load communities</h3>
        <p>{{ communityStore.error }}</p>
        <button @click="loadCommunities" class="retry-btn orange-gradient">
          Try Again
        </button>
      </div>

      <!-- Communities Grid -->
      <div v-else class="communities-grid">
        <!-- Your Communities Section -->
        <section v-if="userCommunities.length > 0" class="communities-section">
          <h2 class="section-title">
            <span class="section-icon">⭐</span>
            Your Communities
          </h2>
          <div class="grid">
            <CommunityCard
              v-for="community in userCommunities"
              :key="community.id"
              :community="community"
              :is-member="true"
              @join="handleJoinCommunity"
              @leave="handleLeaveCommunity"
            />
          </div>
        </section>

        <!-- All Communities Section -->
        <section class="communities-section">
          <h2 class="section-title">
            <span class="section-icon">🏫</span>
            All University Communities
            <span class="count-badge">{{ filteredCommunities.length }}</span>
          </h2>

          <div v-if="filteredCommunities.length === 0" class="no-results">
            <div class="no-results-icon">🔍</div>
            <h3>No communities found</h3>
            <p>Try adjusting your search or create a new community</p>
          </div>

          <div v-else class="grid">
            <CommunityCard
              v-for="community in filteredCommunities"
              :key="community.id"
              :community="community"
              :is-member="communityStore.isUserMember(community.id)"
              @join="handleJoinCommunity"
              @leave="handleLeaveCommunity"
            />
          </div>
        </section>
      </div>
    </div>

    <!-- Create Community Modal -->
    <CreateCommunityModal
      v-if="showCreateModal"
      @close="showCreateModal = false"
      @created="handleCommunityCreated"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useCommunityStore } from "@/stores/communities";
import { useAuthStore } from "@/stores/auth";
import CommunityCard from "@/components/community/CommunityCard.vue";
import CreateCommunityModal from "@/components/community/CreateCommunityModal.vue";

const communityStore = useCommunityStore();
const authStore = useAuthStore();

const searchQuery = ref("");
const showCreateModal = ref(false);

// Computed properties
const userCommunities = computed(() => communityStore.userCommunities);
const allCommunities = computed(() => communityStore.communities);

const filteredCommunities = computed(() => {
  if (!searchQuery.value.trim()) {
    return allCommunities.value.filter(
      (community) =>
        !userCommunities.value.some(
          (userCommunity) => userCommunity.id === community.id
        )
    );
  }

  const query = searchQuery.value.toLowerCase();
  return allCommunities.value.filter(
    (community) =>
      (community.name?.toLowerCase().includes(query) ||
        community.displayName?.toLowerCase().includes(query) ||
        community.description?.toLowerCase().includes(query)) &&
      !userCommunities.value.some(
        (userCommunity) => userCommunity.id === community.id
      )
  );
});

// Methods
const loadCommunities = async () => {
  try {
    // Load user's communities first
    await communityStore.fetchUserCommunities();

    // Then load all university communities
    // Note: You'll need to get the university ID from the user's profile
    // For now, we'll fetch all communities
    await communityStore.fetchUniversityCommunities(1); // Replace with actual university ID
  } catch (error) {
    console.error("Failed to load communities:", error);
  }
};

const handleSearch = () => {
  // Debounced search could be implemented here
};

const handleJoinCommunity = async (communityId) => {
  try {
    await communityStore.joinCommunity(communityId);
  } catch (error) {
    console.error("Failed to join community:", error);
  }
};

const handleLeaveCommunity = async (communityId) => {
  try {
    await communityStore.leaveCommunity(communityId);
  } catch (error) {
    console.error("Failed to leave community:", error);
  }
};

const handleCommunityCreated = (newCommunity) => {
  showCreateModal.value = false;
  // The new community will be automatically added to userCommunities by the store
};

// Lifecycle
onMounted(() => {
  loadCommunities();
});
</script>

<style scoped>
.communities-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #fff7ed 0%, #fed7aa 50%, #fdba74 100%);
  padding: 2rem;
}

.page-header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 2.5rem;
  margin-bottom: 2rem;
  box-shadow: 0 20px 40px rgba(249, 115, 22, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.header-content h1 {
  font-size: 2.5rem;
  font-weight: 700;
  background: linear-gradient(135deg, #f97316, #ea580c);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 0.5rem;
}

.subtitle {
  color: #6b7280;
  font-size: 1.1rem;
  margin-bottom: 2rem;
}

.header-actions {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.search-container {
  position: relative;
  flex: 1;
  max-width: 400px;
}

.search-input {
  width: 100%;
  padding: 1rem 1rem 1rem 3rem;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  font-size: 1rem;
  background: white;
  transition: all 0.3s ease;
}

.search-input:focus {
  outline: none;
  border-color: #f97316;
  box-shadow: 0 0 0 3px rgba(249, 115, 22, 0.1);
}

.search-icon {
  position: absolute;
  left: 1rem;
  top: 50%;
  transform: translateY(-50%);
  color: #6b7280;
}

.create-community-btn {
  padding: 1rem 1.5rem;
  border: none;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: white;
}

.create-community-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(249, 115, 22, 0.3);
}

.communities-content {
  max-width: 1200px;
  margin: 0 auto;
}

.loading-section {
  text-align: center;
  padding: 4rem 2rem;
  color: #6b7280;
}

.loading-spinner-large {
  width: 40px;
  height: 40px;
  border: 4px solid transparent;
  border-top: 4px solid #f97316;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

.error-section {
  text-align: center;
  padding: 4rem 2rem;
  background: white;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.error-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.error-section h3 {
  color: #dc2626;
  margin-bottom: 0.5rem;
}

.retry-btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  color: white;
  margin-top: 1rem;
}

.communities-section {
  margin-bottom: 3rem;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-size: 1.5rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 1.5rem;
}

.section-icon {
  font-size: 1.75rem;
}

.count-badge {
  background: #f97316;
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.875rem;
  font-weight: 500;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 1.5rem;
}

.no-results {
  text-align: center;
  padding: 3rem 2rem;
  background: white;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.05);
}

.no-results-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
  opacity: 0.5;
}

.no-results h3 {
  color: #374151;
  margin-bottom: 0.5rem;
}

.no-results p {
  color: #6b7280;
}

.glass-morphism {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.orange-gradient {
  background: linear-gradient(135deg, #f97316, #ea580c);
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

/* Responsive Design */
@media (max-width: 768px) {
  .communities-page {
    padding: 1rem;
  }

  .page-header {
    padding: 1.5rem;
  }

  .header-content h1 {
    font-size: 2rem;
  }

  .header-actions {
    flex-direction: column;
    align-items: stretch;
  }

  .search-container {
    max-width: none;
  }

  .grid {
    grid-template-columns: 1fr;
  }
}
</style>
