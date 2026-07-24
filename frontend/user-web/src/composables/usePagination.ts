import { ref, type Ref } from 'vue'

export interface UsePaginationOptions<T> {
  fetchFn: (page: number, pageSize: number) => Promise<{ list: T[]; total: number }>
  pageSize?: number
  immediate?: boolean
}

export interface UsePaginationReturn<T> {
  loading: Ref<boolean>
  page: Ref<number>
  pageSize: Ref<number>
  total: Ref<number>
  list: Ref<T[]>
  loadList: () => Promise<void>
  handlePageChange: (p: number) => void
}

export function usePagination<T>(options: UsePaginationOptions<T>): UsePaginationReturn<T> {
  const loading = ref(false)
  const page = ref(1)
  const pageSize = ref(options.pageSize || 10)
  const total = ref(0)
  const list = ref<T[]>([]) as Ref<T[]>

  async function loadList() {
    loading.value = true
    try {
      const data = await options.fetchFn(page.value, pageSize.value)
      list.value = data.list || []
      total.value = data.total || 0
    } catch {
      list.value = []
      total.value = 0
    } finally {
      loading.value = false
    }
  }

  function handlePageChange(p: number) {
    page.value = p
    loadList()
  }

  if (options.immediate !== false) {
    loadList()
  }

  return {
    loading,
    page,
    pageSize,
    total,
    list,
    loadList,
    handlePageChange,
  }
}
