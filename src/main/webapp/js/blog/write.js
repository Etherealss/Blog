/**
 * @ignore  =====================================================================================
 * @fileoverview 新建和修改博客时的操作
 * @author  寒洲
 * @date 2020/8/12
 * @ignore  =====================================================================================
 */
/**提交博客时使用*/
var params;
/**修改博客时判断是否是作者*/
var authorId;
$(function () {
    //获取博客分类数据，添加单选按钮组
    createCategoryRadio();
    // 获取写博客的用户id的值
    params = getParams();
    if (params.key != null) {
        //如果获取到了博客序号 说明是编辑博客，则回显对应博客的信息
        echoBlog(params.key);
        //将“发布博客”按钮改为“确定修改”
        $("#write-publish").text("确定修改");
    }

})

/**
 * 提交博客前检查，完成后提交
 * @returns {boolean}
 */
function publish() {
    var tipObj = $("#write-tip");

    var title = $("#write-title").val();
    if (title == null || title === "") {
        setTip(tipObj, "请输入标题", "warning");
        return false;
    } else if (title.length > 100) {
        setTip(tipObj, "标题太长了！！100个字以内喔", "warning");
        return false;
    }

    var content = $("#write-article").val();
    if (content == null || content === "") {
        setTip(tipObj, "请输入文章内容", "warning");
        return false;
    }

    //获取选中的分类单选框的值，其中包含了分类的索引（从1开始）
    var categoryIndex = $(".category:checked").val();
    if (content == null || content === "") {
        setTip(tipObj, "请选择一个文章分类", "warning");
        return false;
    }

    //标签允许为空
    var mlabel = $("#write-mlabel").val();
    var slabel = $("#write-slabel").val();
    if (mlabel == null) {
        mlabel = "";
    }
    if (slabel == null) {
        slabel = "";
    }
    if (params.key == null) {
        //发布新博客
        //传入作者的userid
        blogSubmit(params.userid, title, content, categoryIndex, mlabel, slabel);
    } else {
        //修改博客
        if (authorId != null && authorId == params.userid) {
            //作者id为当前用户id，可以修改博客
            changeSubmit(params.userid, params.key, title, content, categoryIndex, mlabel, slabel);
        }
    }
    return true;
}

/**
 * 新发布博客时提交信息
 * @param userid
 * @param title
 * @param content
 * @param categoryIndex 文字分类的索引，对于数据库的编号
 * @param mlabel
 * @param slabel
 */
function blogSubmit(userid, title, content, categoryIndex, mlabel, slabel) {
    $.ajax({
        //请求类型
        type: "POST",
        //预期服务器返回的数据类型
        dataType: "json",
        url: "/WriteServlet",
        data: {
            "userid": userid,
            "title": title,
            "content": content,
            "category-index": categoryIndex,
            "mlabel": mlabel,
            "slabel": slabel
        },
        success: function (data) {
            if (data.write) {
                //博客发布成功
                alert("博客发布成功!");
                //返回上一页
                window.history.go(-1);
            } else {
                //博客发布失败
                setTip($("#write-tip"), "博客发布失败，请重试！", "danger");
            }
        },
        error: function () {
            setTip($("#write-tip"), "发布博客出现异常！请重试！", "danger");
        }
    });
}

/**
 * 修改博客时提交信息
 * @param userid
 * @param blogno
 * @param title
 * @param content
 * @param categoryIndex 文字分类的索引，对于数据库的编号
 * @param mlabel
 * @param slabel
 */
function changeSubmit(userid, blogno, title, content, categoryIndex, mlabel, slabel) {
    $.ajax({
        //请求类型
        type: "POST",
        //预期服务器返回的数据类型
        dataType: "json",
        url: "/ChangeBlogServlet",
        data: {
            "userid": userid,
            "blogno": blogno,
            "title": title,
            "content": content,
            "category-index": categoryIndex,
            "mlabel": mlabel,
            "slabel": slabel
        },
        /**
         * @param data
         * @param data.change 判断博客修改是否成功
         */
        success: function (data) {
            if (data.change) {
                //博客修改成功
                alert("data.msg");
                //返回上一页
                window.history.back();
            } else {
                //修改博客发布失败
                alert("data.msg");
            }
        },
        error: function () {
            //注册失败
            setTip($("#write-tip"), "博客修改失败！请重试！", "danger");
        }
    });
}

/**
 * 修改博客时，回显博客
 */
function echoBlog(blogNo) {
    //获取博客信息
    /**
     * {Object} blog 博客数据
     * {Number} blog.blogNo
     * {String} blog.title
     * {String} blog.content
     * {Number} blog.category
     * {String} blog.mlabel
     * {String} blog.slabel
     */
    var blog = getBlogDetail(blogNo);
    if (blog != null) {
        //赋值给全局变量，用于修改后提交时判断
        authorId = blog.authorId;
        if (blog.authorId != params.userid) {
            //不是作者，不可编辑，不回显
            alert("你不是作者，不可以编辑！");
            history.back();
            return;
        }
        $("#write-title").val(blog.title);
        $("#write-article").val(blog.content);
        $("#write-mlabel").val(blog.mlabel);
        $("#write-slabel").val(blog.slabel);

        //回显分类
        var index = blog.category - 1;
        //获取单选框外嵌套的label标签
        var $label = $(".btn-group label");
        //对应分类添加active样式
        //不能直接添加类属性，需要先转为jQuery对象
        $($label[index]).addClass("active");
        //label标签的子类(input标签)添加checked属性
        $($label[index]).children().prop("checked", true);

    }

}

/**
 * 创建博客分类单选组
 */
function createCategoryRadio() {
    var category = getCategory();
    var str = "";
    for (var i = 1; i <= category.length; i++) {
        str += "<label class=\"btn btn-default\" for=\"write-category" + i + "\">" +
            "       <input type=\"radio\" class=\"category\" name=\"category\" id=\"write-category" + i + "\" value=\"" + i + "\"" +
            "              autocomplete=\"off\">" + category[i - 1] +
            "   </label>";
    }
    $(".btn-group").append(str);
}

/**
 * 取消发布
 */
function publishCancel() {
    //返回上一页
    window.history.back();
}