package br.com.sistemaingressos.pagination;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;

public class PageWrapper<T> {

    private final Page<T> page;
    private final List<Integer> pageNumbers;
    private final String baseUrl;

    public PageWrapper(Page<T> page, HttpServletRequest request) {
        this.page = page;

        // monta a lista de índices de página (0 a totalPages-1)
        int totalPages = page.getTotalPages();
        this.pageNumbers = new ArrayList<>(totalPages);
        for (int i = 0; i < totalPages; i++) {
            pageNumbers.add(i);
        }

        // reconstrói a URL sem o parâmetro "page="
        String query = request.getQueryString();
        String uri   = request.getRequestURI();

        if (query == null) {
            this.baseUrl = uri + "?";
        } else {
            // remove qualquer "page=" que já exista
            String cleaned = 
                java.util.Arrays.stream(query.split("&"))
                                .filter(p -> !p.startsWith("page="))
                                .collect(java.util.stream.Collectors.joining("&"));
            this.baseUrl = uri + "?" + (cleaned.isEmpty() ? "" : cleaned + "&");
        }
    }

    /** Retorna o Page<T> original */
    public Page<T> getPage() {
        return page;
    }

    /** Lista de índices de página (zero-based) */
    public List<Integer> getPageNumbers() {
        return pageNumbers;
    }

    /** Constroi a URL para a página indicada */
    public String buildPageUrl(int pageNumber) {
        return baseUrl + "page=" + pageNumber;
    }

    /** Conteúdo da página atual */
    public List<T> getContent() {
        return page.getContent();
    }

    /** Se não há nenhum elemento */
    public boolean isEmpty() {
        return page.isEmpty();
    }

    /** Se existe página anterior */
    public boolean hasPrevious() {
        return page.hasPrevious();
    }

    /** Se existe próxima página */
    public boolean hasNext() {
        return page.hasNext();
    }

    /** Índice da página atual (zero-based) */
    public int getNumber() {
        return page.getNumber();
    }

    /** Total de páginas */
    public int getTotalPages() {
        return page.getTotalPages();
    }
}
