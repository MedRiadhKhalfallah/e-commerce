package com.riadh.ecommerce.features.aiDeepSeek.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeepSeekConfig {

    @Bean("deepSeekChatClient")
    public ChatClient deepSeekChatClient(@Qualifier("openAiChatModel") ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }
}

