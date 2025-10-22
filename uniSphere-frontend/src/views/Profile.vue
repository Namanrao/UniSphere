<template>
  <div class="profile-page">
    <!-- Background gradient -->
    <div class="background-gradient"></div>

    <!-- Header Section -->
    <div class="page-header glass-morphism">
      <div class="header-content">
        <div class="header-main">
          <h1>My Profile</h1>
          <p class="subtitle">
            Manage your academic identity and track your engagement
          </p>
        </div>
        <div class="profile-badge">
          <div class="avatar">
            <span class="avatar-icon">👤</span>
          </div>
          <div class="badge-info">
            <div class="user-level">{{ getUserLevel() }}</div>
            <div class="member-since">Member since {{ formatJoinDate() }}</div>
          </div>
        </div>
      </div>
    </div>

    <div class="profile-content">
      <div class="content-grid">
        <!-- Left Column - Personal Info & Actions -->
        <div class="left-column">
          <!-- Personal Information Card -->
          <div class="profile-card glass-morphism">
            <div class="card-header">
              <h2 class="card-title">
                <span class="title-icon">📋</span>
                Personal Information
              </h2>
              <button class="edit-btn" @click="editProfile">
                <span class="btn-icon">✏️</span>
                Edit
              </button>
            </div>

            <div class="user-info">
              <div class="info-item">
                <div class="info-label">
                  <span class="label-icon">👤</span>
                  Username
                </div>
                <div class="info-value">
                  {{ authStore.currentUser?.username || "N/A" }}
                </div>
              </div>

              <div class="info-item">
                <div class="info-label">
                  <span class="label-icon">📧</span>
                  Email Address
                </div>
                <div class="info-value">
                  {{ authStore.currentUser?.email || "N/A" }}
                </div>
              </div>

              <div class="info-item">
                <div class="info-label">
                  <span class="label-icon">🎓</span>
                  University
                </div>
                <div class="info-value">
                  <span
                    v-if="authStore.currentUniversity"
                    class="university-badge"
                  >
                    {{ authStore.currentUniversity.name }}
                    <span class="verification-status verified"> VERIFIED </span>
                  </span>
                  <span v-else class="no-university">
                    Not associated with a university
                  </span>
                </div>
              </div>

              <div class="info-item">
                <div class="info-label">
                  <span class="label-icon">🆔</span>
                  User ID
                </div>
                <div class="info-value">
                  {{ authStore.currentUser?.id || "N/A" }}
                </div>
              </div>
            </div>
          </div>

          <!-- Quick Actions Card -->
          <div class="actions-card glass-morphism">
            <h2 class="card-title">
              <span class="title-icon">⚡</span>
              Quick Actions
            </h2>
            <div class="actions-grid">
              <button @click="navigateToPosts" class="action-btn">
                <span class="action-icon">📝</span>
                <span class="action-text">My Posts</span>
              </button>
              <button @click="navigateToCommunities" class="action-btn">
                <span class="action-icon">🏫</span>
                <span class="action-text">My Communities</span>
              </button>
              <button
                @click="showVerification"
                class="action-btn"
                v-if="!authStore.currentUniversity"
              >
                <span class="action-icon">✅</span>
                <span class="action-text">Verify University</span>
              </button>
            </div>
          </div>
        </div>

        <!-- Right Column - Stats & Activity -->
        <div class="right-column">
          <!-- Stats Card -->
          <div class="stats-card glass-morphism">
            <h2 class="card-title">
              <span class="title-icon">📊</span>
              My Engagement Stats
            </h2>
            <div class="stats-grid">
              <div class="stat-item">
                <div
                  class="stat-icon"
                  style="background: linear-gradient(135deg, #f97316, #ea580c)"
                >
                  📝
                </div>
                <div class="stat-content">
                  <div class="stat-number">{{ userStats.postCount || 0 }}</div>
                  <div class="stat-label">Posts Created</div>
                </div>
              </div>

              <div class="stat-item">
                <div
                  class="stat-icon"
                  style="background: linear-gradient(135deg, #3b82f6, #1d4ed8)"
                >
                  💬
                </div>
                <div class="stat-content">
                  <div class="stat-number">
                    {{ userStats.commentCount || 0 }}
                  </div>
                  <div class="stat-label">Comments</div>
                </div>
              </div>

              <div class="stat-item">
                <div
                  class="stat-icon"
                  style="background: linear-gradient(135deg, #10b981, #059669)"
                >
                  🏫
                </div>
                <div class="stat-content">
                  <div class="stat-number">
                    {{ userStats.communityCount || 0 }}
                  </div>
                  <div class="stat-label">Communities</div>
                </div>
              </div>

              <div class="stat-item">
                <div
                  class="stat-icon"
                  style="background: linear-gradient(135deg, #f59e0b, #d97706)"
                >
                  👍
                </div>
                <div class="stat-content">
                  <div class="stat-number">
                    {{ userStats.karmaPoints || 0 }}
                  </div>
                  <div class="stat-label">Karma Points</div>
                </div>
              </div>
            </div>
          </div>

          <!-- Recent Activity Card -->
          <div class="activity-card glass-morphism">
            <div class="card-header">
              <h2 class="card-title">
                <span class="title-icon">🕒</span>
                Recent Activity
              </h2>
            </div>

            <div class="activity-list">
              <div v-if="recentActivity.length === 0" class="empty-activity">
                <div class="empty-icon">📊</div>
                <p>No recent activity</p>
                <small>Your activity will appear here</small>
              </div>

              <div v-else class="activity-items">
                <div
                  v-for="activity in recentActivity"
                  :key="activity.id"
                  class="activity-item"
                >
                  <div class="activity-icon" :class="activity.type">
                    {{ getActivityIcon(activity.type) }}
                  </div>
                  <div class="activity-content">
                    <p class="activity-text">{{ activity.text }}</p>
                    <span class="activity-time">{{ activity.time }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- University Info Card (Only if user has university) -->
          <div
            v-if="authStore.currentUniversity"
            class="achievement-card glass-morphism"
          >
            <h2 class="card-title">
              <span class="title-icon">🎓</span>
              University Information
            </h2>
            <div class="university-info">
              <div class="info-row">
                <span class="info-label">Institution:</span>
                <span class="info-value">{{
                  authStore.currentUniversity.name
                }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">Email Domain:</span>
                <span class="info-value">{{
                  authStore.currentUniversity.domain
                }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">Verification Status:</span>
                <span class="info-value verification-badge verified">
                  VERIFIED
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";

const authStore = useAuthStore();
const router = useRouter();

const recentActivity = ref([]);
const userStats = ref({
  postCount: 0,
  commentCount: 0,
  communityCount: 0,
  karmaPoints: 0,
});

// Methods
const formatJoinDate = () => {
  if (authStore.currentUser?.createdAt) {
    return new Date(authStore.currentUser.createdAt).toLocaleDateString(
      "en-US",
      {
        year: "numeric",
        month: "long",
      }
    );
  }
  return new Date().toLocaleDateString("en-US", {
    year: "numeric",
    month: "long",
  });
};

const getUserLevel = () => {
  const karma = userStats.value.karmaPoints || 0;
  if (karma >= 100) return "Academic Scholar";
  if (karma >= 50) return "Active Contributor";
  if (karma >= 10) return "Engaged Student";
  return "New Explorer";
};

const editProfile = () => {
  console.log("Edit profile clicked");
  // Implement edit profile functionality
};

const navigateToPosts = () => {
  router.push("/dashboard/my-posts");
};

const navigateToCommunities = () => {
  router.push("/dashboard/communities");
};

const showVerification = () => {
  console.log("Show verification");
  // Implement verification flow
};

const getActivityIcon = (type) => {
  const icons = {
    post: "📝",
    comment: "💬",
    vote: "👍",
    join: "🏫",
    achievement: "🏆",
  };
  return icons[type] || "🔔";
};

// Fetch user data
const fetchUserData = async () => {
  try {
    // If user doesn't have university data in store, fetch it
    if (!authStore.currentUniversity) {
      await authStore.fetchUserUniversity();
    }

    // Mock stats based on user activity - will be replaced with real API calls
    userStats.value = {
      postCount: 0, // TODO: Fetch from posts API
      commentCount: 0, // TODO: Fetch from comments API
      communityCount: 0, // TODO: Fetch from communities API
      karmaPoints: 0, // TODO: Fetch from user stats API
    };

    // Mock recent activity - will be replaced with real API calls
    recentActivity.value = [];
  } catch (error) {
    console.error("Error fetching user data:", error);
    // Ensure we show empty states on error
    userStats.value = {
      postCount: 0,
      commentCount: 0,
      communityCount: 0,
      karmaPoints: 0,
    };
    recentActivity.value = [];
  }
};

onMounted(() => {
  fetchUserData();
});
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #fff7ed 0%, #fed7aa 50%, #fdba74 100%);
  padding: 2rem;
  position: relative;
}

.background-gradient {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(
      circle at 20% 80%,
      rgba(251, 146, 60, 0.3) 0%,
      transparent 50%
    ),
    radial-gradient(
      circle at 80% 20%,
      rgba(249, 115, 22, 0.4) 0%,
      transparent 50%
    ),
    radial-gradient(
      circle at 40% 40%,
      rgba(253, 186, 116, 0.2) 0%,
      transparent 50%
    );
  animation: gradientShift 8s ease-in-out infinite;
}

@keyframes gradientShift {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0.8;
  }
}

.page-header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 2.5rem;
  margin-bottom: 2rem;
  box-shadow: 0 20px 40px rgba(249, 115, 22, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.3);
  position: relative;
  z-index: 2;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 2rem;
}

