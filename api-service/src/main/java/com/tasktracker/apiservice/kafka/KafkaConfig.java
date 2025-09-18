package com.tasktracker.apiservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${kafka.topics.email-sending}")
    private String emailTopic;

    @Value("${kafka.topics.user-info}")
    private String userInfoTopic;

    @Value("${kafka.topics.task-notification}")
    private String taskNotification;

    @Bean
    public NewTopic emailSendingTopic() {
        return new NewTopic(emailTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic userInfoTopic() {
        return new NewTopic(userInfoTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic taskNotification(){
        return new NewTopic(taskNotification,1, (short) 1);
    }

}
