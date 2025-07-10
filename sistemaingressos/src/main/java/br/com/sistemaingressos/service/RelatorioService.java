package br.com.sistemaingressos.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service
public class RelatorioService {

    private final DataSource dataSource;

    public RelatorioService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public byte[] gerarRelatorioEventos() throws Exception {
    Connection conn = dataSource.getConnection();

    try {
        InputStream relatorioEventosStream = this.getClass().getResourceAsStream("/relatorios/Eventos.jasper");
        InputStream subRelatorioIngressosStream = this.getClass().getResourceAsStream("/relatorios/ingressosSubreport.jasper");
        
        JasperReport subRelatorioIngressos = (JasperReport) JRLoader.loadObject(subRelatorioIngressosStream);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("SUB_RELATORIO_INGRESSOS", subRelatorioIngressos);
        parametros.put("REPORT_CONNECTION", conn);
        
        InputStream logoStream = this.getClass().getResourceAsStream("/images/logo.png");
        parametros.put("LOGO_IMAGE", logoStream);

        JasperPrint print = JasperFillManager.fillReport(relatorioEventosStream, parametros, conn);
        return JasperExportManager.exportReportToPdf(print);

    } finally {
        conn.close();
    }
}
}
