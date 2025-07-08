const tabela = document.getElementById("tabela-usuarios");

let url = "http://localhost:8080/all-user";

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

            if (usuario.role === "CLIENTE") {
                linha.innerHTML = `
                    <td> ${index + 1} </td>
                    <td> ${usuario.nome} </td>
                    <td> ${usuario.email} </td>
                    <td> ${usuario.role} </td>
                    <td> 
                    <button class="btn btn-edit">
                    <i class="bi bi-pencil"></i>
                    Editar
                    </button> </td>`;

                tabela.appendChild(linha);
            }
        });
    })
    .catch(error => {
        console.error("Erro ao buscar usuários:", error);
    });
