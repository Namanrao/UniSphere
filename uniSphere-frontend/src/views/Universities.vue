<template>
  <div class="universities-page">
    <!-- Background gradient -->
    <div class="background-gradient"></div>

    <!-- Header Section -->
    <div class="page-header glass-morphism">
      <div class="header-content">
        <h1>Your Universities</h1>
        <p class="subtitle">
          Join and verify your university associations to create communities
        </p>
      </div>
    </div>

    <div class="universities-content">
      <!-- Loading State -->
      <div
        v-if="universityStore.loading && !universityStore.universities.length"
        class="loading-section"
      >
        <div class="loading-spinner-large"></div>
        <p>Loading universities...</p>
      </div>

      <!-- Error State -->
      <div
        v-else-if="universityStore.error"
        class="error-section glass-morphism"
      >
        <div class="error-icon">⚠️</div>
        <h3>Failed to load universities</h3>
        <p>{{ universityStore.error }}</p>
        <button @click="loadUniversities" class="retry-btn orange-gradient">
          Try Again
        </button>
      </div>

      <!-- Content -->
      <div v-else>
        <!-- Your Universities -->
        <section
          v-if="userUniversities.length > 0"
          class="universities-section"
        >
          <h2 class="section-title">
            <span class="section-icon">🎓</span>
            Your University Associations
          </h2>
          <div class="user-universities-grid">
            <div
              v-for="userUni in userUniversities"
              :key="userUni.id"
              class="user-university-card glass-morphism"
              :class="userUni.verificationStatus.toLowerCase()"
            >
              <div class="university-info">
                <h3 class="university-name">{{ userUni.university.name }}</h3>
                <p class="university-domain">
                  @{{ userUni.university.domain }}
                </p>
                <p class="university-description">
                  {{ userUni.university.description }}
                </p>
                <div class="university-meta">
                  <p
                    class="verification-status"
                    :class="userUni.verificationStatus.toLowerCase()"
                  >
                    <span class="status-icon">{{
                      getStatusIcon(userUni.verificationStatus)
                    }}</span>
                    {{ userUni.verificationStatus }}
                  </p>
                  <p class="joined-date">
                    <span class="date-icon">📅</span>
                    Joined {{ formatDate(userUni.joinedAt) }}
                  </p>
                </div>
              </div>
              <div class="verification-actions">
                <div
                  v-if="userUni.verificationStatus === 'PENDING'"
                  class="pending-message"
                >
                  <span class="pending-icon">⏳</span>
                  Verification pending. Check your email for verification link.
                </div>
                <div
                  v-else-if="userUni.verificationStatus === 'VERIFIED'"
                  class="verified-badge"
                >
                  <span class="verified-icon">✅</span>
                  Verified & Ready
                </div>
              </div>
            </div>
          </div>
        </section>

        <!-- Available Universities -->
        <section class="universities-section">
          <div class="section-header">
            <h2 class="section-title">
              <span class="section-icon">🏫</span>
              Available Universities
              <span class="count-badge">{{
                availableUniversities.length
              }}</span>
            </h2>
            <p class="section-subtitle">
              Join your university to start creating communities
            </p>
          </div>

          <div
            v-if="availableUniversities.length === 0"
            class="no-results glass-morphism"
          >
            <div class="no-results-icon">🎓</div>
            <h3>No universities available</h3>
            <p>You've joined all available universities!</p>
          </div>

          <div v-else class="universities-grid">
            <div
              v-for="university in availableUniversities"
              :key="university.id"
              class="university-card glass-morphism"
            >
              <div class="card-header">
                <div class="university-badge">
                  <span class="badge-icon">🏛️</span>
                </div>
                <h3 class="university-name">{{ university.name }}</h3>
                <p class="university-domain">@{{ university.domain }}</p>
              </div>

              <div class="card-content">
                <p class="university-description">
                  {{ university.description }}
                </p>
                <div class="university-details">
                  <div class="detail-item">
                    <span class="detail-icon">📍</span>
                    <span class="detail-text">{{ university.location }}</span>
                  </div>
                  <div class="detail-item">
                    <span class="detail-icon">👥</span>
                    <span class="detail-text"
                      >{{ university.studentCount }} students</span
                    >
                  </div>
                </div>
              </div>

              <div class="card-footer">
                <button
                  @click="joinUniversity(university.id)"
                  class="join-btn orange-gradient"
                  :disabled="
                    isUniversityJoined(university.id) || universityStore.loading
                  "
                >
                  <span
                    v-if="universityStore.loading"
                    class="loading-spinner-small"
                  ></span>
                  <span v-else class="btn-icon">{{
                    isUniversityJoined(university.id) ? "✅" : "➕"
                  }}</span>
                  {{
                    isUniversityJoined(university.id)
                      ? "Joined"
                      : "Join University"
                  }}
                </button>
              </div>
            </div>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useUniversityStore } from "@/stores/universities";

const universityStore = useUniversityStore();

// Computed
const userUniversities = computed(() => universityStore.userUniversities);
const allUniversities = computed(() => universityStore.universities);

const availableUniversities = computed(() => {
  const joinedIds = userUniversities.value.map((uni) => uni.university.id);
  return allUniversities.value.filter((uni) => !joinedIds.includes(uni.id));
});

