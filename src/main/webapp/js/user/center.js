/**
 * @ignore  =====================================================================================
 * @fileoverview 个人中心
 * @author  寒洲
 * @date 2020/8/16
 * @ignore  =====================================================================================
 */
var params;
var cookies;
/*博客分类*/
var categorys;
$(function () {
    //获取个人博客列表
    params = getParams();
    //获取博客分类，只需要获取一次
    categorys = getCategory();
    var currentPage;
    if (params.p != null) {
        currentPage = params.p;
    } else {
        currentPage = 1;
    }
    if (params.target == null) {
        //当前用户在浏览自己的个人中心
        //获取cookie，截取自己的用户信息
        cookies = getCookieValue(params.userid);
        //获取当前用户的数据，传入自己的userid
        getMyBlogList(params.userid, currentPage, 5);
        getCenterInfo(params.userid);
        //回显个人中心头像
        showAvatar($(".big-avatar"), params.userid);
    } else {
        //如果userid=target说明在访问自己的个人中心
        if (params.userid != null && params.target == params.userid){
            //刷新
            window.location.replace("../../center.html?userid=" + params.userid);
            return;
        }
        //用户在访问他人的个人中心
        //获取被访问的用户的数据，传入被访问的用户的id
        getMyBlogList(params.target, currentPage, 5);
        getCenterInfo(params.target);
        //回显个人中心头像
        showAvatar($(".big-avatar"), params.target);
    }
})

/**
 * 请求获取个人博客列表
 * @param userid
 * @param currentPage
 * @param rows
 */
function getMyBlogList(userid, currentPage, rows) {
    $.ajax({
        //请求类型
        type: "POST",
        //预期服务器返回的数据类型
        dataType: "json",
        url: "/GetUserBlogListServlet",
        data: {
            "userid": userid,
            "current-page": currentPage,
            "rows": rows
        },
        /**
         * @param data
         * @param {Object} data.page 页面信息包
         * @param {Number} data.page.totalCount 总博客数
         * @param {Number} data.page.totalPage 总页数
         * @param {Object} data.page.list 当前页的博客数据
         */
        success: function (data) {
            var totalPage = data.page.totalPage;
            var list = data.page.list;
            //记得传入数字类型，否则在计算页码时会变成字符串拼接
            changePageBtn(Number(currentPage), Number(totalPage));
            createCenterBlogList(list, Number(currentPage), Number(rows));
        },
        error: function () {
            toastr.error("获取博客数据出现意外！请刷新页面或者稍后重新进入本页！");
        }
    });
}

/**
 * 特殊情况下对换页按钮进行操作
 * @param {Number} currentPage
 * @param {Number} totalPage
 */
function changePageBtn(currentPage, totalPage) {
    var urlParam;
    if (params.target == null) {
        //当前用户在浏览自己的个人中心
        urlParam = "?userid=" + params.userid + "&p=";
    } else {
        //访问别人的个人中心，在url的参数中加上target
        if (params.userid != null) {
            //已登录
            urlParam = "?userid=" + params.userid + "&target=" + params.target + "&p=";
        } else {
            //未登录
            urlParam = "?target=" + params.target + "&p=";
        }
    }
    $("#center-previous a").prop("href", urlParam + (currentPage - 1));
    $("#center-next a").prop("href", urlParam + (currentPage + 1));
    //首页禁用上一页
    if (currentPage == 1) {
        $("#center-previous").addClass("disabled");
        $("#center-previous a").prop("href", "javascript:void(0)");
    }
    //尾页禁用下一页
    if (currentPage == totalPage || totalPage == 1 || totalPage == 0) {
        $("#center-next").addClass("disabled");
        $("#center-next a").prop("href", "javascript:void(0)");
    }
}

/**
 * 博客数据显示到页面上
 * @param list 博客数据
 * @param {Number} list.blogNo
 * @param {String} list.title
 * @param {Number} list.updatedate
 * @param {int} list.category
 * @param {String} list.mlabel
 * @param {String} list.slabel
 * @param {Number} currentPage 当前页
 * @param {Number} rows 每页显示的行数
 */
