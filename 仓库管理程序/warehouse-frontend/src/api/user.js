import request from '../utils/request'

export const login = (data) => request.post('/user/login', data)

export const register = (data) => request.post('/user/register', data)

export const getUserInfo = (id) => request.get(`/user/${id}`)

export const updateUser = (data) => request.put('/user/update', data)

export const updatePassword = (data) => request.put('/user/password', data)

export const getUserList = () => request.get('/user/list')

export const deleteUser = (id) => request.delete(`/user/${id}`)