// Methods
const isUniversityJoined = (universityId) => {
  return userUniversities.value.some(
    (uni) => uni.university.id === universityId
  );
};

const joinUniversity = async (universityId) => {
  try {
    await universityStore.joinUniversity(universityId);
  } catch (error) {
    console.error("Failed to join university:", error);
  }
};

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString("en-US", {
    year: "numeric",
    month: "long",
    day: "numeric",
  });
};

const getStatusIcon = (status) => {
  const icons = {
    VERIFIED: "✅",
    PENDING: "⏳",
    REJECTED: "❌",
    EXPIRED: "⌛",
  };
  return icons[status] || "❓";
};

const loadUniversities = async () => {
  await universityStore.fetchUserUniversities();
  await universityStore.fetchAllUniversities();
};

// Lifecycle
onMounted(async () => {
  await loadUniversities();
});
</script>

<style scoped>
.universities-page {
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
  line-height: 1.5;
}

.universities-content {
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  z-index: 2;
}

/* Loading and Error States */
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
  padding: 3rem 2rem;
  border-radius: 16px;
  margin-bottom: 2rem;
}

.error-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
  opacity: 0.7;
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
  transition: all 0.3s ease;
}

.retry-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(249, 115, 22, 0.3);
}

/* Sections */
.universities-section {
  margin-bottom: 3rem;
}

.section-header {
  margin-bottom: 2rem;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-size: 1.5rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 0.5rem;
}

.section-subtitle {
  color: #6b7280;
  font-size: 1rem;
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
  margin-left: 0.5rem;
}

/* User Universities Grid */
.user-universities-grid {
  display: grid;
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.user-university-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 2rem;
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  overflow: hidden;
}

.user-university-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
}

.user-university-card.verified::before {
  background: linear-gradient(135deg, #10b981, #059669);
}

.user-university-card.pending::before {
  background: linear-gradient(135deg, #f59e0b, #d97706);
}

.user-university-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(249, 115, 22, 0.15);
}

.university-info {
  flex: 1;
}

.university-name {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 0.25rem;
}

.university-domain {
  color: #f97316;
  font-weight: 500;
  margin-bottom: 0.5rem;
}

.university-description {
  color: #6b7280;
  line-height: 1.5;
  margin-bottom: 1rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-clamp: 2;
  box-orient: vertical;
}

.university-meta {
  display: flex;
  gap: 1.5rem;
  flex-wrap: wrap;
}

.verification-status,
.joined-date {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
  font-weight: 500;
}

.verification-status.verified {
  color: #10b981;
}

.verification-status.pending {
  color: #f59e0b;
}

.status-icon,
.date-icon {
  font-size: 1rem;
}

.joined-date {
  color: #6b7280;
}

.verification-actions {
  flex-shrink: 0;
  margin-left: 1.5rem;
}

.pending-message {
  background: rgba(245, 158, 11, 0.1);
  border: 1px solid rgba(245, 158, 11, 0.2);
  color: #d97706;
  padding: 0.75rem 1rem;
  border-radius: 8px;
  font-size: 0.875rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  max-width: 250px;
}

.verified-badge {
  background: rgba(16, 185, 129, 0.1);
  border: 1px solid rgba(16, 185, 129, 0.2);
  color: #059669;
  padding: 0.75rem 1rem;
  border-radius: 8px;
  font-size: 0.875rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

/* Universities Grid */
.universities-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));
  gap: 1.5rem;
}

.university-card {
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

.university-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(135deg, #f97316, #ea580c);
}

.university-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 16px 40px rgba(249, 115, 22, 0.2);
}

.card-header {
  text-align: center;
  margin-bottom: 1.5rem;
}

.university-badge {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #f97316, #ea580c);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 1rem;
}

.badge-icon {
  font-size: 1.5rem;
}

.card-content {
  margin-bottom: 1.5rem;
}

.university-details {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-top: 1rem;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #6b7280;
  font-size: 0.875rem;
}

.detail-icon {
  font-size: 1rem;
  opacity: 0.7;
}

.card-footer {
  text-align: center;
}

.join-btn {
  width: 100%;
  padding: 1rem 1.5rem;
  border: none;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  color: white;
}

.join-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(249, 115, 22, 0.3);
}

.join-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none !important;
}

.btn-icon {
  font-size: 1.1rem;
}

.loading-spinner-small {
  width: 16px;
  height: 16px;
  border: 2px solid transparent;
  border-top: 2px solid currentColor;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

/* No Results */
.no-results {
  text-align: center;
  padding: 3rem 2rem;
  border-radius: 16px;
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

/* Utility Classes */
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
  .universities-page {
    padding: 1rem;
  }

  .page-header {
    padding: 1.5rem;
  }

  .header-content h1 {
    font-size: 2rem;
  }

  .user-university-card {
    flex-direction: column;
    text-align: center;
    gap: 1.5rem;
  }

  .verification-actions {
    margin-left: 0;
    width: 100%;
  }

  .pending-message,
  .verified-badge {
    max-width: none;
    justify-content: center;
  }

  .universities-grid {
    grid-template-columns: 1fr;
  }

  .university-meta {
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .universities-grid {
    grid-template-columns: 1fr;
  }

  .university-card {
    padding: 1.5rem;
  }
}
</style>
