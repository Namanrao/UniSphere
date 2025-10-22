import { createApp } from "vue";
import { createPinia } from "pinia";
import "./style.css";
import App from "./App.vue";
import router from "./router"; // ← IMPORT FROM ROUTER FOLDER

// Create app instance
const app = createApp(App);

// Use plugins
app.use(createPinia());
app.use(router); // ← USE THE IMPORTED ROUTER

// Mount the app
app.mount("#app");
