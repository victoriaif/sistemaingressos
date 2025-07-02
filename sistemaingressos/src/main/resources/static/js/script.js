//Script para carregar estados e cidades dinamicamente

document.addEventListener("DOMContentLoaded", function () {
  const estadoSelect = document.getElementById('estado');
  const cidadeSelect = document.getElementById('cidade');

  //Carrega os estados
  fetch('https://servicodados.ibge.gov.br/api/v1/localidades/estados?orderBy=nome')
    .then(response => response.json())
    .then(estados => {
      estados.forEach(estado => {
        const option = document.createElement('option');
        option.value = estado.id;
        option.textContent = estado.nome;
        estadoSelect.appendChild(option);
      });
    });

  //Ao selecionar estados, carrega as cidades
  estadoSelect.addEventListener('change', function () {
    const estadoId = this.value;
    cidadeSelect.innerHTML = '<option value="">-- Carregando cidades... --</option>';

    if (!estadoId) {
      cidadeSelect.innerHTML = '<option value="">-- Selecione uma Cidade --</option>';
      return;
    }

    fetch(`https://servicodados.ibge.gov.br/api/v1/localidades/estados/${estadoId}/municipios`)
      .then(response => response.json())
      .then(cidades => {
        cidadeSelect.innerHTML = '<option value="">-- Selecione uma Cidade --</option>';
        cidades.forEach(cidade => {
          const option = document.createElement('option');
          option.value = cidade.nome;
          option.textContent = cidade.nome;
          cidadeSelect.appendChild(option);
        });
      })
      .catch(error => {
        console.error('Erro ao carregar cidades:', error);
        cidadeSelect.innerHTML = '<option value="">Erro ao carregar cidades</option>';
      });
  });
});
