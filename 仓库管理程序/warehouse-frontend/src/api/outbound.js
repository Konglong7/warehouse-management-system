import request from '../utils/request'

export const getOutboundList = () => request.get('/outbound/list')

export const getOutbound = (id) => request.get(`/outbound/${id}`)

export const addOutbound = (data) => request.post('/outbound', data)
