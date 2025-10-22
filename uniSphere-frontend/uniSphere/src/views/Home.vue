<template>
  <div class="home">
    <!-- Hero Section -->
    <header class="hero-section">
      <div class="hero-background">
        <div class="hero-shapes">
          <div class="shape shape-1"></div>
          <div class="shape shape-2"></div>
          <div class="shape shape-3"></div>
        </div>
      </div>
      <div class="container">
        <div class="hero-content">
          <div class="hero-badge">
            <span>🎓 University Exclusive</span>
          </div>
          <h1 class="hero-title">
            Welcome to <span class="gradient-text">UniSphere</span>
          </h1>
          <p class="hero-subtitle">
            The ultimate platform where university students connect, share
            knowledge, and build communities around their academic journey.
          </p>
          <div class="hero-actions">
            <router-link to="/register" class="btn btn-primary">
              <span>Get Started Free</span>
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                <path
                  d="M5 12H19M19 12L12 5M19 12L12 19"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                />
              </svg>
            </router-link>
            <router-link to="/login" class="btn btn-secondary">
              Sign In
            </router-link>
          </div>
          <div class="hero-stats">
            <div class="stat">
              <div class="stat-number">10K+</div>
              <div class="stat-label">Students</div>
            </div>
            <div class="stat">
              <div class="stat-number">500+</div>
              <div class="stat-label">Communities</div>
            </div>
            <div class="stat">
              <div class="stat-number">50+</div>
              <div class="stat-label">Universities</div>
            </div>
          </div>
        </div>
        <div class="hero-visual">
          <div class="visual-container">
            <div class="floating-card card-1">
              <div class="card-header">
                <div class="card-avatar"></div>
                <div class="card-info">
                  <div class="card-user">Computer Science</div>
                  <div class="card-time">2h ago</div>
                </div>
              </div>
              <div class="card-content">
                <h4>Study Group for CS101 Midterms?</h4>
                <p>Anyone interested in forming a study group this weekend?</p>
              </div>
              <div class="card-actions">
                <span>💬 24</span>
                <span>🔺 15</span>
              </div>
            </div>
            <div class="floating-card card-2">
              <div class="card-header">
                <div class="card-avatar"></div>
                <div class="card-info">
                  <div class="card-user">Campus Life</div>
                  <div class="card-time">5h ago</div>
                </div>
              </div>
              <div class="card-content">
                <h4>Best Coffee Spots on Campus</h4>
                <p>Share your favorite study cafes and hidden gems!</p>
              </div>
              <div class="card-actions">
                <span>💬 42</span>
                <span>🔺 28</span>
              </div>
            </div>
            <div class="floating-card card-3">
              <div class="card-header">
                <div class="card-avatar"></div>
                <div class="card-info">
                  <div class="card-user">Mathematics</div>
                  <div class="card-time">1d ago</div>
                </div>
              </div>
              <div class="card-content">
                <h4>Calculus Help Needed</h4>
                <p>Struggling with integration techniques. Any tips?</p>
              </div>
              <div class="card-actions">
                <span>💬 18</span>
                <span>🔺 12</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </header>

    <!-- Features Section -->
    <section class="features-section">
      <div class="container">
        <div class="section-header">
          <h2>Why Students Love UniSphere</h2>
          <p>
            Designed specifically for university life and academic collaboration
          </p>
        </div>
        <div class="features-grid">
          <div class="feature-card">
            <div class="feature-icon-wrapper">
              <div class="feature-icon">🏫</div>
            </div>
            <h3>University Verified</h3>
            <p>
              Automatic university detection through your email domain ensures
              you connect with real students from your campus.
            </p>
          </div>
          <div class="feature-card">
            <div class="feature-icon-wrapper">
              <div class="feature-icon">💬</div>
            </div>
            <h3>Course Communities</h3>
            <p>
              Join dedicated communities for each course, share notes, ask
              questions, and form study groups effortlessly.
            </p>
          </div>
          <div class="feature-card">
            <div class="feature-icon-wrapper">
              <div class="feature-icon">📚</div>
            </div>
            <h3>Knowledge Sharing</h3>
            <p>
              Access a wealth of academic resources, from lecture notes to
              project ideas, shared by your peers.
            </p>
          </div>
          <div class="feature-card">
            <div class="feature-icon-wrapper">
              <div class="feature-icon">🔒</div>
            </div>
            <h3>Safe & Private</h3>
            <p>
              University-only access creates a trusted environment for open
              discussion and collaboration.
            </p>
          </div>
          <div class="feature-card">
            <div class="feature-icon-wrapper">
              <div class="feature-icon">🚀</div>
            </div>
            <h3>Real-time Updates</h3>
            <p>
              Stay updated with campus events, deadline reminders, and important
              announcements from your university.
            </p>
          </div>
          <div class="feature-card">
            <div class="feature-icon-wrapper">
              <div class="feature-icon">🤝</div>
            </div>
            <h3>Networking</h3>
            <p>
              Connect with students across different years and majors to expand
              your academic and professional network.
            </p>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section class="cta-section">
      <div class="container">
        <div class="cta-content">
          <h2>Ready to Transform Your University Experience?</h2>
          <p>
            Join thousands of students already building their academic
            communities on UniSphere
          </p>
          <div class="cta-actions">
            <router-link to="/register" class="btn btn-cta-primary">
              Start Your Journey
            </router-link>
            <router-link to="/login" class="btn btn-cta-secondary">
              I Have an Account
            </router-link>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
