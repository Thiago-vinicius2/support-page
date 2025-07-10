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

export function displayMessage(msg) {
  const chatBox = document.getElementById("chat-box");

  const token = localStorage.getItem("token");
  const decoded = jwt_decode(token);
  const email = decoded.sub;  

  const div = document.createElement("div");
  if (msg.senderEmail === email) {
    div.classList.add('message-sent');
  } else {
    div.classList.add('message-received');
  }

  div.innerHTML = `<strong>${msg.senderName}:</strong> <br>${msg.content} <br><small">${new Date(msg.timestamp).toLocaleTimeString()}</small>`;

  chatBox.appendChild(div);
  chatBox.scrollTop = chatBox.scrollHeight;
}