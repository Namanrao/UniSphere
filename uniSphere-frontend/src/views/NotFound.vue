<template>
  <div class="not-found-page">
    <!-- Background gradient with floating elements -->
    <div class="background-gradient"></div>
    <div class="floating-element element-1">🎓</div>
    <div class="floating-element element-2">📚</div>
    <div class="floating-element element-3">🔥</div>
    <div class="floating-element element-4">🚀</div>

    <div class="not-found-container glass-morphism">
      <!-- Main Error Content -->
      <div class="error-content">
        <div class="error-visual">
          <div class="error-orb">
            <span class="error-icon">🔍</span>
          </div>
          <div class="error-code">404</div>
        </div>

        <div class="error-text">
          <h1 class="error-title">Page Not Found</h1>
          <p class="error-message">
            Oops! The page you're looking for seems to have taken a coffee break
            ☕. It might have been moved, deleted, or you entered a URL that
            doesn't exist in our academic universe.
          </p>
        </div>

        <!-- Action Buttons -->
        <div class="error-actions">
          <router-link
            to="/dashboard/home"
            class="action-btn primary orange-gradient"
          >
            <span class="btn-icon">🏠</span>
            Back to Dashboard
          </router-link>
          <button @click="goBack" class="action-btn secondary">
            <span class="btn-icon">↩️</span>
            Go Back
          </button>
          <router-link to="/dashboard/communities" class="action-btn outline">
            <span class="btn-icon">🏫</span>
            Explore Communities
          </router-link>
        </div>

        <!-- Help Section -->
        <div class="error-help">
          <div class="help-icon">💡</div>
          <div class="help-content">
            <h4>Lost in the academic sphere?</h4>
            <p>
              If you believe this is an error or need assistance, don't hesitate
              to reach out to our support team.
            </p>
            <div class="help-actions">
              <button @click="contactSupport" class="help-btn">
                <span class="btn-icon">💬</span>
                Contact Support
              </button>
            </div>
          </div>
        </div>

        <!-- Quick Links -->
        <div class="quick-links">
          <h5>Quick Navigation</h5>
          <div class="links-grid">
            <router-link to="/dashboard/home" class="quick-link">
              <span class="link-icon">📊</span>
              Dashboard
            </router-link>
            <router-link to="/dashboard/communities" class="quick-link">
              <span class="link-icon">🏫</span>
              Communities
            </router-link>
            <router-link to="/dashboard/my-posts" class="quick-link">
              <span class="link-icon">📝</span>
              My Posts
            </router-link>
            <router-link to="/dashboard/profile" class="quick-link">
              <span class="link-icon">👤</span>
              Profile
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";

const router = useRouter();

const goBack = () => {
  if (window.history.length > 1) {
    router.go(-1);
  } else {
    router.push("/dashboard/home");
  }
};

const contactSupport = () => {
  // Implement contact support logic
  console.log("Contact support clicked");
  // You could open a support email or redirect to a contact page
};
</script>

<style scoped>
.not-found-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #fff7ed 0%, #fed7aa 50%, #fdba74 100%);
  padding: 2rem;
  position: relative;
  overflow: hidden;
}

.background-gradient {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(
      circle at 20% 80%,
      rgba(251, 146, 60, 0.4) 0%,
      transparent 50%
    ),
    radial-gradient(
      circle at 80% 20%,
      rgba(249, 115, 22, 0.3) 0%,
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
    opacity: 0.7;
  }
}

.floating-element {
  position: absolute;
  font-size: 2rem;
  opacity: 0.1;
  animation: float 6s ease-in-out infinite;
  z-index: 1;
}

.element-1 {
  top: 20%;
  left: 10%;
  animation-delay: 0s;
}

.element-2 {
  top: 60%;
  right: 15%;
  animation-delay: 1.5s;
}

.element-3 {
  bottom: 20%;
  left: 20%;
  animation-delay: 3s;
}

.element-4 {
  top: 30%;
  right: 25%;
  animation-delay: 4.5s;
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(10deg);
  }
}

.not-found-container {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 3rem;
  box-shadow: 0 25px 50px rgba(249, 115, 22, 0.2),
    0 10px 30px rgba(0, 0, 0, 0.1), inset 0 1px 0 rgba(255, 255, 255, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.3);
  max-width: 600px;
  width: 100%;
  position: relative;
  z-index: 2;
  text-align: center;
}

.error-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2rem;
}

.error-visual {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1.5rem;
  margin-bottom: 1rem;
}

