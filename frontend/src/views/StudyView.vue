<template>
  <div class="study-page">
    <div class="page-header">
      <h1>{{ selectedCategory }} 학습</h1>
      <a-button @click="goBack">뒤로가기</a-button>
    </div>
    <div class="page-content">

      <div v-if="loading" class="loading-container">
        <a-spin size="large" />
        <p>카드를 불러오는 중...</p>
      </div>

      <div v-else-if="cards.length === 0" class="no-cards">
        <a-empty description="해당 카테고리에 카드가 없습니다." />
      </div>

      <div v-else class="study-container">
        <div class="progress-info">
          <a-progress 
            :percent="progressPercent" 
            :format="() => `${currentIndex + 1} / ${cards.length}`"
          />
        </div>

        <div class="flashcard-container">
          <div 
            class="flashcard" 
            :class="{ flipped: isFlipped }"
            @click="flipCard"
          >
            <div class="card-front">
              <h2>{{ currentCard?.english }}</h2>
              <p class="hint">카드를 클릭하면 한글 뜻이 나타납니다</p>
            </div>
            <div class="card-back">
              <h2>{{ currentCard?.korean }}</h2>
              <p class="english-word">{{ currentCard?.english }}</p>
            </div>
          </div>
        </div>

        <div v-if="isFlipped" class="action-buttons">
          <a-space size="large">
            <a-button size="large" danger @click="markAsWrong">
              모름 😕
            </a-button>
            <a-button size="large" type="primary" @click="markAsCorrect">
              알고 있음 😊
            </a-button>
          </a-space>
        </div>

        <div class="study-info">
          <a-statistic-countdown title="학습한 카드" :value="studiedCount" />
          <a-statistic title="정답" :value="correctCount" value-style="color: #3f8600" />
          <a-statistic title="오답" :value="wrongCount" value-style="color: #cf1322" />
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

interface Card {
  id: number
  english: string
  korean: string
  category: string
}

const selectedCategory = ref<string>('')
const cards = ref<Card[]>([])
const currentIndex = ref(0)
const isFlipped = ref(false)
const loading = ref(true)

// 학습 통계
const studiedCount = ref(0)
const correctCount = ref(0)
const wrongCount = ref(0)

const currentCard = computed(() => cards.value[currentIndex.value])
const progressPercent = computed(() => 
  cards.value.length > 0 ? Math.round(((currentIndex.value + 1) / cards.value.length) * 100) : 0
)

const flipCard = () => {
  isFlipped.value = !isFlipped.value
}

const markAsCorrect = () => {
  correctCount.value++
  studiedCount.value++
  nextCard()
}

const markAsWrong = () => {
  wrongCount.value++
  studiedCount.value++
  // TODO: 오답 카드를 별도 리스트에 저장
  nextCard()
}

const nextCard = () => {
  if (currentIndex.value < cards.value.length - 1) {
    currentIndex.value++
    isFlipped.value = false
  } else {
    // 학습 완료
    showCompletionMessage()
  }
}

const showCompletionMessage = () => {
  const accuracy = studiedCount.value > 0 ? Math.round((correctCount.value / studiedCount.value) * 100) : 0
  
  // TODO: 통계를 백엔드에 저장
  
  router.push({ 
    name: 'statistics', 
    query: { 
      category: selectedCategory.value,
      studied: studiedCount.value,
      correct: correctCount.value,
      wrong: wrongCount.value,
      accuracy
    }
  })
}

const loadCards = async () => {
  try {
    loading.value = true
    const response = await axios.get(`http://localhost:8080/api/cards?category=${selectedCategory.value}`)
    cards.value = response.data
  } catch (error) {
    console.error('카드 로드 실패:', error)
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.push({ name: 'category' })
}

onMounted(() => {
  selectedCategory.value = route.query.category as string || '동물'
  loadCards()
})
</script>

<style scoped>
.study-page {
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
  justify-content: space-between;
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
  width: 100%;
  height: calc(100vh - 70px);
}

.loading-container {
  text-align: center;
  padding: 3rem;
}

.no-cards {
  text-align: center;
  padding: 3rem;
}

.study-container {
  text-align: center;
  max-width: 100%;
  width: 100%;
}

.progress-info {
  margin-bottom: 2rem;
}

.flashcard-container {
  margin: 2rem 0;
  perspective: 1000px;
}

.flashcard {
  width: min(500px, 90vw);
  height: 300px;
  margin: 0 auto;
  position: relative;
  transform-style: preserve-3d;
  transition: transform 0.6s;
  cursor: pointer;
}

.flashcard.flipped {
  transform: rotateY(180deg);
}

.card-front, .card-back {
  position: absolute;
  width: 100%;
  height: 100%;
  backface-visibility: hidden;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 2rem;
}

.card-front {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.card-back {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
  transform: rotateY(180deg);
}

.card-front h2, .card-back h2 {
  font-size: 2.5rem;
  margin: 0;
  font-weight: bold;
}

.hint {
  margin-top: 1rem;
  opacity: 0.8;
  font-size: 0.9rem;
}

.english-word {
  margin-top: 1rem;
  opacity: 0.8;
  font-size: 1.2rem;
}

.action-buttons {
  margin: 2rem 0;
}

.study-info {
  display: flex;
  justify-content: space-around;
  margin-top: 3rem;
  padding: 2rem;
  background: #f5f5f5;
  border-radius: 12px;
}

@media (max-width: 768px) {
  .page-content {
    padding: 1rem;
  }
  
  .flashcard {
    width: min(350px, 95vw);
    height: 250px;
  }
  
  .card-front h2, .card-back h2 {
    font-size: 1.8rem;
  }
  
  .study-info {
    flex-direction: column;
    gap: 1rem;
  }
}
</style> 