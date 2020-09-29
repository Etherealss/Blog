/**
 * @ignore  =====================================================================================
 * @fileoverview 操作博客时的js工具代码
 * @author  寒洲
 * @date 2020/8/16
 * @ignore  =====================================================================================
 */
/**
 * 前往作者的个人中心，配置url参数
 * @param targetId
 */
toUserCenter = function(targetId) {
    var urlParam = "?";
    var params = getParams();
    if (params.userid != null){
        //已登录，url中加入userid参数。userid是已登录用户固有的url参数
        urlParam += "userid=" + params.userid;
        //判断用户是否要浏览自己的个人中心
        if(targetId != params.userid){
            //浏览别人的个人中心，补充上浏览的对象的id，参数用target表示
            //记得带 “ & ”
            urlParam += "&target=" + targetId;
        }
    }else {
        //用户未登录，此处访问别人的个人中心
        urlParam += "target=" + targetId;
    }

    //否则为用户通过博客作者进入自己的个人中心
    //则不添加targetId，直接前往自己的个人中心
    window.location.href = "../../center.html" + urlParam;
}

/**
 * 获取博客分类名
 */
getCategory = function() {
    var result = [];
    $.ajax({
        type: "POST",
        //同步
        async: false,
        url: "/GetCategoryServlet",
        dataType: "json",
        /**
         * @param data
         * @param {Array} data.category
         * @returns {Array} 博客分类名数组
         */
        success: function (data) {
            result = data.category;
        }
    });
    return result;
}

/**
 * 获取博客信息
 * @param blogNo
 * @returns {*} 博客数据blog，如果获取失败返回null
 */
function getBlogDetail(blogNo) {
    var blog;
    $.ajax({
        //请求类型
        type: "POST",
        //同步
        async: false,
        //预期服务器返回的数据类型
        dataType: "json",
        url: "/GetBlogServlet",
        data: {
            "blogno": blogNo
        },
        /**
         * @param data
         * @param {Boolean} data.get 判断是否获取到数据
         * @param {Object} data.blog 博客数据
         */
        success: function (data) {
            if (data.get) {
                //获取到博客数据
                blog =  data.blog;
            } else {
                //出现异常，没有获取到博客
                toastr.error("出现异常！请刷新页面或重新进入！");
                blog = null;
            }
        },
        error: function () {
            //获取博客内容失败
            toastr.error("出现异常！请刷新页面或重新进入！");
            blog = null;
        }
    });
    return blog;
}


/**
 * 点击标签跳转到对应页面
 * @param type 标签类型 cy / ml / sl
 * @param data 分类索引 / 标签文本内容
 */
function toSearch(type, data) {
    var url = "../../index.html?";
    var userid = getParams().userid;
    var param = "";
    if (userid != null) {
        param += "userid=" + userid + "&";
    }
    param += type + "=" + encodeURIComponent(data);
    //新窗口打开
    window.open(url + param,"_blank");
}
