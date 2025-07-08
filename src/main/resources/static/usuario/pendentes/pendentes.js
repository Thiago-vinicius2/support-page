const tabela = document.getElementById("tabela-usuarios-pendentes");

let url = "http://localhost:8080/all-user-pending";


fetch(url)
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error("Erro ao buscar usuários");
        }
    })
    .then(usuarios => {
        tabela.innerHTML = "";

        usuarios.forEach((usuario, index) => {
            const linha = document.createElement("tr");

            linha.dataset.usuarioId = usuario.id;

            if (usuario.status == "PENDENTE") {
                linha.innerHTML = `
                <td> ${index + 1} </td>
                <td> ${usuario.nome} </td>
                <td> ${usuario.email} </td>
                <td> 
                    <select class="form-select role-select">
                        <option value="CLIENTE" ${usuario.roleSelected === 'CLIENTE' ? 'selected' : ''}>CLIENTE</option>
                        <option value="TÉCNICO" ${usuario.roleSelected === 'TÉCNICO' ? 'selected' : ''}>TÉCNICO</option>
                        <option value="ADMIN" ${usuario.roleSelected === 'ADMIN' ? 'selected' : ''}>ADMIN</option>
                    </select>   
                </td>
                <td> ${usuario.tentativasCadastro} </td>
                <td class="accion"> 
                <button class="btn btn-accion btn-approve">
                <i class="bi bi-check2"></i>
                Aprovar
                </button> 
                <button class="btn btn-accion btn-refuse">
                <i class="bi bi-x-lg"></i>
                Recusar
                </button> 
                </td>`;
                tabela.appendChild(linha);
            }
        });
    })
    .catch(error => {
        console.error("Erro ao buscar usuários:", error);
    });

tabela.addEventListener("click", function (event) {
    const target = event.target;

    const btnApprove = target.closest(".btn-approve");
    const btnRefuse = target.closest(".btn-refuse");

    if (btnApprove) {
        const linha = btnApprove.closest("tr")
        confirmarAprova(linha);
    }

    if (btnRefuse) {
        const linha = btnRefuse.closest("tr")
        confirmarRecusa(linha);
    }
});

function confirmarAprova(linha) {
    const email = linha.cells[2].textContent.trim();
    const select = linha.querySelector(".role-select")

    const payload = { email };

    if (select) {
        payload.novaRole = select.value;
    }

    Swal.fire({
        title: "Deseja aprovar este usuário?",
        icon: "warning",
        iconHtml: '<i class="bi bi-person-fill-check"></i>',
        showCancelButton: true,
        confirmButtonText: "Aprovar",
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
            aprovarUsuario(payload, linha);
        }
    });
};

function aprovarUsuario(payload, linha) {
    const url = "http://localhost:8080/approve-user";
    const token = localStorage.getItem("token");

    fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(payload)
    })
        .then(response => {
            if (response.ok) {
                Swal.fire({
                    title: "Aprovado!",
                    text: "Usuário aprovado com sucesso.",
                    icon: "success",
                    iconHtml: '<i class="bi bi-person-check-fill"></i>',
                    confirmButtonText: "OK",
                    customClass: {
                        popup: "meu-popup",
                        title: "meu-titulo",
                        htmlContainer: "meu-texto",
                        confirmButton: "meu-botao-ok"
                    }
                }).then(() => {
                    if (linha) linha.remove();
                });
            } else {
                Swal.fire({
                    title: "Erro",
                    text: "Falha ao aprovar o usuário.",
                    icon: "error",
                    iconHtml: '<i class="bi bi-exclamation-triangle-fill"></i>',
                    confirmButtonText: "OK",
                    customClass: {
                        popup: "meu-popup",
                        title: "meu-titulo",
                        htmlContainer: "meu-texto",
                        confirmButton: "meu-botao-cancel"
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
                iconHtml: '<i class="bi bi-x-octagon-fill"></i>',
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

function confirmarRecusa(linha) {
    const email = linha.cells[2].textContent.trim();

    const payload = { email }

    Swal.fire({
        title: "Deseja recusar este usuário?",
        icon: "warning",
        iconHtml: '<i class="bi bi-person-fill-x"></i>',
        showCancelButton: true,
        confirmButtonText: "Recusar",
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
            recusarUsuario(payload, linha);
        }
    });
};

function recusarUsuario(payload, linha) {
    const url = "http://localhost:8080/refuse-user";
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
                    title: "Recusado!",
                    text: "Usuário recusado com sucesso.",
                    icon: "success",
                    iconHtml: '<i class="bi bi-person-x-fill"></i>',
                    confirmButtonText: "OK",
                    customClass: {
                        popup: "meu-popup",
                        title: "meu-titulo",
                        htmlContainer: "meu-texto",
                        confirmButton: "meu-botao-ok"
                    }
                }).then(() => {
                    if (linha) linha.remove();
                });
            } else {
                Swal.fire({
                    title: "Erro",
                    text: "Falha ao recusar o usuário.",
                    icon: "error",
                    iconHtml: '<i class="bi bi-exclamation-triangle-fill"></i>',
                    confirmButtonText: "OK",
                    customClass: {
                        popup: "meu-popup",
                        title: "meu-titulo",
                        htmlContainer: "meu-texto",
                        confirmButton: "meu-botao-cancel"
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
                iconHtml: '<i class="bi bi-x-octagon-fill"></i>',
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
