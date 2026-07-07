import request from '../request'

export interface RoleItem {
  id: number
  roleName: string
  roleCode: string
  description?: string
  status: number
  createdAt?: string
}

export interface PermissionItem {
  id: number
  permissionName: string
  permissionCode: string
  resourceType: string
  httpMethod: string
  httpPath: string
  description?: string
}

export interface MenuItem {
  id: number
  menuName: string
  menuCode: string
  parentId: number
  menuType: string
  icon?: string
  path?: string
  sort: number
  status: number
  children?: MenuItem[]
}

// Roles
export function getRoles() {
  return request.get<unknown, RoleItem[]>('/admin/roles')
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

// Permissions
export function getPermissions() {
  return request.get<unknown, PermissionItem[]>('/admin/permissions')
}

// Menus
export function getMenus() {
  return request.get<unknown, MenuItem[]>('/admin/menus')
}

export function createMenu(data: Omit<MenuItem, 'id'>) {
  return request.post<unknown, MenuItem>('/admin/menus', data)
}

export function updateMenu(id: number, data: Omit<MenuItem, 'id'>) {
  return request.put<unknown, MenuItem>(`/admin/menus/${id}`, data)
}

export function deleteMenu(id: number) {
  return request.delete<unknown, void>(`/admin/menus/${id}`)
}
