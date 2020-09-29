/**
 * @ignore  =====================================================================================
 * @fileoverview 查看博客
 * @author  寒洲
 * @date 2020/8/14
 * @ignore  =====================================================================================
 */

$(function () {
    // 初始化页面时获取url后包含的参数博客id
    var params = getParams();
    //博客id在url中用key表示
    var blogNo = params.key;
    ////获取博客信息
    var blog = getBlogDetail(blogNo);
    if (blog != null){
        //回显博客信息
        showBlog(blog);
    }
})


/**
 * @param {Object} blog 博客数据
 * @param {Number} blog.blogNo
 * @param {Number} blog.authorId
 * @param {Number} blog.authorName
 * @param {String} blog.title
 * @param {String} blog.content
 * @param {Number} blog.createdate
 * @param {Number} blog.updatedate
 * @param {Number} blog.category
 * @param {String} blog.mlabel
 * @param {String} blog.slabel
 */
function showBlog(blog) {
    //获取日期字符串
    var createDate = dateFormat(blog.createdate);
    var updateDate = dateFormat(blog.updatedate);
    //将类别索引转为字符串
    var category = getCategory()[blog.category-1];

    //添加纯文本信息，避免博客内容中有HTML数据
    $("#detail-title").text(blog.title);
    $("#detail-category").text(category);
    $("#detail-mlabel").text(blog.mlabel);
    $("#detail-slabel").text(blog.slabel);
    $("#detail-createDate").text("发布时间："+createDate);
    $("#detail-updateDate").text("最后更新："+updateDate);
    $("#detail-content").text(blog.content);

    //判断作者是否已注销账号
    if (blog.authorId != null){
        //显示作者昵称
        $("#detail-author-name").text(blog.authorName);
        //显示作者头像
        $("#detail-author-img").prop("src","/UserAvatarServlet?id=" + blog.authorId);
        //前往作者的个人中心
        $("#detail-author-img").on("click",function () {
            toUserCenter(blog.authorId);
        });
        $("#detail-author-name").on("click",function () {
            toUserCenter(blog.authorId);
        });
    }else{
        //显示“用户已注销”
        $("#detail-author-name").text("用户已注销");
        //已注销用户专属头像
        $("#detail-author-img").prop("src","/UserAvatarServlet?id=cancelled");
    }

    //为标签添加绑定事件
    $("#detail-category").on("click", function () {
        //索引不必-1，因为是用这个值去查询数据库的，数据库从1开始
        toSearch("cy", blog.category)
    })
    $("#detail-mlabel").on("click", function () {
        toSearch("ml", blog.mlabel)
    })
    $("#detail-slabel").on("click", function () {
        toSearch("sl", blog.slabel)
    })
}