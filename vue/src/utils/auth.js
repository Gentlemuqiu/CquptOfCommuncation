/**
 * 用户登录态工具：统一从 sessionStorage 读写当前用户，避免各组件重复解析
 */

const STORAGE_KEY = 'user';

export function getCurrentUser() {
  try {
    const raw = sessionStorage.getItem(STORAGE_KEY) || '{}';
    const u = JSON.parse(raw);
    return u && u.id != null ? u : {};
  } catch {
    return {};
  }
}

export function setUser(user) {
  if (user && user.id != null) {
    sessionStorage.setItem(STORAGE_KEY, JSON.stringify(user));
    window.dispatchEvent(new Event('userUpdate'));
  }
}

export function clearUser() {
  sessionStorage.removeItem(STORAGE_KEY);
  window.dispatchEvent(new Event('userUpdate'));
}

export function isLoggedIn() {
  const u = getCurrentUser();
  return !!(u && u.id != null);
}
