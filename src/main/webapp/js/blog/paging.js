/**
 * @ignore  =====================================================================================
 * @fileoverview 获取页面数据及分页
 * @author  寒洲
 * @date 2020/8/13
 * @ignore  =====================================================================================
 */
/*当前页*/
var currentPage;
/**
 * 参数对象
 * @param params.userid 用户id
 * @param params.p 当前页码
 * @param params.wd 搜索框输入的搜索词
 * @param params.cy 分类搜索
 * @param params.ml 主要标签搜索
 * @param params.sl 次要标签搜索
 */
var params;
/*博客分类名数组*/
var categorys;
//初始化
$(function () {
    params = getParams();

    //获取当前页码
    if (params.p != null && params.p != "") {
        // 从参数中获取page的值
        currentPage = Number(params.p);
    } else {
        currentPage = 1;
    }
    //初始化时加载页面信息
    getPageMsg(currentPage, 5, params.wd, params.cy,params.ml,params.sl);
    //获取博客分类名，只需要在这里获取一次
    categorys = getCategory();
})


/**
 * 请求获取分页信息和博客数据
 * @param currentPage 当前页码
 * @param rows 每页显示的博客数
 * @param search 搜索的内容
 * @param category 搜索的分类
 * @param mlabel 搜索的主要标签
 * @param slabel 搜索的次要标签
 */
function getPageMsg(currentPage, rows, search,category,mlabel,slabel) {
    $.ajax({
        //方法类型
        type: "POST",
        //预期服务器返回的数据类型
        dataType: "json",
        url: "/GetPageServlet",
        data: {
            "current-page": currentPage,
            "rows": rows,
            "search": search,
            "category": category,
            "mlabel": mlabel,
            "slabel": slabel
        },
        /**
         * @param data
         * @param {Object} data.page 页面信息包
         * @param {Number} data.page.totalPage 总页数
         * @param {Object} data.page.list 当前页的博客数据
         */
        success: function (data) {
            var totalPage = data.page.totalPage;
            if (totalPage == 0) {
                alert("无匹配项！");
                window.close();
                // window.location.href = "../../index.html";
            } else {
                var list = data.page.list;
                createPage(currentPage, totalPage);
                createBlogList(list, rows);
            }
        },
        error: function () {
            toastr.error("获取博客数据出现意外！请重试！");
        }
    });
}

/**
 * 形成url的参数
 * @param {Number} toPage 前往的页码数
 * @returns {string}
 * 例子：
 *      有userid：?userid=123123&p=2
 *      无userid：?p=2
 */
function getParamFroUrl(toPage) {
    var result = "?";
    //判断并获取userid
    var userid = params.userid;
    var word = params.wd;
    var cy = params.cy;
    var ml = params.ml;
    var sl = params.sl;

    if (userid != null) {
        result += "userid=" + userid + "&";
    }

    if (word != null) {
        result += "wd=" + word + "&";
    }else if(cy != null){
        result += "cy=" + cy + "&";
    }else if(ml != null){
        result += "ml=" + ml + "&";
    }else if(sl != null){
        result += "sl=" + sl + "&";
    }
    //添加页码参数
    result += "p=" + toPage;
    return result;
}

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
    if ((currentPage >= 4 && totalPage >= 4) ) {
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

/**
 * 创建博客列表
 * @param list
 * @param {Number} list.blogNo
 * @param {Number} list.authorId
 * @param {String} list.title
 * @param {Number} list.updatedate
 * @param {int} list.category
 * @param {String} list.mlabel
 * @param {String} list.slabel
 * @param {Number} rows
 */
function createBlogList(list, rows) {
    //获取list的数据储存在数组里，方便调用
    var arr = [];
    for (var item in list) {
        arr.push(list[item]);
    }
    for (var i = 0; i < arr.length; i++) {
        //获取修改时间
        /*
        实测证明从后端获取的日期对象时以毫秒数发送的
        但是获取的毫秒数不能够直接使用
        通过dateFormat格式化获取字符串

         */
        var updatedate = dateFormat(arr[i].updatedate);
        //将类别索引转为字符串
        var category = categorys[arr[i].category - 1];

        //跳转页面是的url参数
        var urlParaml = "?";
        if (params.userid != null) {
            urlParaml += "userid=" + params.userid + "&";
        }
        //key=后加上博客编号，在str中添加
        urlParaml += "key=";

        var str = "<!--一条博客-->" +
            "        <div class=\"blog-item row\">" +
            "            <!--左侧 博客信息-->" +
            "            <div class=\"col-xs-7 col-sm-6 col-sm-offset-1 col-md-7 col-md-offset-1\">" +
            "                <div class=\"row\">" +
            "                    <!--博客标题-->" +
            "                    <div class=\"title col-xs-8 col-sm-8 col-md-7\">" +
            "                       <!--url中博客id用key表示，方便与userid区分。添加title，鼠标悬浮显示所有内容-->" +
            "                        <a href='blog.html" + urlParaml + arr[i].blogNo + "' title='" + arr[i].title + "'>" + arr[i].title + "</a>" +
            "                    </div>" +
            "                    <!--博客类别和标签-->" +
            "                    <div class=\"col-xs-3 col-sm-4 col-md-5\">" +
            "                        <div class=\"blog-label\">" +
            "                            <span class=\"label label-primary\" onclick=\"toSearch('cy'," + arr[i].category + ")\">" + category + "</span>" +
            "                            <span class=\"label label-info\" onclick=\"toSearch('ml','" + arr[i].mlabel + "')\">" + arr[i].mlabel + "</span>" +
            "                            <span class=\"label label-info\" onclick=\"toSearch('sl','" + arr[i].slabel + "')\">" + arr[i].slabel + "</span>" +
            "                        </div>" +
            "                    </div>" +
            "                </div>" +
            "                <!--博客时间-->" +
            "                <div class=\"datetime\">" + updatedate + "</div>" +
            "            </div>" +
            "            <!--右侧 博客作者  text-center-->" +
            "            <div class=\" col-xs-5 col-sm-4\">" +
            "                <div class=\"col-xs-2 col-sm-2\">" +
            "                    <img class=\"author-img\" src=\"UserAvatarServlet?id=";
        //判断账号是否已注销
        if (arr[i].authorId != null) {
            str += arr[i].authorId;
            //添加点击事件
            str += "\" onclick=\"toUserCenter(" + arr[i].authorId + ")\"";
        } else {
            //账号已注销
            str += "cancelled";
        }
        str += "\"/></div></div></div>";

        //追加信息
        $(".blog-list").append(str);
    }
}
