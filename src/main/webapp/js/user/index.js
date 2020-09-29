/**
 * @ignore  =====================================================================================
 * @fileoverview 首页
 * @author  寒洲
 * @date 2020/8/19
 * @ignore  =====================================================================================
 */
$(function () {
    /*
    抵达首页时不允许返回上一页
    这样可以避免登录和退出登录时返回上一页带来的麻烦
    而且首页本就应该作为“起点”
     */
    var search = window.location.search;
    if(window.history && window.history.pushState){
        $(window).on("popstate", function(){
            var search = window.location.search;
            window.history.pushState("forward", null, "index.html" + search);
            window.history.forward();
        });
    }
    //在IE中必须得有这两行
    window.history.pushState("forward", null, "index.html" + search);
    window.history.forward();
})

/**
 * 搜索框回车确认
 * @param event
 */
function onKeyDown(event) {
    var e = event || window.event || arguments.callee.caller.arguments[0];
    // 按下 enter 键
    if (e && e.keyCode == 13) {
        doSearch();
    }
}

function doSearch() {
    var input = $("#input-search").val();
    //配置url参数，跳转到搜索后的页面
    var url = "../../index.html?";
    url += "wd=" + encodeURIComponent(input);
    //新窗口打开
    window.open(url,"_blank");
}