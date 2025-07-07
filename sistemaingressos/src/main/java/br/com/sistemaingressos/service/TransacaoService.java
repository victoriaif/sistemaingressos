package br.com.sistemaingressos.service;

import br.com.sistemaingressos.model.Ingresso;
import br.com.sistemaingressos.model.StatusIngresso;
import br.com.sistemaingressos.model.Transacao;
import br.com.sistemaingressos.model.Usuario;
import br.com.sistemaingressos.repository.IngressoRepository;
import br.com.sistemaingressos.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final UsuarioService usuarioService;
    private final IngressoRepository ingressoRepository;

    public TransacaoService(
            TransacaoRepository transacaoRepository,
            UsuarioService usuarioService,
            IngressoRepository ingressoRepository) {
        this.transacaoRepository = transacaoRepository;
        this.usuarioService = usuarioService;
        this.ingressoRepository = ingressoRepository;
    }

    public void salvar(Transacao transacao) {
        transacaoRepository.save(transacao);
    }

    public void alterar(Transacao transacao) {
        transacaoRepository.save(transacao);
    }

    public void excluir(Long id) {
        if (!transacaoRepository.existsById(id)) {
            throw new RuntimeException("Transação com ID inválido");
        }
        transacaoRepository.deleteById(id);
    }

    public Transacao buscarPorId(Long id) {
        return transacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação com ID inválido"));
    }

    public List<Transacao> listarTodas() {
        return transacaoRepository.findAll();
    }

    // Rega de negocio - Transacao: comprar ingresso
    public void comprarIngresso(Long idIngresso) {
        // Aqui você implementa a lógica para:
        // 1. Buscar o ingresso
        // 2. Verificar se está disponível
        // 3. Setar o comprador
        // 4. Marcar como vendido
        // 5. Criar e salvar a transação

        // Exemplo básico (você pode expandir depois):
        Ingresso ingresso = ingressoRepository.findById(idIngresso)
                .orElseThrow(() -> new RuntimeException("Ingresso não encontrado"));

        if (ingresso.getStatus() != StatusIngresso.DISPONIVEL) {
            throw new RuntimeException("Ingresso não está disponível para compra");
        }

        Usuario comprador = usuarioService.getUsuarioLogado();

        Transacao transacao = new Transacao();
        transacao.setDataHora(LocalDateTime.now());
        transacao.setComprador(comprador);
        transacao.setVendedor(ingresso.getUsuarioAnunciante());
        transacao.setIngresso(ingresso);
        transacao.setValor(ingresso.getPreco());

        ingresso.setStatus(StatusIngresso.VENDIDO);

        transacaoRepository.save(transacao);
    }

}
