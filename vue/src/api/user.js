/**
 * 用户相关接口
 */
import request from './request';

const BASE = '/user';

export function login(data) {
  return request.post(`${BASE}/login`, data);
}

export function register(data) {
  return request.post(`${BASE}/register`, data);
}

export function getUserById(id) {
  return request.get(`${BASE}/${id}`);
}

export function getUserPage(params) {
  return request.get(`${BASE}/page`, { params });
}

export function updateUser(data) {
  return request.put(BASE, data);
}

export function updatePassword(data) {
  return request.put(`${BASE}/password`, data);
}

export function deleteUser(id) {
  return request.delete(`${BASE}/${id}`);
}

export function resetPassVerify(data) {
  return request.post(`${BASE}/resetPassVerify`, data);
}

export function resetPass(data) {
  return request.post(`${BASE}/resetPass`, data);
}
