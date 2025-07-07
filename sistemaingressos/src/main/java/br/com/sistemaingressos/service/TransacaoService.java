package br.com.sistemaingressos.service;

import br.com.sistemaingressos.model.Ingresso;
import br.com.sistemaingressos.model.StatusIngresso;
import br.com.sistemaingressos.model.Transacao;
import br.com.sistemaingressos.model.Usuario;
import br.com.sistemaingressos.repository.IngressoRepository;
import br.com.sistemaingressos.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
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

    // Rega de negocio - Transacao: comprar ingresso:  
    //busca ingresso, verifica status disponivel, seta comprador, marca status como vendido, salva no banco
    public void comprarIngresso(Long idIngresso) {  

        Ingresso ingresso = ingressoRepository.findById(idIngresso)
                .orElseThrow(() -> new RuntimeException("Ingresso não encontrado"));
        Usuario comprador = usuarioService.getUsuarioLogado();
        Usuario vendedor = ingresso.getUsuarioAnunciante();

        // Regra de negócio: não é possivel comprar ingresso status diferente de "Disponivel" (ou seja: vendido / expirado)
        if (ingresso.getStatus() != StatusIngresso.DISPONIVEL) {
            throw new RuntimeException("Ingresso não está disponível para compra");
        }

        // Regra de negócio: não é possivel comprar ingresso com data anterior à hoje
        if (ingresso.getData().isBefore(LocalDate.now())) {
            throw new RuntimeException("Não é possível comprar ingressos com data passada.");
        }

        // Regra de negócio: o usuário comprador não pode ser o próprio anunciante       
        if (comprador.getId().equals(vendedor.getId())) {
            throw new RuntimeException("Você não pode comprar um ingresso que você mesmo anunciou.");
        }

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
