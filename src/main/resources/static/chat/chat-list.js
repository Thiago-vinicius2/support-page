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
                item.classList.add("chat-conversations");
                item.style.cursor = "pointer";

                item.innerHTML = `
                    <div class="chat-header">
                        <span class="name-sent">
                        <i class="bi bi-person-fill"></i>
                        ${chat.nomeUserChat}
                        </span>
                        <span class="number-ticket-id">Ticket #${chat.ticketId}</span>
                    </div>
                    <span class="lastMessage">
                   ${chat.ultimaMensagem ? `<i class="bi bi-check2"></i> ${chat.ultimaMensagem}` : ''}
                    </span>
                `;

                item.addEventListener("click", () => {
                    ticketAtualId = chat.ticketId;
                    connectToTicket(chat.ticketId);
                    carregarMensagens(chat.ticketId);
                    InfoTicket(chat.ticketId);

                    document.getElementById("chat-form").style.display = "flex";
                    document.getElementById("chat-info").style.display = "flex";
                    document.getElementById("chat-icon").style.display = "none";
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
            Swal.fire({
                title: "Ops...",
                text: "Selecione um ticket antes de enviar mensagem.",
                icon: "warning",
                iconHtml: '<i class="bi bi-reply-fill"></i>',
                showCancelButton: true,
                cancelButtonColor: "#d33",
                confirmButtonText: "Sim, quero sair!",
                cancelButtonText: "Cancelar",
                customClass: {
                    popup: "meu-popup",
                    title: "meu-titulo",
                    htmlContainer: "meu-texto",
                    confirmButton: "meu-botao-ok",
                    cancelButton: "meu-botao-cancel"
                }
            })
            return;
        }
        sendMessage(ticketAtualId, textoDigitado);
        inputMensagem.value = "";
    });
}

function InfoTicket(ticketId) {
    const token = localStorage.getItem("token");

    fetch(`http://localhost:8080/info-ticket/${ticketId}`, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
        .then(res => {
            if (!res.ok) throw new Error("Erro ao buscar informações do ticket");
            return res.json();
        })
        .then(data => {
            document.getElementById("ticket-solicitante").textContent = data.nomeSolicitante;
            document.getElementById("ticket-titulo").innerHTML = `<i class="bi bi-dash"></i>` + data.titulo + `<i class="bi bi-dash"></i>`;
            document.getElementById("ticket-descricao").textContent = "Descrição: " + data.descricao;
            document.getElementById("ticket-abertura").textContent = data.dataAbertura;
        })
        .catch(err => console.error("Erro:", err));
}

window.addEventListener("DOMContentLoaded", () => {
    listarChats();
    configurarEnvioMensagem();
});