.header-main h1 {
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
  line-height: 1.5;
}

.profile-badge {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem 1.5rem;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  min-width: 200px;
}

.avatar {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #f97316, #ea580c);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 20px rgba(249, 115, 22, 0.3);
}

.avatar-icon {
  font-size: 1.5rem;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
}

.user-level {
  font-size: 1rem;
  font-weight: 700;
  color: #f97316;
  margin-bottom: 0.25rem;
}

.member-since {
  font-size: 0.875rem;
  color: #6b7280;
}

.profile-content {
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  z-index: 2;
}

.content-grid {
  display: grid;
  grid-template-columns: 1fr 1.5fr;
  gap: 2rem;
}

.left-column,
.right-column {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

/* Card Styles */
.profile-card,
.actions-card,
.stats-card,
.activity-card,
.achievement-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 2rem;
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.profile-card::before,
.actions-card::before,
.stats-card::before,
.activity-card::before,
.achievement-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(135deg, #f97316, #ea580c);
}

.profile-card:hover,
.actions-card:hover,
.stats-card:hover,
.activity-card:hover,
.achievement-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(249, 115, 22, 0.15);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-size: 1.25rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
}

.title-icon {
  font-size: 1.5rem;
}

/* Profile Information */
.user-info {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 1rem 0;
  border-bottom: 1px solid rgba(229, 231, 235, 0.5);
}

