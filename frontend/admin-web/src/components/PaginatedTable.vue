<script setup lang="ts">
import { computed } from 'vue'

export interface ColumnConfig {
  prop: string
  label: string
  width?: string | number
  minWidth?: string | number
  fixed?: 'left' | 'right' | boolean
  align?: 'left' | 'center' | 'right'
  sortable?: boolean
  showOverflowTooltip?: boolean
  [key: string]: unknown
}

const props = withDefaults(
  defineProps<{
    data: Record<string, unknown>[]
    columns: ColumnConfig[]
    loading?: boolean
    total?: number
    page?: number
    pageSize?: number
    pageSizes?: number[]
    border?: boolean
    stripe?: boolean
    height?: string | number
    maxHeight?: string | number
    rowKey?: string | ((row: Record<string, unknown>) => string)
  }>(),
  {
    loading: false,
    total: 0,
    page: 1,
    pageSize: 20,
    pageSizes: () => [10, 20, 50, 100],
    border: true,
    stripe: true,
    rowKey: 'id',
  }
)

const emit = defineEmits<{
  (e: 'update:page', page: number): void
  (e: 'update:pageSize', pageSize: number): void
  (e: 'selection-change', selection: Record<string, unknown>[]): void
}>()

const showPagination = computed(() => props.total > 0)

function handleCurrentChange(val: number) {
  emit('update:page', val)
}

function handleSizeChange(val: number) {
  emit('update:pageSize', val)
  emit('update:page', 1)
}

function handleSelectionChange(selection: Record<string, unknown>[]) {
  emit('selection-change', selection)
}

// Template refs (used in template, referenced here to satisfy vue-tsc)
void showPagination
void handleCurrentChange
void handleSizeChange
void handleSelectionChange
</script>

<template>
  <div class="paginated-table">
    <el-table
      :data="data"
      v-loading="loading"
      :border="border"
      :stripe="stripe"
      :height="height"
      :max-height="maxHeight"
      :row-key="rowKey"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <slot name="selection">
        <el-table-column type="selection" width="55" align="center" />
      </slot>
      <slot name="index">
        <el-table-column type="index" label="#" width="60" align="center" />
      </slot>
      <el-table-column
        v-for="col in columns"
        :key="col.prop"
        :prop="col.prop"
        :label="col.label"
        :width="col.width"
        :min-width="col.minWidth"
        :fixed="col.fixed"
        :align="col.align"
        :sortable="col.sortable"
        :show-overflow-tooltip="col.showOverflowTooltip !== false"
      >
        <template #default="scope">
          <slot :name="col.prop" :row="scope.row" :column="col" :$index="scope.$index">
            {{ scope.row[col.prop] }}
          </slot>
        </template>
      </el-table-column>
      <slot name="append" />
      <slot name="empty">
        <template #empty>
          <el-empty description="暂无数据" />
        </template>
      </slot>
    </el-table>
    <div v-if="showPagination" class="paginated-table__pagination">
      <el-pagination
        :current-page="page"
        :page-size="pageSize"
        :page-sizes="pageSizes"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        background
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>
  </div>
</template>

<style scoped lang="scss">
.paginated-table {
  width: 100%;

  &__pagination {
    display: flex;
    justify-content: flex-end;
    padding: var(--space-md, 16px) 0 0;
  }
}
</style>
