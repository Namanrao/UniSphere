<template>
  <div class="app-layout">
    <!-- Stunning Header -->
    <header class="app-header">
      <div class="header-background"></div>
      <div class="container">
        <div class="logo">
          <router-link to="/dashboard" class="logo-link">
            <div class="logo-icon">🎓</div>
            <h2>UniSphere</h2>
          </router-link>
        </div>

        <nav class="main-nav">
          <router-link to="/dashboard/home" class="nav-link">
            <svg
              width="20"
              height="20"
              viewBox="0 0 24 24"
              fill="none"
              class="nav-icon"
            >
              <path
                d="M3 9L12 2L21 9V20C21 20.5304 20.7893 21.0391 20.4142 21.4142C20.0391 21.7893 19.5304 22 19 22H5C4.46957 22 3.96086 21.7893 3.58579 21.4142C3.21071 21.0391 3 20.5304 3 20V9Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M9 22V12H15V22"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
            <span>Home</span>
          </router-link>
          <router-link to="/dashboard/communities" class="nav-link">
            <svg
              width="20"
              height="20"
              viewBox="0 0 24 24"
              fill="none"
              class="nav-icon"
            >
              <path
                d="M17 21V19C17 17.9391 16.5786 16.9217 15.8284 16.1716C15.0783 15.4214 14.0609 15 13 15H5C3.93913 15 2.92172 15.4214 2.17157 16.1716C1.42143 16.9217 1 17.9391 1 19V21"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M9 11C11.2091 11 13 9.20914 13 7C13 4.79086 11.2091 3 9 3C6.79086 3 5 4.79086 5 7C5 9.20914 6.79086 11 9 11Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M23 21V19C22.9993 18.1137 22.7044 17.2528 22.1614 16.5523C21.6184 15.8519 20.8581 15.3516 20 15.13"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M16 3.13C16.8604 3.35031 17.623 3.85071 18.1676 4.55232C18.7122 5.25392 19.0078 6.11683 19.0078 7.005C19.0078 7.89318 18.7122 8.75608 18.1676 9.45769C17.623 10.1593 16.8604 10.6597 16 10.88"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
            <span>Communities</span>
          </router-link>
          <router-link to="/dashboard/universities" class="nav-link">
            <svg
              width="20"
              height="20"
              viewBox="0 0 24 24"
              fill="none"
              class="nav-icon"
            >
              <path
                d="M4 19L12 23L20 19"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M12 23V12"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M20 9L12 13L4 9L12 5L20 9Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
            <span>Universities</span>
          </router-link>
        </nav>

        <!-- Enhanced Search -->
        <div class="search-container">
          <div class="search-bar">
            <svg
              width="18"
              height="18"
              viewBox="0 0 24 24"
              fill="none"
              class="search-icon"
            >
              <path
                d="M21 21L16.514 16.506L21 21ZM19 10.5C19 15.194 15.194 19 10.5 19C5.806 19 2 15.194 2 10.5C2 5.806 5.806 2 10.5 2C15.194 2 19 5.806 19 10.5Z"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
            <input
              type="text"
              placeholder="Search posts, communities, courses..."
              v-model="searchQuery"
              @keyup.enter="performSearch"
              class="search-input"
            />
            <button @click="performSearch" class="search-btn">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                <path
                  d="M5 12H19M19 12L12 5M19 12L12 19"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                />
              </svg>
            </button>
          </div>
        </div>

        <!-- Sexy User Menu -->
        <div class="user-menu">
          <div class="user-info" @click="toggleDropdown">
            <div class="user-avatar">
              <div class="avatar-initials">{{ getUserInitials() }}</div>
              <div class="online-indicator"></div>
            </div>
            <div class="user-details">
              <span class="username">{{
                authStore.currentUser?.username
              }}</span>
              <span class="user-status">Online</span>
            </div>
            <svg
              width="16"
              height="16"
              viewBox="0 0 24 24"
              fill="none"
              class="dropdown-arrow"
              :class="{ rotated: showDropdown }"
            >
              <path
                d="M6 9L12 15L18 9"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
          </div>

          <transition name="dropdown">
            <div v-if="showDropdown" class="dropdown-menu">
              <div class="dropdown-header">
                <div class="dropdown-avatar">{{ getUserInitials() }}</div>
                <div class="dropdown-user-info">
                  <div class="dropdown-username">
                    {{ authStore.currentUser?.username }}
                  </div>
                  <div class="dropdown-email">
                    {{ authStore.currentUser?.email }}
                  </div>
                </div>
              </div>
              <div class="dropdown-divider"></div>
              <router-link to="/dashboard/profile" class="dropdown-item">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
                  <path
                    d="M20 21V19C20 17.9391 19.5786 16.9217 18.8284 16.1716C18.0783 15.4214 17.0609 15 16 15H8C6.93913 15 5.92172 15.4214 5.17157 16.1716C4.42143 16.9217 4 17.9391 4 19V21"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                  <path
                    d="M12 11C14.2091 11 16 9.20914 16 7C16 4.79086 14.2091 3 12 3C9.79086 3 8 4.79086 8 7C8 9.20914 9.79086 11 12 11Z"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                </svg>
                <span>My Profile</span>
              </router-link>
              <router-link to="/dashboard/my-posts" class="dropdown-item">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
                  <path
                    d="M14 2H6C5.46957 2 4.96086 2.21071 4.58579 2.58579C4.21071 2.96086 4 3.46957 4 4V20C4 20.5304 4.21071 21.0391 4.58579 21.4142C4.96086 21.7893 5.46957 22 6 22H18C18.5304 22 19.0391 21.7893 19.4142 21.4142C19.7893 21.0391 20 20.5304 20 20V8L14 2Z"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                  <path
                    d="M14 2V8H20"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                  <path
                    d="M16 13H8"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                  <path
                    d="M16 17H8"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                  <path
                    d="M10 9H8"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                </svg>
                <span>My Posts</span>
              </router-link>
              <div class="dropdown-divider"></div>
              <button @click="handleLogout" class="dropdown-item logout-btn">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
                  <path
                    d="M9 21H5C4.46957 21 3.96086 20.7893 3.58579 20.4142C3.21071 20.0391 3 19.5304 3 19V5C3 4.46957 3.21071 3.96086 3.58579 3.58579C3.96086 3.21071 4.46957 3 5 3H9"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                  <path
                    d="M16 17L21 12L16 7"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                  <path
                    d="M21 12H9"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                </svg>
                <span>Logout</span>
              </button>
            </div>
          </transition>
        </div>
      </div>
    </header>

    <!-- Main Content Area -->
    <main class="app-main">
      <div class="container">
        <!-- Beautiful Sidebar -->
        <aside class="sidebar">
          <!-- Quick Actions Card -->
          <div class="sidebar-card">
            <div class="card-header">
              <h3>Quick Actions</h3>
            </div>
            <div class="card-content">
              <button @click="showCreatePost = true" class="action-btn primary">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <path
                    d="M12 5V19M5 12H19"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                </svg>
                <span>Create Post</span>
              </button>
              <button
                @click="navigateToCreateCommunity"
                class="action-btn secondary"
                :disabled="!canCreateCommunity"
              >
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <path
                    d="M17 21V19C17 17.9391 16.5786 16.9217 15.8284 16.1716C15.0783 15.4214 14.0609 15 13 15H5C3.93913 15 2.92172 15.4214 2.17157 16.1716C1.42143 16.9217 1 17.9391 1 19V21"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                  <path
                    d="M9 11C11.2091 11 13 9.20914 13 7C13 4.79086 11.2091 3 9 3C6.79086 3 5 4.79086 5 7C5 9.20914 6.79086 11 9 11Z"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                  <path
                    d="M23 21V19C22.9993 18.1137 22.7044 17.2528 22.1614 16.5523C21.6184 15.8519 20.8581 15.3516 20 15.13"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                  <path
                    d="M16 3.13C16.8604 3.35031 17.623 3.85071 18.1676 4.55232C18.7122 5.25392 19.0078 6.11683 19.0078 7.005C19.0078 7.89318 18.7122 8.75608 18.1676 9.45769C17.623 10.1593 16.8604 10.6597 16 10.88"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                </svg>
                <span>
                  {{
                    canCreateCommunity
                      ? "Create Community"
                      : "Verify University First"
                  }}
                </span>
              </button>
            </div>
          </div>

          <!-- My Communities Card -->
          <div class="sidebar-card">
            <div class="card-header">
              <h3>My Communities</h3>
              <span class="badge">{{ userCommunities.length }}</span>
            </div>
            <div class="card-content">
              <div class="community-list">
                <!-- Loading State -->
                <div v-if="loadingCommunities" class="loading-communities">
                  <div class="loading-spinner-small"></div>
                  <span>Loading communities...</span>
                </div>

                <!-- Real Communities -->
                <div
                  v-else
                  v-for="community in userCommunities"
                  :key="community.id"
                  class="community-item"
                  @click="navigateToCommunity(community.id)"
                >
                  <div class="community-avatar">
                    <span>r/</span>
                  </div>
                  <div class="community-info">
                    <span class="community-name">{{
                      community.displayName || community.name
                    }}</span>
                    <span class="community-members"
                      >{{ community.memberCount || 0 }} members</span
                    >
                  </div>
                </div>

                <!-- Empty State -->
                <div
                  v-if="!loadingCommunities && userCommunities.length === 0"
                  class="no-communities"
                >
                  <div class="empty-state">
                    <svg width="48" height="48" viewBox="0 0 24 24" fill="none">
                      <path
                        d="M17 21V19C17 17.9391 16.5786 16.9217 15.8284 16.1716C15.0783 15.4214 14.0609 15 13 15H5C3.93913 15 2.92172 15.4214 2.17157 16.1716C1.42143 16.9217 1 17.9391 1 19V21"
                        stroke="currentColor"
                        stroke-width="2"
                        stroke-linecap="round"
                        stroke-linejoin="round"
                      />
                      <path
                        d="M9 11C11.2091 11 13 9.20914 13 7C13 4.79086 11.2091 3 9 3C6.79086 3 5 4.79086 5 7C5 9.20914 6.79086 11 9 11Z"
                        stroke="currentColor"
                        stroke-width="2"
                        stroke-linecap="round"
                        stroke-linejoin="round"
                      />
                      <path
                        d="M23 21V19C22.9993 18.1137 22.7044 17.2528 22.1614 16.5523C21.6184 15.8519 20.8581 15.3516 20 15.13"
                        stroke="currentColor"
                        stroke-width="2"
                        stroke-linecap="round"
                        stroke-linejoin="round"
                      />
                      <path
                        d="M16 3.13C16.8604 3.35031 17.623 3.85071 18.1676 4.55232C18.7122 5.25392 19.0078 6.11683 19.0078 7.005C19.0078 7.89318 18.7122 8.75608 18.1676 9.45769C17.623 10.1593 16.8604 10.6597 16 10.88"
                        stroke="currentColor"
                        stroke-width="2"
                        stroke-linecap="round"
                        stroke-linejoin="round"
                      />
                    </svg>
                    <p>No communities yet</p>
                    <router-link
                      to="/dashboard/communities"
                      class="explore-link"
                    >
                      Explore Communities
                    </router-link>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- University Info Card -->
          <div class="sidebar-card">
            <div class="card-header">
              <h3>University</h3>
            </div>
            <div class="card-content">
              <!-- Loading State -->
              <div v-if="loadingUniversity" class="loading-university">
                <div class="loading-spinner-small"></div>
                <span>Loading university...</span>
              </div>

              <!-- Real University Info -->
              <div class="university-info" v-else-if="userUniversity">
                <div class="uni-avatar">
                  <span>🏫</span>
                </div>
                <div class="uni-details">
                  <strong>{{
                    userUniversity.university?.name || userUniversity.name
                  }}</strong>
                  <span
                    class="verification-status"
                    :class="userUniversity.verificationStatus?.toLowerCase()"
                  >
                    {{
                      getVerificationStatusText(
                        userUniversity.verificationStatus
                      )
                    }}
                  </span>
                </div>
              </div>

              <!-- No University -->
              <div v-else class="no-university">
                <div class="empty-state">
                  <svg width="48" height="48" viewBox="0 0 24 24" fill="none">
                    <path
                      d="M4 19L12 23L20 19"
                      stroke="currentColor"
                      stroke-width="2"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                    />
                    <path
                      d="M12 23V12"
                      stroke="currentColor"
                      stroke-width="2"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                    />
                    <path
                      d="M20 9L12 13L4 9L12 5L20 9Z"
                      stroke="currentColor"
                      stroke-width="2"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                    />
                  </svg>
                  <p>No university</p>
                  <span class="hint">Join a university to get started</span>
                  <router-link
                    to="/dashboard/universities"
                    class="explore-link"
                  >
                    Join University
                  </router-link>
                </div>
              </div>
            </div>
          </div>
        </aside>

        <!-- Main Content -->
        <div class="main-content">
          <router-view />
        </div>
      </div>
    </main>

    <!-- Create Post Modal -->
    <div
      v-if="showCreatePost"
      class="modal-overlay"
      @click="showCreatePost = false"
    >
      <div class="modal-content" @click.stop>
        <CreatePost
          @close="showCreatePost = false"
          @created="handlePostCreated"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import { useCommunityStore } from "@/stores/communities";
