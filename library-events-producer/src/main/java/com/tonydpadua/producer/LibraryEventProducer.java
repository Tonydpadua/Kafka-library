package com.tonydpadua.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tonydpadua.libraryEvent.LibraryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
@RequiredArgsConstructor
public class LibraryEventProducer {

    private final KafkaTemplate<Long, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {
        Long key = libraryEvent.getId();
        String value = this.objectMapper.writeValueAsString(libraryEvent);

        ListenableFuture<SendResult<Long, String>> listenableFuture = this.kafkaTemplate.sendDefault(key, value);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Long, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Long, String> result) {
                handlerSuccess(key, value, result);
            }
        });
    }

    private void handlerSuccess(Long key, String value, SendResult< Long, String> result) {
        log.info("Message Sent SuccessFully for the key: {} and the value is {} , partition is {}", key, value, result.getRecordMetadata().partition());
    }

    private void handleFailure(Long key, String value, Throwable ex) {
        log.error("Error sending the message and the exception is {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in onFailure: {}", throwable.getMessage());
        }
    }
}
