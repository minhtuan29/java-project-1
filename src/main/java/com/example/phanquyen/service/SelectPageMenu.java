package com.example.phanquyen.service;

public class SelectPageMenu {
    private static int PAGES_NUM = 5;
    private static int BEFORE_AFTER = PAGES_NUM/2;
    private int startPage;
    private int endPage;

    public SelectPageMenu(int totalPages, int currentPage) {

        this.startPage = currentPage - BEFORE_AFTER;
        this.endPage = currentPage + BEFORE_AFTER;

        // if curpage = [1,2]
        if (currentPage < BEFORE_AFTER + 1) { // if over fist
            this.startPage = 1;
            this.endPage = PAGES_NUM;
        }
        if (currentPage + BEFORE_AFTER > totalPages) { // if over last
            this.endPage = totalPages;
            this.startPage = totalPages - PAGES_NUM+ 1;
        }

    }


    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }
}
