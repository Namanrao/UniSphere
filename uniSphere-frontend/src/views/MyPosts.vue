<template>
  <div class="my-posts-page">
    <!-- Background gradient -->
    <div class="background-gradient"></div>

    <!-- Header Section -->
    <div class="page-header glass-morphism">
      <div class="header-content">
        <div class="header-main">
          <h1>My Posts</h1>
          <p class="subtitle">
            Manage and view your created posts across all communities
          </p>
        </div>
        <div class="header-stats">
          <div class="stat-card">
            <div class="stat-icon">📝</div>
            <div class="stat-info">
              <div class="stat-number">{{ posts.length }}</div>
              <div class="stat-label">Total Posts</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon">💬</div>
            <div class="stat-info">
              <div class="stat-number">{{ totalComments }}</div>
              <div class="stat-label">Total Comments</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon">🔥</div>
            <div class="stat-info">
              <div class="stat-number">{{ totalVotes }}</div>
              <div class="stat-label">Total Votes</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="posts-content">
      <!-- Loading State -->
      <div v-if="postStore.loading" class="loading-section">
        <div class="loading-spinner-large"></div>
        <p>Loading your posts...</p>
      </div>

      <!-- Error State -->
      <div v-else-if="postStore.error" class="error-section glass-morphism">
        <div class="error-icon">⚠️</div>
        <h3>Failed to load posts</h3>
        <p>{{ postStore.error }}</p>
        <button @click="loadPosts" class="retry-btn orange-gradient">
          Try Again
        </button>
      </div>

      <!-- Empty State -->
      <div v-else-if="posts.length === 0" class="empty-state glass-morphism">
        <div class="empty-icon">📝</div>
        <h2>No Posts Yet</h2>
        <p>
          You haven't created any posts yet. Start sharing your thoughts and
          questions with the community!
        </p>
        <div class="empty-actions">
          <button
            @click="navigateToDashboard"
            class="action-btn orange-gradient"
          >
            <span class="btn-icon">🏠</span>
            Browse Dashboard
          </button>
          <button @click="navigateToCommunities" class="action-btn outline">
            <span class="btn-icon">🏫</span>
            Join Communities
          </button>
        </div>
      </div>

      <!-- Posts List -->
      <div v-else class="posts-section">
        <!-- Filters and Search -->
        <div class="filters-bar glass-morphism">
          <div class="search-container">
            <input
              v-model="searchQuery"
              type="text"
              placeholder="Search your posts..."
              class="search-input"
            />
            <span class="search-icon">🔍</span>
          </div>
          <div class="filter-buttons">
            <button
              v-for="filter in filters"
              :key="filter.key"
              @click="activeFilter = filter.key"
              class="filter-btn"
              :class="{ active: activeFilter === filter.key }"
            >
              {{ filter.label }}
            </button>
          </div>
          <div class="sort-controls">
            <select v-model="sortBy" class="sort-select">
              <option value="newest">Newest First</option>
              <option value="oldest">Oldest First</option>
              <option value="popular">Most Popular</option>
            </select>
          </div>
        </div>

        <!-- Posts Grid -->
        <div class="posts-grid">
          <div
            v-for="post in filteredPosts"
            :key="post.id"
            class="post-card glass-morphism"
          >
            <div class="post-header">
              <div class="community-badge">
                <span class="badge-icon">🏷️</span>
                <span class="community-name">{{ post.communityName }}</span>
              </div>
              <div class="post-meta">
                <span class="post-date">{{ formatDate(post.createdAt) }}</span>
                <span class="post-status" :class="post.status?.toLowerCase()">
                  {{ post.status || "ACTIVE" }}
                </span>
              </div>
            </div>

            <div class="post-content">
              <h3 class="post-title">{{ post.title }}</h3>
              <p class="post-excerpt">{{ truncateContent(post.content) }}</p>
            </div>

            <div class="post-stats">
              <div class="stat">
                <span class="stat-icon">👍</span>
                <span class="stat-number">{{ post.upvoteCount || 0 }}</span>
              </div>
              <div class="stat">
                <span class="stat-icon">👎</span>
                <span class="stat-number">{{ post.downvoteCount || 0 }}</span>
              </div>
              <div class="stat">
                <span class="stat-icon">💬</span>
                <span class="stat-number">{{ post.commentCount || 0 }}</span>
              </div>
              <div class="stat">
                <span class="stat-icon">🔥</span>
                <span class="stat-number">{{ post.voteScore || 0 }}</span>
              </div>
            </div>

            <div class="post-actions">
              <button @click="editPost(post)" class="action-btn edit-btn">
                <span class="btn-icon">✏️</span>
                Edit
              </button>
              <button @click="viewPost(post)" class="action-btn view-btn">
                <span class="btn-icon">👀</span>
                View
              </button>
              <button
                @click="deletePost(post.id)"
                class="action-btn delete-btn"
              >
                <span class="btn-icon">🗑️</span>
                Delete
              </button>
            </div>
          </div>
        </div>

        <!-- Load More -->
        <div v-if="hasMorePosts" class="load-more-section">
          <button @click="loadMore" class="load-more-btn orange-gradient">
            <span v-if="loadingMore" class="loading-spinner"></span>
            {{ loadingMore ? "Loading..." : "Load More Posts" }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { usePostStore } from "@/stores/posts";
import { useAuthStore } from "@/stores/auth";

const router = useRouter();
const postStore = usePostStore();
const authStore = useAuthStore();

// Reactive data
const searchQuery = ref("");
const activeFilter = ref("all");
const sortBy = ref("newest");
const loadingMore = ref(false);
const currentPage = ref(0);

// Filters
const filters = [
  { key: "all", label: "All Posts" },
  { key: "popular", label: "Most Popular" },
  { key: "recent", label: "Recently Active" },
];

// Computed properties
const posts = computed(() => postStore.posts);
const totalComments = computed(() =>
  posts.value.reduce((sum, post) => sum + (post.commentCount || 0), 0)
);
const totalVotes = computed(() =>
  posts.value.reduce((sum, post) => sum + (post.voteScore || 0), 0)
);

const filteredPosts = computed(() => {
  let filtered = [...posts.value];

  // Search filter
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase();
    filtered = filtered.filter(
      (post) =>
        post.title.toLowerCase().includes(query) ||
        post.content.toLowerCase().includes(query)
    );
  }

  // Sort
  switch (sortBy.value) {
    case "newest":
      filtered.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
      break;
    case "oldest":
      filtered.sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt));
      break;
    case "popular":
      filtered.sort((a, b) => (b.voteScore || 0) - (a.voteScore || 0));
      break;
  }

  return filtered;
});