.info-item:last-child {
  border-bottom: none;
}

.info-label {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-weight: 600;
  color: #374151;
  min-width: 140px;
}

.label-icon {
  font-size: 1.1rem;
  opacity: 0.8;
}

.info-value {
  color: #1f2937;
  text-align: right;
  flex: 1;
}

.university-badge {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  background: rgba(16, 185, 129, 0.1);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-weight: 500;
}

.verification-status {
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
}

.verification-status.verified {
  background: rgba(16, 185, 129, 0.2);
  color: #059669;
}

.verification-status.pending {
  background: rgba(245, 158, 11, 0.2);
  color: #d97706;
}

.verification-status.not_verified {
  background: rgba(107, 114, 128, 0.2);
  color: #6b7280;
}

.no-university {
  color: #6b7280;
  font-style: italic;
}

/* University Info */
.university-info {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem 0;
  border-bottom: 1px solid rgba(229, 231, 235, 0.5);
}

.info-row:last-child {
  border-bottom: none;
}

.info-row .info-label {
  font-weight: 600;
  color: #374151;
  min-width: auto;
}

.info-row .info-value {
  text-align: right;
}

.verification-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
}

.verification-badge.verified {
  background: rgba(16, 185, 129, 0.2);
  color: #059669;
}

.verification-badge.pending {
  background: rgba(245, 158, 11, 0.2);
  color: #d97706;
}

