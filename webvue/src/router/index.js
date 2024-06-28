import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '../components/LoginPage.vue';
import RegisterPage from '../components/RegisterPage.vue';
import HomePage from "@/components/HomePage.vue";
import ShowBlogPage from "@/components/ShowBlogPage.vue";
import MyBlogPage from "@/components/MyBlogPage.vue";
import AdvancedSearchPage from "@/components/AdvancedSearchPage.vue";
import EditBlogPage from "@/components/EditBlogPage.vue";
import InfoPage from "@/components/InfoPage.vue";

const routes = [
  { path: '/', redirect: '/login' }, // 根路径重定向到登录
  { path: '/:pathMatch(.*)*', redirect: '/login' }, // 未知路径重定向到登录
  {
    path: '/login',
    name: 'Login',
    component: LoginPage
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterPage
  },
  {
    path: '/editeblog',
    name: 'EditBlog',
    component: EditBlogPage // 将博客页面组件关联到路由
  },
  {
    path: '/home',
    name: 'Home',
    component: HomePage // 将博客页面组件关联到路由
  },
  {
    path: '/show',
    name: 'Show',
    component: ShowBlogPage // 将博客页面组件关联到路由
  },
  {
    path: '/myblog',
    name: 'MyBlog',
    component: MyBlogPage // 将博客页面组件关联到路由
  },
  {
    path: '/info',
    name: 'Info',
    component: InfoPage // 将博客页面组件关联到路由
  },
  {
    path: '/advancedsearch',
    name: 'AdvancedSearch',
    component: AdvancedSearchPage // 将博客页面组件关联到路由
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
