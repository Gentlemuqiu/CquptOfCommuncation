import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../layout/Layout.vue'

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: "/front/home",
    children: [
      {
        path: 'user',
        name: 'User',
        component: () => import("@/views/User"),
      },
      {
        path: 'person',
        name: 'Person',
        component: () => import("@/views/Person"),
      },
      {
        path: 'sayings',
        name: 'Sayings',
        component: () => import("@/views/Sayings.vue"),
      },
      {
        path: 'comment',
        name: 'Comment',
        component: () => import("@/views/Comment.vue"),
      },
      {
        path: 'jubao',
        name: 'Jubao',
        component: () => import("@/views/jubao"),
      },
      {
        path: 'notice',
        name: 'Notice',
        component: () => import("@/views/notice"),
      },
    ]
  },
  {
    path: "/front",
    name: "Front",
    component: () => import("@/layout/Front"),
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import("@/views/front/Home"),
      },
      {
        path: 'detail',
        name: 'Detail',
        component: () => import("@/views/front/Detail"),
      },
      {
        path: 'littleSaying',
        name: 'LittleSaying',
        component: () => import("@/views/front/LittleSaying.vue"),
      },
      {
        path: 'person',
        name: 'FrontPerson',
        component: () => import("@/views/Person"),
      },
      {
        path: 'search',
        name: 'Search',
        component: () => import("@/views/front/Search"),
      },
      {
        path: 'subscribePerson',
        name: 'SubscribePerson',
        component: () => import("@/views/front/SubscribePerson.vue"),
      },
      {
        path: 'collectedContent',
        name: 'CollectedContent',
        component: () => import("@/views/front/CollectedContent.vue"),
      },
      {
        path: 'authorWorks',
        name: 'AuthorWorks',
        component: () => import("@/views/front/AuthorWorks.vue"),
      },
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import("@/views/Login")
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import("@/views/Register")
  },
  {
    path: '/pass',
    name: 'Pass',
    component: () => import("@/views/FindPassword.vue")
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
