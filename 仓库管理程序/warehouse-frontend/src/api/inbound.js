import request from '../utils/request'

export const getInboundList = () => request.get('/inbound/list')

export const getInbound = (id) => request.get(`/inbound/${id}`)

export const addInbound = (data) => request.post('/inbound', data)
