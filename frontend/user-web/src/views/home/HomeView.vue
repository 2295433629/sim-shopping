<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getCategories, getRecommend, getHot, getNew } from '@/api/modules/product'
import { getBanners } from '@/api/modules/banner'
import type { CategoryNode, ProductCardVO } from '@/types/product'
import type { BannerItem } from '@/api/modules/banner'
import ProductCard from '@/components/ProductCard.vue'

const router = useRouter()

const loading = ref(false)
const categories = ref<CategoryNode[]>([])
const recommendList = ref<ProductCardVO[]>([])
const hotList = ref<ProductCardVO[]>([])
const newList = ref<ProductCardVO[]>([])
const banners = ref<BannerItem[]>([])

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
  }
}
</script>

<template>
  <div class="home-container" v-loading="loading">
    <!-- Banner 轮播区 -->
    <el-card shadow="never" class="banner-section" v-if="banners.length > 0">
      <el-carousel height="360px" :interval="4000" arrow="hover">
        <el-carousel-item v-for="banner in banners" :key="banner.id">
          <div class="banner-item" @click="onBannerClick(banner)">
            <el-image
              v-if="banner.imageUrl"
              :src="banner.imageUrl"
              fit="cover"
              class="banner-img"
            />
            <div v-else class="banner-text">
              <h2>{{ banner.title }}</h2>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </el-card>

    <!-- 分类导航 -->
    <el-card shadow="never" class="section">
      <template #header>
        <div class="section-header">
          <span class="section-title">分类导航</span>
          <el-button link type="primary" @click="router.push('/category')">全部分类</el-button>
        </div>
      </template>
      <el-row :gutter="16">
        <el-col :xs="8" :sm="6" :md="4" v-for="cat in categories" :key="cat.id">
          <div class="category-item" @click="goToCategory(cat.id)">
            <el-image
              v-if="cat.icon"
              :src="cat.icon"
              fit="cover"
              class="category-icon"
            />
            <el-icon v-else :size="32" color="#409eff"><Menu /></el-icon>
            <p>{{ cat.name }}</p>
          </div>
        </el-col>
      </el-row>
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
  max-width: 1200px;
  margin: 0 auto;

  .section {
    margin-top: 20px;

    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .section-title {
        font-size: 16px;
        font-weight: bold;
      }
    }
  }

  .banner-section {
    :deep(.el-card__body) {
      padding: 0;
    }

    .banner-item {
      cursor: pointer;
      height: 100%;

      .banner-img {
        width: 100%;
        height: 100%;
        border-radius: 4px;
      }

      .banner-text {
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border-radius: 4px;

        h2 {
          margin: 0;
          color: #fff;
          font-size: 28px;
          font-weight: 600;
          letter-spacing: 2px;
        }
      }
    }
  }

  .category-item {
    text-align: center;
    padding: 16px 0;
    cursor: pointer;
    transition: color 0.3s;

    &:hover {
      color: #409eff;
    }

    .category-icon {
      width: 40px;
      height: 40px;
      border-radius: 50%;
    }

    p {
      margin-top: 8px;
      font-size: 14px;
      color: #666;
    }
  }
}
</style>
