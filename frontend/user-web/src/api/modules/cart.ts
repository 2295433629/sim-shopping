import request from '@/api/request'

/** Cart item */
export interface CartItem {
  cartItemId: number
  productId: number
  skuId: number
  productName: string
  productImage: string
  skuName: string
  price: number
  quantity: number
  selected: number
  shopId: number
  shopName: string
  stock: number
}

/** Cart grouped by shop */
export interface CartShopGroup {
  shopId: number
  shopName: string
  items: CartItem[]
}

/** Cart response */
export interface CartVO {
  shopGroups: CartShopGroup[]
  totalCount: number
  totalAmount: number
  selectedCount: number
  selectedAmount: number
}

/** Get cart */
export function getCart() {
  return request.get<unknown, CartVO>('/user/cart')
}

/** Add to cart */
export function addToCart(productId: number, skuId: number, quantity: number) {
  return request.post<unknown, void>('/user/cart', { productId, skuId, quantity })
}

/** Update cart item */
export function updateCartItem(cartItemId: number, data: { quantity?: number; selected?: number }) {
  return request.put<unknown, void>(`/user/cart/items/${cartItemId}`, data)
}

/** Remove cart item */
export function removeCartItem(cartItemId: number) {
  return request.delete<unknown, void>(`/user/cart/items/${cartItemId}`)
}

/** Clear cart */
export function clearCart() {
  return request.delete<unknown, void>('/user/cart')
}

/** Select all items */
export function selectAll(selected: number) {
  return request.put<unknown, void>('/user/cart/select-all', { selected })
}
