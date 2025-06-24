<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const totalCards = ref(0)

const goToCategory = () => {
  router.push('/category')
}

const goToQuiz = () => {
  router.push('/quiz')
}

const goToStatistics = () => {
  router.push('/statistics')
}

const loadTotalCards = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/cards/count')
    totalCards.value = response.data
  } catch (error) {
    console.error('카드 수 조회 실패:', error)
  }
}

onMounted(() => {
  loadTotalCards()
})
</script>

<template>
  <div class="home-page">
    <div class="page-header">
      <h1>홈</h1>
    </div>
        <div class="page-content">
      <div class="page-area">
        <h2>페이지 영역</h2>
        <p>영어 플래시카드 학습 시스템에 오신 것을 환영합니다!</p>
        
        <div class="quick-actions">
          <a-space size="large" direction="vertical">
            <a-button 
              type="primary" 
              size="large" 
              @click="goToCategory"
              class="action-button"
            >
              📚 카테고리 선택하기
            </a-button>
            <a-button 
              size="large" 
              @click="goToQuiz"
              class="action-button"
            >
              🧠 퀴즈 풀기
            </a-button>
            <a-button 
              size="large" 
              @click="goToStatistics"
              class="action-button"
            >
              📊 학습 통계 보기
            </a-button>
          </a-space>
        </div>
        
        <div class="stats-info">
          <p>총 <strong>{{ totalCards }}</strong>개의 카드로 학습할 수 있습니다.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.home-page {
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

.page-area {
  max-width: 100%;
  margin: 0;
  text-align: center;
  padding: 3rem 2rem;
}

.page-area h2 {
  font-size: 2rem;
  color: #1890ff;
  margin-bottom: 1rem;
}

.page-area p {
  font-size: 1.1rem;
  color: #666;
  margin-bottom: 3rem;
}

.quick-actions {
  margin: 3rem 0;
}

.action-button {
  width: 280px;
  height: 50px;
  font-size: 1rem;
  border-radius: 8px;
  transition: all 0.2s ease;
}

.action-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.stats-info {
  margin-top: 3rem;
  padding: 2rem;
  background: white;
  border-radius: 8px;
  border: 1px solid #e8e8e8;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.stats-info p {
  margin: 0;
  font-size: 1rem;
  color: #333;
}

@media (max-width: 768px) {
  .page-content {
    padding: 1rem;
    height: calc(100vh - 70px);
  }
  
  .page-area {
    padding: 1.5rem 1rem;
  }
  
  .page-area h2 {
    font-size: 1.5rem;
  }
  
  .action-button {
    width: 100%;
    height: 45px;
    font-size: 0.9rem;
  }
}
</style>
