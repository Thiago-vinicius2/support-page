import { displayMessage } from './message.js';

let stompClient = null;

export function connectToTicket(ticketId) {
    const token = localStorage.getItem("token");

    const socket = new SockJS("http://localhost:8080/ws");
    stompClient = Stomp.over(socket);

    stompClient.connect({ Authorization: `Bearer ${token}` },
        function (frame) {
            console.log("Conectado: " + frame);

            stompClient.subscribe(`/topic/ticket/${ticketId}`, function (message) {
                const data = JSON.parse(message.body);
                displayMessage(data);
            });
        });
}

export function sendMessage(ticketId, content) {
    const token = localStorage.getItem("token");

    stompClient.send("/app/ticket/send",
        { Authorization: `Bearer ${token}` },
        JSON.stringify({
            ticketId,
            conteudo: content
        }));
}