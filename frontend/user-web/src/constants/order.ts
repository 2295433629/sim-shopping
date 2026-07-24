/**
 * 订单状态相关常量，统一管理和复用订单状态映射关系
 * 避免在多个Vue组件中重复定义相同的状态文本、标签类型等
 */

/** 订单状态文本映射（中文） */
export const ORDER_STATUS_TEXT: Record<string, string> = {
  CREATED: '待付款',
  PAID: '已付款',
  SHIPPED: '已发货',
  IN_TRANSIT: '运输中',
  OUT_FOR_DELIVERY: '配送中',
  DELIVERED: '已送达',
  COMPLETED: '已完成',
  CANCELLED: '已取消',
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

/** 订单状态筛选选项（用于tab或select组件） */
export const ORDER_STATUS_TABS = [
  { label: '全部', value: '' },
  { label: '待付款', value: 'CREATED' },
  { label: '已付款', value: 'PAID' },
  { label: '已发货', value: 'SHIPPED' },
  { label: '运输中', value: 'IN_TRANSIT' },
  { label: '配送中', value: 'OUT_FOR_DELIVERY' },
  { label: '已送达', value: 'DELIVERED' },
  { label: '已完成', value: 'COMPLETED' },
  { label: '已取消', value: 'CANCELLED' },
]

/** 可退款的状态列表 */
export const REFUNDABLE_STATUSES = ['PAID', 'SHIPPED', 'IN_TRANSIT', 'OUT_FOR_DELIVERY', 'DELIVERED']
