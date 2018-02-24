package com.css.activiti.model;

import java.util.List;

/**
 * Created by 46597 on 2018/2/24.
 */
public class PageInfo<T> {


    public Integer pageSize = 5 ;
    private Integer count ;
    private List<T>  pageList ;
    private Integer pageIndex; //当前也号
    private Integer totalPages;//总页数

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<T> getPageList() {
        return pageList;
    }

    public void setPageList(List<T> pageList) {
        this.pageList = pageList;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    //总页数可以根据其它值计算出来  有点忘记除法运算了 。。。
    public Integer getTotalPages() {

        totalPages = count /pageSize;
        if(count % pageSize != 0 ){
            this.totalPages ++ ;
        }
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
