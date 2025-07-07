//Evento : Script para carregar estados e cidades dinamicamente
document.addEventListener("DOMContentLoaded", function () {
  const estadoSelect = document.getElementById('estado');
  const cidadeSelect = document.getElementById('cidade');
  const localInput = document.getElementById('local');

  // Carrega estados
  if (estadoSelect) {
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

    //meu:
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

    // da vic:
    //   // Quando muda o estado, carrega as cidades
    //   estadoSelect.addEventListener("change", () => {
    //     const estadoSigla = estadoSelect.value;
    //     cidadeSelect.innerHTML = '<option>Carregando...</option>';

    //     if (!estadoSigla) {
    //       cidadeSelect.innerHTML = '<option value="">-- Selecione uma Cidade --</option>';
    //       return;
    //     }

    //     fetch(`https://servicodados.ibge.gov.br/api/v1/localidades/estados/${estadoSigla}/municipios`)
    //       .then(res => res.json())
    //       .then(cidades => {
    //         cidadeSelect.innerHTML = '<option value="">-- Selecione uma Cidade --</option>';
    //         cidades.forEach(cidade => {
    //           const opt = document.createElement('option');
    //           opt.value = cidade.nome;
    //           opt.textContent = cidade.nome;
    //           cidadeSelect.appendChild(opt);
    //         });
    //       });
    //   });
    // }

    // // Quando cidade for selecionada, atualiza o campo `local`
    // if (cidadeSelect && estadoSelect && localInput) {
    //   cidadeSelect.addEventListener("change", () => {
    //     const cidade = cidadeSelect.value;
    //     const estado = estadoSelect.value;
    //     if (cidade && estado) {
    //       localInput.value = `${cidade} - ${estado}`;
    //     } else {
    //       localInput.value = '';
    //     }
    //   });
    // }

    //Função principal que busca os eventos na barra de pesquisa
    //no backend e atualiza a lista de sugestões
    const input = document.getElementById('search-eventos');
    const lista = document.getElementById('eventos-list');

    if (input && lista) {
      //Função para evitar muitas requisições ao digitar
      function debounce(func, delay) {
        let timeout;
        return function (...args) {
          clearTimeout(timeout);
          timeout = setTimeout(() => func.apply(this, args), delay);
        }
      }

      //Função que busca e atualiza lista
      function buscarEventos() {
        const query = input.value.trim();

        if (query.length < 2) {
          lista.innerHTML = '';
          return;
        }

        fetch(`/eventos/autocomplete?q=${encodeURIComponent(query)}`)
          .then(res => res.json())
          .then(eventos => {
            lista.innerHTML = '';

            if (eventos.length === 0) {
              lista.innerHTML = '<li class="p-2 text-gray-500">Nenhum evento encontrado</li>';
              return;
            }

            eventos.forEach(evento => {
              const li = document.createElement('li');
              li.className = 'p-3 hover:bg-gray-200 cursor-pointer flex flex-col border-b border-gray-300';

              // Linha superior: nome e data
              const linhaSuperior = document.createElement('div');
              linhaSuperior.className = 'flex justify-between items-center mb-1';

              const nome = document.createElement('strong');
              nome.textContent = evento.nome;
              nome.className = 'text-black font-semibold';

              const data = document.createElement('span');
              const dataFormatada = new Date(evento.data).toLocaleDateString('pt-BR');
              data.textContent = dataFormatada;
              data.className = 'text-gray-600 text-sm';

              linhaSuperior.appendChild(nome);
              linhaSuperior.appendChild(data);

              // Local e descrição
              const local = document.createElement('span');
              local.textContent = evento.local;
              local.className = 'text-gray-700 text-sm';

              const descricao = document.createElement('span');
              descricao.textContent = evento.descricao;
              descricao.className = 'text-gray-600 text-xs mt-1';

              li.appendChild(linhaSuperior);
              li.appendChild(local);
              li.appendChild(descricao);

              li.addEventListener('click', () => {
                window.location.href = `/eventos?q=${encodeURIComponent(evento.nome)}`;
              });

              lista.appendChild(li);
            });
          })
          .catch(() => {
            lista.innerHTML = '<li class="p-2 text-red-500">Erro ao buscar eventos</li>';
          });
      }

      input.addEventListener('input', debounce(buscarEventos, 300));

      //Abrir a página nova com resultados filtrados
      input.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
          event.preventDefault();
          const query = input.value.trim();
          if (query.length > 0) {
            window.location.href = `/eventos?q=${encodeURIComponent(query)}`;
          }
        }
      });
    }
  }
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

//Regra de negócio- Ingresso: formata preço para sempre ter 2 casas decimais (ex: 00.00)
document.addEventListener("DOMContentLoaded", () => {
  const precoInput = document.getElementById("preco");

  if (precoInput) {
    precoInput.addEventListener("blur", () => {
      const valor = parseFloat(precoInput.value);
      if (!isNaN(valor)) {
        precoInput.value = valor.toFixed(2);
      }
    });
  }
});

//Regra de negócio - Transação: janela pop de confirmar / cancelar compra
document.addEventListener("DOMContentLoaded", function () {
  // Variável global para armazenar o ID do ingresso selecionado
  let ingressoSelecionadoId = null;

  // Função para abrir o popup de confirmação
  window.abrirPopupCompra = function (id) {
    ingressoSelecionadoId = id;
    const popup = document.getElementById('popupCompra');
    if (popup) {
      popup.classList.remove('hidden');
      popup.classList.add('flex');
    }
  };

  // Função para fechar o popup
  window.fecharPopupCompra = function () {
    const popup = document.getElementById('popupCompra');
    if (popup) {
      popup.classList.add('hidden');
      popup.classList.remove('flex');
    }
    ingressoSelecionadoId = null;
  };

  // Função para confirmar a compra
  window.confirmarCompra = function () {
    if (!ingressoSelecionadoId) return;

    fetch('/transacoes/comprar/' + ingressoSelecionadoId, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-Requested-With': 'XMLHttpRequest'
      }
    })
      .then(response => {
        if (response.ok) {
          window.location.href = '/ingressos'; // redireciona após compra
        } else {
          alert('Erro ao realizar a compra');
        }
      })
      .catch(error => {
        console.error('Erro ao conectar com o servidor:', error);
        alert('Erro ao conectar com o servidor');
      });

    fecharPopupCompra();
  };
});
