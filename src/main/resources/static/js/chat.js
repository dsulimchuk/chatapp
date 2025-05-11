let stompClient = null;
let username = '';

document.addEventListener('DOMContentLoaded', function() {
    const joinBtn = document.getElementById('join-btn');
    const sendBtn = document.getElementById('send-btn');
    const usernameForm = document.getElementById('username-form');
    const chatBox = document.getElementById('chat-box');
    const usernameInput = document.getElementById('username');
    const messageInput = document.getElementById('message-input');
    const messagesContainer = document.getElementById('messages-container');

    // Scroll to bottom of messages container
    scrollToBottom();

    joinBtn.addEventListener('click', function() {
        username = usernameInput.value.trim();
        if (username) {
            usernameForm.style.display = 'none';
            chatBox.style.display = 'flex';
            connect();
        }
    });

    sendBtn.addEventListener('click', sendMessage);

    messageInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            sendMessage();
        }
    });

    function connect() {
        const socket = new SockJS('/chat-websocket');
        stompClient = Stomp.over(socket);
        
        stompClient.connect({}, function(frame) {
            console.log('Connected: ' + frame);
            
            stompClient.subscribe('/topic/messages', function(message) {
                const messageData = JSON.parse(message.body);
                displayMessage(messageData);
            });
            
            // Send a system message that user joined
            sendSystemMessage(username + ' joined the chat');
        });
    }

    function sendSystemMessage(content) {
        const message = {
            username: 'System',
            content: content,
            timestamp: new Date()
        };
        
        stompClient.send('/app/send', {}, JSON.stringify(message));
    }

    function sendMessage() {
        const content = messageInput.value.trim();
        if (content && stompClient) {
            const message = {
                username: username,
                content: content,
                timestamp: new Date()
            };
            
            stompClient.send('/app/send', {}, JSON.stringify(message));
            messageInput.value = '';
        }
    }

    function displayMessage(message) {
        const messageElement = document.createElement('div');
        messageElement.className = 'message';
        
        const usernameElement = document.createElement('span');
        usernameElement.className = 'username';
        usernameElement.textContent = message.username;
        
        const timestampElement = document.createElement('span');
        timestampElement.className = 'timestamp';
        const messageTime = new Date(message.timestamp);
        timestampElement.textContent = messageTime.getHours().toString().padStart(2, '0') + 
                                      ':' + 
                                      messageTime.getMinutes().toString().padStart(2, '0');
        
        const contentElement = document.createElement('p');
        contentElement.className = 'content';
        contentElement.textContent = message.content;
        
        messageElement.appendChild(usernameElement);
        messageElement.appendChild(timestampElement);
        messageElement.appendChild(contentElement);
        
        messagesContainer.appendChild(messageElement);
        scrollToBottom();
    }

    function scrollToBottom() {
        if (messagesContainer) {
            messagesContainer.scrollTop = messagesContainer.scrollHeight;
        }
    }
});