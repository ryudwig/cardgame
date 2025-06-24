<template>
  <div class="quiz-page">
    <div class="page-header">
      <h1>퀴즈</h1>
    </div>
    <div class="page-content">
      <div class="quiz-container">
        <div class="quiz-mode-selector" v-if="!quizStarted">
          <h3>퀴즈 모드를 선택하세요</h3>
          <a-space direction="vertical" size="large">
            <a-card hoverable @click="startQuiz('multiple')" class="mode-card">
              <h4>객관식 퀴즈</h4>
              <p>영어 단어를 보고 정답을 선택하세요</p>
            </a-card>
            <a-card hoverable @click="startQuiz('fill')" class="mode-card">
              <h4>빈칸 채우기</h4>
              <p>영어 단어를 보고 한글 뜻을 입력하세요</p>
            </a-card>
          </a-space>
        </div>

        <div v-else class="quiz-content">
          <div class="quiz-progress">
            <a-progress 
              :percent="progressPercent" 
              :format="() => `${currentQuestionIndex + 1} / ${quizQuestions.length}`"
            />
          </div>

          <div v-if="currentQuestion" class="question-card">
            <h2>{{ currentQuestion.english }}</h2>
            <p class="question-hint">다음 영어 단어의 한글 뜻은?</p>

            <!-- 객관식 퀴즈 -->
            <div v-if="quizMode === 'multiple'" class="multiple-choice">
              <a-radio-group 
                v-model:value="selectedAnswer" 
                size="large"
                @change="handleAnswerSelect"
              >
                <a-space direction="vertical" size="middle">
                  <a-radio 
                    v-for="(option, index) in currentQuestion.options" 
                    :key="index"
                    :value="option"
                    class="option-radio"
                  >
                    {{ option }}
                  </a-radio>
                </a-space>
              </a-radio-group>
            </div>

            <!-- 빈칸 채우기 -->
            <div v-else-if="quizMode === 'fill'" class="fill-blank">
              <a-input 
                v-model:value="userAnswer" 
                size="large"
                placeholder="한글 뜻을 입력하세요"
                @pressEnter="submitFillAnswer"
                class="answer-input"
              />
              <a-button 
                type="primary" 
                size="large" 
                @click="submitFillAnswer"
                :disabled="!userAnswer.trim()"
                class="submit-btn"
              >
                제출
              </a-button>
            </div>

            <!-- 결과 표시 -->
            <div v-if="showResult" class="result-display">
              <a-result
                :status="isCorrect ? 'success' : 'error'"
                :title="isCorrect ? '정답!' : '오답!'"
                :sub-title="resultMessage"
              >
                <template #extra>
                  <a-button type="primary" @click="nextQuestion">
                    {{ isLastQuestion ? '결과 보기' : '다음 문제' }}
                  </a-button>
                </template>
              </a-result>
            </div>
          </div>

          <div class="quiz-stats">
            <a-statistic title="정답" :value="correctAnswers" value-style="color: #3f8600" />
            <a-statistic title="오답" :value="wrongAnswers" value-style="color: #cf1322" />
            <a-statistic title="정답률" :value="accuracyPercent" suffix="%" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

interface Card {
  id: number
  english: string
  korean: string
  category: string
}

interface QuizQuestion {
  english: string
  correct: string
  options?: string[]
}

const quizStarted = ref(false)
const quizMode = ref<'multiple' | 'fill'>('multiple')
const cards = ref<Card[]>([])
const quizQuestions = ref<QuizQuestion[]>([])
const currentQuestionIndex = ref(0)
const selectedAnswer = ref('')
const userAnswer = ref('')
const showResult = ref(false)
const isCorrect = ref(false)
const correctAnswers = ref(0)
const wrongAnswers = ref(0)

const currentQuestion = computed(() => quizQuestions.value[currentQuestionIndex.value])
const progressPercent = computed(() => 
  quizQuestions.value.length > 0 ? Math.round(((currentQuestionIndex.value + 1) / quizQuestions.value.length) * 100) : 0
)
const isLastQuestion = computed(() => currentQuestionIndex.value === quizQuestions.value.length - 1)
const accuracyPercent = computed(() => {
  const total = correctAnswers.value + wrongAnswers.value
  return total > 0 ? Math.round((correctAnswers.value / total) * 100) : 0
})
const resultMessage = computed(() => 
  isCorrect.value 
    ? `정답: ${currentQuestion.value?.correct}` 
    : `정답은 "${currentQuestion.value?.correct}"입니다`
)

