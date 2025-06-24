package com.cardgame.backend.repository;

import com.cardgame.backend.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    
    // 카테고리별 통계 조회
    Optional<Statistics> findByCategory(String category);
    
    // 카테고리 존재 여부 확인
    boolean existsByCategory(String category);
} 