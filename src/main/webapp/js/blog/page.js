/**
 * @ignore  =====================================================================================
 * @fileoverview 分页
 * @author  寒洲
 * @date 2020/11/16
 * @ignore  =====================================================================================
 */
/**
 * 创建分页按钮组
 * @param {Number} currentPage 当前页码
 * @param {Number} totalPage 总页码
 */
function createPage(currentPage, totalPage) {
    //获取分页栏
    var pageArea = $("#page-area");
    //用append添加“上一页”的HTML
    pageArea.append("<li id='page-previous'>" +
        "<a href='index.html" + getParamFroUrl(currentPage - 1) + "' aria-label='Previous'>" +
        "<span aria-hidden='true'>&laquo;</span></a></li>");
    //如果当前页大于等于4，那么下面的for循环会从第2页创建，这里补第1页
    if ((currentPage >= 4 && totalPage >= 4)) {
        pageArea.append("<li><a href='index.html" + getParamFroUrl(1) + "'>" + 1 + "</a></li>");
    }
    //如果当前页大于4，下面的for循环会从第3页创建，在第1页和第3页直接补省略号
    if (currentPage > 4 && currentPage <= totalPage && totalPage > 5) {
        pageArea.append("<li><span>...</span></li>");
    }
    // 中间页数利用循环生成
    /*
    生产当前页以及其前两页和后两页
    如果当前页小于4，第1页会在这里生成
    如果大于等于4，那么第1页会在上面的if语句生成
    其他的用省略号替代
     */
    var start = currentPage - 2;
    var end = currentPage + 2;
    for (; start <= end; start++) {
        //start>=1使页码从1开始
        if (start <= totalPage && start >= 1) {
            if (start != currentPage) {
                pageArea.append("<li><a href='index.html" + getParamFroUrl(start) + "'>" + start + "</a></li>");
            } else {
                //当前页添加选中样式
                pageArea.append("<li class='active'><span>" + start + "</span></li>");
            }
        }
    }
    // 判断临界值插入省略号
    /*
    当前页+2页就是已生成的页码，总页数-1页就是 可能 要添加省略号的页码
    如果currentPage + 2 < totalPage - 1，说明总页数上一页没有生成页码
    那么就要添加省略号
    特别的，当总页数不大于5页时，上面的for循环会生成所有需要的页码，不用添加省略号
     */
    if (currentPage + 2 < totalPage - 1 && totalPage > 5) {
        pageArea.append("<li><span>...</span></li>")
    }
    /*
    和第1页一样，最后一页同样需要判断后再决定要不要添加
     */
    if ((currentPage < totalPage - 2 && totalPage >= 4)) {
        pageArea.append("<li><a href='index.html" + getParamFroUrl(totalPage) + "'>" + totalPage + "</a></li>");
    }
    // }
    var str = "<li id=\"page-next\">" +
        "           <a href='index.html" + getParamFroUrl(currentPage + 1) + "' aria-label=\"Next\">" +
        "               <span aria-hidden=\"true\">&raquo;</span>" +
        "           </a>" +
        "      </li>";
    pageArea.append(str);
    if (currentPage == 1) {
        //在第1页时不能点击“上一页”
        $("#page-previous").addClass("disabled");
        $("#page-previous a").prop("href", "javascript:void(0)");
    }
    //不要用else if 避免了只有一页的情况
    if (currentPage == totalPage) {
        //在最后一页时不能点击“下一页”
        $("#page-next").addClass("disabled");
        $("#page-next a").prop("href", "javascript:void(0)")
    }
}