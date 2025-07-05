//Script para carregar estados e cidades dinamicamente

document.addEventListener("DOMContentLoaded", function () {
  const estadoSelect = document.getElementById('estado');
  const cidadeSelect = document.getElementById('cidade');
  const localInput = document.getElementById('local');

  // Carrega estados
  fetch('https://servicodados.ibge.gov.br/api/v1/localidades/estados?orderBy=nome')
    .then(res => res.json())
    .then(estados => {
      estados.forEach(estado => {
        const opt = document.createElement('option');
        opt.value = estado.sigla; // ← PEGA SIGLA, tipo "MG"
        opt.textContent = estado.nome;
        estadoSelect.appendChild(opt);
      });
    });

  // Quando muda o estado, carrega as cidades
  estadoSelect.addEventListener("change", () => {
    const estadoSigla = estadoSelect.value;
    cidadeSelect.innerHTML = '<option>Carregando...</option>';

    if (!estadoSigla) return;

    fetch(`https://servicodados.ibge.gov.br/api/v1/localidades/estados/${estadoSigla}/municipios`)
      .then(res => res.json())
      .then(cidades => {
        cidadeSelect.innerHTML = '<option value="">-- Selecione uma Cidade --</option>';
        cidades.forEach(cidade => {
          const opt = document.createElement('option');
          opt.value = cidade.nome;
          opt.textContent = cidade.nome;
          cidadeSelect.appendChild(opt);
        });
      });
  });

  // Quando cidade for selecionada, atualiza o campo `local`
  cidadeSelect.addEventListener("change", () => {
    const cidade = cidadeSelect.value;
    const estado = estadoSelect.value;
    if (cidade && estado) {
      localInput.value = `${cidade} - ${estado}`;
    }
  });
});


//Regra de negócio- Ingresso: Preencher campos data e local a partir do Evento selecionado
document.addEventListener("DOMContentLoaded", function () {
  const eventoSelect = document.getElementById("evento");
  const dataInput = document.getElementById("data");
  const localInput = document.getElementById("local");

  if (eventoSelect && dataInput && localInput) {
    eventoSelect.addEventListener("change", function () {
      const eventoId = this.value;
      if (!eventoId) return;

      fetch(`/eventos/detalhes/${eventoId}`)
        .then(response => response.json())
        .then(evento => {
          dataInput.value = evento.data;   // deve vir em yyyy-MM-dd
          localInput.value = evento.local;
        })
        .catch(error => console.error("Erro ao buscar evento:", error));
    });
  }
});