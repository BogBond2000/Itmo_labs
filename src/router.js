import { createRouter, createWebHistory } from 'vue-router';
import MainPage from '@/views/Main.vue';
import HomePage from './views/Home.vue';
import Registration from "@/views/Registration.vue";
import Login from "@/views/Login.vue"

const routes = [
    {
        path: '/',
        name: 'app',
        component:  MainPage
    },
    {
        path: '/home',
        name: 'Home',
        component: HomePage
    },
    {
        path: "/reg",
        name: "Reg",
        component: Registration
    },
    {
        path: "/log",
        name: "Log",
        component: Login
    }
];


const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;