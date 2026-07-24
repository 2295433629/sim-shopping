import { ref, shallowRef, type Ref } from 'vue'

export interface UsePaginationOptions<T> {
  page?: number
  pageSize?: number
  fetch: (page: number, pageSize: number) => Promise<{ list: T[]; total: number }>
  onError?: (err: unknown) => void
}

export interface UsePaginationResult<T> {
  loading: Ref<boolean>
  page: Ref<number>
  pageSize: Ref<number>
  total: Ref<number>
  list: Ref<T[]>
  loadList: () => Promise<void>
  handlePageChange: (newPage: number) => void
}

export function usePagination<T>(options: UsePaginationOptions<T>): UsePaginationResult<T> {
  const loading = ref(false)
  const page = ref(options.page ?? 1)
  const pageSize = ref(options.pageSize ?? 20)
  const total = ref(0)
  const list = shallowRef<T[]>([])

  async function loadList() {
    loading.value = true
    try {
      const data = await options.fetch(page.value, pageSize.value)
      list.value = data.list || []
      total.value = data.total || 0
    } catch (err) {
      list.value = []
      total.value = 0
      options.onError?.(err)
    } finally {
      loading.value = false
    }
  }

  function handlePageChange(newPage: number) {
    page.value = newPage
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
