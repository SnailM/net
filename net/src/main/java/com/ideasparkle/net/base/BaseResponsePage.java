package com.ideasparkle.net.base;

public class BaseResponsePage {
    private String count;
    private String totalPage;
    private String pageSize;
    private String pageNo;
    private int programNum;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public int getProgramNum() {
        return programNum;
    }

    public void setProgramNum(int programNum) {
        this.programNum = programNum;
    }
}