import { useUniversityStore } from "@/stores/universities";
import CreatePost from "@/components/post/CreatePost.vue";

const router = useRouter();
const authStore = useAuthStore();
const communityStore = useCommunityStore();
const universityStore = useUniversityStore();

const searchQuery = ref("");
const showDropdown = ref(false);
const showCreatePost = ref(false);
const loadingCommunities = ref(false);
const loadingUniversity = ref(false);

// Computed properties for real data
const userCommunities = computed(() => communityStore.userCommunities);
const userUniversity = computed(() => {
  const userUnis = universityStore.userUniversities;
  return userUnis.length > 0 ? userUnis[0] : null;
});

const canCreateCommunity = computed(() => {
  return userUniversity.value?.verificationStatus === "VERIFIED";
});

// Fixed dropdown functionality
const toggleDropdown = (event) => {
  event.stopPropagation();
  showDropdown.value = !showDropdown.value;
};

// Close dropdown when clicking outside
const closeDropdown = (event) => {
  if (!event.target.closest(".user-menu")) {
    showDropdown.value = false;
  }
};

const handleLogout = () => {
  authStore.logout();
  router.push("/login");
  showDropdown.value = false;
};

const performSearch = () => {
  if (searchQuery.value.trim()) {
    router.push(`/dashboard/search?q=${encodeURIComponent(searchQuery.value)}`);
  }
};

