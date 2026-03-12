package com.fitnessai.fitness_ai_backend.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * ChatClientConfig
 *
 * Registers a Spring AI {@link ChatClient} bean.
 *
 * Spring AI auto-configures a {@link ChatModel} bean using the
 * spring.ai.openai.* properties in application.yml, so we just
 * wrap it with ChatClient.builder() here.
 *
 * ChatClient provides a fluent, immutable API on top of ChatModel,
 * with built-in support for system prompts, retry, and streaming.
 */
@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }
}