function createCenterBlogList(list, currentPage, rows) {
    //获取list的数据储存在数组里，方便调用
    var arr = [];
    for (var item in list) {
        arr.push(list[item]);
    }
    for (var i = 0; i < arr.length; i++) {
        //获取日期字符串
        var updatedate = dateFormat(arr[i].updatedate);
        //获取用户的博客序号
        var index = (currentPage - 1) * rows + i + 1;
        //获取跳转的url的参数
        var urlParam = "?";
        /*
         * url中会有两种用户id
         * userid是当前用户，target是被访问的用户id
         * userid==null说明未登录
         */
        if (params.userid != null) {
            //登录状态下访问他人的个人中心，跳转时要在url中加上当前用户的id标识
            urlParam += "userid=" + params.userid + "&";
        }
        //未登录下访问他人的个人中心，不用添加userid
        urlParam += "key=" + arr[i].blogNo;

        var str = "<tr>\n" +
            "         <td>" + index + "</td>\n" +
            "         <td>" + arr[i].blogNo + "</td>\n" +
            "         <!--悬浮鼠标显示博客完整标题，下同-->" +
            "         <td title='" + arr[i].title + "'>" + arr[i].title + "</td>\n" +
            "         <td>" + updatedate + "</td>\n" +
            "         <td>" + categorys[arr[i].category-1] + "</td>\n" +
            "         <td title='" + arr[i].mlabel + "'>" + arr[i].mlabel + "</td>\n" +
            "         <td title='" + arr[i].slabel + "'>" + arr[i].slabel + "</td>\n" +
            "         <td>\n" +
            "             <a href=\"blog.html" + urlParam + "\" class=\"btn btn-primary btn-xs\">\n" +
            "                 <span class=\"glyphicon glyphicon-info-sign\"></span>详情\n" +
            "             </a>";

        //如果是访问他人的个人中心，那么不显示编辑按钮和删除按钮。否则显示
        if (params.target == null) {
            str += "<a href=\"write.html" + urlParam + "\" class=\"btn btn-info btn-xs\">\n" +
                "        <span class=\"glyphicon glyphicon-edit\"></span> 编辑\n" +
                "   </a>";
            str += "<a href=\"\" class=\"btn btn-danger btn-xs\" onclick='deleteBlog(" + arr[i].blogNo + ")'>\n" +
                "      <span class=\"glyphicon glyphicon-remove\"></span> 删除\n" +
                "  </a></td></tr>";
        }
        //将字符串添加到表格中
        $(str).appendTo($("thead"));
    }
}

/**
 * 回显用户信息
 * @param userid
 */
function getCenterInfo(userid) {
    //当浏览他人的个人空间时获取不到对应的Cookie，所以要用ajax请求
    $.ajax({
        //请求类型
        type: "POST",
        //预期服务器返回的数据类型
        dataType: "json",
        url: "/GetUserInfoServlet",
        data: {"userid": userid},
        /**
         * @param data
         * @param {Boolean} data.get 是否获取到信息
         * @param {Object} data.user 用户信息
         */
        success: function (data) {
            if (data.get) {
                //显示信息到页面上
                showInfo(data.user);
            } else {
                //获取失败
                toastr.error("获取用户信息失败！请重试！");
            }
        },
        error: function () {
            //登录失败
            toastr.error("获取用户信息失败！请重试！");
        }
    });
}

/**
 * 回显用户信息
 * @param user
 */
function showInfo(user) {
    $("#center-name").text(user.nickname);
    //回显头像
    $(".user-avatar").prop("href", "/UserAvatarServlet?id=" + user.id);
    $(".big-avatar").prop("src", "/UserAvatarServlet?id=" + user.id);
    //其他信息
    $("#center-id").text(user.id);
    $("#center-birthday").text("生日：" + user.birthday);
    if (user.sex) {
        //男为true
        $("#center-sex").text("男");
    } else {
        $("#center-sex").text("女");
    }
}

/**
 * 删除博客
 * @param blogNo
 */
function deleteBlog(blogNo) {
    var b = confirm("确定要删除这条博客吗？");
    if (b) {
        //删除
        $.post("/DeleteBlogServlet", {"blogno": blogNo});
    }
}