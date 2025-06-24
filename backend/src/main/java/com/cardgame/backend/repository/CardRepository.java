package com.cardgame.backend.repository;

import com.cardgame.backend.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    
    // 카테고리별 카드 조회
    List<Card> findByCategory(String category);
    
    // 카테고리별 카드 수 조회
    long countByCategory(String category);
} 