import { ref, watch, type Ref, type WatchSource } from 'vue'

export interface UsePaginationOptions {
  defaultPageSize?: number
  watchSources?: WatchSource[]
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

export function usePagination<T>(
  fetcher: () => Promise<{ list: T[]; total: number }>,
  options?: UsePaginationOptions
): UsePaginationReturn<T> {
  const loading = ref(false)
  const page = ref(1)
  const pageSize = ref(options?.defaultPageSize ?? 10)
  const total = ref(0)
  const list: Ref<T[]> = ref([])

  async function loadList() {
    loading.value = true
    try {
      const data = await fetcher()
      list.value = data.list
      total.value = data.total
    } catch {
      list.value = []
      total.value = 0
    } finally {
      loading.value = false
    }
  }

  function handlePageChange(p: number) {
    page.value = p
  }

  if (options?.watchSources !== undefined && options.watchSources.length > 0) {
    watch(options.watchSources, loadList)
  }
  watch(page, loadList)

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