const startQuiz = async (mode: 'multiple' | 'fill') => {
  quizMode.value = mode
  await loadCards()
  generateQuizQuestions()
  quizStarted.value = true
}

const loadCards = async () => {
  try {
    // 모든 카드를 가져와서 랜덤하게 섞기
    const response = await axios.get<Card[]>('http://localhost:8080/api/cards')
    cards.value = shuffleArray(response.data).slice(0, 10) // 10문제만
  } catch (error) {
    console.error('카드 로드 실패:', error)
  }
}

const generateQuizQuestions = () => {
  quizQuestions.value = cards.value.map(card => {
    const question: QuizQuestion = {
      english: card.english,
      correct: card.korean
    }

    if (quizMode.value === 'multiple') {
      // 같은 카테고리에서 오답 3개 생성
      const sameCategory = cards.value.filter(c => 
        c.category === card.category && c.id !== card.id
      )
      const wrongOptions = shuffleArray(sameCategory)
        .slice(0, 3)
        .map(c => c.korean)
      
      question.options = shuffleArray([...wrongOptions, card.korean])
    }

    return question
  })
}

const shuffleArray = <T>(array: T[]): T[] => {
  const shuffled = [...array]
  for (let i = shuffled.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]]
  }
  return shuffled
}

const handleAnswerSelect = () => {
  checkAnswer(selectedAnswer.value)
}

const submitFillAnswer = () => {
  if (userAnswer.value.trim()) {
    checkAnswer(userAnswer.value.trim())
  }
}

const checkAnswer = (answer: string) => {
  isCorrect.value = answer === currentQuestion.value.correct
  
  if (isCorrect.value) {
    correctAnswers.value++
  } else {
    wrongAnswers.value++
  }
  
  showResult.value = true
}

const nextQuestion = () => {
  if (isLastQuestion.value) {
    // 퀴즈 완료 - 통계 페이지로 이동
    router.push({
      name: 'statistics',
      query: {
        mode: 'quiz',
        correct: correctAnswers.value,
        wrong: wrongAnswers.value,
        total: quizQuestions.value.length,
        accuracy: accuracyPercent.value
      }
    })
  } else {
    // 다음 문제로
    currentQuestionIndex.value++
    selectedAnswer.value = ''
    userAnswer.value = ''
    showResult.value = false
  }
}
</script>

<style scoped>
.quiz-page {
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
  width: 100%;
  height: calc(100vh - 70px);
}

.quiz-mode-selector {
  text-align: center;
  padding: 2rem;
}

.mode-card {
  cursor: pointer;
  transition: transform 0.2s;
  max-width: 400px;
  margin: 0 auto;
}

.mode-card:hover {
  transform: translateY(-4px);
}

.quiz-progress {
  margin-bottom: 2rem;
}

.question-card {
  text-align: center;
  padding: 2rem;
  background: #f9f9f9;
  border-radius: 12px;
  margin-bottom: 2rem;
  max-width: 100%;
}

.question-card h2 {
  font-size: 3rem;
  color: #1890ff;
  margin-bottom: 1rem;
}

.question-hint {
  color: #666;
  margin-bottom: 2rem;
}

.multiple-choice {
  margin: 2rem 0;
}

.option-radio {
  display: block;
  padding: 1rem;
  margin: 0.5rem 0;
  border: 1px solid #d9d9d9;
  border-radius: 8px;
  background: white;
  transition: all 0.2s;
}

.option-radio:hover {
  border-color: #1890ff;
  background: #f0f8ff;
}

.fill-blank {
  margin: 2rem 0;
}

.answer-input {
  max-width: 300px;
  margin-right: 1rem;
  margin-bottom: 1rem;
}

.submit-btn {
  margin-left: 1rem;
}

.result-display {
  margin: 2rem 0;
}

.quiz-stats {
  display: flex;
  justify-content: space-around;
  padding: 2rem;
  background: #f5f5f5;
  border-radius: 12px;
}

@media (max-width: 768px) {
  .question-card h2 {
    font-size: 2rem;
  }
  
  .quiz-stats {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }
  
  .answer-input {
    max-width: 100%;
    margin-right: 0;
  }
  
  .submit-btn {
    margin-left: 0;
    margin-top: 1rem;
    width: 100%;
  }
}
</style> 