// Home page logic can be added here
</script>

<style scoped>
.home {
  min-height: 100vh;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1.5rem;
}

/* Hero Section */
.hero-section {
  position: relative;
  background: linear-gradient(135deg, #fff7ed 0%, #fed7aa 50%, #fdba74 100%);
  min-height: 100vh;
  display: flex;
  align-items: center;
  overflow: hidden;
}

.hero-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
}

.hero-shapes .shape {
  position: absolute;
  border-radius: 50%;
  background: linear-gradient(45deg, #fb923c, #f97316);
  opacity: 0.1;
}

.shape-1 {
  width: 300px;
  height: 300px;
  top: -150px;
  right: -100px;
}

.shape-2 {
  width: 200px;
  height: 200px;
  bottom: 100px;
  left: -50px;
}

.shape-3 {
  width: 150px;
  height: 150px;
  top: 50%;
  right: 20%;
}

.hero-section .container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 4rem;
  align-items: center;
  position: relative;
  z-index: 2;
}

.hero-badge {
  display: inline-flex;
  background: rgba(251, 146, 60, 0.1);
  border: 1px solid rgba(251, 146, 60, 0.3);
  color: #c2410c;
  padding: 0.5rem 1rem;
  border-radius: 50px;
  font-size: 0.875rem;
  font-weight: 600;
  margin-bottom: 1.5rem;
  backdrop-filter: blur(10px);
}

.hero-title {
  font-size: 3.5rem;
  font-weight: 800;
  line-height: 1.1;
  margin-bottom: 1.5rem;
  color: #1f2937;
}

