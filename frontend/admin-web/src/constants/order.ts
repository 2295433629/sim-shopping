/**
 * 订单状态相关常量（Admin端英文版），统一管理和复用订单状态映射关系
 */

/** 订单状态文本映射（英文） */
export const ORDER_STATUS_TEXT: Record<string, string> = {
  CREATED: 'Unpaid',
  PAID: 'Paid',
  SHIPPED: 'Shipped',
  IN_TRANSIT: 'In Transit',
  OUT_FOR_DELIVERY: 'Out for Delivery',
  DELIVERED: 'Delivered',
  COMPLETED: 'Completed',
  CANCELLED: 'Cancelled',
}

/** 订单状态标签类型映射（用于el-tag的type属性） */
export const ORDER_STATUS_TAG_TYPE: Record<string, string> = {
  CREATED: 'warning',
  PAID: 'primary',
  SHIPPED: 'info',
  IN_TRANSIT: 'primary',
  OUT_FOR_DELIVERY: 'warning',
  DELIVERED: 'success',
  COMPLETED: 'success',
  CANCELLED: 'danger',
}

/** 订单状态筛选选项（用于select组件） */
export const ORDER_STATUS_OPTIONS = [
  { label: 'All', value: '' },
  { label: 'Unpaid', value: 'CREATED' },
  { label: 'Paid', value: 'PAID' },
  { label: 'Shipped', value: 'SHIPPED' },
  { label: 'Delivered', value: 'DELIVERED' },
  { label: 'Completed', value: 'COMPLETED' },
  { label: 'Cancelled', value: 'CANCELLED' },
]
