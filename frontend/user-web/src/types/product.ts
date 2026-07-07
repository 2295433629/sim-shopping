/** 商品卡片 VO */
export interface ProductCardVO {
  productId: number
  name: string
  subtitle?: string
  mainImage?: string
  price: number
  originalPrice?: number
  sales: number
  shopId?: number
  shopName?: string
}

/** 商品详情 VO */
export interface ProductDetailVO {
  productId: number
  name: string
  subtitle?: string
  description?: string
  mainImage?: string
  images: string[]
  price: number
  originalPrice?: number
  stock: number
  sales: number
  viewCount?: number
  categoryId: number
  categoryName?: string
  shopId: number
  shopName: string
  skus: SkuVO[]
  reviewSummary?: {
    avgRating: number
    reviewCount: number
    goodRate: number
  }
}

/** SKU VO */
export interface SkuVO {
  skuId: number
  skuName: string
  price: number
  stock: number
}

/** 分类树节点 */
export interface CategoryNode {
  id: number
  name: string
  icon?: string
  children?: CategoryNode[]
}

/** 店铺信息 */
export interface ShopInfo {
  shopId: number
  shopName: string
  shopLogo?: string
  description?: string
  productCount?: number
  createdAt?: string
}

/** 搜索建议 */
export interface SearchSuggest {
  suggestions: string[]
}

/** 热门搜索词 */
export interface HotKeywords {
  keywords: string[]
}

/** 搜索历史项 */
export interface SearchHistoryItem {
  id: number
  keyword: string
  searchedAt: string
}

/** 浏览历史项 */
export interface BrowseHistoryItem {
  id: number
  productId: number
  productName: string
  productImage?: string
  price: number
  viewedAt: string
}

/** 商品列表查询参数 */
export interface ProductQueryParams {
  page?: number
  size?: number
  categoryId?: number
  keyword?: string
  minPrice?: number
  maxPrice?: number
  sort?: string
}

/** 搜索参数 */
export interface SearchParams {
  keyword: string
  page?: number
  size?: number
  categoryId?: number
  minPrice?: number
  maxPrice?: number
  sort?: string
}
