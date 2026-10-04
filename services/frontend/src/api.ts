export type ApiEnvelope<T> = { success: boolean; data: T; message: string }

const API_BASE = '/api/v1'

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const token = localStorage.getItem('ecommerce_token')
  const headers = new Headers(options.headers)
  headers.set('Content-Type', 'application/json')
  if (token) headers.set('Authorization', `Bearer ${token}`)
  const response = await fetch(`${API_BASE}${path}`, { ...options, headers })
  if (response.status === 204) return undefined as T
  const body = await response.json().catch(() => ({}))
  if (!response.ok) throw new Error(body.message || body.error || `Request failed (${response.status})`)
  return body as T
}

export interface User {
  id: string; firstName: string; lastName: string; email: string; mobile: string
  status: string; role: string; emailVerified: boolean; mobileVerified: boolean; createdAt: string
}
export interface Address {
  id: string; userId: string; addressType: 'HOME'|'WORK'|'OTHER'; fullName: string; mobile: string
  addressLine1: string; addressLine2?: string; city: string; state: string; country: string
  postalCode: string; defaultAddress: boolean; createdAt: string
}
export interface LoginData { token: string; user: User }
export interface RegisterInput { firstName: string; lastName: string; email: string; mobile: string; password: string }
export interface AddressInput { addressType: Address['addressType']; fullName: string; mobile: string; addressLine1: string; addressLine2?: string; city: string; state: string; country: string; postalCode: string; defaultAddress: boolean }

export const userApi = {
  async register(input: RegisterInput) { return request<ApiEnvelope<User>>('/users/register', { method:'POST', body:JSON.stringify(input) }) },
  async login(input: { email:string; password:string }) { return request<ApiEnvelope<LoginData>>('/users/login', { method:'POST', body:JSON.stringify(input) }) },
  async me() { return request<ApiEnvelope<User>>('/users/me') },
  async updateProfile(input: { firstName:string; lastName:string; mobile:string }) { return request<ApiEnvelope<User>>('/users/me', { method:'PUT', body:JSON.stringify(input) }) },
  async addresses() { return request<ApiEnvelope<Address[]>>('/users/me/addresses') },
  async addAddress(input: AddressInput) { return request<ApiEnvelope<Address>>('/users/me/addresses', { method:'POST', body:JSON.stringify(input) }) },
  async updateAddress(id:string,input:AddressInput) { return request<ApiEnvelope<Address>>(`/users/me/addresses/${id}`, { method:'PUT', body:JSON.stringify(input) }) },
  async deleteAddress(id:string) { return request<void>(`/users/me/addresses/${id}`, { method:'DELETE' }) },
  async setDefaultAddress(id:string) { return request<ApiEnvelope<Address>>(`/users/me/addresses/${id}/default`, { method:'PUT' }) },
}
