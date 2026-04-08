import request from '../utils/request'

export const getMaterialList = () => request.get('/material/list')

export const getMaterial = (id) => request.get(`/material/${id}`)

export const addMaterial = (data) => request.post('/material', data)

export const updateMaterial = (id, data) => request.put(`/material/${id}`, data)

export const deleteMaterial = (id) => request.delete(`/material/${id}`)

export const getWarningList = () => request.get('/material/warning')
