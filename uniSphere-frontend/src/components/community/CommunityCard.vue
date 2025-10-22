<template>
  <div class="community-card glass-morphism">
    <div class="card-header">
      <div class="community-info">
        <h3 class="community-name">{{ community.displayName }}</h3>
        <p class="community-members">{{ community.memberCount }} members</p>
      </div>
      <div class="community-type" :class="community.type.toLowerCase()">
        {{ community.type }}
      </div>
    </div>

    <p class="community-description" v-if="community.description">
      {{ community.description }}
    </p>

    <div class="card-footer">
      <div class="community-meta">
        <span class="university">{{ community.universityName }}</span>
      </div>
      <button
        v-if="!isMember"
        @click="$emit('join', community.id)"
        class="join-btn orange-gradient"
        :disabled="loading"
      >
        <span v-if="loading" class="loading-spinner-small"></span>
        {{ loading ? "Joining..." : "Join" }}
      </button>
      <button
        v-else
        @click="$emit('leave', community.id)"
        class="leave-btn"
        :disabled="loading"
      >
        <span v-if="loading" class="loading-spinner-small"></span>
        {{ loading ? "Leaving..." : "Leave" }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

defineProps({
  community: {
    type: Object,
    required: true,
  },
  isMember: {
    type: Boolean,
    default: false,
  },
});

defineEmits(["join", "leave"]);

const loading = ref(false);
</script>

<style scoped>
.community-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 1.5rem;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.community-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(249, 115, 22, 0.15);
}

.community-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(135deg, #f97316, #ea580c);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1rem;
}

.community-info {
  flex: 1;
}

.community-name {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 0.25rem;
  line-height: 1.3;
}

.community-members {
  color: #6b7280;
  font-size: 0.875rem;
}

.community-type {
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.community-type.public {
  background: rgba(34, 197, 94, 0.1);
  color: #16a34a;
}

.community-type.private {
  background: rgba(249, 115, 22, 0.1);
  color: #ea580c;
}

.community-description {
  color: #4b5563;
  line-height: 1.5;
  margin-bottom: 1.5rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  /* Add standard properties for better browser support */
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  /* Standard property (limited support but future-proof) */
  line-clamp: 2;
  box-orient: vertical;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.community-meta {
  font-size: 0.875rem;
  color: #6b7280;
}

.university {
  font-weight: 500;
}

.join-btn,
.leave-btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
}

.join-btn {
  color: white;
}

.join-btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(249, 115, 22, 0.3);
}

.leave-btn {
  background: #f3f4f6;
  color: #6b7280;
  border: 1px solid #e5e7eb;
}

.leave-btn:hover:not(:disabled) {
  background: #fee2e2;
  color: #dc2626;
  border-color: #fecaca;
}

.join-btn:disabled,
.leave-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none !important;
}

.loading-spinner-small {
  width: 14px;
  height: 14px;
  border: 2px solid transparent;
  border-top: 2px solid currentColor;
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

.glass-morphism {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
}

.orange-gradient {
  background: linear-gradient(135deg, #f97316, #ea580c);
}
</style>