.gradient-text {
  background: linear-gradient(135deg, #f97316, #ea580c);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-subtitle {
  font-size: 1.25rem;
  line-height: 1.7;
  color: #6b7280;
  margin-bottom: 2.5rem;
  max-width: 500px;
}

.hero-actions {
  display: flex;
  gap: 1rem;
  margin-bottom: 3rem;
  flex-wrap: wrap;
}

.btn {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 1rem 2rem;
  border-radius: 12px;
  text-decoration: none;
  font-weight: 600;
  font-size: 1rem;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 2px solid transparent;
}

.btn-primary {
  background: linear-gradient(135deg, #f97316, #ea580c);
  color: white;
  box-shadow: 0 4px 15px rgba(249, 115, 22, 0.3);
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(249, 115, 22, 0.4);
}

.btn-secondary {
  background: white;
  color: #f97316;
  border-color: #f97316;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.btn-secondary:hover {
  background: #fff7ed;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.hero-stats {
  display: flex;
  gap: 2rem;
}

.stat {
  text-align: center;
}

.stat-number {
  font-size: 1.5rem;
  font-weight: 700;
  color: #f97316;
  margin-bottom: 0.25rem;
}

.stat-label {
  font-size: 0.875rem;
  color: #6b7280;
  font-weight: 500;
}

/* Hero Visual */
.hero-visual {
  position: relative;
  height: 500px;
}

.visual-container {
  position: relative;
  width: 100%;
  height: 100%;
}

.floating-card {
  position: absolute;
  background: white;
  border-radius: 16px;
  padding: 1.5rem;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  border: 1px solid #f3f4f6;
  backdrop-filter: blur(10px);
  transition: transform 0.3s ease;
}

.floating-card:hover {
  transform: translateY(-5px);
}

.card-1 {
  top: 10%;
  left: 10%;
  width: 280px;
  z-index: 3;
  border-left: 4px solid #f97316;
}

.card-2 {
  top: 30%;
  right: 5%;
  width: 260px;
  z-index: 2;
  border-left: 4px solid #fb923c;
}

.card-3 {
  bottom: 15%;
  left: 20%;
  width: 250px;
  z-index: 1;
  border-left: 4px solid #fdba74;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1rem;
}

.card-avatar {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: linear-gradient(135deg, #fed7aa, #fdba74);
}

.card-info {
  flex: 1;
}

.card-user {
  font-weight: 600;
  color: #1f2937;
  font-size: 0.875rem;
}

.card-time {
  font-size: 0.75rem;
  color: #9ca3af;
}

.card-content h4 {
  font-size: 1rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 0.5rem;
  line-height: 1.4;
}

.card-content p {
  font-size: 0.875rem;
  color: #6b7280;
  line-height: 1.5;
  margin-bottom: 1rem;
}

.card-actions {
  display: flex;
  gap: 1rem;
  font-size: 0.75rem;
  color: #6b7280;
}

/* Features Section */
.features-section {
  padding: 6rem 0;
  background: #f8f9fa;
}

.section-header {
  text-align: center;
  margin-bottom: 4rem;
}

.section-header h2 {
  font-size: 2.5rem;
  font-weight: 700;
  color: #1f2937;
  margin-bottom: 1rem;
}

.section-header p {
  font-size: 1.125rem;
  color: #6b7280;
  max-width: 600px;
  margin: 0 auto;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 2rem;
}

.feature-card {
  background: white;
  padding: 2.5rem 2rem;
  border-radius: 20px;
  text-align: center;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid #f3f4f6;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.feature-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(135deg, #f97316, #fb923c);
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.12);
}

.feature-icon-wrapper {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #fff7ed, #fed7aa);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 1.5rem;
}

.feature-icon {
  font-size: 2rem;
}

.feature-card h3 {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 1rem;
}

.feature-card p {
  color: #6b7280;
  line-height: 1.6;
  font-size: 0.95rem;
}

/* CTA Section */
.cta-section {
  padding: 6rem 0;
  background: linear-gradient(135deg, #1f2937 0%, #374151 100%);
  color: white;
}

.cta-content {
  text-align: center;
  max-width: 600px;
  margin: 0 auto;
}

.cta-content h2 {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 1rem;
  line-height: 1.2;
}

.cta-content p {
  font-size: 1.125rem;
  color: #d1d5db;
  margin-bottom: 2.5rem;
  line-height: 1.6;
}

.cta-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;
}

.btn-cta-primary {
  background: linear-gradient(135deg, #f97316, #ea580c);
  color: white;
  padding: 1rem 2.5rem;
  border-radius: 12px;
  text-decoration: none;
  font-weight: 600;
  font-size: 1.1rem;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(249, 115, 22, 0.3);
}

.btn-cta-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(249, 115, 22, 0.4);
}

.btn-cta-secondary {
  background: transparent;
  color: white;
  border: 2px solid #4b5563;
  padding: 1rem 2.5rem;
  border-radius: 12px;
  text-decoration: none;
  font-weight: 600;
  font-size: 1.1rem;
  transition: all 0.3s ease;
}

.btn-cta-secondary:hover {
  background: #374151;
  border-color: #6b7280;
  transform: translateY(-2px);
}

/* Responsive Design */
@media (max-width: 1024px) {
  .hero-section .container {
    gap: 3rem;
  }

  .hero-title {
    font-size: 3rem;
  }
}

@media (max-width: 768px) {
  .hero-section .container {
    grid-template-columns: 1fr;
    text-align: center;
    gap: 3rem;
  }

  .hero-title {
    font-size: 2.5rem;
  }

  .hero-subtitle {
    font-size: 1.125rem;
    margin: 0 auto 2.5rem;
  }

  .hero-actions {
    justify-content: center;
  }

  .hero-stats {
    justify-content: center;
  }

  .hero-visual {
    height: 400px;
  }

  .floating-card {
    position: relative;
    margin: 1rem auto;
    width: 100%;
    max-width: 300px;
  }

  .card-1,
  .card-2,
  .card-3 {
    position: static;
    margin-bottom: 1.5rem;
  }

  .features-grid {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }

  .section-header h2 {
    font-size: 2rem;
  }

  .cta-content h2 {
    font-size: 2rem;
  }

  .cta-actions {
    flex-direction: column;
    align-items: center;
  }

  .btn-cta-primary,
  .btn-cta-secondary {
    width: 100%;
    max-width: 300px;
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .container {
    padding: 0 1rem;
  }

  .hero-title {
    font-size: 2rem;
  }

  .hero-actions {
    flex-direction: column;
    align-items: center;
  }

  .btn {
    width: 100%;
    max-width: 280px;
    justify-content: center;
  }

  .hero-stats {
    flex-direction: column;
    gap: 1rem;
  }

  .features-grid {
    grid-template-columns: 1fr;
  }

  .feature-card {
    padding: 2rem 1.5rem;
  }
}
</style>