const getUserInitials = () => {
  const user = authStore.currentUser;
  if (!user) return "U";
  return (user.firstName?.[0] || user.username[0]).toUpperCase();
};

const handlePostCreated = () => {
  showCreatePost.value = false;
};

const navigateToCreateCommunity = () => {
  if (canCreateCommunity.value) {
    router.push("/dashboard/communities");
  } else {
    alert(
      "You need to verify your university before creating communities. Please visit the Universities page."
    );
    router.push("/dashboard/universities");
  }
};

const navigateToCommunity = (communityId) => {
  router.push(`/dashboard/communities?community=${communityId}`);
};

const getVerificationStatusText = (status) => {
  const statusMap = {
    VERIFIED: "Verified",
    PENDING: "Pending Verification",
    REJECTED: "Verification Failed",
    EXPIRED: "Verification Expired",
  };
  return statusMap[status] || "Not Verified";
};

// Load user data
const loadUserData = async () => {
  try {
    loadingCommunities.value = true;
    loadingUniversity.value = true;

    await communityStore.fetchUserCommunities();
    await universityStore.fetchUserUniversities();
  } catch (error) {
    console.error("Failed to load user data:", error);
  } finally {
    loadingCommunities.value = false;
    loadingUniversity.value = false;
  }
};

onMounted(() => {
  loadUserData();
  document.addEventListener("click", closeDropdown);
});