.error-orb {
  width: 100px;
  height: 100px;
  background: linear-gradient(135deg, #f97316, #ea580c);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 15px 35px rgba(249, 115, 22, 0.3),
    inset 0 4px 0 rgba(255, 255, 255, 0.2);
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%,
  100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
}

.error-icon {
  font-size: 2.5rem;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
}

.error-code {
  font-size: 4rem;
  font-weight: 800;
  background: linear-gradient(135deg, #f97316, #ea580c);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  line-height: 1;
  text-shadow: 0 4px 8px rgba(249, 115, 22, 0.2);
}

.error-text {
  text-align: center;
}

.error-title {
  font-size: 2.25rem;
  font-weight: 700;
  color: #1f2937;
  margin-bottom: 1rem;
  line-height: 1.2;
}

.error-message {
  font-size: 1.1rem;
  color: #6b7280;
  line-height: 1.6;
  margin-bottom: 0;
}

.error-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;
  margin: 1rem 0;
}

.action-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.875rem 1.5rem;
  border: none;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: 600;
  text-decoration: none;
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: center;
  min-width: 140px;
  justify-content: center;
}

.action-btn.primary {
  color: white;
}

.action-btn.primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(249, 115, 22, 0.4);
}

.action-btn.secondary {
  background: rgba(255, 255, 255, 0.9);
  color: #374151;
  border: 2px solid #e5e7eb;
  backdrop-filter: blur(10px);
}

.action-btn.secondary:hover {
  background: white;
  border-color: #f97316;
  color: #f97316;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.action-btn.outline {
  background: transparent;
  color: #f97316;
  border: 2px solid #f97316;
}

.action-btn.outline:hover {
  background: #f97316;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(249, 115, 22, 0.3);
}

.btn-icon {
  font-size: 1.1rem;
}

.error-help {
  background: rgba(249, 115, 22, 0.05);
  border: 1px solid rgba(249, 115, 22, 0.1);
  border-radius: 16px;
  padding: 1.5rem;
  margin: 1.5rem 0;
  display: flex;
  align-items: flex-start;
  gap: 1rem;
  text-align: left;
  max-width: 500px;
}

.help-icon {
  font-size: 1.5rem;
  flex-shrink: 0;
  margin-top: 0.25rem;
}

.help-content h4 {
  color: #1f2937;
  margin-bottom: 0.5rem;
  font-size: 1.1rem;
  font-weight: 600;
}

.help-content p {
  color: #6b7280;
  margin-bottom: 1rem;
  line-height: 1.5;
}

.help-actions {
  display: flex;
  gap: 0.75rem;
  flex-wrap: wrap;
}

.help-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  color: #374151;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  text-decoration: none;
}

.help-btn:hover {
  background: white;
  border-color: #f97316;
  color: #f97316;
  transform: translateY(-1px);
}

.quick-links {
  margin-top: 1.5rem;
  padding-top: 1.5rem;
  border-top: 1px solid rgba(255, 255, 255, 0.3);
}

.quick-links h5 {
  color: #6b7280;
  font-size: 0.875rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-bottom: 1rem;
}

.links-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 0.75rem;
  max-width: 300px;
  margin: 0 auto;
}

.quick-link {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1rem;
  background: rgba(255, 255, 255, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 10px;
  color: #6b7280;
  font-size: 0.875rem;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.quick-link:hover {
  background: white;
  color: #f97316;
  border-color: #f97316;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(249, 115, 22, 0.15);
}

.link-icon {
  font-size: 1rem;
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
@media (max-width: 768px) {
  .not-found-page {
    padding: 1rem;
  }

  .not-found-container {
    padding: 2rem 1.5rem;
  }

  .error-visual {
    flex-direction: column;
    gap: 1rem;
  }

  .error-code {
    font-size: 3rem;
  }

  .error-title {
    font-size: 1.75rem;
  }

  .error-message {
    font-size: 1rem;
  }

  .error-actions {
    flex-direction: column;
    align-items: center;
  }

  .action-btn {
    width: 100%;
    max-width: 250px;
  }

  .error-help {
    flex-direction: column;
    text-align: center;
  }

  .help-icon {
    align-self: center;
  }

  .links-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .not-found-container {
    padding: 1.5rem 1rem;
  }

  .error-orb {
    width: 80px;
    height: 80px;
  }

  .error-icon {
    font-size: 2rem;
  }

  .error-code {
    font-size: 2.5rem;
  }

  .error-title {
    font-size: 1.5rem;
  }

  .floating-element {
    font-size: 1.5rem;
  }
}

/* Reduced motion for accessibility */
@media (prefers-reduced-motion: reduce) {
  .background-gradient,
  .floating-element,
  .error-orb,
  .action-btn,
  .help-btn,
  .quick-link {
    animation: none;
    transition: none;
  }
}
</style>
