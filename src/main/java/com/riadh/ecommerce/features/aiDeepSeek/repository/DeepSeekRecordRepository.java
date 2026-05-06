package com.riadh.ecommerce.features.aiDeepSeek.repository;

import com.riadh.ecommerce.features.aiDeepSeek.entity.DeepSeekRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeepSeekRecordRepository extends JpaRepository<DeepSeekRecord, Long> {
}

