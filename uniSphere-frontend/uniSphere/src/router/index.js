import { createRouter, createWebHistory } from "vue-router";
import { useAuthStore } from "@/stores/auth";

const routes = [
  {
    path: "/",
    name: "Home",
    component: () => import("@/views/Home.vue"),
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("@/components/auth/Login.vue"),
    meta: { requiresGuest: true },
  },
  {
    path: "/register",
    name: "Register",
    component: () => import("@/components/auth/Register.vue"),
    meta: { requiresGuest: true },
  },
  {
    path: "/dashboard",
    name: "DashboardLayout",
    component: () => import("@/components/layout/AppLayout.vue"),
    meta: { requiresAuth: true },
    redirect: "/dashboard/home",
    children: [
      {
        path: "home",
        name: "Dashboard",
        component: () => import("@/views/Dashboard.vue"),
      },
      {
        path: "profile",
        name: "Profile",
        component: () => import("@/views/Profile.vue"),
      },
      {
        path: "my-posts",
        name: "MyPosts",
        component: () => import("@/views/MyPosts.vue"),
      },
      {
        path: "communities",
        name: "Communities",
        component: () => import("@/views/Communities.vue"),
      },
      {
        path: "universities",
        name: "Universities",
        component: () => import("@/views/Universities.vue"),
      },
    ],
  },
  {
    path: "/:pathMatch(.*)*",
    name: "NotFound",
    component: () => import("@/views/NotFound.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();

  // Check if the route requires authentication
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next("/login");
  }
  // Check if the route requires guest (non-authenticated) status
  else if (to.meta.requiresGuest && authStore.isAuthenticated) {
    next("/dashboard/home");
  }
  // Allow navigation for all other cases
  else {
    next();
  }
});

export default router;
