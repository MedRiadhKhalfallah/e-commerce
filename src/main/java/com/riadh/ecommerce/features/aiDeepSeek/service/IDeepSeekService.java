package com.riadh.ecommerce.features.aiDeepSeek.service;

import com.riadh.ecommerce.features.aiDeepSeek.dto.DeepSeekRequest;
import com.riadh.ecommerce.features.aiDeepSeek.dto.DeepSeekResponse;

import java.util.List;

public interface IDeepSeekService {
    DeepSeekResponse summarize(DeepSeekRequest request);
    List<DeepSeekResponse> getAll();
    DeepSeekResponse getById(Long id);
}

