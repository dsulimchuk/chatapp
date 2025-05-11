package com.ds.chat;

import com.ds.chat.model.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class WebSocketIntegrationTest {

    @LocalServerPort
    private int port;

    private WebSocketStompClient stompClient;
    private final CompletableFuture<Message> completableFuture = new CompletableFuture<>();

    @BeforeEach
    void setup() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        SockJsClient sockJsClient = new SockJsClient(transports);

        stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    void testSendMessage() throws ExecutionException, InterruptedException, TimeoutException {
        // Create a room ID for testing
        Long roomId = 1L; // This should match a room in test database

        StompSessionHandler sessionHandler = new TestStompSessionHandler(completableFuture, roomId);
        StompSession session = stompClient.connectAsync("ws://localhost:" + port + "/chat-websocket", sessionHandler)
                .get(5, TimeUnit.SECONDS);

        String username = "TestUser";
        String content = "Test WebSocket Message";

        Message message = new Message(username, content);
        message.setRoomId(roomId);
        session.send("/app/chat/" + roomId, message);

        // Wait for the message to be processed
        Message receivedMessage = completableFuture.get(5, TimeUnit.SECONDS);

        assertNotNull(receivedMessage);
        assertEquals(username, receivedMessage.getUsername());
        assertEquals(content, receivedMessage.getContent());

        session.disconnect();
    }

    private static class TestStompSessionHandler extends StompSessionHandlerAdapter {
        private final CompletableFuture<Message> completableFuture;
        private final Long roomId;

        public TestStompSessionHandler(CompletableFuture<Message> completableFuture, Long roomId) {
            this.completableFuture = completableFuture;
            this.roomId = roomId;
        }

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            session.subscribe("/topic/chat/" + roomId, new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return Message.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    completableFuture.complete((Message) payload);
                }
            });
        }

        @Override
        public void handleException(StompSession session, StompCommand command, 
                StompHeaders headers, byte[] payload, Throwable exception) {
            completableFuture.completeExceptionally(exception);
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            completableFuture.completeExceptionally(exception);
        }
    }
}