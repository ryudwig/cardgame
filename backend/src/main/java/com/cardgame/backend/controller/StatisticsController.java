package com.cardgame.backend.controller;

import com.cardgame.backend.entity.Statistics;
import com.cardgame.backend.repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsRepository statisticsRepository;

    // 모든 통계 조회
    @GetMapping
    public List<Statistics> getAllStatistics() {
        return statisticsRepository.findAll();
    }

    // 카테고리별 통계 조회
    @GetMapping("/{category}")
    public ResponseEntity<Statistics> getStatisticsByCategory(@PathVariable String category) {
        Optional<Statistics> statistics = statisticsRepository.findByCategory(category);
        if (statistics.isPresent()) {
            return ResponseEntity.ok(statistics.get());
        }
        return ResponseEntity.notFound().build();
    }

    // 통계 생성 또는 업데이트
    @PostMapping
    public Statistics saveOrUpdateStatistics(@RequestBody Statistics statistics) {
        Optional<Statistics> existingStats = statisticsRepository.findByCategory(statistics.getCategory());
        
        if (existingStats.isPresent()) {
            // 기존 통계 업데이트
            Statistics existing = existingStats.get();
            existing.setStudied(statistics.getStudied());
            existing.setCorrect(statistics.getCorrect());
            existing.setWrong(statistics.getWrong());
            existing.calculateAccuracy();
            return statisticsRepository.save(existing);
        } else {
            // 새 통계 생성
            statistics.calculateAccuracy();
            return statisticsRepository.save(statistics);
        }
    }

    // 학습 결과 업데이트 (정답/오답 기록)
    @PutMapping("/{category}/update")
    public ResponseEntity<Statistics> updateLearningResult(
            @PathVariable String category, 
            @RequestParam boolean isCorrect) {
        
        Optional<Statistics> optionalStats = statisticsRepository.findByCategory(category);
        Statistics statistics;
        
        if (optionalStats.isPresent()) {
            statistics = optionalStats.get();
        } else {
            // 카테고리 통계가 없으면 새로 생성
            statistics = new Statistics(category);
        }
        
        statistics.updateStats(isCorrect);
        statistics = statisticsRepository.save(statistics);
        
        return ResponseEntity.ok(statistics);
    }

    // 통계 초기화
    @DeleteMapping("/{category}")
    public ResponseEntity<?> resetStatistics(@PathVariable String category) {
        Optional<Statistics> optionalStats = statisticsRepository.findByCategory(category);
        if (optionalStats.isPresent()) {
            Statistics statistics = optionalStats.get();
            statistics.setStudied(0);
            statistics.setCorrect(0);
            statistics.setWrong(0);
            statistics.setAccuracy(0.0f);
            statisticsRepository.save(statistics);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
} 