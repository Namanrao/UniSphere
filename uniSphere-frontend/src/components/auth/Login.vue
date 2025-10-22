<template>
  <div class="login-container">
    <!-- Background gradient with glass morphism overlay -->
    <div class="background-gradient"></div>

    <div class="login-content">
      <!-- Decorative elements -->
      <div class="floating-circle circle-1"></div>
      <div class="floating-circle circle-2"></div>
      <div class="floating-circle circle-3"></div>

      <form @submit.prevent="handleLogin" class="login-form glass-morphism">
        <!-- Header with logo -->
        <div class="form-header">
          <div class="logo">
            <div class="logo-icon">🎓</div>
            <h1>UniSphere</h1>
          </div>
          <h2>Welcome Back!</h2>
          <p class="subtitle">Continue your academic journey</p>
        </div>

        <div class="form-group">
          <div class="input-container">
            <input
              id="username"
              v-model="loginData.username"
              type="text"
              required
              placeholder=" "
              class="form-input"
            />
            <label for="username" class="form-label">
              <span class="icon">👤</span>
              Username
            </label>
          </div>
        </div>

        <div class="form-group">
          <div class="input-container">
            <input
              id="password"
              v-model="loginData.password"
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

        <button
          type="submit"
          :disabled="authStore.isLoading"
          class="login-btn orange-gradient"
        >
          <span v-if="!authStore.isLoading" class="btn-content">
            <span class="btn-icon">🚀</span>
            Login to UniSphere
          </span>
          <span v-else class="btn-content">
            <span class="loading-spinner"></span>
            Logging in...
          </span>
        </button>

        <div v-if="authStore.error" class="error-message glass-morphism">
          <span class="error-icon">⚠️</span>
          {{ authStore.error }}
        </div>

        <p class="signup-link">
          New to UniSphere?
          <router-link to="/register" class="link-orange">
            Create an account
          </router-link>
        </p>
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

const loginData = ref({
  username: "",
  password: "",
});

const handleLogin = async () => {
  try {
    await authStore.login(loginData.value);
    router.push("/dashboard");
  } catch (error) {
    // Error handled by store
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #fff7ed 0%, #fed7aa 50%, #fdba74 100%);
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

.login-content {
  position: relative;
  z-index: 2;
  width: 100%;
  max-width: 440px;
  padding: 2rem;
}

.floating-circle {
  position: absolute;
  border-radius: 50%;
  background: linear-gradient(45deg, #f97316, #fb923c);
  opacity: 0.1;
  animation: float 6s ease-in-out infinite;
}

.circle-1 {
  width: 120px;
  height: 120px;
  top: 10%;
  left: 10%;
  animation-delay: 0s;
}

.circle-2 {
  width: 80px;
  height: 80px;
  top: 60%;
  right: 15%;
  animation-delay: 2s;
}

.circle-3 {
  width: 60px;
  height: 60px;
  bottom: 20%;
  left: 20%;
  animation-delay: 4s;
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0px) scale(1);
  }
  50% {
    transform: translateY(-20px) scale(1.05);
  }
}

.login-form {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 20px;
  padding: 2.5rem;
  box-shadow: 0 20px 40px rgba(249, 115, 22, 0.15),
    0 8px 24px rgba(0, 0, 0, 0.1), inset 0 1px 0 rgba(255, 255, 255, 0.6);
  transition: all 0.3s ease;
}

.login-form:hover {
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

.login-btn {
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
}

.orange-gradient {
  background: linear-gradient(135deg, #f97316, #ea580c);
  color: white;
}

.login-btn:not(:disabled):hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 24px rgba(249, 115, 22, 0.3),
    0 6px 12px rgba(234, 88, 12, 0.2);
}

.login-btn:disabled {
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

.signup-link {
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

/* Responsive design */
@media (max-width: 480px) {
  .login-content {
    padding: 1rem;
  }

  .login-form {
    padding: 2rem 1.5rem;
  }

  .logo h1 {
    font-size: 1.5rem;
  }
}
</style>
