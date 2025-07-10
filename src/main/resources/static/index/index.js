document.getElementById('btn-logout').addEventListener('click', function () {
    Swal.fire({
        title: "Deseja sair?",
        icon: "warning",
        iconHtml: '<i class="bi bi-reply-fill"></i>',
        showCancelButton: true,
        cancelButtonColor: "#d33",
        confirmButtonText: "Sim, quero sair!",
        cancelButtonText: "Cancelar",
        customClass: {
            popup: "meu-popup",
            title: "meu-titulo",
            confirmButton: "meu-botao-ok",
            cancelButton: "meu-botao-cancel"
        }
    }).then((result) => {
        if (result.isConfirmed) {
            localStorage.removeItem('token');
            window.location.href = '/src/main/resources/static/login/login.html';
        }
    });

});