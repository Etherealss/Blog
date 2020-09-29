/**
 * @ignore  =====================================================================================
 * @fileoverview 登录后的用户常用操作
 * @author  寒洲
 * @date 2020/8/12
 * @ignore  =====================================================================================
 */

/**
 * 获取该用户的Cookie的值
 * @param userid 找到对应cookie的用户id
 * @returns {{}} cookies数据包
 */
getCookieValue = function (userid) {
    // 读取属于当前文档的所有cookies
    var allcookies = document.cookie;
    console.log("getCookieValue allcookies = [" + allcookies + "]");
    console.log("getCookieValue userid = [" + userid + "]");
    //索引长度，开始索引的位置
    var cookie_pos = allcookies.indexOf(userid);

    // 如果找到了索引，就代表cookie存在,否则不存在
    if (cookie_pos !== -1) {
        console.log("getCookieValue cookie存在")
        // 把cookie_pos放在值的开始，只要给值加1即可
        // 计算取cookie值得开始索引，加的1为“=”
        cookie_pos = cookie_pos + userid.length + 1;
        //计算取cookie值得结束索引
        var cookie_end = allcookies.indexOf(";", cookie_pos);

        if (cookie_end === -1) {
            cookie_end = allcookies.length;
        }
        //得到想要的cookie的值
        var value = unescape(allcookies.substring(cookie_pos, cookie_end));
        console.log("getCookie value = [ " + value + " ]")
    } else {
        console.log("getCookieValue cookie不存在");
    }
    var cookies = {};
    var arr = value.split("&");
    for(var i=0;i<arr.length;i++){
        //前者为参数名称，后台为参数值
        cookies[arr[i].split("=")[0]]=arr[i].split("=")[1];
    }
    return cookies;
}

/**
 * 发布发布文章的页面
 */
function toPublish(userid) {
    window.location.href = "../../write.html?userid=" + userid;
}

/**
 * 个人中心
 */
function toCenter(userid) {
    if (window.location.pathname.indexOf("center.html") == -1 || window.location.search.indexOf("target") != -1){
        //1、当前页面不是个人中心页面，跳转页面
        //2、现在在浏览别人的个人中心，要跳转到自己的个人中心去
        window.location.href = "../../center.html?userid=" + userid;
    }
}

/**
 * 前往账号设置界面
 */
function toManage(userid) {
    if (window.location.pathname.indexOf("manage.html") == -1){
        //当前页面不是账号设置页面，跳转页面
        window.location.href = "../../manage.html?userid=" + userid;
    }
}

/**
 * 退出登录
 */
function toLogout() {
    var b = confirm("要退出登录并回到首页吗？");
    if (b){
        //确定退出
        $.post("/LogoutServlet");
        // history.pushState({ page: 1 }, null, "index.html");
        // window.onhashchange = function (event) {window.location.hash = "index.html"; };
        window.location.replace("index.html");
    }
}

/**
 * 删除账户
 */
function toDeleteUser() {
    $("#delete-modal").modal("show");
}

/**
 * 确定删除和提交信息
 */
function deleteSubmit() {
    //判断输入是否为空
    if (validateDelete()) {
        //输入均不为空
        if (confirm("确定要删除吗？")) {
            //确定删除
            $.ajax({
                //方法类型
                type: "POST",
                //预期服务器返回的数据类型
                dataType: "json",
                url: "/DeleteUserServlet",
                data: $("#delete-form").serialize(),
                success: function (data) {
                    // 因为之前弹出了确认框，所以这里再弹出警告框也不显得突兀
                    // 所以不用提示框了
                    alert(data.msg);
                    if (data.delete) {
                        //删除成功，回到首页
                        window.location.href = "../../index.html";
                    }
                },
                error: function () {
                    //登录失败
                    setTip($("#delete-tip"), "账号删除失败，请重试！", "danger");
                }
            });
        }
    }

}

/**
 * 删除账号时检查输入框
 * @returns {boolean}
 */
function validateDelete() {
    var tipObj = $("#delete-tip");
    var userid = $("#delete-userid").val();
    if (userid == null || userid == "") {
        //设置提示框内容
        setTip(tipObj, "请输入当前账号的ID", "warning");
        return false;
    }
    var password = $("#delete-password").val();
    if (password == null || password == "") {
        //设置提示框内容
        setTip(tipObj, "请输入当前账号的密码", "warning");
        return false;
    }
    return true;
}