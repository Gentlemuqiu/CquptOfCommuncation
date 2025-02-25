import {createRouter, createWebHashHistory, createWebHistory} from 'vue-router'
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
        path: 'longComment',
        name: 'LongComment',
        component: () => import("@/views/front/LongComment"),
      },
      {
        path: 'search',
        name: 'Search',
        component: () => import("@/views/front/Search"),
      },
      {
        path: 'collect',
        name: 'Collect',
        component: () => import("@/views/front/Collect"),
      },
      {
        path: 'subscribePerson',
        name: 'SubscribePerson',
        component: () => import("@/views/front/SubscribePerson.vue"),
      },
      {
        path: 'longCommentTable',
        name: 'LongCommentTable',
        component: () => import("@/views/front/LongCommentTable"),
      },
      {
        path: 'collectedContent',
        name: 'CollectedContent',
        component: () => import("@/views/front/CollectedContent.vue"),
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

// router.beforeEach((to, from, next) => {
//   console.log(to.path)
//   if (!to.path.includes('front') && !(to.path === '/login') && !(to.path === '/register')) {
//     let user = sessionStorage.getItem("user")
//     if (!user) {
//       router.replace("/login")
//     } else {
//       next()
//     }
//   }
// })

export default router
