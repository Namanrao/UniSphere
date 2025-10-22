<template>
  <div class="dashboard">
    <!-- Welcome Header -->
    <div class="welcome-section">
      <div class="welcome-content">
        <div class="welcome-badge">
          <span>🎓 Welcome back, {{ authStore.currentUser?.username }}!</span>
        </div>
        <h1 class="welcome-title">
          Your University <span class="gradient-text">Community</span> Awaits
        </h1>
        <p class="welcome-subtitle">
          Discover the latest discussions, share your knowledge, and connect
          with fellow students
        </p>
        <div class="welcome-stats">
          <div class="stat-card">
            <div class="stat-icon">📚</div>
            <div class="stat-info">
              <div class="stat-number">12</div>
              <div class="stat-label">Active Courses</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon">💬</div>
            <div class="stat-info">
              <div class="stat-number">24</div>
              <div class="stat-label">Discussions</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon">👥</div>
            <div class="stat-info">
              <div class="stat-number">5</div>
              <div class="stat-label">Communities</div>
            </div>
          </div>
        </div>
      </div>
      <div class="welcome-illustration">
        <div class="illustration-container">
          <div class="floating-element el-1">🚀</div>
          <div class="floating-element el-2">💡</div>
          <div class="floating-element el-3">🤝</div>
        </div>
      </div>
    </div>

    <!-- Posts Section -->
    <div class="posts-container">
      <div class="section-header">
        <div class="header-content">
          <h2>Recent Discussions</h2>
          <p>Stay updated with the latest from your communities</p>
        </div>
        <div class="header-actions">
          <div class="sort-tabs">
            <button
              v-for="option in sortOptions"
              :key="option.value"
              @click="currentSort = option.value"
              :class="['sort-tab', { active: currentSort === option.value }]"
            >
              <span class="tab-label">{{ option.label }}</span>
              <span class="tab-indicator"></span>
            </button>
          </div>
          <button @click="showCreatePost = true" class="create-post-btn">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path
                d="M12 5V19M5 12H19"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
            New Post
          </button>
        </div>
      </div>

      <!-- Posts Grid -->
      <div class="posts-grid">
        <PostCard
          v-for="post in filteredPosts"
          :key="post.id"
          :post="post"
          @vote="handleVote"
          @comment="handleComment"
          class="post-card-item"
        />

        <!-- Empty State -->
        <div v-if="posts.length === 0" class="empty-state">
          <div class="empty-illustration">
            <svg width="120" height="120" viewBox="0 0 24 24" fill="none">
              <path
                d="M14 2H6C5.46957 2 4.96086 2.21071 4.58579 2.58579C4.21071 2.96086 4 3.46957 4 4V20C4 20.5304 4.21071 21.0391 4.58579 21.4142C4.96086 21.7893 5.46957 22 6 22H18C18.5304 22 19.0391 21.7893 19.4142 21.4142C19.7893 21.0391 20 20.5304 20 20V8L14 2Z"
                stroke="currentColor"
                stroke-width="1.5"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M14 2V8H20"
                stroke="currentColor"
                stroke-width="1.5"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M16 13H8"
                stroke="currentColor"
                stroke-width="1.5"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M16 17H8"
                stroke="currentColor"
                stroke-width="1.5"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M10 9H8"
                stroke="currentColor"
                stroke-width="1.5"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
            </svg>
          </div>
          <div class="empty-content">
            <h3>No discussions yet</h3>
            <p>
              Be the first to start a conversation in your university community!
            </p>
            <button @click="showCreatePost = true" class="cta-button">
              Start First Discussion
            </button>
          </div>
        </div>
      </div>

      <!-- Load More -->
      <div v-if="posts.length > 0" class="load-more-section">
        <button class="load-more-btn">
          <span>Load More Discussions</span>
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
            <path
              d="M12 5V19M5 12H19"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
            />
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useAuthStore } from "@/stores/auth";
import PostCard from "@/components/post/PostCard.vue";

const authStore = useAuthStore();
const currentSort = ref("new");
const posts = ref([]);
const showCreatePost = ref(false);

const sortOptions = [
  { value: "new", label: "Latest" },
  { value: "hot", label: "Trending" },
  { value: "top", label: "Popular" },
];

