document.addEventListener("DOMContentLoaded", () => {
    const inputTitulo = document.getElementById("form-title");
    const inputDescricao = document.getElementById("form-desc");
    const btnEnviar = document.getElementById("btn-new-ticket");

    btnEnviar?.addEventListener("click", async () => {
        const titulo = inputTitulo.value.trim();
        const descricao = inputDescricao.value.trim();

        if (!titulo || !descricao) {
            Swal.fire({
                title: "Atenção!",
                text: "Preencha todos os campos.",
                icon: "info",
                iconHtml: "<i class='bi bi-info-lg'></i>",
                confirmButtonText: "OK",
                customClass: {
                    popup: "meu-popup",
                    title: "meu-titulo",
                    htmlContainer: "meu-texto",
                    confirmButton: "meu-botao-ok"
                }
            });
            return;
        }

        const token = localStorage.getItem("token");
        if (!token) {
            Swal.fire({
                title: "Faça login novamente.",
                text: "Não foi possivel identificar o usuário",
                icon: "warning",
                iconHtml: "<i class='bi bi-shield-exclamation'></i>",
                confirmButtonText: "OK",
                customClass: {
                    popup: "meu-popup",
                    title: "meu-titulo",
                    htmlContainer: "meu-texto",
                    confirmButton: "meu-botao-ok"
                }
            });
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/create-ticket", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify({ titulo, descricao })
            });

            let responseBody;
            try {
                responseBody = await response.json();
            } catch {
                responseBody = { message: "Erro inesperado." };
            }

            if (response.ok) {
                Swal.fire({
                    title: "Sucesso!",
                    text: responseBody.message,
                    icon: "success",
                    iconHtml: '<i class="bi bi-clipboard2-check"></i>',
                    confirmButtonText: "OK",
                    customClass: {
                        popup: "meu-popup",
                        title: "meu-titulo",
                        htmlContainer: "meu-texto",
                        icon: "meu-icone",
                        confirmButton: "meu-botao-ok"
                    }
                }).then(() => {
                    inputTitulo.value = "";
                    inputDescricao.value = "";
                });
            } else {
                Swal.fire({
                    title: "Erro!",
                    text: responseBody.message,
                    icon: "error",
                    iconHtml: '<i class="bi bi-x-circle-fill"></i>',
                    confirmButtonText: "OK",
                    customClass: {
                        popup: "meu-popup",
                        title: "meu-titulo",
                        htmlContainer: "meu-texto",
                        icon: "meu-icone",
                        confirmButton: "meu-botao-ok"
                    }
                });
            }
        } catch (error) {
            console.error("Erro na requisição:", error);
            Swal.fire({
                title: "Erro de rede!",
                text: "Não foi possível conectar ao servidor.",
                icon: "info",
                iconHtml: "<i class='bi bi-exclamation-triangle'></i>",
                confirmButtonText: "OK",
                customClass: {
                    popup: "meu-popup",
                    title: "meu-titulo",
                    htmlContainer: "meu-texto",
                    confirmButton: "meu-botao-ok"
                }
            });
        }
    });
});
