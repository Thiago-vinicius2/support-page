export function filtroTabela(inputSelector, tabelaSelector) {
    const inputPesquisa = document.querySelector(inputSelector);

    if (!inputPesquisa) return;

    inputPesquisa.addEventListener("input", function () {

        const valor = inputPesquisa.value.toLowerCase();
        const linhas = document.querySelectorAll(`${tabelaSelector} tr`);

        linhas.forEach(linha => {
            const textoLinha = linha.textContent.toLowerCase();

            if (textoLinha.includes(valor)) {
                linha.style.display = ""
            }
            else {
                linha.style.display = "none"
            }
        });
    });
}