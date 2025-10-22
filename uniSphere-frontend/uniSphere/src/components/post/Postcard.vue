<template>
  <div class="post-card">
    <div class="vote-section">
      <button
        @click="handleVote(1)"
        :class="['vote-btn upvote', { active: post.userVote === 1 }]"
      >
        ▲
      </button>
      <span class="vote-count">{{ post.votes }}</span>
      <button
        @click="handleVote(-1)"
        :class="['vote-btn downvote', { active: post.userVote === -1 }]"
      >
        ▼
      </button>
    </div>

    <div class="post-content">
      <div class="post-header">
        <span class="community">r/{{ post.community }}</span>
        <span class="separator">•</span>
        <span class="author">Posted by u/{{ post.author }}</span>
        <span class="separator">•</span>
        <span class="timestamp">{{ formatTime(post.createdAt) }}</span>
      </div>

      <h3 class="post-title">{{ post.title }}</h3>

      <p class="post-body">{{ post.content }}</p>

      <div class="post-actions">
        <button @click="$emit('comment', post.id)" class="action-btn">
          💬 {{ post.commentCount }} comments
        </button>
        <button class="action-btn">📤 Share</button>
        <button class="action-btn">🔖 Save</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
  post: {
    type: Object,
    required: true,
  },
});

const emit = defineEmits(["vote", "comment"]);

const handleVote = (voteType) => {
  emit("vote", props.post.id, voteType);
};

const formatTime = (timestamp) => {
  const now = new Date();
  const postTime = new Date(timestamp);
  const diffInHours = Math.floor((now - postTime) / (1000 * 60 * 60));

  if (diffInHours < 1) return "just now";
  if (diffInHours < 24) return `${diffInHours}h ago`;

  const diffInDays = Math.floor(diffInHours / 24);
  return `${diffInDays}d ago`;
};
</script>

<style scoped>
.post-card {
  display: flex;
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  overflow: hidden;
  transition: box-shadow 0.2s;
}

.post-card:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.vote-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 1rem;
  background: #f8f9fa;
  min-width: 60px;
}

.vote-btn {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.25rem;
  font-size: 1.25rem;
  color: #666;
  transition: color 0.2s;
}

.vote-btn.upvote:hover {
  color: #ff4500;
}

.vote-btn.downvote:hover {
  color: #7193ff;
}

.vote-btn.active.upvote {
  color: #ff4500;
}

.vote-btn.active.downvote {
  color: #7193ff;
}

.vote-count {
  font-weight: 600;
  margin: 0.25rem 0;
  font-size: 0.875rem;
}

.post-content {
  flex: 1;
  padding: 1rem;
}

.post-header {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.8rem;
  color: #666;
  margin-bottom: 0.5rem;
}

.community {
  font-weight: 600;
  color: #333;
}

.author {
  color: #666;
}

.separator {
  color: #999;
}

.timestamp {
  color: #999;
}

.post-title {
  margin: 0 0 0.75rem 0;
  font-size: 1.125rem;
  font-weight: 600;
  color: #333;
  line-height: 1.4;
}

.post-body {
  margin: 0 0 1rem 0;
  color: #555;
  line-height: 1.5;
  font-size: 0.95rem;
}

.post-actions {
  display: flex;
  gap: 1rem;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  background: none;
  border: none;
  padding: 0.5rem 0.75rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.8rem;
  color: #666;
  transition: background-color 0.2s;
}

.action-btn:hover {
  background: #f8f9fa;
}
</style>
