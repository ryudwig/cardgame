package com.cardgame.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cards")
public class Card {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String english;
    
    @Column(nullable = false)
    private String korean;
    
    @Column(nullable = false)
    private String category;
    
    // 기본 생성자
    public Card() {}
    
    // 생성자
    public Card(String english, String korean, String category) {
        this.english = english;
        this.korean = korean;
        this.category = category;
    }
    
    // Getter, Setter
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEnglish() {
        return english;
    }
    
    public void setEnglish(String english) {
        this.english = english;
    }
    
    public String getKorean() {
        return korean;
    }
    
    public void setKorean(String korean) {
        this.korean = korean;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", english='" + english + '\'' +
                ", korean='" + korean + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
} 