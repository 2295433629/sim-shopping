<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getCategories, getRecommend, getHot, getNew } from '@/api/modules/product'
import { getBanners } from '@/api/modules/banner'
import type { CategoryNode, ProductCardVO } from '@/types/product'
import type { BannerItem } from '@/api/modules/banner'
import ProductCard from '@/components/ProductCard.vue'
import { ArrowRight } from '@element-plus/icons-vue'

const router = useRouter()

const loading = ref(false)
const categories = ref<CategoryNode[]>([])
const recommendList = ref<ProductCardVO[]>([])
const hotList = ref<ProductCardVO[]>([])
const newList = ref<ProductCardVO[]>([])
const banners = ref<BannerItem[]>([])

// 默认 banner 图片列表（本地静态资源）
const base = import.meta.env.BASE_URL
const defaultBannerImages = [
  `${base}banners/banner-festival.jpg`,
  `${base}banners/banner-tech.jpg`,
  `${base}banners/banner-lifestyle.jpg`,
]

// 为无图片的 banner 分配默认图片
const resolvedBanners = computed(() => {
  if (banners.value.length === 0) return []
  return banners.value.map((banner, index) => ({
    ...banner,
    resolvedImage: banner.imageUrl || defaultBannerImages[index % defaultBannerImages.length],
  }))
})

onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    const [cats, rec, hot, newest, bannerRes] = await Promise.allSettled([
      getCategories(),
      getRecommend(8),
      getHot(8),
      getNew(8),
      getBanners(),
    ])
    if (cats.status === 'fulfilled') categories.value = cats.value
    if (rec.status === 'fulfilled') recommendList.value = rec.value
    if (hot.status === 'fulfilled') hotList.value = hot.value
    if (newest.status === 'fulfilled') newList.value = newest.value
    if (bannerRes.status === 'fulfilled') banners.value = (Array.isArray(bannerRes.value) ? bannerRes.value : [])
  } finally {
    loading.value = false
  }
}

function goToCategory(catId: number) {
  router.push(`/category/${catId}`)
}

function goToProducts() {
  router.push('/products')
}

function onBannerClick(banner: BannerItem) {
  if (banner.linkUrl) {
    router.push(banner.linkUrl)
  } else if (banner.productId) {
    router.push(`/products/${banner.productId}`)
  } else {
    router.push('/products')
  }
}

