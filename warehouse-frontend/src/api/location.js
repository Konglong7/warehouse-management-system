import request from '../utils/request'

export const getLocationList = () => request.get('/location/list')

export const getLocation = (id) => request.get(`/location/${id}`)

export const addLocation = (data) => request.post('/location', data)

export const updateLocation = (id, data) => request.put(`/location/${id}`, data)

export const deleteLocation = (id) => request.delete(`/location/${id}`)