const hasMorePosts = computed(() => {
  // Implement pagination logic based on your API
  return posts.value.length > 0 && posts.value.length % 20 === 0;
});

// Methods
const loadPosts = async () => {
  try {
    // This will need to be implemented based on your user posts API
    // For now, we'll use the existing posts from the store
    console.log("Loading user posts...");
  } catch (error) {
    console.error("Failed to load posts:", error);
  }
};

const loadMore = async () => {
  loadingMore.value = true;
  currentPage.value += 1;
  // Implement pagination logic
  setTimeout(() => {
    loadingMore.value = false;
  }, 1000);
};

const editPost = (post) => {
  console.log("Edit post:", post);
  // Implement edit functionality
};

const viewPost = (post) => {
  console.log("View post:", post);
  // Navigate to post detail page
  router.push(`/post/${post.id}`);
};

const deletePost = async (postId) => {
  if (confirm("Are you sure you want to delete this post?")) {
    try {
      await postStore.deletePost(postId);
    } catch (error) {
      console.error("Failed to delete post:", error);
    }
  }
};

const truncateContent = (content, length = 150) => {
  if (!content) return "";
  return content.length > length
    ? content.substring(0, length) + "..."
    : content;
};

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString("en-US", {
    year: "numeric",
    month: "short",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};

const navigateToDashboard = () => {
  router.push("/dashboard/home");
};

const navigateToCommunities = () => {
  router.push("/dashboard/communities");
};

// Lifecycle
onMounted(() => {
  loadPosts();
});
</script>

<style scoped>
.my-posts-page {
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
  align-items: flex-start;
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

.header-stats {
  display: flex;
  gap: 1rem;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem 1.5rem;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  min-width: 140px;
}

.stat-icon {
  font-size: 1.5rem;
  opacity: 0.8;
}

.stat-number {
  font-size: 1.5rem;
  font-weight: 700;
  color: #f97316;
  line-height: 1;
}

.stat-label {
  font-size: 0.875rem;
  color: #6b7280;
  font-weight: 500;
}

.posts-content {
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  z-index: 2;
}

/* Loading and Error States */
.loading-section,
.error-section,
.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  border-radius: 16px;
  margin-bottom: 2rem;
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

.error-icon,
.empty-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
  opacity: 0.7;
}

.error-section h3,
.empty-state h2 {
  color: #1f2937;
  margin-bottom: 0.5rem;
}

.error-section p,
.empty-state p {
  color: #6b7280;
  margin-bottom: 1.5rem;
}

.retry-btn,
.action-btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  margin: 0 0.5rem;
}

.retry-btn,
.action-btn.orange-gradient {
  background: linear-gradient(135deg, #f97316, #ea580c);
  color: white;
}

.retry-btn:hover,
.action-btn.orange-gradient:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(249, 115, 22, 0.3);
}

