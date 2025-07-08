document.getElementById("btn-cadastro")?.addEventListener('click', async () => {
    const nome = document.getElementById('cadastro-nome').value;
    const email = document.getElementById('cadastro-email').value;
    const senha = document.getElementById('cadastro-senha').value;
    const role = document.getElementById('cadastro-role').value;

    if (!nome || !email || !senha || !role) {
      Swal.fire({
        title: "Atenção!",
        text: "Preencha todos os campos.",
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
      const token = localStorage.getItem('token');
      const response = await fetch('http://localhost:8080/create-user', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ nome, email, senha, roleSelected: role })
      });

      let responseBody;
      try {
        responseBody = await response.json();
      } catch {
        responseBody = { message: "Erro inesperado" };
      }

      if (response.ok) {
        Swal.fire({
          title: "Sucesso!",
          text: responseBody.message,
          icon: "success",
          iconHtml: '<i class="bi bi-person-fill-check"></i>',
          confirmButtonText: 'OK',
          customClass: {
            popup: "meu-popup",
            title: "meu-titulo",
            htmlContainer: "meu-texto",
            icon: "meu-icone",
            confirmButton: "meu-botao"
          }
        }).then(() => {
          document.getElementById('cadastro-nome').value = '';
          document.getElementById('cadastro-email').value = '';
          document.getElementById('cadastro-senha').value = '';
          document.getElementById('cadastro-role').value = '';
        });
      } else {
        Swal.fire({
          title: 'Erro ao cadastrar!',
          text: responseBody.message,
          icon: 'error',
          iconHtml: '<i class="bi bi-person-fill-x"></i>',
          confirmButtonText: 'OK',
          customClass: {
            popup: "meu-popup",
            title: "meu-titulo",
            htmlContainer: "meu-texto",
            icon: "meu-icone",
            confirmButton: "meu-botao"
          }
        });
      }
    } catch (err) {
      console.error('Erro na requisição', err);
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