// Mock data - replace with actual API calls
const mockPosts = [
  {
    id: 1,
    title: "Welcome to UniSphere!",
    content:
      "This is the first post in our university community. Feel free to introduce yourself and share what you're studying!",
    author: "admin",
    community: "welcome",
    votes: 15,
    commentCount: 8,
    createdAt: "2024-01-15T10:30:00Z",
    userVote: 0,
    tags: ["welcome", "introduction"],
  },
  {
    id: 2,
    title: "Study Group for CS101 Midterms - Anyone Interested?",
    content:
      "Looking for serious students to form a study group for the upcoming midterms. We can share resources and practice together!",
    author: "cs_student",
    community: "cs101",
    votes: 23,
    commentCount: 12,
    createdAt: "2024-01-15T09:15:00Z",
    userVote: 1,
    tags: ["study-group", "cs101", "midterms"],
  },
  {
    id: 3,
    title: "Best Quiet Study Spots on Campus? Library is Always Packed!",
    content:
      "What are your favorite hidden gems for studying? The main library is always so crowded during exam season...",
    author: "studious_guy",
    community: "campuslife",
    votes: 8,
    commentCount: 15,
    createdAt: "2024-01-14T16:45:00Z",
    userVote: -1,
    tags: ["study-spots", "campus", "tips"],
  },
  {
    id: 4,
    title: "Calculus II Help - Integration Techniques",
    content:
      "Struggling with some integration problems. Anyone have good resources or tips for mastering these techniques?",
    author: "math_learner",
    community: "mathematics",
    votes: 12,
    commentCount: 6,
    createdAt: "2024-01-14T14:20:00Z",
    userVote: 0,
    tags: ["calculus", "help", "mathematics"],
  },
];

const filteredPosts = computed(() => {
  const sortedPosts = [...posts.value];

  switch (currentSort.value) {
    case "new":
      return sortedPosts.sort(
        (a, b) => new Date(b.createdAt) - new Date(a.createdAt)
      );
    case "hot":
      return sortedPosts.sort(
        (a, b) => b.votes * 2 + b.commentCount - (a.votes * 2 + a.commentCount)
      );
    case "top":
      return sortedPosts.sort((a, b) => b.votes - a.votes);
    default:
      return sortedPosts;
  }
});

onMounted(() => {
  // Fetch posts from API
  posts.value = mockPosts;
});

const handleVote = (postId, voteType) => {
  // Handle vote logic
  console.log(`Vote ${voteType} on post ${postId}`);
};

const handleComment = (postId) => {
  // Handle comment logic
  console.log(`Comment on post ${postId}`);
};
</script>

<style scoped>
.dashboard {
  padding: 0;
}

/* Welcome Section */
.welcome-section {
  background: linear-gradient(135deg, #fff7ed 0%, #fed7aa 100%);
  border-radius: 24px;
  padding: 3rem;
  margin-bottom: 2rem;
  position: relative;
  overflow: hidden;
  border: 1px solid rgba(251, 146, 60, 0.2);
}

.welcome-section::before {
  content: "";
  position: absolute;
  top: 0;
  right: 0;
  width: 200px;
  height: 200px;
  background: linear-gradient(
    135deg,
    rgba(251, 146, 60, 0.1),
    rgba(249, 115, 22, 0.05)
  );
  border-radius: 50%;
  transform: translate(30%, -30%);
}

.welcome-content {
  position: relative;
  z-index: 2;
}

.welcome-badge {
  display: inline-flex;
  background: rgba(251, 146, 60, 0.15);
  border: 1px solid rgba(251, 146, 60, 0.3);
  color: #c2410c;
  padding: 0.5rem 1rem;
  border-radius: 50px;
  font-size: 0.875rem;
  font-weight: 600;
  margin-bottom: 1.5rem;
  backdrop-filter: blur(10px);
}

.welcome-title {
  font-size: 2.5rem;
  font-weight: 800;
  line-height: 1.1;
  margin-bottom: 1rem;
  color: #1f2937;
}

.gradient-text {
  background: linear-gradient(135deg, #f97316, #ea580c);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.welcome-subtitle {
  font-size: 1.125rem;
  line-height: 1.6;
  color: #6b7280;
  margin-bottom: 2.5rem;
  max-width: 500px;
}

.welcome-stats {
  display: flex;
  gap: 1.5rem;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 1rem;
  background: white;
  padding: 1.25rem;
  border-radius: 16px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(251, 146, 60, 0.2);
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.12);
}

.stat-icon {
  font-size: 1.5rem;
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #fed7aa, #fdba74);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-number {
  font-size: 1.5rem;
  font-weight: 700;
  color: #f97316;
  line-height: 1;
  margin-bottom: 0.25rem;
}

.stat-label {
  font-size: 0.875rem;
  color: #6b7280;
  font-weight: 500;
}

.welcome-illustration {
  position: absolute;
  top: 50%;
  right: 3rem;
  transform: translateY(-50%);
}

.illustration-container {
  position: relative;
  width: 200px;
  height: 200px;
}

.floating-element {
  position: absolute;
  font-size: 2rem;
  animation: float 3s ease-in-out infinite;
}

.el-1 {
  top: 20%;
  right: 0;
  animation-delay: 0s;
}

.el-2 {
  top: 50%;
  right: 40%;
  animation-delay: 1s;
}

.el-3 {
  bottom: 20%;
  right: 20%;
  animation-delay: 2s;
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-10px);
  }
}

