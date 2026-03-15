/**
 * 关注作者相关接口
 */
import request from './request';

export function getFollowListByUser(username) {
  return request.get(`/lookAuthor/user/${username}`);
}

export function followAuthor(data) {
  return request.post('/lookAuthor/', data);
}

export function unfollowAuthor(user, commentUser) {
  return request.delete(`/lookAuthor/${user}/${commentUser}`);
}
