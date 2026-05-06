package com.riadh.ecommerce.features.aiDeepSeek.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class DeepSeekRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String originalText;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String summary;

    private LocalDateTime createdAt;
}