// 分类元数据：emoji + 渐变配色（参考京东/淘宝分类导航风格）
const categoryMeta = computed(() => {
  const meta: Record<string, { emoji: string; gradient: string }> = {
    '服装':   { emoji: '👕', gradient: 'linear-gradient(135deg, #ff9a56 0%, #ff6b6b 100%)' },
    '数码':   { emoji: '📱', gradient: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)' },
    '美妆':   { emoji: '💄', gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)' },
    '食品':   { emoji: '🍔', gradient: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)' },
    '家居':   { emoji: '🏠', gradient: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)' },
    '运动':   { emoji: '⚽', gradient: 'linear-gradient(135deg, #30cfd0 0%, #330867 100%)' },
    '图书':   { emoji: '📚', gradient: 'linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%)' },
    '母婴':   { emoji: '🍼', gradient: 'linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%)' },
    '手机':   { emoji: '📱', gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
    '电脑':   { emoji: '💻', gradient: 'linear-gradient(135deg, #11998e 0%, #38ef7d 100%)' },
    '耳机':   { emoji: '🎧', gradient: 'linear-gradient(135deg, #fc5c7d 0%, #6a82fb 100%)' },
    '护肤':   { emoji: '✨', gradient: 'linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%)' },
    '彩妆':   { emoji: '💅', gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)' },
    '个护':   { emoji: '🧴', gradient: 'linear-gradient(135deg, #89f7fe 0%, #66a6ff 100%)' },
    '男装':   { emoji: '👔', gradient: 'linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%)' },
    '女装':   { emoji: '👗', gradient: 'linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%)' },
    '鞋靴':   { emoji: '👟', gradient: 'linear-gradient(135deg, #d4fc79 0%, #96e6a1 100%)' },
    '零食':   { emoji: '🍿', gradient: 'linear-gradient(135deg, #f6d365 0%, #fda085 100%)' },
    '饮料':   { emoji: '🥤', gradient: 'linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%)' },
    '生鲜':   { emoji: '🥬', gradient: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)' },
    '家纺':   { emoji: '🛏️', gradient: 'linear-gradient(135deg, #e0c3fc 0%, #8ec5fc 100%)' },
    '厨具':   { emoji: '🍳', gradient: 'linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%)' },
    '收纳':   { emoji: '📦', gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
    '运动鞋': { emoji: '👟', gradient: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)' },
    '运动服': { emoji: '🎽', gradient: 'linear-gradient(135deg, #30cfd0 0%, #330867 100%)' },
    '健身':   { emoji: '🏋️', gradient: 'linear-gradient(135deg, #11998e 0%, #38ef7d 100%)' },
    '文学':   { emoji: '📖', gradient: 'linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%)' },
    '科技':   { emoji: '🔬', gradient: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)' },
    '教育':   { emoji: '🎓', gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
    '奶粉':   { emoji: '🥛', gradient: 'linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%)' },
    '纸尿裤': { emoji: '👶', gradient: 'linear-gradient(135deg, #89f7fe 0%, #66a6ff 100%)' },
    '玩具':   { emoji: '🧸', gradient: 'linear-gradient(135deg, #f6d365 0%, #fda085 100%)' },
  }
  return meta
})

function getCategoryStyle(name: string) {
  const meta = categoryMeta.value
  // 尝试精确匹配，再尝试包含匹配
  if (meta[name]) return meta[name]
  for (const key of Object.keys(meta)) {
    if (name.includes(key)) return meta[key]
  }
  // 默认 fallback
  return { emoji: '📦', gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' }
}
</script>

<template>
  <div class="home-container" v-loading="loading">
    <!-- Banner 轮播区 -->
    <el-card shadow="never" class="banner-section" v-if="resolvedBanners.length > 0">
      <el-carousel height="360px" :interval="4000" arrow="hover">
        <el-carousel-item v-for="banner in resolvedBanners" :key="banner.id">
          <div class="banner-item" @click="onBannerClick(banner)">
            <el-image
              :src="banner.resolvedImage"
              fit="cover"
              class="banner-img"
            />
            <div class="banner-overlay">
              <div class="banner-content">
                <h2 class="banner-title">{{ banner.title }}</h2>
                <p class="banner-subtitle">探索更多精彩</p>
                <span class="banner-cta">立即查看 →</span>
              </div>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </el-card>

    <!-- 分类导航 -->
    <el-card shadow="never" class="section category-section">
      <template #header>
        <div class="section-header">
          <span class="section-title">分类导航</span>
          <el-button link type="primary" @click="router.push('/category')">
            全部分类 <el-icon class="arrow-icon"><ArrowRight /></el-icon>
          </el-button>
        </div>
      </template>
      <div class="category-grid">
        <div
          class="category-card"
          v-for="cat in categories"
          :key="cat.id"
          @click="goToCategory(cat.id)"
        >
          <div
            class="category-circle"
            :style="{ background: getCategoryStyle(cat.name).gradient }"
          >
            <span class="category-emoji">{{ getCategoryStyle(cat.name).emoji }}</span>
          </div>
          <span class="category-name">{{ cat.name }}</span>
        </div>
      </div>
    </el-card>

    <!-- 推荐商品区 -->
    <el-card shadow="never" class="section">
      <template #header>
        <div class="section-header">
          <span class="section-title">🔥 为你推荐</span>
          <el-button link type="primary" @click="goToProducts">查看更多</el-button>
        </div>
      </template>
      <el-row :gutter="16">
        <el-col :xs="12" :sm="8" :md="6" v-for="item in recommendList" :key="item.productId">
          <ProductCard :product="item" />
        </el-col>
      </el-row>
      <el-empty v-if="!loading && recommendList.length === 0" description="暂无推荐商品" />
    </el-card>

    <!-- 热门商品区 -->
    <el-card shadow="never" class="section">
      <template #header>
        <div class="section-header">
          <span class="section-title">⚡ 热门商品</span>
          <el-button link type="primary" @click="goToProducts">查看更多</el-button>
        </div>
      </template>
      <el-row :gutter="16">
        <el-col :xs="12" :sm="8" :md="6" v-for="item in hotList" :key="item.productId">
          <ProductCard :product="item" />
        </el-col>
      </el-row>
      <el-empty v-if="!loading && hotList.length === 0" description="暂无热门商品" />
    </el-card>

    <!-- 新品区 -->
    <el-card shadow="never" class="section">
      <template #header>
        <div class="section-header">
          <span class="section-title">✨ 新品上架</span>
          <el-button link type="primary" @click="goToProducts">查看更多</el-button>
        </div>
      </template>
      <el-row :gutter="16">
        <el-col :xs="12" :sm="8" :md="6" v-for="item in newList" :key="item.productId">
          <ProductCard :product="item" />
        </el-col>
      </el-row>
      <el-empty v-if="!loading && newList.length === 0" description="暂无新品" />
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.home-container {
  max-width: 1280px;
  margin: 0 auto;

  .banner-section {
    margin-bottom: var(--space-xxl);
    border-radius: var(--rounded-xl);
    overflow: hidden;
    border: none;
    box-shadow: none;

    :deep(.el-card__body) {
      padding: 0;
    }

    .banner-item {
      cursor: pointer;
      height: 100%;
      border-radius: 0;
      position: relative;

      .banner-img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }

      .banner-overlay {
        position: absolute;
        inset: 0;
        background: linear-gradient(135deg, rgba(0, 0, 0, 0.45) 0%, rgba(0, 0, 0, 0.1) 60%, transparent 100%);
        display: flex;
        align-items: center;
        padding-left: var(--space-huge);
        transition: background var(--transition-normal);

        &:hover {
          background: linear-gradient(135deg, rgba(0, 0, 0, 0.55) 0%, rgba(0, 0, 0, 0.2) 60%, transparent 100%);
        }
      }

      .banner-content {
        max-width: 480px;
      }

      .banner-title {
        font-family: var(--font-display, 'Helvetica Neue', sans-serif);
        font-weight: 600;
        font-size: 36px;
        color: #fff;
        letter-spacing: 1px;
        margin: 0 0 var(--space-sm);
        text-shadow: 0 2px 12px rgba(0, 0, 0, 0.25);
      }

      .banner-subtitle {
        font-size: var(--font-size-body-lg, 16px);
        color: rgba(255, 255, 255, 0.88);
        margin: 0 0 var(--space-lg);
        font-weight: 400;
        letter-spacing: 0.5px;
        text-shadow: 0 1px 6px rgba(0, 0, 0, 0.2);
      }

      .banner-cta {
        display: inline-block;
        padding: var(--space-xs) var(--space-xl);
        background: rgba(255, 255, 255, 0.2);
        backdrop-filter: blur(8px);
        border: 1px solid rgba(255, 255, 255, 0.35);
        border-radius: var(--rounded-pill);
        color: #fff;
        font-size: var(--font-size-caption);
        font-weight: 500;
        letter-spacing: 0.5px;
        transition: all var(--transition-normal);
      }

      &:hover .banner-cta {
        background: rgba(255, 255, 255, 0.35);
        transform: translateX(4px);
      }
    }

    :deep(.el-carousel__indicators) {
      .el-carousel__indicator {
        .el-carousel__button {
          border-radius: var(--rounded-pill);
          height: 4px;
          opacity: 0.4;
        }

        &.is-active .el-carousel__button {
          opacity: 1;
          width: 24px;
        }
      }
    }
  }

  .section {
    margin-bottom: var(--space-xxl);
    border: 1px solid var(--color-hairline-light);

    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .section-title {
        font-family: var(--font-display, 'Helvetica Neue', sans-serif);
        font-size: var(--font-size-heading-lg, 24px);
        font-weight: 500;
        color: var(--color-ink);
        letter-spacing: 0.36px;
      }
    }
  }

  .category-section {
    :deep(.el-card__body) {
      padding: var(--space-xl) var(--space-xxl);
    }
  }

  .category-grid {
    display: grid;
    grid-template-columns: repeat(8, 1fr);
    gap: var(--space-lg);

    @media (max-width: 1024px) {
      grid-template-columns: repeat(4, 1fr);
    }

    @media (max-width: 640px) {
      grid-template-columns: repeat(4, 1fr);
      gap: var(--space-md);
    }
  }

  .category-card {
    display: flex;
    flex-direction: column;
    align-items: center;
    cursor: pointer;
    padding: var(--space-md);
    border-radius: var(--rounded-lg);
    transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);

    &:hover {
      transform: translateY(-6px);

      .category-circle {
        transform: scale(1.12);
        box-shadow: 0 12px 24px -4px rgba(0, 0, 0, 0.18);
      }

      .category-name {
        color: var(--color-ink);
      }
    }

    &:active {
      transform: translateY(-2px) scale(0.98);
    }
  }

  .category-circle {
    width: 64px;
    height: 64px;
    border-radius: var(--rounded-pill);
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.35s cubic-bezier(0.34, 1.56, 0.64, 1);
    box-shadow: 0 4px 12px -2px rgba(0, 0, 0, 0.1);
    margin-bottom: var(--space-sm);

    @media (max-width: 640px) {
      width: 52px;
      height: 52px;
    }
  }

  .category-emoji {
    font-size: 28px;
    line-height: 1;
    filter: drop-shadow(0 1px 2px rgba(0, 0, 0, 0.1));

    @media (max-width: 640px) {
      font-size: 24px;
    }
  }

  .category-name {
    font-size: var(--font-size-caption);
    font-weight: 500;
    color: var(--color-shade-60);
    letter-spacing: 0.28px;
    transition: color 0.2s ease;
    white-space: nowrap;
  }

  .arrow-icon {
    margin-left: 2px;
    font-size: 12px;
    transition: transform 0.2s ease;
  }

  .section-header .el-button:hover .arrow-icon {
    transform: translateX(3px);
  }
}
</style>
