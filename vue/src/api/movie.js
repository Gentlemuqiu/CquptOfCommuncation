/**
 * 就业信息（电影/帖子）相关接口
 */
import request from './request';

const BASE = '/movie';

export function getMoviePage(params) {
  return request.get(`${BASE}/page`, { params });
}

export function getMoviePageByArea(params) {
  return request.get(`${BASE}/page/area`, { params });
}

export function getMovieById(id) {
  return request.get(`${BASE}/${id}`);
}

export function searchMovie(params) {
  return request.get(`${BASE}/search`, { params });
}

export function getMovieByUser(userId) {
  return request.get(`${BASE}/user/${userId}`);
}

export function createMovie(data) {
  return request.post(BASE, data);
}

export function updateMovie(data) {
  return request.put(BASE, data);
}

export function deleteMovie(id) {
  return request.delete(`${BASE}/${id}`);
}
