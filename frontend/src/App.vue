<script setup lang="ts">
import { RouterView } from 'vue-router'
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { 
  HomeOutlined, 
  BookOutlined, 
  ReadOutlined, 
  QuestionCircleOutlined, 
  BarChartOutlined 
} from '@ant-design/icons-vue'

const router = useRouter()
const route = useRoute()

const collapsed = ref(false)
const selectedKeys = ref([route.name as string])

const menuItems = [
  {
    key: 'home',
    icon: HomeOutlined,
    label: '홈',
    path: '/'
  },
  {
    key: 'category',
    icon: BookOutlined,
    label: '카테고리 선택',
    path: '/category'
  },
  {
    key: 'study',
    icon: ReadOutlined,
    label: '학습',
    path: '/study'
  },
  {
    key: 'quiz',
    icon: QuestionCircleOutlined,
    label: '퀴즈',
    path: '/quiz'
  },
  {
    key: 'statistics',
    icon: BarChartOutlined,
    label: '통계',
    path: '/statistics'
  }
]

const handleMenuClick = ({ key }: { key: string }) => {
  const item = menuItems.find(item => item.key === key)
  if (item) {
    router.push(item.path)
  }
}

// 라우트 변경 시 선택된 메뉴 업데이트
router.afterEach((to) => {
  selectedKeys.value = [to.name as string]
})
</script>

<template>
  <a-layout style="min-height: 100vh">
    <!-- 좌측 사이드바 -->
    <a-layout-sider 
      v-model:collapsed="collapsed" 
      collapsible
      theme="light"
      width="200"
      :collapsed-width="60"
    >
      <div class="logo">
        <h3 v-if="!collapsed">단어 암기</h3>
        <h3 v-else>단어</h3>
      </div>
      <a-menu
        v-model:selectedKeys="selectedKeys"
        mode="inline"
        @click="handleMenuClick"
      >
        <a-menu-item v-for="item in menuItems" :key="item.key">
          <component :is="item.icon" />
          <span>{{ item.label }}</span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>

    <!-- 메인 콘텐츠 영역 -->
    <a-layout>


      <!-- 콘텐츠 -->
      <a-layout-content class="content">
        <div class="page-container">
          <RouterView />
        </div>
      </a-layout-content>


    </a-layout>
  </a-layout>
</template>

<style scoped>
.logo {
  padding: 0.8rem;
  text-align: center;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 0.5rem;
}

.logo h3 {
  margin: 0;
  color: #1890ff;
  font-size: 1rem;
  font-weight: 600;
}

.content {
  background: #f5f5f5;
  height: 100vh;
  padding: 0;
  overflow: hidden;
}

.page-container {
  height: 100vh;
  background: white;
  margin: 0;
  padding: 0;
  border-radius: 0;
  overflow: hidden;
}

/* 사이드바 스타일 커스터마이징 */
:deep(.ant-layout-sider) {
  box-shadow: 2px 0 8px rgba(0,0,0,0.1);
}

:deep(.ant-menu) {
  border-right: none;
}

:deep(.ant-menu-item) {
  margin: 2px 4px;
  border-radius: 6px;
  height: 44px;
  line-height: 44px;
  font-size: 14px;
}

:deep(.ant-menu-item-selected) {
  background: #e6f7ff !important;
  color: #1890ff !important;
}

:deep(.ant-menu-item:hover) {
  background: #f0f8ff;
  color: #1890ff;
}

:deep(.ant-menu-item-icon) {
  font-size: 16px;
}

/* 반응형 스타일 */
@media (max-width: 768px) {
  .content {
    height: 100vh;
  }
}
</style>
