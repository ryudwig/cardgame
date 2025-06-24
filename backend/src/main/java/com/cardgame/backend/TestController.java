package com.cardgame.backend;

import com.cardgame.backend.repository.CardRepository;
import com.cardgame.backend.repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private StatisticsRepository statisticsRepository;

    @GetMapping("/test")
    public String test() {
        return "백엔드 연결 성공!";
    }
    
    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @GetMapping("/test/data")
    public Map<String, Object> testData() {
        Map<String, Object> result = new HashMap<>();
        result.put("totalCards", cardRepository.count());
        result.put("animalCards", cardRepository.countByCategory("동물"));
        result.put("foodCards", cardRepository.countByCategory("음식"));
        result.put("verbCards", cardRepository.countByCategory("동사"));
        result.put("totalStatistics", statisticsRepository.count());
        result.put("message", "데이터 로드 성공!");
        return result;
    }
} 