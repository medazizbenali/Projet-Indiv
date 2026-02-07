import axios from 'axios'

export function createApi(token) {
  const api = axios.create({
    baseURL: '/',
    headers: { 'Content-Type': 'application/json' }
  })

  api.interceptors.request.use((config) => {
    if (token) config.headers.Authorization = `Bearer ${token}`
    return config
  })

  return api
}
