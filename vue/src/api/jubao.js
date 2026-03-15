/**
 * 投诉管理相关接口
 */
import request from './request';

const BASE = '/jubao';

export function getJubaoPage(params) {
  return request.get(`${BASE}/page`, { params });
}

export function deleteJubao(id) {
  return request.delete(`${BASE}/${id}`)
}

export function submitJubao(data) {
  return request.post(BASE, data)
}
