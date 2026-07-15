import request from '@/api/request'
import type { PageResponse } from '@/types/common'
import type {
  ProductCardVO,
  ProductDetailVO,
  ProductQueryParams,
  CategoryNode,
  ShopInfo,
  SearchParams,
  SearchSuggest,
  HotKeywords,
  SearchHistoryItem,
  BrowseHistoryItem,
} from '@/types/product'

/** 商品列表（分页） */
export function getProducts(params: ProductQueryParams) {
  return request.get<unknown, PageResponse<ProductCardVO>>('/public/products', { params })
}

/** 商品详情 */
export function getProductDetail(productId: number) {
  return request.get<unknown, ProductDetailVO>(`/public/products/${productId}`)
}

/** 商品搜索 */
export function searchProducts(params: SearchParams) {
  return request.get<unknown, PageResponse<ProductCardVO>>('/public/products/search', { params })
}

/** 分类树 */
export function getCategories() {
  return request.get<unknown, CategoryNode[]>('/public/categories')
}

/** 首页推荐商品 */
export function getRecommend(size = 10) {
  return request.get<unknown, ProductCardVO[]>('/public/products/recommend', { params: { size } })
}

/** 热门商品 */
export function getHot(size = 10) {
  return request.get<unknown, ProductCardVO[]>('/public/products/hot', { params: { size } })
}

/** 新品列表 */
export function getNew(size = 10) {
  return request.get<unknown, ProductCardVO[]>('/public/products/new', { params: { size } })
}

/** 搜索联想 */
export function suggest(keyword: string) {
  return request.get<unknown, SearchSuggest>('/public/products/search/suggest', { params: { keyword } })
}

/** 热门搜索词 */
export function hotKeywords() {
  return request.get<unknown, HotKeywords>('/public/products/search/hot')
}

/** 店铺详情 */
export function getShop(shopId: number) {
  return request.get<unknown, ShopInfo>(`/public/shops/${shopId}`)
}

/** 店铺商品列表 */
export function getShopProducts(shopId: number, params: { page?: number; size?: number; sort?: string }) {
  return request.get<unknown, PageResponse<ProductCardVO>>(`/public/shops/${shopId}/products`, { params })
}

/** 收藏商品 */
export function addFavorite(productId: number) {
  return request.post<unknown, void>(`/user/favorites/${productId}`)
}

/** 取消收藏 */
export function removeFavorite(productId: number) {
  return request.delete<unknown, void>(`/user/favorites/${productId}`)
}

/** 收藏列表 */
export function getFavorites(params: { page?: number; size?: number }) {
  return request.get<unknown, PageResponse<ProductCardVO>>('/user/favorites', { params })
}

/** 检查是否已收藏 */
export function isFavorite(productId: number) {
  return request.get<unknown, boolean>(`/user/favorites/check/${productId}`)
}

/** 搜索历史列表 */
export function getSearchHistory() {
  return request.get<unknown, SearchHistoryItem[]>('/user/search-history')
}

/** 清空搜索历史 */
export function clearSearchHistory() {
  return request.delete<unknown, void>('/user/search-history')
}

/** 浏览历史列表 */
export function getBrowseHistory() {
  return request.get<unknown, BrowseHistoryItem[]>('/user/history')
}

/** 清空浏览历史 */
export function clearBrowseHistory() {
  return request.delete<unknown, void>('/user/history')
}
