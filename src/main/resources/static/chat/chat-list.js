import { connectToTicket, sendMessage } from './websocket.js';
import { carregarMensagens } from './message.js';

let ticketAtualId = null;

function listarChats() {

    const token = localStorage.getItem("token");
    const decoded = jwt_decode(token);
    const role = decoded.role;

    let url = null;

    if (role === "TÉCNICO" || role === "ADMIN") {
        url = "http://localhost:8080/chats-tecnico";
    } else if (role === "CLIENTE") {
        url = "http://localhost:8080/chats-cliente";
    } else {
        console.warn("Perfil sem permissão para visualizar chats.");
        return;
    }

    fetch(url, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
        .then(response => {
            if (!response.ok) throw new Error("Erro ao buscar chats");
            return response.json();
        })
        .then(chats => {
            const listaChats = document.getElementById("lista-chats");
            listaChats.innerHTML = "";

            chats.forEach(chat => {
                const item = document.createElement("li");
                item.classList.add("chat-conversations", "mb-3", "px-2");
                item.style.cursor = "pointer";

                item.innerHTML = `
                    <div class="chat-header">
                        <span class="name-sent">${chat.nomeUserChat}</span>
                        <span class="number-ticket-id">Ticket #${chat.ticketId}</span>
                    </div>
                    <span class="text-muted small">${chat.ultimaMensagem || ""}</span>
                `;

                item.addEventListener("click", () => {
                    ticketAtualId = chat.ticketId;
                    connectToTicket(chat.ticketId);
                    carregarMensagens(chat.ticketId);
                });

                listaChats.appendChild(item);
            });
        })
        .catch(error => {
            console.error("Erro ao carregar chats:", error);
        });
}

function configurarEnvioMensagem() {
    const form = document.getElementById("chat-form");   
    const inputMensagem = document.getElementById("chat-input");
    

    form.addEventListener("submit", (event) => {
        event.preventDefault();
        const textoDigitado = inputMensagem.value.trim();
        if (!textoDigitado) return;

        if (!ticketAtualId) {
            alert("Selecione um ticket antes de enviar mensagem.");
            return;
        }

        sendMessage(ticketAtualId, textoDigitado);
        inputMensagem.value = "";
    });
}

window.addEventListener("DOMContentLoaded", () => {
    listarChats();
    configurarEnvioMensagem();
});


