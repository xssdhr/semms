package com.xssdhr.entity;

/**
 * @author xssdhr
 * @9/3/2023 下午10:40
 * @Company 南京点子跳跃传媒有限公司
 */
public class PageBean {
    private int pageNum; // 第几页
    private int pageSize; // 每页记录数
    private int start; // 起始页
    private String query; // 查询参数
    private String queryId; //用户id&表单id
    private boolean isHide; //是否隐藏作废单据（2-审核通过 3-审核不通过'）
    public boolean isHide() {
        return isHide;
    }

    public void setHide(boolean hide) {
        isHide = hide;
    }







    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public PageBean() {
    }
    public PageBean(int pageNum, int pageSize, String query) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.query = query;
    }
    public PageBean(int pageNum, int pageSize) {
        super();
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
    public int getPageNum() {
        return pageNum;
    }
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getStart() {
        return (pageNum-1)*pageSize;
    }
    public String getQuery() {
        return query;
    }
    public void setQuery(String query) {
        this.query = query;
    }
}