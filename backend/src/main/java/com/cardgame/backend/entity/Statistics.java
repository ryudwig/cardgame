package com.cardgame.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "statistics")
public class Statistics {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String category;
    
    @Column(nullable = false)
    private Integer studied = 0;
    
    @Column(nullable = false)
    private Integer correct = 0;
    
    @Column(nullable = false)  
    private Integer wrong = 0;
    
    @Column(nullable = false)
    private Float accuracy = 0.0f;
    
    // 기본 생성자
    public Statistics() {}
    
    // 생성자
    public Statistics(String category) {
        this.category = category;
        this.studied = 0;
        this.correct = 0;
        this.wrong = 0;
        this.accuracy = 0.0f;
    }
    
    // 정답률 계산 메서드
    public void calculateAccuracy() {
        if (studied > 0) {
            this.accuracy = (float) correct / studied * 100;
        } else {
            this.accuracy = 0.0f;
        }
    }
    
    // Getter, Setter
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Integer getStudied() {
        return studied;
    }
    
    public void setStudied(Integer studied) {
        this.studied = studied;
        calculateAccuracy();
    }
    
    public Integer getCorrect() {
        return correct;
    }
    
    public void setCorrect(Integer correct) {
        this.correct = correct;
        calculateAccuracy();
    }
    
    public Integer getWrong() {
        return wrong;
    }
    
    public void setWrong(Integer wrong) {
        this.wrong = wrong;
        calculateAccuracy();
    }
    
    public Float getAccuracy() {
        return accuracy;
    }
    
    public void setAccuracy(Float accuracy) {
        this.accuracy = accuracy;
    }
    
    // 통계 업데이트 메서드
    public void updateStats(boolean isCorrect) {
        this.studied++;
        if (isCorrect) {
            this.correct++;
        } else {
            this.wrong++;
        }
        calculateAccuracy();
    }
    
    @Override
    public String toString() {
        return "Statistics{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", studied=" + studied +
                ", correct=" + correct +
                ", wrong=" + wrong +
                ", accuracy=" + accuracy +
                '}';
    }
} 