/* Posts Container */
.posts-container {
  background: white;
  border-radius: 24px;
  padding: 2.5rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid #f3f4f6;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 2.5rem;
  padding-bottom: 1.5rem;
  border-bottom: 2px solid #fff7ed;
}

.header-content h2 {
  font-size: 1.75rem;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 0.5rem 0;
}

.header-content p {
  color: #6b7280;
  margin: 0;
  font-size: 1rem;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.sort-tabs {
  display: flex;
  background: #f8f9fa;
  padding: 0.25rem;
  border-radius: 12px;
  border: 1px solid #e5e7eb;
}

.sort-tab {
  position: relative;
  padding: 0.75rem 1.25rem;
  border: none;
  background: transparent;
  color: #6b7280;
  font-weight: 600;
  cursor: pointer;
  border-radius: 8px;
  transition: all 0.3s ease;
  font-size: 0.9rem;
}

.sort-tab:hover {
  color: #f97316;
}

.sort-tab.active {
  color: #f97316;
}

.sort-tab.active .tab-indicator {
  opacity: 1;
  transform: scaleX(1);
}

.tab-indicator {
  position: absolute;
  bottom: -0.25rem;
  left: 0.75rem;
  right: 0.75rem;
  height: 2px;
  background: linear-gradient(135deg, #f97316, #fb923c);
  border-radius: 2px;
  opacity: 0;
  transform: scaleX(0);
  transition: all 0.3s ease;
}

.create-post-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  background: linear-gradient(135deg, #f97316, #fb923c);
  color: white;
  border: none;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(249, 115, 22, 0.3);
}

.create-post-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(249, 115, 22, 0.4);
}

/* Posts Grid */
.posts-grid {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.post-card-item {
  transition: all 0.3s ease;
}

.post-card-item:hover {
  transform: translateY(-2px);
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  background: #f8f9fa;
  border-radius: 20px;
  border: 2px dashed #e5e7eb;
}

.empty-illustration {
  margin-bottom: 2rem;
}

.empty-illustration svg {
  color: #d1d5db;
}

.empty-content h3 {
  font-size: 1.5rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 1rem 0;
}

.empty-content p {
  color: #6b7280;
  margin: 0 0 2rem 0;
  font-size: 1rem;
  max-width: 400px;
  margin-left: auto;
  margin-right: auto;
}

.cta-button {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 1rem 2rem;
  background: linear-gradient(135deg, #f97316, #fb923c);
  color: white;
  border: none;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(249, 115, 22, 0.3);
}

.cta-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(249, 115, 22, 0.4);
}

/* Load More Section */
.load-more-section {
  text-align: center;
  margin-top: 3rem;
  padding-top: 2rem;
  border-top: 1px solid #f3f4f6;
}

.load-more-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 1rem 2rem;
  background: white;
  color: #f97316;
  border: 2px solid #fed7aa;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.load-more-btn:hover {
  background: #fff7ed;
  border-color: #f97316;
  transform: translateY(-2px);
}

/* Responsive Design */
@media (max-width: 1024px) {
  .welcome-stats {
    flex-wrap: wrap;
  }

  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1.5rem;
  }

  .header-actions {
    width: 100%;
    justify-content: space-between;
  }
}

@media (max-width: 768px) {
  .welcome-section {
    padding: 2rem 1.5rem;
    text-align: center;
  }

  .welcome-title {
    font-size: 2rem;
  }

  .welcome-illustration {
    display: none;
  }

  .welcome-stats {
    justify-content: center;
  }

  .posts-container {
    padding: 1.5rem;
  }

  .sort-tabs {
    width: 100%;
    justify-content: center;
  }

  .header-actions {
    flex-direction: column;
    gap: 1rem;
  }

  .create-post-btn {
    width: 100%;
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .welcome-section {
    padding: 1.5rem 1rem;
  }

  .welcome-title {
    font-size: 1.75rem;
  }

  .stat-card {
    flex-direction: column;
    text-align: center;
    gap: 0.75rem;
  }

  .posts-container {
    padding: 1rem;
    border-radius: 16px;
  }

  .sort-tab {
    padding: 0.5rem 1rem;
    font-size: 0.8rem;
  }
}
</style>
