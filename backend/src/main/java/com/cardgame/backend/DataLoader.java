package com.cardgame.backend;

import com.cardgame.backend.entity.Card;
import com.cardgame.backend.entity.Statistics;
import com.cardgame.backend.repository.CardRepository;
import com.cardgame.backend.repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Override
    public void run(String... args) throws Exception {
        // 기존 데이터가 없을 때만 샘플 데이터 삽입
        if (cardRepository.count() == 0) {
            loadSampleCards();
        }
        
        if (statisticsRepository.count() == 0) {
            loadInitialStatistics();
        }
    }

    private void loadSampleCards() {
        // 동물 카테고리 카드 (10개)
        cardRepository.save(new Card("cat", "고양이", "동물"));
        cardRepository.save(new Card("dog", "강아지", "동물"));
        cardRepository.save(new Card("elephant", "코끼리", "동물"));
        cardRepository.save(new Card("lion", "사자", "동물"));
        cardRepository.save(new Card("tiger", "호랑이", "동물"));
        cardRepository.save(new Card("rabbit", "토끼", "동물"));
        cardRepository.save(new Card("bear", "곰", "동물"));
        cardRepository.save(new Card("wolf", "늑대", "동물"));
        cardRepository.save(new Card("horse", "말", "동물"));
        cardRepository.save(new Card("cow", "소", "동물"));

        // 음식 카테고리 카드 (10개)
        cardRepository.save(new Card("apple", "사과", "음식"));
        cardRepository.save(new Card("banana", "바나나", "음식"));
        cardRepository.save(new Card("orange", "오렌지", "음식"));
        cardRepository.save(new Card("bread", "빵", "음식"));
        cardRepository.save(new Card("rice", "쌀", "음식"));
        cardRepository.save(new Card("chicken", "닭고기", "음식"));
        cardRepository.save(new Card("fish", "생선", "음식"));
        cardRepository.save(new Card("milk", "우유", "음식"));
        cardRepository.save(new Card("water", "물", "음식"));
        cardRepository.save(new Card("coffee", "커피", "음식"));

        // 동사 카테고리 카드 (10개)
        cardRepository.save(new Card("run", "달리다", "동사"));
        cardRepository.save(new Card("walk", "걷다", "동사"));
        cardRepository.save(new Card("eat", "먹다", "동사"));
        cardRepository.save(new Card("drink", "마시다", "동사"));
        cardRepository.save(new Card("sleep", "자다", "동사"));
        cardRepository.save(new Card("study", "공부하다", "동사"));
        cardRepository.save(new Card("read", "읽다", "동사"));
        cardRepository.save(new Card("write", "쓰다", "동사"));
        cardRepository.save(new Card("play", "놀다", "동사"));
        cardRepository.save(new Card("work", "일하다", "동사"));

        System.out.println("샘플 카드 데이터가 성공적으로 로드되었습니다. 총 " + cardRepository.count() + "개의 카드");
    }

    private void loadInitialStatistics() {
        // 각 카테고리별 초기 통계 생성
        statisticsRepository.save(new Statistics("동물"));
        statisticsRepository.save(new Statistics("음식"));
        statisticsRepository.save(new Statistics("동사"));

        System.out.println("초기 통계 데이터가 성공적으로 로드되었습니다.");
    }
} 