import request from '../request'

export interface PageResult<T> {
  list: T[]
  total: number
  page: number
  size: number
  totalPages: number
}

export interface RoleItem {
  id: number
  roleName: string
  roleCode: string
  status: string
  description?: string
  createdAt?: string
}

export interface PermissionItem {
  id: number
  permissionType: number
  permissionName: string
  permissionCode: string
  module: string
  description?: string
  createdAt?: string
}

export interface MenuItem {
  id: number
  menuName: string
  parentId: number
  menuType: string
  path?: string
  component?: string
  icon?: string
  sortOrder: number
  visible: number
  createdAt?: string
  children?: MenuItem[]
}

// Roles
export function getRoles(page = 1, size = 20) {
  return request.get<unknown, PageResult<RoleItem>>('/admin/roles', { params: { page, size } })
}

export function createRole(data: Omit<RoleItem, 'id' | 'createdAt'>) {
  return request.post<unknown, RoleItem>('/admin/roles', data)
}

export function updateRole(id: number, data: Omit<RoleItem, 'id' | 'createdAt'>) {
  return request.put<unknown, RoleItem>(`/admin/roles/${id}`, data)
}

export function deleteRole(id: number) {
  return request.delete<unknown, void>(`/admin/roles/${id}`)
}

// Role Permissions
export function getRolePermissions(roleId: number) {
  return request.get<unknown, PermissionItem[]>(`/admin/roles/${roleId}/permissions`)
}

export function assignPermissions(roleId: number, permissionIds: number[]) {
  return request.put<unknown, void>(`/admin/roles/${roleId}/permissions`, { permissionIds })
}

// Role Menus
export function getRoleMenus(roleId: number) {
  return request.get<unknown, MenuItem[]>(`/admin/roles/${roleId}/menus`)
}

export function assignMenus(roleId: number, menuIds: number[]) {
  return request.put<unknown, void>(`/admin/roles/${roleId}/menus`, { menuIds })
}

// Permissions
export function getPermissions(page = 1, size = 20) {
  return request.get<unknown, PageResult<PermissionItem>>('/admin/permissions', { params: { page, size } })
}

export function createPermission(data: Omit<PermissionItem, 'id' | 'createdAt'>) {
  return request.post<unknown, PermissionItem>('/admin/permissions', data)
}

export function updatePermission(id: number, data: Omit<PermissionItem, 'id' | 'createdAt'>) {
  return request.put<unknown, PermissionItem>(`/admin/permissions/${id}`, data)
}

export function deletePermission(id: number) {
  return request.delete<unknown, void>(`/admin/permissions/${id}`)
}

// Menus
export function getMenus() {
  return request.get<unknown, MenuItem[]>('/admin/menus')
}

export function createMenu(data: Omit<MenuItem, 'id' | 'createdAt' | 'children'>) {
  return request.post<unknown, MenuItem>('/admin/menus', data)
}

export function updateMenu(id: number, data: Omit<MenuItem, 'id' | 'createdAt' | 'children'>) {
  return request.put<unknown, MenuItem>(`/admin/menus/${id}`, data)
}

export function deleteMenu(id: number) {
  return request.delete<unknown, void>(`/admin/menus/${id}`)
}
