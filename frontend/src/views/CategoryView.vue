<template>
  <div class="category-page">
    <div class="page-header">
      <h1>카테고리 선택</h1>
    </div>
    <div class="page-content">
      <div class="category-grid">
        <a-card 
          v-for="category in categories" 
          :key="category.name"
          hoverable 
          class="category-card"
          @click="selectCategory(category.name)"
        >
          <template #cover>
            <div class="category-icon">{{ category.icon }}</div>
          </template>
          <a-card-meta :title="category.name" :description="category.description" />
          <div class="category-count">
            {{ getCategoryCount(category.name) }}개의 카드
          </div>
        </a-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const categories = ref([
  { name: '동물', icon: '🐾', description: '동물 관련 영어 단어를 학습합니다' },
  { name: '음식', icon: '🍎', description: '음식 관련 영어 단어를 학습합니다' },
  { name: '동사', icon: '🏃', description: '동작 관련 영어 단어를 학습합니다' }
])

const categoryCounts = ref<Record<string, number>>({})

const selectCategory = (category: string) => {
  // 카테고리를 선택하고 학습 페이지로 이동
  router.push({ name: 'study', query: { category } })
}

const getCategoryCount = (category: string) => {
  return categoryCounts.value[category] || 0
}

const loadCategoryCounts = async () => {
  try {
    for (const category of categories.value) {
      const response = await axios.get(`http://localhost:8080/api/cards/count?category=${category.name}`)
      categoryCounts.value[category.name] = response.data
    }
  } catch (error) {
    console.error('카테고리 카드 수 조회 실패:', error)
  }
}

onMounted(() => {
  loadCategoryCounts()
})
</script>

<style scoped>
.category-page {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.page-header {
  background: white;
  padding: 1rem 2rem;
  border-bottom: 1px solid #f0f0f0;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  height: 70px;
  display: flex;
  align-items: center;
}

.page-header h1 {
  margin: 0;
  font-size: 1.8rem;
  color: #1890ff;
  font-weight: 600;
}

.page-content {
  flex: 1;
  padding: 1.5rem;
  background: #f5f5f5;
  overflow-y: auto;
  height: calc(100vh - 70px);
}

.category-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 1.5rem;
  margin-top: 1rem;
  max-width: 100%;
}

.category-card {
  text-align: center;
  cursor: pointer;
  transition: transform 0.2s;
}

.category-card:hover {
  transform: translateY(-4px);
}

.category-icon {
  font-size: 4rem;
  padding: 2rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  margin: 0;
}

.category-count {
  margin-top: 1rem;
  color: #666;
  font-size: 0.9rem;
}

@media (max-width: 768px) {
  .page-content {
    padding: 1rem;
    height: calc(100vh - 70px);
  }
  
  .category-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .category-card:hover {
    transform: none;
  }
}
</style> 