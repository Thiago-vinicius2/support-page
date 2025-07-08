document.getElementById("btn-login")?.addEventListener('click', async () => {
    const email = document.getElementById('login-email').value;
    const senha = document.getElementById('login-senha').value;

    if (!email || !senha) {
        Swal.fire({
            title: "Atenção!",
            text: "Preencha todos os campos",
            icon: "info",
            iconHtml: "<i class='bi bi-info-lg'></i>",
            confirmButtonText: 'OK',
            customClass: {
                popup: "meu-popup",
                title: "meu-titulo",
                htmlContainer: "meu-texto",
                confirmButton: "meu-botao"
            }
        });
        return;
    }
    try {
        const response = await fetch('http://localhost:8080/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, senha })
        });
        if (response.ok) {
            const data = await response.json();

            localStorage.setItem('token', data.token);
            Swal.fire({
                title: "Sucesso",
                text: "Login realizado!",
                timer: 1500,
                icon: "success",
                iconHtml: '<i class="bi bi-check-lg"></i>',
                showConfirmButton: false,
                customClass: {
                    popup: "meu-popup",
                    title: "meu-titulo",
                    htmlContainer: "meu-texto",
                    icon: "meu-icone"
                }
            }).then(() => {
                window.location.href = '/src/main/resources/static/index/index.html';
            });
        } else {
            console.log("Código de status:", response.status);
            let errorMessage = 'Erro inesperado.';
            try {
                const errorData = await response.json();
                errorMessage = errorData.message || errorMessage;
            } catch (jsonErr) {
                console.warn('Não foi possível interpretar o corpo da resposta como JSON.', jsonErr);
            }
            if (response.status === 401 || response.status === 400) {
                Swal.fire({
                    title: 'Credenciais inválidas!',
                    text: 'E-mail ou senha incorretos.',
                    icon: 'error',
                    iconHtml: '<i class="bi bi-person-fill-slash"></i>',
                    confirmButtonText: 'OK',
                    customClass: {
                        popup: "meu-popup",
                        title: "meu-titulo",
                        htmlContainer: "meu-texto",
                        confirmButton: "meu-botao",
                    }
                });
            } else {
                Swal.fire({
                    title: 'Erro ao fazer login!',
                    text: errorMessage,
                    icon: 'error',
                    iconHtml: '<i class="bi bi-exclamation-lg"></i>',
                    customClass: {
                        popup: "meu-popup",
                        title: "meu-titulo",
                        htmlContainer: "meu-texto",
                        confirmButton: "meu-botao",
                    }
                });
            }
        }
    } catch (err) {
        console.error('Erro na requisição: ', err);
        Swal.fire({
            title: 'Erro de rede!',
            text: 'Não foi possível conectar ao servidor.',
            icon: 'info',
            iconHtml: '<i class="bi bi-exclamation-lg"></i>',
            confirmButtonText: 'OK',
            customClass: {
                popup: "meu-popup",
                title: "meu-titulo",
                htmlContainer: "meu-texto",
                confirmButton: "meu-botao"
            }
        });
    }
});
