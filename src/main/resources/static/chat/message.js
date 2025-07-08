export function carregarMensagens(ticketId) {
  const token = localStorage.getItem("token");
  const chatBox = document.getElementById("chat-box");
  chatBox.innerHTML = "";

  fetch(`http://localhost:8080/ticket/${ticketId}/messages`, {
    method: "GET",
    headers: {
      "Authorization": `Bearer ${token}`
    }
  })
    .then(response => response.json())
    .then(mensagens => {
      mensagens.forEach(m => displayMessage(m));
    })
    .catch(error => console.error("Erro ao carregar mensagens:", error));
}

export function displayMessage(msg){
    const chatBox = document.getElementById("chat-box");

    const div = document.createElement("div");
    div.classList.add(msg.senderType === 'TÉCNICO' ? 'message-tecnico' : 'message-cliente');
    div.innerHTML = `<strong>${msg.senderName}:</strong> ${msg.content} <br><small>${new Date(msg.timestamp).toLocaleTimeString()}</small>`;

    chatBox.appendChild(div);
    chatBox.scrollTop = chatBox.scrollHeight;
}