/**
 * 评论/留言相关接口
 */
import request from './request';

const BASE = '/message';

export function getMessagePage(params) {
  return request.get(`${BASE}/page`, { params });
}

export function getMessageByForeign(foreignId, type = 1) {
  return request.get(`${BASE}/foreign/${foreignId}/${type}`);
}

export function addMessage(data) {
  return request.post(BASE, data);
}

export function deleteMessage(id) {
  return request.delete(`${BASE}/${id}`);
}
