import request from '../utils/request'

export const getSupplierList = () => request.get('/supplier/list')

export const getSupplier = (id) => request.get(`/supplier/${id}`)

export const addSupplier = (data) => request.post('/supplier', data)

export const updateSupplier = (id, data) => request.put(`/supplier/${id}`, data)

export const deleteSupplier = (id) => request.delete(`/supplier/${id}`)
