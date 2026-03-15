/**
 * 收藏相关接口
 */
import request from './request';

const BASE = '/collectMovie';

export function getCollectPage(params) {
  return request.get(`${BASE}/page`, { params });
}

export function addCollect(data) {
  return request.post(`${BASE}/`, data);
}

export function removeCollect(userId, movieId) {
  return request.delete(`${BASE}/${userId}/${movieId}`);
}