onUnmounted(() => {
  document.removeEventListener("click", closeDropdown);
});
</script>

<style scoped>
.app-layout {
  min-height: 100vh;
  background: linear-gradient(135deg, #fff7ed 0%, #fed7aa 100%);
}

.container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 1.5rem;
  display: flex;
}

/* Header Styles */
.app-header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(251, 146, 60, 0.2);
  padding: 0.75rem 0;
  position: sticky;
  top: 0;
  z-index: 1000;
  box-shadow: 0 4px 20px rgba(249, 115, 22, 0.1);
}

.app-header .container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 2rem;
}

/* Logo */
.logo-link {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  text-decoration: none;
  transition: transform 0.3s ease;
}

.logo-link:hover {
  transform: scale(1.05);
}

.logo-icon {
  font-size: 1.5rem;
  background: linear-gradient(135deg, #f97316, #fb923c);
  border-radius: 12px;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo h2 {
  background: linear-gradient(135deg, #f97316, #ea580c);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 0;
  font-weight: 800;
  font-size: 1.5rem;
}

/* Navigation */
.main-nav {
  display: flex;
  gap: 0.5rem;
  background: rgba(255, 247, 237, 0.8);
  padding: 0.5rem;
  border-radius: 16px;
  border: 1px solid rgba(251, 146, 60, 0.2);
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.25rem;
  text-decoration: none;
  color: #6b7280;
  font-weight: 600;
  border-radius: 12px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.nav-link::before {
  content: "";
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #f97316, #fb923c);
  transition: left 0.3s ease;
  z-index: -1;
}

.nav-link:hover {
  color: white;
  transform: translateY(-2px);
}

.nav-link:hover::before {
  left: 0;
}

.nav-link.router-link-active {
  background: linear-gradient(135deg, #f97316, #fb923c);
  color: white;
  box-shadow: 0 4px 15px rgba(249, 115, 22, 0.3);
}

.nav-icon {
  flex-shrink: 0;
}

/* Search Bar */
.search-container {
  flex: 1;
  max-width: 400px;
}

.search-bar {
  position: relative;
  display: flex;
  align-items: center;
  background: white;
  border: 2px solid #fed7aa;
  border-radius: 16px;
  padding: 0.5rem 1rem;
  transition: all 0.3s ease;
  box-shadow: 0 2px 10px rgba(249, 115, 22, 0.1);
}

.search-bar:focus-within {
  border-color: #f97316;
  box-shadow: 0 4px 20px rgba(249, 115, 22, 0.2);
}

.search-icon {
  color: #fb923c;
  margin-right: 0.75rem;
}

.search-input {
  flex: 1;
  border: none;
  background: transparent;
  outline: none;
  font-size: 0.95rem;
  color: #1f2937;
}

.search-input::placeholder {
  color: #9ca3af;
}

.search-btn {
  background: linear-gradient(135deg, #f97316, #fb923c);
  border: none;
  border-radius: 12px;
  padding: 0.5rem;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.search-btn:hover {
  transform: scale(1.1);
  box-shadow: 0 4px 15px rgba(249, 115, 22, 0.4);
}

/* User Menu */
.user-menu {
  position: relative;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.5rem 1rem;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(251, 146, 60, 0.2);
}

.user-info:hover {
  background: white;
  box-shadow: 0 4px 15px rgba(249, 115, 22, 0.15);
  transform: translateY(-2px);
}

.user-avatar {
  position: relative;
}

.avatar-initials {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #f97316, #fb923c);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 1rem;
}

.online-indicator {
  position: absolute;
  bottom: 2px;
  right: 2px;
  width: 12px;
  height: 12px;
  background: #10b981;
  border: 2px solid white;
  border-radius: 50%;
}

.user-details {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.username {
  font-weight: 600;
  color: #1f2937;
  font-size: 0.95rem;
}

.user-status {
  font-size: 0.75rem;
  color: #10b981;
  font-weight: 500;
}

.dropdown-arrow {
  color: #6b7280;
  transition: transform 0.3s ease;
}

.dropdown-arrow.rotated {
  transform: rotate(180deg);
}

/* Dropdown Menu */
.dropdown-enter-active,
.dropdown-leave-active {
  transition: all 0.3s ease;
}

.dropdown-enter-from,
.dropdown-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  background: white;
  border: 1px solid #f3f4f6;
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
  min-width: 280px;
  z-index: 1000;
  margin-top: 0.75rem;
  overflow: hidden;
}

.dropdown-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1.5rem;
  background: linear-gradient(135deg, #fff7ed, #fed7aa);
}

.dropdown-avatar {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #f97316, #fb923c);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 1.1rem;
}

.dropdown-user-info {
  flex: 1;
}

.dropdown-username {
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 0.25rem;
}

.dropdown-email {
  font-size: 0.875rem;
  color: #6b7280;
}

.dropdown-divider {
  height: 1px;
  background: #f3f4f6;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem 1.5rem;
  text-decoration: none;
  color: #374151;
  border: none;
  background: none;
  width: 100%;
  text-align: left;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
}

.dropdown-item:hover {
  background: #fff7ed;
  color: #f97316;
}

.dropdown-item.logout-btn {
  color: #ef4444;
}

.dropdown-item.logout-btn:hover {
  background: #fef2f2;
  color: #dc2626;
}

/* Main Content Layout */
.app-main {
  padding: 2rem 0;
}

.app-main .container {
  gap: 2rem;
  align-items: flex-start;
}

/* Sidebar */
.sidebar {
  width: 320px;
  flex-shrink: 0;
  position: sticky;
  top: 6rem;
}

.sidebar-card {
  background: white;
  border-radius: 20px;
  margin-bottom: 1.5rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid #f3f4f6;
  overflow: hidden;
  position: relative;
}

.sidebar-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(135deg, #f97316, #fb923c);
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1.5rem 1.5rem 1rem;
}

.card-header h3 {
  margin: 0;
  font-size: 1.125rem;
  font-weight: 600;
  color: #1f2937;
}

.badge {
  background: linear-gradient(135deg, #f97316, #fb923c);
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 600;
}

.card-content {
  padding: 0 1.5rem 1.5rem;
}

/* Action Buttons */
.action-btn {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  width: 100%;
  padding: 1rem 1.25rem;
  border: none;
  border-radius: 12px;
  text-decoration: none;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  margin-bottom: 0.75rem;
}

.action-btn.primary {
  background: linear-gradient(135deg, #f97316, #fb923c);
  color: white;
  box-shadow: 0 4px 15px rgba(249, 115, 22, 0.3);
}

.action-btn.primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(249, 115, 22, 0.4);
}

.action-btn.secondary {
  background: white;
  color: #f97316;
  border: 2px solid #fed7aa;
}

.action-btn.secondary:hover {
  background: #fff7ed;
  transform: translateY(-2px);
  border-color: #f97316;
}

.action-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none !important;
}

.action-btn:disabled:hover {
  transform: none !important;
  box-shadow: none !important;
}

/* Loading States */
.loading-communities,
.loading-university {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem;
  color: #6b7280;
  font-size: 0.875rem;
  justify-content: center;
}

.loading-spinner-small {
  width: 16px;
  height: 16px;
  border: 2px solid transparent;
  border-top: 2px solid #f97316;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

/* Community List */
.community-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.community-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.75rem;
  border-radius: 12px;
  transition: all 0.3s ease;
  cursor: pointer;
}

.community-item:hover {
  background: #fff7ed;
  transform: translateX(4px);
}

.community-avatar {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #fed7aa, #fdba74);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  color: #c2410c;
  font-size: 0.875rem;
}

.community-info {
  flex: 1;
}

.community-name {
  display: block;
  font-weight: 600;
  color: #1f2937;
  font-size: 0.9rem;
  margin-bottom: 0.25rem;
}

.community-members {
  font-size: 0.75rem;
  color: #6b7280;
}

/* Empty States */
.empty-state {
  text-align: center;
  padding: 2rem 1rem;
  color: #6b7280;
}

.empty-state svg {
  color: #d1d5db;
  margin-bottom: 1rem;
}

.empty-state p {
  margin: 0 0 1rem;
  font-size: 0.9rem;
}

.explore-link {
  display: inline-block;
  background: linear-gradient(135deg, #f97316, #fb923c);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  text-decoration: none;
  font-size: 0.8rem;
  font-weight: 600;
  transition: all 0.3s ease;
}

.explore-link:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(249, 115, 22, 0.3);
}

.hint {
  font-size: 0.75rem;
  color: #9ca3af;
}

/* University Info */
.university-info {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background: #fff7ed;
  border-radius: 12px;
  border: 1px solid #fed7aa;
}

.uni-avatar {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #fed7aa, #fdba74);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
}

.uni-details {
  flex: 1;
}

.uni-details strong {
  display: block;
  font-size: 0.95rem;
  color: #1f2937;
  margin-bottom: 0.25rem;
}

.verification-status {
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.verification-status.verified {
  color: #10b981;
}

.verification-status.pending {
  color: #f59e0b;
}

.verification-status.rejected,
.verification-status.expired {
  color: #ef4444;
}

.verification-status:not(.verified):not(.pending):not(.rejected):not(.expired) {
  color: #6b7280;
}

/* Main Content */
.main-content {
  flex: 1;
  min-width: 0;
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
  backdrop-filter: blur(5px);
}

.modal-content {
  background: white;
  border-radius: 20px;
  max-width: 600px;
  width: 90%;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.25);
}

/* Responsive Design */
@media (max-width: 1024px) {
  .container {
    padding: 0 1rem;
  }

  .sidebar {
    width: 280px;
  }
}

@media (max-width: 768px) {
  .app-header .container {
    flex-wrap: wrap;
    gap: 1rem;
  }

  .main-nav {
    order: 3;
    width: 100%;
    justify-content: center;
  }

  .search-container {
    order: 2;
    max-width: none;
    flex: 1;
  }

  .app-main .container {
    flex-direction: column;
  }

  .sidebar {
    width: 100%;
    position: static;
  }
}

@media (max-width: 480px) {
  .container {
    padding: 0 0.75rem;
  }

  .nav-link span {
    display: none;
  }

  .nav-link {
    padding: 0.75rem;
  }

  .user-details {
    display: none;
  }

  .dropdown-menu {
    min-width: 250px;
    right: -50%;
  }
}
</style>