.verification-badge.not_verified {
  background: rgba(107, 114, 128, 0.2);
  color: #6b7280;
}

/* Buttons */
.edit-btn {
  padding: 0.5rem 1rem;
  border: 2px solid #e5e7eb;
  background: white;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
}

.edit-btn:hover {
  border-color: #f97316;
  color: #f97316;
  transform: translateY(-1px);
}

/* Quick Actions */
.actions-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.action-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.75rem;
  padding: 1.5rem 1rem;
  border: 2px solid rgba(229, 231, 235, 0.5);
  background: rgba(255, 255, 255, 0.6);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: center;
}

.action-btn:hover {
  background: white;
  border-color: #f97316;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(249, 115, 22, 0.15);
}

.action-icon {
  font-size: 1.5rem;
}

.action-text {
  font-weight: 500;
  color: #374151;
  font-size: 0.875rem;
}

/* Stats Grid */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.5rem;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1.25rem;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
}

.stat-item:hover {
  background: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.25rem;
  flex-shrink: 0;
}

.stat-content {
  flex: 1;
}

.stat-number {
  font-size: 1.5rem;
  font-weight: 700;
  color: #1f2937;
  line-height: 1;
  margin-bottom: 0.25rem;
}

.stat-label {
  font-size: 0.875rem;
  color: #6b7280;
  font-weight: 500;
  margin-bottom: 0.25rem;
}

/* Activity List */
.activity-list {
  max-height: 300px;
  overflow-y: auto;
}

.empty-activity {
  text-align: center;
  padding: 2rem 1rem;
  color: #6b7280;
}

.empty-icon {
  font-size: 2rem;
  margin-bottom: 1rem;
  opacity: 0.5;
}

.activity-items {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.activity-item {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
  padding: 1rem;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
}

.activity-item:hover {
  background: white;
  transform: translateX(4px);
}

.activity-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.1rem;
  flex-shrink: 0;
  background: rgba(249, 115, 22, 0.1);
}

.activity-content {
  flex: 1;
}

.activity-text {
  color: #374151;
  margin-bottom: 0.25rem;
  line-height: 1.4;
}

.activity-time {
  font-size: 0.75rem;
  color: #6b7280;
}

/* Utility Classes */
.glass-morphism {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.orange-gradient {
  background: linear-gradient(135deg, #f97316, #ea580c);
}

/* Responsive Design */
@media (max-width: 1024px) {
  .content-grid {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }

  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .profile-page {
    padding: 1rem;
  }

  .page-header {
    padding: 1.5rem;
  }

  .header-content {
    flex-direction: column;
    text-align: center;
    gap: 1.5rem;
  }

  .header-main h1 {
    font-size: 2rem;
  }

  .profile-badge {
    align-self: center;
  }

  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .actions-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .profile-card,
  .actions-card,
  .stats-card,
  .activity-card,
  .achievement-card {
    padding: 1.5rem;
  }

  .stats-grid {
    grid-template-columns: 1fr;
  }

  .info-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }

  .info-value {
    text-align: left;
  }

  .info-row {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }

  .info-row .info-value {
    text-align: left;
  }
}
</style>
