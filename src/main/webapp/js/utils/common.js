/**
 * @ignore  =====================================================================================
 * @fileoverview 常用的js操作
 * @author  寒洲
 * @date 2020/8/12
 * @ignore  =====================================================================================
 */
/**
 * 清除提示框原有的样式
 * @returns {*|jQuery.fn.init|jQuery|HTMLElement}
 */
$(function () {
    /*
    加载页面时检查登录状态，修正url参数
    如果url有userid参数，但是未登录，需要修正
    如果url无userid参数但是已登录，也需要修正
     */
    $.post(
        "/CheckLoginServlet",
        function (data) {
            //判断返回信息，检查是否已登录
            if (data.indexOf("false") != -1) {
                //如果返回false说明未登录
                // 判断url中是否有userid
                if (getParams().userid != null) {
                    //存在userid参数，异常，跳转页面
                    location.replace("warning.html");
                    return;
                }
                //创建页眉登录注册按钮
                userUnlogin();
            } else {
                //已登录，服务器发回的data中的数据是userid
                //创建页眉用户头像按钮
                userLogged(data);
                // 判断url中是否有userid
                if (getParams().userid == null) {
                    //没有userid参数，则添加userid到url中
                    var url = window.location.href;
                    //跳转页面
                    setDirection(data);
                }
            }
        });
})

/**
 * 已登录状态下设置正确的url并跳转
 * @param userid
 */
setDirection = function(userid) {
    //获取url的文件路径
    var pathname = window.location.pathname;
    //除去pathname开头的 “/”
    pathname = pathname.substr(1);
    //获取url后的参数
    var param = window.location.search;
    //如果存在“？”则说明存在参数
    if (param.indexOf("?") != -1) {
        //去除“？”，保留之后的所有字符
        param = param.substr(1);
        if (pathname.indexOf("center.html") != -1 && param.indexOf("target=" + userid) != -1) {
            //在要登录的用户的个人中心中
            var arr = param.split("&");
            //重置url参数
            param = "";
            //for循环检查并删去target参数，保留其他参数
            for (var i = 0; i < arr.length; i++) {
                if (arr[i].indexOf("target=") == -1) {
                    //不是target参数，添加到param中
                    param += arr[i] + "&";
                }
            }
            //排除可能存在的target后，判断还有没有参数
            if (param.length == 0) {
                //除去target后没有参数了
                // window.location.href = "../../" + pathname + "?userid=" + userid;
                window.location.replace("../../" + pathname + "?userid=" + userid);
                return;

            }else{
                //还有参数，删除最后的&。通过后面的代码跳转页面
                param = param.substring(0, param.length - 1);
            }
        }
        //else 如果当前个人中心不是登录用户的个人中心，则不必考虑target

        //最后在路径和参数之间插入userid
        window.location.replace("../../" + pathname + "?userid=" + userid + "&" + param);
    }else{
        //没有参数
        window.location.replace("../../" + pathname + "?userid=" + userid);
    }
}

/**
 * 获取已经清空样式的提示框
 * @param tip 提示框对象
 * @returns {*}
 */
getPreparedTip = function (tip) {
    //先移除原来的提示内容和样式
    tip.empty();
    tip.removeClass();
    //这个属性要加回去不能清除。
    tip.prop("role", "alert");
    return tip;
}

/**
 * 获取提示框对象，设置提示信息和样式
 * @param tipObj 提示框对象
 * @param message 提示信息
 * @param type 提示框样式
 */
setTip = function (tipObj, message, type) {
    //获取提示框对象
    var tip = getPreparedTip(tipObj);
    //根据type添加不同的样式，如alert-warning
    tip.addClass("alert alert-" + type);
    tip.html("<p>" + message + "</p>");
}

/**
 * 获取url中的参数，返回参数包对象
 * @returns {{}} params
 */
getParams = function () {
    var params = {};
    //获取当前界面url中的参数
    var url = location.search;

    //如果存在“？”则说明存在参数
    if (url.indexOf("?") != -1) {
        //去除“？”，保留之后的所有字符
        var str = url.substr(1);
        //切割字符串，把每个参数及参数值分成数组
        var paramArr = str.split("&");
        for (var i = 0; i < paramArr.length; i++) {
            //前者为参数名称，后台为参数值
            params[paramArr[i].split("=")[0]] = paramArr[i].split("=")[1];
        }
    }
    return params;
}

/**
 * 日期毫秒数转为字符串
 * @param {Number} dateMs 日期毫秒数
 * @returns {string} 格式化日期yyyy-MM-dd HH:mm:ss
 */
dateFormat = function (dateMs) {
    /*
    实践证明，从后端获取到的日期对象时以毫秒数发送的
    但是此处直接使用接收到的数据调用日期对象的方法会报错
    应该用获得到的毫秒数创建js日期对象
     */
    var date = new Date(dateMs);
    var year = date.getFullYear();
    var month = date.getMonth();
    var day = date.getDate();
    var hour = date.getHours();
    var mintue = date.getMinutes()
    var second = date.getSeconds()
    return year + "-" + month + "-" + day + " " + hour + ":" + mintue + ":" + second;
}

/**
 * 显示用户头像
 * @param imgObj 头像显示框
 * @param userid 用户id
 */
showAvatar = function (imgObj, userid) {
    imgObj.prop("src", "/UserAvatarServlet?id=" + userid);
}