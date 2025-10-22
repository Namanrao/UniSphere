<template>
  <div class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-content">
      <div class="modal-header">
        <h2>Create New Community</h2>
        <button @click="$emit('close')" class="close-btn">×</button>
      </div>

      <form @submit.prevent="handleSubmit" class="modal-body">
        <div class="form-group">
          <label for="name">Community Name *</label>
          <input
            id="name"
            v-model="formData.name"
            type="text"
            placeholder="e.g., cs101-discussions"
            required
            :disabled="loading"
          />
          <small
            >Unique identifier for the community (no spaces, lowercase)</small
          >
        </div>

        <div class="form-group">
          <label for="displayName">Display Name *</label>
          <input
            id="displayName"
            v-model="formData.displayName"
            type="text"
            placeholder="e.g., CS101 - Introduction to Computer Science"
            required
            :disabled="loading"
          />
        </div>

        <div class="form-group">
          <label for="description">Description</label>
          <textarea
            id="description"
            v-model="formData.description"
            placeholder="Describe what this community is about..."
            rows="3"
            :disabled="loading"
          ></textarea>
        </div>

        <div class="form-group">
          <label for="university">University *</label>
          <select
            id="university"
            v-model="formData.universityId"
            required
            :disabled="loading || universityStore.loading"
          >
            <option value="">Select your university</option>
            <option
              v-for="userUni in universityStore.userUniversities"
              :key="userUni.university.id"
              :value="userUni.university.id"
              :disabled="userUni.verificationStatus !== 'VERIFIED'"
            >
              {{ userUni.university.name }}
              ({{ userUni.verificationStatus }})
            </option>
          </select>

          <!-- University Verification Messages -->
          <div
            v-if="universityStore.loading"
            class="verification-message loading"
          >
            <span class="loading-spinner-small"></span>
            Loading your universities...
          </div>

          <div
            v-else-if="!universityStore.hasVerifiedUniversity"
            class="verification-message warning"
          >
            <span class="warning-icon">❗</span>
            You need to verify your university membership before creating
            communities.
            <router-link
              to="/dashboard/universities"
              @click="$emit('close')"
              class="verification-link"
            >
              Verify your university
            </router-link>
          </div>

          <div
            v-else-if="formData.universityId && !canCreateCommunity"
            class="verification-message warning"
          >
            <span class="warning-icon">❗</span>
            You are not verified for this university.
          </div>

          <div
            v-else-if="formData.universityId && canCreateCommunity"
            class="verification-message success"
          >
            <span class="success-icon">✅</span>
            You are verified for this university and can create communities.
          </div>
        </div>

        <div class="form-group">
          <label for="type">Community Type</label>
          <select id="type" v-model="formData.type" :disabled="loading">
            <option value="PUBLIC">Public - Anyone can join</option>
            <option value="PRIVATE">Private - Requires approval</option>
          </select>
        </div>

        <!-- Error Message -->
        <div v-if="error" class="error-message">
          <span class="error-icon">⚠️</span>
          {{ error }}
        </div>

        <div class="form-actions">
          <button
            type="button"
            @click="$emit('close')"
            class="btn btn-secondary"
            :disabled="loading"
          >
            Cancel
          </button>
          <button
            type="submit"
            class="btn btn-primary"
            :disabled="loading || !isFormValid || !canCreateCommunity"
          >
            <span v-if="loading" class="loading-spinner"></span>
            {{ loading ? "Creating..." : "Create Community" }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { useCommunityStore } from "@/stores/communities";
import { useUniversityStore } from "@/stores/universities";

const emit = defineEmits(["close", "created"]);
const router = useRouter();

const communityStore = useCommunityStore();
const universityStore = useUniversityStore();
const loading = ref(false);
const error = ref("");

const formData = ref({
  name: "",
  displayName: "",
  description: "",
  type: "PUBLIC",
  universityId: null,
});

const isFormValid = computed(() => {
  return (
    formData.value.name.trim() &&
    formData.value.displayName.trim() &&
    formData.value.universityId
  );
});

const canCreateCommunity = computed(() => {
  if (!formData.value.universityId) return false;
  return universityStore.isUserVerifiedForUniversity(
    formData.value.universityId
  );
});

const handleSubmit = async () => {
  if (!isFormValid.value || !canCreateCommunity.value) return;

  loading.value = true;
  error.value = "";

  try {
    const newCommunity = await communityStore.createCommunity(formData.value);
    emit("created", newCommunity);
  } catch (err) {
    error.value = err.message;
  } finally {
    loading.value = false;
  }
};

// Load user's universities on mount
onMounted(async () => {
  try {
    await universityStore.fetchUserUniversities();
    // Auto-select first verified university if available
    if (universityStore.verifiedUniversities.length > 0) {
      formData.value.universityId =
        universityStore.verifiedUniversities[0].university.id;
    }
  } catch (err) {
    console.error("Failed to load user universities:", err);
  }
});
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(4px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  padding: 1rem;
}

.modal-content {
  background: white;
  border-radius: 20px;
  width: 100%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.25);
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
}

small {
  color: #6b7280;
  font-size: 0.8rem;
  margin-top: 0.25rem;
  display: block;
}

/* Verification Messages */
.verification-message {
  margin-top: 0.5rem;
  padding: 0.75rem;
  border-radius: 8px;
  font-size: 0.875rem;
  display: flex;
  align-items: flex-start;
  gap: 0.5rem;
  line-height: 1.4;
}

.verification-message.loading {
  background: rgba(249, 115, 22, 0.05);
  border: 1px solid rgba(249, 115, 22, 0.2);
  color: #f97316;
}

.verification-message.warning {
  background: rgba(245, 158, 11, 0.1);
  border: 1px solid rgba(245, 158, 11, 0.2);
  color: #d97706;
}

.verification-message.success {
  background: rgba(16, 185, 129, 0.1);
  border: 1px solid rgba(16, 185, 129, 0.2);
  color: #059669;
}

.verification-link {
  color: #f97316;
  font-weight: 600;
  text-decoration: none;
  margin-left: 0.25rem;
}

.verification-link:hover {
  text-decoration: underline;
}

.loading-spinner-small {
  width: 14px;
  height: 14px;
  border: 2px solid transparent;
  border-top: 2px solid currentColor;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  flex-shrink: 0;
  margin-top: 0.1rem;
}

.warning-icon,
.success-icon {
  font-size: 1rem;
  flex-shrink: 0;
  margin-top: 0.1rem;
}

/* Error Message */
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

/* Responsive Design */
@media (max-width: 480px) {
  .modal-content {
    margin: 1rem;
  }

  .modal-body {
    padding: 1rem;
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

