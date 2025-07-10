package br.com.sistemaingressos.pagination;

import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

public class PageWrapper<T> {

    private Page<T> page;
    private List<Integer> pageNumbers;
    private HttpServletRequest request;

    public PageWrapper(Page<T> page, HttpServletRequest request) {
        this.page = page;
        this.request = request;
        int totalPages = page.getTotalPages();
        this.pageNumbers = new ArrayList<>();
        for (int i = 0; i < totalPages; i++) {
            pageNumbers.add(i);
        }
    }

    public Page<T> getPage() {
        return page;
    }

    public List<Integer> getPageNumbers() {
        return pageNumbers;
    }

    public String buildPageUrl(int pageNumber) {
        String query = request.getQueryString();
        return request.getRequestURI() + "?page=" + pageNumber +
                (query != null ? "&" + query : "");
    }

    public List<T> getContent() {
        return page.getContent();
    }

    public boolean isEmpty() {
        return page.isEmpty();
    }
}