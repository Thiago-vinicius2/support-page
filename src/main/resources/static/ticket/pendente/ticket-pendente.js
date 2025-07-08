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

            if (ticket.statusTicket === "PENDENTE") {
                linha.innerHTML = `
                    <td> ${index + 1} </td>
                    <td> ${ticket.titulo} </td>
                    <td> ${ticket.descricao} </td>
                    <td> ${ticket.dataAbertura} </td>
                    <td> ${ticket.userSolicitante} </td>
                    <td> 
                    <button class="btn btn-assume">
                    Assumir
                    </button> </td>`;

                tabela.appendChild(linha);
            }
        });
    })
    .catch(error => {
        console.error("Erro ao buscar tickets:", error);
    });

tabela.addEventListener("click", function (event) {
    const target = event.target;

    const btnAssume = target.closest(".btn-assume");

    if (btnAssume) {
        const linha = btnAssume.closest("tr")
        confirmarAssume(linha);
    }
});

function confirmarAssume(linha) {
    const id = parseInt(linha.getAttribute("data-id"));
    const titulo = linha.cells[1].textContent.trim();

    const payload = { id, titulo };

    Swal.fire({
        title: "Deseja assumir este ticket?",
        icon: "warning",
        iconHtml: '<i class="bi bi-card-checklist"></i>',
        showCancelButton: true,
        confirmButtonText: "Assumir",
        cancelButtonText: "Cancelar",
        customClass: {
            popup: "meu-popup",
            title: "meu-titulo",
            htmlContainer: "meu-texto",
            confirmButton: "meu-botao-ok",
            cancelButton: "meu-botao-cancel"
        }
    }).then((result) => {
        if (result.isConfirmed) {
            assumeTicket(payload, linha);
        }
    });
};

function assumeTicket(payload, linha) {
    const url = "http://localhost:8080/assume-ticket";
    const token = localStorage.getItem("token");

    fetch(url, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(payload)
    })
        .then(response => {
            if (response.ok) {
                Swal.fire({
                    title: "Assumido",
                    text: "O ticket foi assumido",
                    icon: "success",
                    iconHtml: '<i class="bi bi-file-earmark-check"></i>',
                    confirmButtonText: "OK",
                    customClass: {
                        popup: "meu-popup",
                        title: "meu-titulo",
                        htmlContainer: "meu-texto",
                        confirmButton: "meu-botao-ok"
                    }
                }).then(() => {
                    if (linha) linha.remove();
                    listarChats();
                });
            } else {
                Swal.fire({
                    title: "Erro",
                    text: "Falha ao assumir o ticket.",
                    icon: "error",
                    iconHtml: '<i class="bi bi-exclamation-triangle"></i>',
                    confirmButtonText: "OK",
                    customClass: {
                        popup: "meu-popup",
                        title: "meu-titulo",
                        htmlContainer: "meu-texto",
                        confirmButton: "meu-botao-ok"
                    }
                });
            }
        })
        .catch(error => {
            console.error("Erro:", error);
            Swal.fire({
                title: "Erro",
                text: "Erro de comunicação com o servidor.",
                icon: "error",
                iconHtml: '<i class="bi bi-x-octagon"></i>',
                confirmButtonText: "OK",
                customClass: {
                    popup: "meu-popup",
                    title: "meu-titulo",
                    htmlContainer: "meu-texto",
                    confirmButton: "meu-botao-cancel"
                }
            });
        });
};
