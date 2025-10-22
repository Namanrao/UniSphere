<template>
  <div class="register-container">
    <!-- Background gradient with glass morphism overlay -->
    <div class="background-gradient"></div>

    <div class="register-content">
      <!-- Decorative elements -->
      <div class="floating-circle circle-1"></div>
      <div class="floating-circle circle-2"></div>
      <div class="floating-circle circle-3"></div>
      <div class="floating-circle circle-4"></div>

      <form
        @submit.prevent="handleRegister"
        class="register-form glass-morphism"
      >
        <!-- Header with logo -->
        <div class="form-header">
          <div class="logo">
            <div class="logo-icon">🎓</div>
            <h1>UniSphere</h1>
          </div>
          <h2>Join Our Academic Community</h2>
          <p class="subtitle">Start your journey with university peers</p>
        </div>

        <!-- Name Row -->
        <div class="form-row">
          <div class="form-group">
            <div class="input-container">
              <input
                id="firstName"
                v-model="signupData.firstName"
                type="text"
                required
                placeholder=" "
                class="form-input"
              />
              <label for="firstName" class="form-label">
                <span class="icon">👤</span>
                First Name
              </label>
            </div>
          </div>

          <div class="form-group">
            <div class="input-container">
              <input
                id="lastName"
                v-model="signupData.lastName"
                type="text"
                required
                placeholder=" "
                class="form-input"
              />
              <label for="lastName" class="form-label">
                <span class="icon">👥</span>
                Last Name
              </label>
            </div>
          </div>
        </div>

        <!-- Username -->
        <div class="form-group">
          <div class="input-container">
            <input
              id="username"
              v-model="signupData.username"
              type="text"
              required
              placeholder=" "
              class="form-input"
            />
            <label for="username" class="form-label">
              <span class="icon">💎</span>
              Username
            </label>
          </div>
        </div>

        <!-- University Email -->
        <div class="form-group">
          <div class="input-container">
            <input
              id="email"
              v-model="signupData.email"
              type="email"
              required
              placeholder=" "
              class="form-input"
            />
            <label for="email" class="form-label">
              <span class="icon">🏫</span>
              University Email
            </label>
          </div>
          <div class="email-hint">
            <span class="hint-icon">🔍</span>
            We'll automatically detect your university from your email domain
          </div>
        </div>

        <!-- Password -->
        <div class="form-group">
          <div class="input-container">
            <input
              id="password"
              v-model="signupData.password"
              type="password"
              required
              placeholder=" "
              class="form-input"
            />
            <label for="password" class="form-label">
              <span class="icon">🔒</span>
              Password
            </label>
          </div>
        </div>

        <!-- Submit Button -->
        <button
          type="submit"
          :disabled="authStore.isLoading"
          class="register-btn orange-gradient"
        >
          <span v-if="!authStore.isLoading" class="btn-content">
            <span class="btn-icon">✨</span>
            Create Academic Account
          </span>
          <span v-else class="btn-content">
            <span class="loading-spinner"></span>
            Creating Account...
          </span>
        </button>

        <!-- Error Message -->
        <div v-if="authStore.error" class="error-message glass-morphism">
          <span class="error-icon">⚠️</span>
          {{ authStore.error }}
        </div>

        <!-- Login Link -->
        <p class="login-link">
          Already part of our community?
          <router-link to="/login" class="link-orange">
            Sign in here
          </router-link>
        </p>

        <!-- University Benefits -->
        <div class="benefits-section glass-morphism">
          <h4>🎯 University Benefits</h4>
          <ul class="benefits-list">
            <li>• Join course-specific communities</li>
            <li>• Connect with peers from your university</li>
            <li>• Share study resources securely</li>
            <li>• Verified academic environment</li>
          </ul>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";

const router = useRouter();
const authStore = useAuthStore();

const signupData = ref({
  firstName: "",
  lastName: "",
  username: "",
  email: "",
  password: "",
});

const handleRegister = async () => {
  try {
    await authStore.register(signupData.value);
    router.push("/login?registered=true");
  } catch (error) {
    // Error handled by store
  }
};
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #fff7ed 0%, #fed7aa 50%, #fdba74 100%);
  padding: 1rem;
}