.action-btn.outline {
  background: transparent;
  border: 2px solid #f97316;
  color: #f97316;
}

.action-btn.outline:hover {
  background: #f97316;
  color: white;
  transform: translateY(-2px);
}

/* Filters Bar */
.filters-bar {
  display: flex;
  gap: 1.5rem;
  align-items: center;
  padding: 1.5rem;
  margin-bottom: 2rem;
  border-radius: 16px;
}

.search-container {
  position: relative;
  flex: 1;
  max-width: 300px;
}

.search-input {
  width: 100%;
  padding: 0.75rem 1rem 0.75rem 3rem;
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

.filter-buttons {
  display: flex;
  gap: 0.5rem;
}

.filter-btn {
  padding: 0.5rem 1rem;
  border: 2px solid #e5e7eb;
  background: white;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.filter-btn.active {
  background: #f97316;
  color: white;
  border-color: #f97316;
}

.filter-btn:hover:not(.active) {
  border-color: #f97316;
  color: #f97316;
}

.sort-select {
  padding: 0.5rem 1rem;
  border: 2px solid #e5e7eb;
  border-radius: 8px;
  background: white;
  font-size: 0.875rem;
  cursor: pointer;
}

.sort-select:focus {
  outline: none;
  border-color: #f97316;
}

/* Posts Grid */
.posts-grid {
  display: grid;
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.post-card {
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

.post-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(135deg, #f97316, #ea580c);
}

.post-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(249, 115, 22, 0.15);
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.community-badge {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: rgba(249, 115, 22, 0.1);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.875rem;
  font-weight: 500;
  color: #f97316;
}

.badge-icon {
  font-size: 1rem;
}

.post-meta {
  display: flex;
  gap: 1rem;
  align-items: center;
  font-size: 0.875rem;
  color: #6b7280;
}

.post-status {
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
}

.post-status.active {
  background: rgba(16, 185, 129, 0.1);
  color: #059669;
}

.post-content {
  margin-bottom: 1.5rem;
}

.post-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 0.75rem;
  line-height: 1.4;
}

.post-excerpt {
  color: #6b7280;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-clamp: 3;
  box-orient: vertical;
}

.post-stats {
  display: flex;
  gap: 1.5rem;
  margin-bottom: 1.5rem;
  padding: 1rem 0;
  border-top: 1px solid #e5e7eb;
  border-bottom: 1px solid #e5e7eb;
}

.stat {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
  color: #6b7280;
}

.stat-icon {
  font-size: 1rem;
}

.stat-number {
  font-weight: 600;
  color: #1f2937;
}

.post-actions {
  display: flex;
  gap: 0.75rem;
}

.action-btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
}

.edit-btn {
  background: rgba(59, 130, 246, 0.1);
  color: #3b82f6;
}

.edit-btn:hover {
  background: #3b82f6;
  color: white;
}

.view-btn {
  background: rgba(16, 185, 129, 0.1);
  color: #059669;
}

.view-btn:hover {
  background: #059669;
  color: white;
}

.delete-btn {
  background: rgba(239, 68, 68, 0.1);
  color: #dc2626;
}

.delete-btn:hover {
  background: #dc2626;
  color: white;
}

/* Load More */
.load-more-section {
  text-align: center;
  margin-top: 2rem;
}

.load-more-btn {
  padding: 1rem 2rem;
  border: none;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  color: white;
}

.load-more-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(249, 115, 22, 0.3);
}

.load-more-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.loading-spinner {
  width: 16px;
  height: 16px;
  border: 2px solid transparent;
  border-top: 2px solid currentColor;
  border-radius: 50%;
  animation: spin 1s linear infinite;
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
  .my-posts-page {
    padding: 1rem;
  }

  .page-header {
    padding: 1.5rem;
  }

  .header-content {
    flex-direction: column;
    gap: 1.5rem;
  }

  .header-main h1 {
    font-size: 2rem;
  }

  .header-stats {
    width: 100%;
    justify-content: space-between;
  }

  .stat-card {
    flex: 1;
    min-width: auto;
    justify-content: center;
  }

  .filters-bar {
    flex-direction: column;
    align-items: stretch;
    gap: 1rem;
  }

  .search-container {
    max-width: none;
  }

  .filter-buttons {
    justify-content: center;
  }

  .post-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }

  .post-stats {
    justify-content: space-between;
  }

  .post-actions {
    flex-wrap: wrap;
  }

  .action-btn {
    flex: 1;
    justify-content: center;
    min-width: 80px;
  }
}

@media (max-width: 480px) {
  .header-stats {
    flex-direction: column;
  }

  .posts-grid {
    grid-template-columns: 1fr;
  }

  .post-card {
    padding: 1.5rem;
  }
}
</style>
