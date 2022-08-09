package com.riven.util.page;
import com.riven.core.page.PageDomain;
import com.riven.core.page.TableSupport;
import com.riven.util.sql.SqlUtil;

/**
 * 分页工具类
 * 
 * @author ishangu
 */
public class PageUtils
{
    /**
     * 设置请求分页数据
     */
    public static void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();

    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage()
    {

    }
}
