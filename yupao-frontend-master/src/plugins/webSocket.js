// src/services/websocketService.js
class WebSocketService {
    constructor() {
        this.socket = null;
        this.callbacks = null;
    }

    connect(url) {
        this.socket = new WebSocket(url);

        this.socket.onopen = () => {
            console.log("WebSocket connection established");
        };

        this.socket.onmessage = (event) => {
            // 对收到的json message进行解析
            const message = JSON.parse(event.data);
            console.log(message)
            this.callback(message);
        };

        this.socket.onclose = () => {
            console.log("WebSocket connection closed");
        };
    }

    disconnect() {
        if (this.socket) {
            this.socket.close();
        }
    }

    sendMessage(message) {
        if (this.socket && this.socket.readyState === WebSocket.OPEN) {
            this.socket.send(JSON.stringify(message));
        }
    }

    // 传递回调函数
    onMessage(callback) {
        this.callback = callback;
    }
}

export default new WebSocketService();
