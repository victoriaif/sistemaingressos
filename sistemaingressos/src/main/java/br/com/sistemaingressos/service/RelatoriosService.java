package br.com.sistemaingressos.service;

import br.com.sistemaingressos.model.Evento;
import br.com.sistemaingressos.repository.EventoRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatoriosService {

    private final EventoRepository eventoRepository;

    public RelatoriosService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public byte[] gerarRelatorioEventoIngresso() {
        try {
            InputStream relatorioStream = this.getClass().getResourceAsStream("/relatorios/evento.jasper");

            String subreportDir = this.getClass().getResource("/relatorios/").toURI().getPath() + "/";

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("SUBREPORT_DIR", subreportDir);
            
        
            List<Evento> listaEventos = eventoRepository.findAll();

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listaEventos);

            JasperPrint jasperPrint = JasperFillManager.fillReport(relatorioStream, parametros, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório de evento com ingressos", e);
        }
    }
}
