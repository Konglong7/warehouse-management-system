import request from '../utils/request'

export const getDashboardStats = () => request.get('/stats/dashboard')

export const getInventoryStats = () => request.get('/stats/inventory')

export const getTrendStats = () => request.get('/stats/trend')
