import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layout/Layout.vue'

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: '/front/home',
    children: [
      { path: 'user', name: 'User', component: () => import('@/views/User.vue') },
      { path: 'person', name: 'Person', component: () => import('@/views/Person.vue') },
      { path: 'sayings', name: 'Sayings', component: () => import('@/views/Sayings.vue') },
      { path: 'comment', name: 'Comment', component: () => import('@/views/Comment.vue') },
      { path: 'jubao', name: 'Jubao', component: () => import('@/views/jubao.vue') },
    ]
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('@/layout/Front.vue'),
    children: [
      { path: 'home', name: 'Home', component: () => import('@/views/front/Home.vue') },
      { path: 'detail', name: 'Detail', component: () => import('@/views/front/Detail.vue') },
      { path: 'littleSaying', name: 'LittleSaying', component: () => import('@/views/front/LittleSaying.vue') },
      { path: 'person', name: 'FrontPerson', component: () => import('@/views/Person.vue') },
      { path: 'search', name: 'Search', component: () => import('@/views/front/Search.vue') },
      { path: 'subscribePerson', name: 'SubscribePerson', component: () => import('@/views/front/SubscribePerson.vue') },
      { path: 'collectedContent', name: 'CollectedContent', component: () => import('@/views/front/CollectedContent.vue') },
      { path: 'authorWorks', name: 'AuthorWorks', component: () => import('@/views/front/AuthorWorks.vue') },
    ]
  },
  { path: '/login', name: 'Login', component: () => import('@/views/Login.vue') },
  { path: '/register', name: 'Register', component: () => import('@/views/Register.vue') },
  { path: '/pass', name: 'Pass', component: () => import('@/views/FindPassword.vue') },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
