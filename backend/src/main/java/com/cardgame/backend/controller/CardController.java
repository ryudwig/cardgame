package com.cardgame.backend.controller;

import com.cardgame.backend.entity.Card;
import com.cardgame.backend.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    // 모든 카드 조회 또는 카테고리별 카드 조회
    @GetMapping
    public List<Card> getAllCards(@RequestParam(required = false) String category) {
        if (category != null && !category.isEmpty()) {
            return cardRepository.findByCategory(category);
        }
        return cardRepository.findAll();
    }

    // ID로 특정 카드 조회
    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable Long id) {
        Optional<Card> card = cardRepository.findById(id);
        if (card.isPresent()) {
            return ResponseEntity.ok(card.get());
        }
        return ResponseEntity.notFound().build();
    }

    // 새 카드 생성
    @PostMapping
    public Card createCard(@RequestBody Card card) {
        return cardRepository.save(card);
    }

    // 카드 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCard(@PathVariable Long id, @RequestBody Card cardDetails) {
        Optional<Card> optionalCard = cardRepository.findById(id);
        if (optionalCard.isPresent()) {
            Card card = optionalCard.get();
            card.setEnglish(cardDetails.getEnglish());
            card.setKorean(cardDetails.getKorean());
            card.setCategory(cardDetails.getCategory());
            return ResponseEntity.ok(cardRepository.save(card));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 카드 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCard(@PathVariable Long id) {
        if (cardRepository.existsById(id)) {
            cardRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 카테고리별 카드 수 조회
    @GetMapping("/count")
    public long getCardCount(@RequestParam(required = false) String category) {
        if (category != null && !category.isEmpty()) {
            return cardRepository.countByCategory(category);
        }
        return cardRepository.count();
    }
} 