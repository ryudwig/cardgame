<template>
  <div class="statistics-page">
    <div class="page-header">
      <h1>학습 통계</h1>
    </div>
    <div class="page-content">
      <div class="statistics-container">
        <!-- 전체 통계 요약 -->
        <div class="summary-cards">
          <a-row :gutter="[16, 16]">
            <a-col :xs="24" :sm="12" :md="6">
              <a-card size="small">
                <a-statistic 
                  title="총 학습 카드 수" 
                  :value="totalStudied"
                  :value-style="{ color: '#1890ff' }"
                />
              </a-card>
            </a-col>
            <a-col :xs="24" :sm="12" :md="6">
              <a-card size="small">
                <a-statistic 
                  title="정답 수" 
                  :value="totalCorrect"
                  :value-style="{ color: '#52c41a' }"
                />
              </a-card>
            </a-col>
            <a-col :xs="24" :sm="12" :md="6">
              <a-card size="small">
                <a-statistic 
                  title="오답 수" 
                  :value="totalWrong"
                  :value-style="{ color: '#ff4d4f' }"
                />
              </a-card>
            </a-col>
            <a-col :xs="24" :sm="12" :md="6">
              <a-card size="small">
                <a-statistic 
                  title="전체 정답률" 
                  :value="overallAccuracy"
                  suffix="%"
                  :value-style="{ color: '#722ed1' }"
                />
              </a-card>
            </a-col>
          </a-row>
        </div>

        <!-- 카테고리별 상세 통계 -->
        <div class="category-statistics">
          <h3>카테고리별 통계</h3>
          <a-table 
            :dataSource="categoryStats" 
            :columns="columns"
            :pagination="false"
            class="stats-table"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'accuracy'">
                <a-progress 
                  :percent="record.accuracy" 
                  size="small"
                  :stroke-color="getAccuracyColor(record.accuracy)"
                />
              </template>
              <template v-else-if="column.key === 'actions'">
                <a-space>
                  <a-button size="small" @click="studyCategory(record.category)">
                    학습하기
                  </a-button>
                  <a-button size="small" danger @click="resetCategoryStats(record.category)">
                    초기화
                  </a-button>
                </a-space>
              </template>
            </template>
          </a-table>
        </div>

        <!-- 최근 학습 결과 (query params에서 온 경우) -->
        <div v-if="recentResult" class="recent-result">
          <a-alert
            :message="recentResult.title"
            :description="recentResult.description"
            :type="recentResult.type"
            show-icon
            closable
            @close="clearRecentResult"
          />
        </div>

        <!-- 액션 버튼들 -->
        <div class="action-buttons">
          <a-space>
            <a-button type="primary" @click="goToCategory">
              새로 학습하기
            </a-button>
            <a-button @click="goToQuiz">
              퀴즈 풀기
            </a-button>
            <a-button danger @click="resetAllStats">
              전체 통계 초기화
            </a-button>
          </a-space>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

interface Statistics {
  id: number
  category: string
  studied: number
  correct: number
  wrong: number
  accuracy: number
}

const categoryStats = ref<Statistics[]>([])
const recentResult = ref<any>(null)

const columns = [
  { title: '카테고리', dataIndex: 'category', key: 'category' },
  { title: '학습 카드 수', dataIndex: 'studied', key: 'studied' },
  { title: '정답', dataIndex: 'correct', key: 'correct' },
  { title: '오답', dataIndex: 'wrong', key: 'wrong' },
  { title: '정답률', key: 'accuracy' },
  { title: '액션', key: 'actions' }
]

const totalStudied = computed(() => 
  categoryStats.value.reduce((sum, stat) => sum + stat.studied, 0)
)

const totalCorrect = computed(() => 
  categoryStats.value.reduce((sum, stat) => sum + stat.correct, 0)
)

const totalWrong = computed(() => 
  categoryStats.value.reduce((sum, stat) => sum + stat.wrong, 0)
)

const overallAccuracy = computed(() => {
  const total = totalStudied.value
  return total > 0 ? Math.round((totalCorrect.value / total) * 100) : 0
})

const loadStatistics = async () => {
  try {
    const response = await axios.get<Statistics[]>('http://localhost:8080/api/statistics')
    categoryStats.value = response.data
  } catch (error) {
    console.error('통계 로드 실패:', error)
  }
}

const getAccuracyColor = (accuracy: number) => {
  if (accuracy >= 80) return '#52c41a'
  if (accuracy >= 60) return '#faad14'
  return '#ff4d4f'
}

const studyCategory = (category: string) => {
  router.push({ name: 'study', query: { category } })
}

const resetCategoryStats = async (category: string) => {
  try {
    await axios.delete(`http://localhost:8080/api/statistics/${category}`)
    loadStatistics() // 통계 다시 로드
  } catch (error) {
    console.error('통계 초기화 실패:', error)
  }
}

const resetAllStats = async () => {
  try {
    for (const stat of categoryStats.value) {
      await axios.delete(`http://localhost:8080/api/statistics/${stat.category}`)
    }
    loadStatistics() // 통계 다시 로드
  } catch (error) {
    console.error('전체 통계 초기화 실패:', error)
  }
}

const goToCategory = () => {
  router.push({ name: 'category' })
}

const goToQuiz = () => {
  router.push({ name: 'quiz' })
}

const clearRecentResult = () => {
  recentResult.value = null
}

const processRecentResult = () => {
  const { mode, category, studied, correct, wrong, accuracy } = route.query
  
  if (mode || category) {
    if (mode === 'quiz') {
      recentResult.value = {
        title: '퀴즈 완료!',
        description: `총 ${route.query.total}문제 중 ${correct}문제 정답 (정답률: ${accuracy}%)`,
        type: Number(accuracy) >= 70 ? 'success' : 'warning'
      }
    } else if (category) {
      recentResult.value = {
        title: `${category} 카테고리 학습 완료!`,
        description: `${studied}개 카드 학습 (정답: ${correct}개, 오답: ${wrong}개, 정답률: ${accuracy}%)`,
        type: Number(accuracy) >= 70 ? 'success' : 'warning'
      }
    }
  }
}

onMounted(() => {
  loadStatistics()
  processRecentResult()
})
</script>

<style scoped>
.statistics-page {
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

.statistics-container {
  max-width: 100%;
  margin: 0;
}

.summary-cards {
  margin-bottom: 3rem;
}

.category-statistics {
  margin-bottom: 3rem;
}

.category-statistics h3 {
  margin-bottom: 1rem;
  color: #1890ff;
}

.stats-table {
  background: white;
  border-radius: 8px;
}

.recent-result {
  margin-bottom: 2rem;
}

.action-buttons {
  text-align: center;
  padding: 2rem 0;
}

@media (max-width: 768px) {
  .statistics-page {
    padding: 1rem;
  }
  
  .action-buttons .ant-space {
    flex-direction: column;
    width: 100%;
  }
  
  .action-buttons .ant-btn {
    width: 100%;
  }
}
</style> 