package br.com.sistemaingressos.controller;

import br.com.sistemaingressos.service.RelatoriosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RelatoriosController {

    @Autowired
    private RelatoriosService relatoriosService;

    @GetMapping("/relatorios/eventos-ingressos")
public ResponseEntity<byte[]> gerarRelatorioEventoIngresso() {
    byte[] relatorio = relatoriosService.gerarRelatorioEventoIngresso();

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=evento-ingressos.pdf")
            .body(relatorio);
}
}
