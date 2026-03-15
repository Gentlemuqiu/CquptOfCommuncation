/* 就业信息中心 - 导航配置 */
import {
  House, Edit, Star, Setting, User, Coordinate
} from '@element-plus/icons-vue';

/** 前台信息分类 */
export const INFO_AREAS = [
  { key: '校园招聘', label: '校园招聘', icon: '🏫' },
  { key: '实习信息', label: '实习信息', icon: '💼' },
  { key: '社会招聘', label: '社会招聘', icon: '🏢' },
  { key: '求职技巧', label: '求职技巧', icon: '💡' },
  { key: '行业动态', label: '行业动态', icon: '📊' },
];

/** 前台顶部导航菜单（icon 为 Element Plus 图标组件） */
export function getFrontMenuItems(user = {}) {
  const items = [
    { path: '/front/home', label: '首页', icon: House }
  ];

  if (user.id) {
    items.push({ path: '/front/littleSaying', label: '发布信息', icon: Edit });
    items.push({ path: '/front/collectedContent', label: '我的收藏', icon: Star });
  }

  return items;
}

/** 用户下拉菜单（icon 为 Element Plus 图标组件） */
export function getUserDropdownItems(user = {}) {
  const items = [];

  if (user.role === 1) {
    items.push({ path: '/sayings', label: '后台管理', icon: Setting });
  }

  return items.concat([
    { path: '/front/person',          label: '个人信息',   icon: User },
    { path: '/front/subscribePerson', label: '我关注的人', icon: Coordinate },
  ]);
}

/** 后台管理侧边栏（icon 为字符串，由 Aside.vue 中的 ICON_MAP 映射为组件） */
export const ADMIN_SIDEBAR_ITEMS = [
  { path: '/user',    label: '用户管理', icon: 'UserFilled',   requireRole: 1, group: '系统管理' },
  { path: '/sayings', label: '信息管理', icon: 'Document' },
  { path: '/jubao',   label: '投诉管理', icon: 'Warning' },
  { path: '/comment', label: '评论管理', icon: 'ChatDotRound' },
];