.background-gradient {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(
      circle at 10% 20%,
      rgba(251, 146, 60, 0.4) 0%,
      transparent 50%
    ),
    radial-gradient(
      circle at 90% 80%,
      rgba(249, 115, 22, 0.3) 0%,
      transparent 50%
    ),
    radial-gradient(
      circle at 60% 30%,
      rgba(253, 186, 116, 0.25) 0%,
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

.register-content {
  position: relative;
  z-index: 2;
  width: 100%;
  max-width: 500px;
}

.floating-circle {
  position: absolute;
  border-radius: 50%;
  background: linear-gradient(45deg, #f97316, #fb923c);
  opacity: 0.1;
  animation: float 6s ease-in-out infinite;
}

.circle-1 {
  width: 100px;
  height: 100px;
  top: 5%;
  left: 5%;
  animation-delay: 0s;
}

.circle-2 {
  width: 70px;
  height: 70px;
  top: 70%;
  right: 10%;
  animation-delay: 1.5s;
}

.circle-3 {
  width: 50px;
  height: 50px;
  bottom: 10%;
  left: 15%;
  animation-delay: 3s;
}

.circle-4 {
  width: 80px;
  height: 80px;
  top: 20%;
  right: 20%;
  animation-delay: 4.5s;
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0px) scale(1);
  }
  50% {
    transform: translateY(-15px) scale(1.05);
  }
}

.register-form {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 20px;
  padding: 2.5rem;
  box-shadow: 0 20px 40px rgba(249, 115, 22, 0.15),
    0 8px 24px rgba(0, 0, 0, 0.1), inset 0 1px 0 rgba(255, 255, 255, 0.6);
  transition: all 0.3s ease;
}

.register-form:hover {
  transform: translateY(-5px);
  box-shadow: 0 25px 50px rgba(249, 115, 22, 0.2),
    0 12px 30px rgba(0, 0, 0, 0.15), inset 0 1px 0 rgba(255, 255, 255, 0.6);
}

.glass-morphism {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.form-header {
  text-align: center;
  margin-bottom: 2rem;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.75rem;
  margin-bottom: 1rem;
}

.logo-icon {
  font-size: 2rem;
}

.logo h1 {
  font-size: 1.8rem;
  font-weight: 700;
  background: linear-gradient(135deg, #f97316, #ea580c);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.form-header h2 {
  font-size: 1.5rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 0.5rem;
}

.subtitle {
  color: #6b7280;
  font-size: 0.9rem;
}

.form-row {
  display: flex;
  gap: 1rem;
  margin-bottom: 0;
}

.form-row .form-group {
  flex: 1;
  margin-bottom: 1.5rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.input-container {
  position: relative;
}

.form-input {
  width: 100%;
  padding: 1rem 1rem 1rem 3rem;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  font-size: 1rem;
  background: rgba(255, 255, 255, 0.8);
  transition: all 0.3s ease;
  outline: none;
}

.form-input:focus {
  border-color: #f97316;
  background: white;
  box-shadow: 0 0 0 3px rgba(249, 115, 22, 0.1);
  transform: translateY(-2px);
}

.form-input:focus + .form-label,
.form-input:not(:placeholder-shown) + .form-label {
  transform: translateY(-180%) scale(0.9);
  color: #f97316;
}

.form-label {
  position: absolute;
  left: 3rem;
  top: 50%;
  transform: translateY(-50%);
  color: #6b7280;
  font-size: 0.9rem;
  pointer-events: none;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.icon {
  font-size: 1rem;
}

.email-hint {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-top: 0.5rem;
  font-size: 0.8rem;
  color: #6b7280;
  background: rgba(249, 115, 22, 0.05);
  padding: 0.5rem 0.75rem;
  border-radius: 8px;
  border-left: 3px solid #f97316;
}

.hint-icon {
  font-size: 0.9rem;
}

.register-btn {
  width: 100%;
  padding: 1rem 2rem;
  border: none;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  margin-top: 0.5rem;
}

.orange-gradient {
  background: linear-gradient(135deg, #f97316, #ea580c);
  color: white;
}

.register-btn:not(:disabled):hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 24px rgba(249, 115, 22, 0.3),
    0 6px 12px rgba(234, 88, 12, 0.2);
}

.register-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
}

.btn-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.btn-icon {
  font-size: 1.2rem;
}

.loading-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid transparent;
  border-top: 2px solid white;
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

.error-message {
  margin-top: 1rem;
  padding: 1rem;
  border-radius: 12px;
  text-align: center;
  font-size: 0.9rem;
  color: #dc2626;
  background: rgba(220, 38, 38, 0.1);
  border: 1px solid rgba(220, 38, 38, 0.2);
}

.error-icon {
  margin-right: 0.5rem;
}

.login-link {
  text-align: center;
  margin-top: 1.5rem;
  color: #6b7280;
  font-size: 0.9rem;
}

.link-orange {
  color: #f97316;
  text-decoration: none;
  font-weight: 600;
  transition: color 0.3s ease;
}

.link-orange:hover {
  color: #ea580c;
  text-decoration: underline;
}

.benefits-section {
  margin-top: 2rem;
  padding: 1.5rem;
  border-radius: 12px;
  background: rgba(249, 115, 22, 0.05);
  border: 1px solid rgba(249, 115, 22, 0.1);
}

.benefits-section h4 {
  color: #f97316;
  margin-bottom: 0.75rem;
  font-size: 0.9rem;
  font-weight: 600;
}

.benefits-list {
  list-style: none;
  padding: 0;
  margin: 0;
  font-size: 0.8rem;
  color: #6b7280;
}

.benefits-list li {
  padding: 0.25rem 0;
  transition: color 0.3s ease;
}

.benefits-list li:hover {
  color: #f97316;
}

/* Responsive design */
@media (max-width: 480px) {
  .register-content {
    padding: 0.5rem;
  }

  .register-form {
    padding: 2rem 1.5rem;
  }

  .form-row {
    flex-direction: column;
    gap: 0;
  }

  .logo h1 {
    font-size: 1.5rem;
  }

  .benefits-section {
    padding: 1rem;
  }
}
</style>
