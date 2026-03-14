/* 就业信息中心 - 导航配置 */

/** 前台信息分类 */
export const INFO_AREAS = [
  { key: '校园招聘', label: '校园招聘', icon: '🏫' },
  { key: '实习信息', label: '实习信息', icon: '💼' },
  { key: '社会招聘', label: '社会招聘', icon: '🏢' },
  { key: '求职技巧', label: '求职技巧', icon: '💡' },
  { key: '行业动态', label: '行业动态', icon: '📊' },
];

/** 前台顶部导航菜单 */
export function getFrontMenuItems(user = {}) {
  const items = [
    { path: '/front/home', label: '首页', icon: 'el-icon-house' }
  ];

  if (user.id && user.role === 2) {
    items.push({
      path: '/front/littleSaying',
      label: '发布信息',
      icon: 'el-icon-edit'
    });
  }

  if (user.id) {
    // 指向"我收藏的内容"（帖子），而非已删除的收藏评论页
    items.push({
      path: '/front/collectedContent',
      label: '我的收藏',
      icon: 'el-icon-star-off'
    });
  }

  return items;
}

/** 用户下拉菜单 */
export function getUserDropdownItems(user = {}) {
  const items = [];

  if (user.role === 1) {
    items.push({
      path: '/sayings',
      label: '后台管理',
      icon: 'el-icon-setting'
    });
  }

  if (user.role === 2) {
    items.push({
      path: '/front/littleSaying',
      label: '发布信息',
      icon: 'el-icon-edit'
    });
  }

  return items.concat([
    { path: '/front/person',           label: '个人信息',     icon: 'el-icon-user' },
    { path: '/front/collectedContent', label: '我的收藏',     icon: 'el-icon-collection' },
    { path: '/front/subscribePerson',  label: '我关注的人',   icon: 'el-icon-coordinate' },
  ]);
}

/** 后台管理侧边栏 */
export const ADMIN_SIDEBAR_ITEMS = [
  { path: '/user',    label: '用户管理', icon: 'el-icon-user-solid',      requireRole: 1, group: '系统管理' },
  { path: '/sayings', label: '信息管理', icon: 'el-icon-s-order' },
  { path: '/jubao',   label: '投诉管理', icon: 'el-icon-warning-outline' },
  { path: '/comment', label: '评论管理', icon: 'el-icon-chat-dot-round' },
  { path: '/notice',  label: '公告管理', icon: 'el-icon-bell' },
];
