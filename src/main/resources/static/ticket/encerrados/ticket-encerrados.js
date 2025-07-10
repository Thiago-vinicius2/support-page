const tabela = document.getElementById("tabela-ticket");

let url = "http://localhost:8080/all-ticket";

const token = localStorage.getItem("token");

fetch(url, {
    method: "GET",
    headers: {
        "Authorization": `Bearer ${token}`
    }
})
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error("Erro ao buscar usuários");
        }
    })
    .then(tickets => {
        tabela.innerHTML = "";

        tickets.forEach((ticket, index) => {
            const linha = document.createElement("tr");
            linha.setAttribute("data-id", ticket.id);

            if (ticket.statusTicket === "ENCERRADO") {
                linha.innerHTML = `
                    <td> ${index + 1} </td>
                    <td> ${ticket.titulo} </td>
                    <td> ${ticket.descricao} </td>
                    <td> ${ticket.dataAbertura} </td>
                    <td> ${ticket.userSolicitante} </td>
                    <td> ${ticket.userResponsavel} </td>
                    <td> ${ticket.userEncerramento} </td>`;

                tabela.appendChild(linha);
            }
        });
    })
    .catch(error => {
        console.error("Erro ao buscar tickets:", error);
    });
