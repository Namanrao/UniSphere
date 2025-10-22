<template>
  <div class="create-post">
    <div class="modal-header">
      <h2>Create New Post</h2>
      <button @click="$emit('close')" class="close-btn">×</button>
    </div>
    <div class="modal-body">
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="title">Post Title</label>
          <input
            id="title"
            v-model="postData.title"
            type="text"
            placeholder="Enter a catchy title..."
            required
            :disabled="postStore.loading"
          />
        </div>

        <div class="form-group">
          <label for="content">Post Content</label>
          <textarea
            id="content"
            v-model="postData.content"
            placeholder="Share your thoughts, questions, or resources..."
            rows="6"
            required
            :disabled="postStore.loading"
          ></textarea>
        </div>

        <div class="form-group">
          <label for="community">Community</label>
          <select
            id="community"
            v-model="postData.communityId"
            required
            :disabled="postStore.loading || communityStore.loading"
          >
            <option value="">Select a community</option>
            <option
              v-for="community in availableCommunities"
              :key="community.id"
              :value="community.id"
            >
              {{ community.displayName }} ({{ community.memberCount }} members)
            </option>
          </select>
          <div v-if="communityStore.loading" class="loading-communities">
            <span class="loading-spinner-small"></span>
            Loading your communities...
          </div>
          <div v-else-if="!availableCommunities.length" class="no-communities">
            You haven't joined any communities yet.
            <a href="#" @click.prevent="showCommunitiesPage"
              >Browse and join communities first</a
            >
          </div>
        </div>

        <!-- Error Messages -->
        <div v-if="postStore.error" class="error-message">
          <span class="error-icon">⚠️</span>
          {{ postStore.error }}
        </div>
        <div v-if="communityStore.error" class="error-message">
          <span class="error-icon">⚠️</span>
          {{ communityStore.error }}
        </div>

        <div class="form-actions">
          <button
            type="button"
            @click="$emit('close')"
            class="btn btn-secondary"
            :disabled="postStore.loading"
          >
            Cancel
          </button>
          <button
            type="submit"
            class="btn btn-primary"
            :disabled="
              postStore.loading || !isFormValid || communityStore.loading
            "
          >
            <span v-if="postStore.loading" class="loading-spinner"></span>
            {{ postStore.loading ? "Creating..." : "Create Post" }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { usePostStore } from "@/stores/posts";
import { useCommunityStore } from "@/stores/communities";

const emit = defineEmits(["close", "created"]);
const router = useRouter();

const postStore = usePostStore();
const communityStore = useCommunityStore();

const postData = ref({
  title: "",
  content: "",
  communityId: null,
});

const availableCommunities = computed(() => {
  return communityStore.userCommunities;
});

const isFormValid = computed(() => {
  return (
    postData.value.title.trim() &&
    postData.value.content.trim() &&
    postData.value.communityId
  );
});

const handleSubmit = async () => {
  if (!isFormValid.value) return;

  try {
    const createdPost = await postStore.createPost({
      title: postData.value.title,
      content: postData.value.content,
      communityId: postData.value.communityId,
    });

    // Reset form
    postData.value = {
      title: "",
      content: "",
      communityId: null,
    };

    // Emit events
    emit("created", createdPost);
    emit("close");
  } catch (error) {
    // Error is handled by the store
    console.error("Failed to create post:", error);
  }
};

const showCommunitiesPage = () => {
  emit("close");
  router.push("/communities");
};

// Load user's communities when component mounts
onMounted(async () => {
  try {
    await communityStore.fetchUserCommunities();
  } catch (error) {
    console.error("Failed to load user communities:", error);
  }
});
</script>

<style scoped>
.create-post {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 20px 40px rgba(249, 115, 22, 0.15);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid #e5e7eb;
  background: linear-gradient(135deg, #fff7ed 0%, #fed7aa 100%);
}

.modal-header h2 {
  margin: 0;
  color: #1f2937;
  font-weight: 600;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #6b7280;
  padding: 0.25rem;
  transition: color 0.2s;
}

.close-btn:hover {
  color: #374151;
}

.modal-body {
  padding: 1.5rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 600;
  color: #374151;
}

input,
textarea,
select {
  width: 100%;
  padding: 0.75rem;
  border: 2px solid #e5e7eb;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.3s ease;
  background: white;
}

input:focus,
textarea:focus,
select:focus {
  outline: none;
  border-color: #f97316;
  box-shadow: 0 0 0 3px rgba(249, 115, 22, 0.1);
  transform: translateY(-1px);
}

input:disabled,
textarea:disabled,
select:disabled {
  background-color: #f9fafb;
  cursor: not-allowed;
  opacity: 0.7;
}

textarea {
  resize: vertical;
  font-family: inherit;
  min-height: 120px;
}

.loading-communities {
  margin-top: 0.5rem;
  color: #6b7280;
  font-size: 0.875rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.loading-spinner-small {
  width: 14px;
  height: 14px;
  border: 2px solid transparent;
  border-top: 2px solid #6b7280;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

.no-communities {
  margin-top: 0.5rem;
  color: #6b7280;
  font-size: 0.875rem;
  line-height: 1.4;
}

.no-communities a {
  color: #f97316;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.2s;
}

.no-communities a:hover {
  color: #ea580c;
  text-decoration: underline;
}

.error-message {
  background: rgba(220, 38, 38, 0.1);
  border: 1px solid rgba(220, 38, 38, 0.2);
  color: #dc2626;
  padding: 0.75rem;
  border-radius: 8px;
  margin-bottom: 1rem;
  font-size: 0.875rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.error-icon {
  font-size: 1rem;
}

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: 2rem;
}

.btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none !important;
}

.btn-primary {
  background: linear-gradient(135deg, #f97316, #ea580c);
  color: white;
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(249, 115, 22, 0.3);
}

.btn-secondary {
  background: #6b7280;
  color: white;
}

.btn-secondary:hover:not(:disabled) {
  background: #4b5563;
  transform: translateY(-1px);
}

.loading-spinner {
  width: 16px;
  height: 16px;
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

/* Responsive design */
@media (max-width: 480px) {
  .modal-body {
    padding: 1rem;
  }

  .modal-header {
    padding: 1rem 1.5rem;
  }

  .form-actions {
    flex-direction: column-reverse;
  }

  .btn {
    width: 100%;
    justify-content: center;
  }
}
</style>
