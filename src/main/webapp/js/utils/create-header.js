/**
 * @ignore  =====================================================================================
 * @fileoverview 动态添加和显示登录、注册以及注册成功模态框
 * @author  寒洲
 * @date 2020/8/18
 * @ignore  =====================================================================================
 */
/**
 * 用户已登录时的操作
 */
userLogged = function (userid) {
    //已登录。显示用户头像按钮
    addUserBtn(userid);
}

/**
 * 用户未登录时的操作
 */
userUnlogin = function () {
    //添加登录注册按钮
    addBtn();
    //添加模态框，点击按钮显示
    addLoginModal();
    addRegisterModal();
}

/**
 * 添加页眉的登录、注册按钮
 */
function addBtn() {
    var str = "<div class=\"row\">" +
        "        <div class=\"my-blog-name\" title=\"回到首页\">个人博客</div>";
    //搜索框
    str += addSearchHtml();
    str += "     <!--登录注册按钮-->" +
        "        <div id=\"header-btn-box\">" +
        "            <!-- 登录按钮，打开登录模态窗口-->" +
        "            <div id=\"headerBtns\"> " +
        "                 <button type=\"button\" class=\"btn\" id=\"login-btn\" data-toggle=\"modal\" data-target=\"#login-modal\">" +
        "                     登录" +
        "                 </button>" +
        "                 <!-- 注册按钮，打开注册模态窗口-->" +
        "                 <button type=\"button\" class=\"btn\" id=\"register-btn\" data-toggle=\"modal\" data-target=\"#register-modal\" onclick=\"addRegisterSuccessModal()\">" +
        "                     注册" +
        "                 </button>" +
        "            </div>" +
        "        </div>";
    $("header").append(str);
    addBlogTitleOnClick();
    //回显搜索框的内容
    echoSearchWord();
}

function addSearchHtml() {
    var path = window.location.pathname;
    // 在首页为true，如果是首页（path没有index.html）也会在index.js中被转为index.html
    var isIndex = (path.indexOf("index.html") != -1);
    var str;
    if (isIndex){
        // 如果是首页，再插入搜索框HTML。
        // label是被迫加上去的因为bootstrap这么要求，改为sr-only为不显示
        str = "<div id='search-area-box'>" +
            "       <div class=\"form-group has-feedback search-area\">\n" +
            "         <label class=\"control-label sr-only\" for=\"input-search\" >搜索</label>"+
            "         <input type=\"text\" class=\"form-control\" id='input-search' " +
            "           placeholder=\"搜索...\" style='width: 300px' onkeydown=\"onKeyDown(event)\"/>" +
            //点击事件不起作用
            "         <span style='cursor: pointer' onclick=\"doSearch();\">" +
            "             <span class=\"glyphicon glyphicon-search form-control-feedback\"></span>" +
            "         </span>"+
            "     </div>" +
            "   </div>";
    }
    return str;
}

/**
 * 登录后显示用户头像按钮
 */
function addUserBtn(userid) {
    var str = "<div class=\"form-inline row\" style='' '>" +
              "<div class=\"my-blog-name\">个人博客</div>";
    //搜索框
    str += addSearchHtml();
    str +="<div class=\"row\" id='header-userbtn-box'>" +
          "      <!--下拉菜单按钮，用css代码做了排版调整-->" +
          "      <div class=\"dropdown\" style='display: inline-block;'>" +
          "             <button type=\"button\" class=\"btn btn-link dropdown-toggle avatar-btn\" data-toggle=\"dropdown\"" +
          "                  aria-haspopup=\"true\" aria-expanded=\"false\" style=\"margin-top: -6px\">" +
          "                <!--把用户头像作为按钮显示-->" +
          "                <img class=\"img-circle\" alt=\"用户头像\">" +
          "             </button>" +
          "              <!--下拉菜单组件-->" +
          "             <ul class=\"dropdown-menu\" aria-labelledby=\"dLabel\">" +
          "                  <li><a href=\"#\" onclick=\"toCenter(" + userid + ");\">个人中心</a></li>" +
          "                  <li><a href=\"#\" onclick=\"toManage(" + userid + ");\">账号设置</a></li>" +
          "                  <!--分割线-->" +
          "                  <li role=\"separator\" class=\"divider\"></li>" +
          "                  <li><a href=\"#\" onclick=\"toLogout();\">退出登录</a></li>" +
          "                  <li><a href=\"#\" onclick=\"toDeleteUser();\">注销账号</a></li>" +
          "             </ul>" +
          "      </div>";
    var path = window.location.pathname;
    //在首页为true
    var isIndex = (path.indexOf("index.html") != -1 || path.indexOf(".html") == -1);
    if (isIndex){
        //首页添加发布博客按钮
        str += "<div style='display: inline-block;margin-left: 5px'>" +
            "        <img class=\"publish-btn\" src=\"images/publish.png\" alt=\"发布博客\" onclick=\"toPublish(" + userid + ");\" />" +
            "   </div>";
    }
    str +="</div></div></div>";
    $("header").append(str);

    //回显用户头像方法
    showAvatar($(".avatar-btn img"), userid);
    //页眉博客名点击返回主页
    addBlogTitleOnClick();
    //添加删除账号模态框
    addDeleteModal();
    //回显搜索框的内容
    echoSearchWord();
}

/**
 * 如果是首页且是搜索页面，在搜索框回显搜索词
 */
function echoSearchWord() {
    var params = getParams();
    if (params.wd != null){
        if (window.location.pathname.indexOf("index.html")!=-1){
            var urlWord = decodeURIComponent(params.wd);
            $("#input-search").val(urlWord);
        }
    }
}

/**
 * 点击页眉博客名返回主页
 */
function addBlogTitleOnClick() {
    $(".my-blog-name").on("click", function () {
        //判断当前页是否为首页
        var search = location.search;
        //判断是否在首页，以及是否有搜索
        if (window.location.pathname.indexOf("index.html") == -1 ||
            (search.indexOf("wd") != -1) || (search.indexOf("cy") != -1)
             || (search.indexOf("ml") != -1) || (search.indexOf("sl") != -1)
             || (search.indexOf("p") != -1)  ){
            //当前页不是首页，跳转到首页
            var userid = getParams().userid;
            if (userid != null){
                window.location.href = "../../index.html?userid=" + userid;
            }else{
                window.location.href = "../../";
            }
        }else{
            //是首页

        }
    })
}

/**
 * 添加登录模态框的HTML
 */
function addLoginModal() {
    var str = "<div class=\"modal fade\" id=\"login-modal\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"login-modal\"" +
        "     data-backdrop=\"static\">" +
        "    <div class=\"modal-dialog\" role=\"document\">" +
        "        <div class=\"modal-content\">" +
        "            <!--头部-->" +
        "            <div class=\"modal-header\">" +
        "                <!--右上角关闭按钮，关闭时清空表单-->" +
        "                <button type=\"button\" class=\"close\" id=\"login-close\" data-dismiss=\"modal\" aria-label=\"Close\"" +
        "                        onclick=\"document.getElementById('login-form').reset();\">" +
        "                    <span aria-hidden=\"true\">&times;</span>" +
        "                </button>" +
        "                <!--标题-->" +
        "                <h4 class=\"modal-title\" id=\"login-modal-label\">登录</h4>" +
        "            </div>" +
        "            <!--主体-->" +
        "            <div class=\"modal-body\">" +
        "                <!-------------------------------登录表单-------------------------------->" +
        "                <form class=\"form-horizontal\" id=\"login-form\" role=\"form\" method=\"get\">" +
        "                    <!--两个输入框，autocomplete=\"off\"不保存输入记录-->" +
        "                    <div class=\"form-group\">" +
        "                        <label for=\"login-userid\" class=\"col-sm-2 control-label\">账号</label>" +
        "                        <div class=\"col-sm-9\">" +
        "                            <input type=\"text\" class=\"form-control\" id=\"login-userid\" name=\"login-userid\"" +
        "                                   placeholder=\"请输入账号\" autofocus autocomplete=\"off\"/>" +
        "                        </div>" +
        "                    </div>" +
        "                    <div class=\"form-group\">" +
        "                        <label for=\"login-password\" class=\"col-sm-2 control-label\">密码</label>" +
        "                        <div class=\"col-sm-9\">" +
        "                            <input type=\"password\" class=\"form-control\" id=\"login-password\" name=\"login-password\"" +
        "                                   placeholder=\"请输入密码\" autocomplete=\"off\">" +
        "                        </div>" +
        "                    </div>" +
        "                    <!-----提示框----->" +
        "                    <div class=\"form-group\" id=\"login-tip\"></div>" +
        "                    <!--底部按钮-->" +
        "                    <div class=\"form-group\">" +
        "                        <div class=\"col-xs-2 col-xs-offset-5 col-sm-2 col-sm-offset-5\">" +
        "                            <button type=\"button\" class=\"form-control btn btn-primary\" onclick=\"validateLogin()\"" +
        "                                    style=\"margin-bottom: 5px\">" +
        "                                登录" +
        "                            </button>" +
        "                        </div>" +
        "                        <div class=\"col-xs-2 col-xs-offset-5 col-sm-2 col-sm-offset-5\">" +
        "                            <!-- 注册按钮，打开模态窗口-->" +
        "                            <button type=\"button\" class=\"form-control btn btn-default\" data-toggle=\"modal\"" +
        "                                    data-target=\"#register-modal\">" +
        "                                注册" +
        "                            </button>" +
        "                        </div>" +
        "                    </div>" +
        "                </form>" +
        "            </div>" +
        "        </div>" +
        "    </div>" +
        "</div>";
    $("body").append(str);
    //登录模态框中输入框获取焦点
    $('#login-modal').on('shown.bs.modal', function () {
        $('#login-userid').focus();
    });
}

/**
 * 添加注册模态框的HTML
 */
function addRegisterModal() {
    var str = "<div class=\"modal fade\" id=\"register-modal\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"register-modal\"" +
        "     data-backdrop=\"static\">" +
        "    <div class=\"modal-dialog\" role=\"document\">" +
        "        <div class=\"modal-content\">" +
        "            <!--头部-->" +
        "            <div class=\"modal-header\">" +
        "                <!--右上角关闭按钮-->" +
        "                <!--关闭时清空表单-->" +
        "                <button type=\"button\" class=\"close\" id=\"register-close\" data-dismiss=\"modal\" aria-label=\"Close\"" +
        "                        onclick=\"registerReset()\">" +
        "                    <span aria-hidden=\"true\">&times;</span>" +
        "                </button>" +
        "                <!--标题-->" +
        "                <h4 class=\"modal-title\" id=\"register-modal-label\">注册</h4>" +
        "            </div>" +
        "            <!--主体-->" +
        "            <div class=\"modal-body\">" +
        "                <!-------------------------------注册表单-------------------------------->" +
        "                <form class=\"form-horizontal container-fluid\" id=\"register-form\" role=\"form\"" +
        "                      action=\"/RegisterServlet\" method=\"post\">" +
        "                    <!-----输入框，autocomplete=\"off\"不保存输入记录----->" +
        "                    <!--昵称-->" +
        "                    <div class=\"form-group\">" +
        "                        <label for=\"register-nickname\" class=\"col-sm-2 control-label\">昵称</label>" +
        "                        <div class=\"col-sm-9\">" +
        "                            <!--发现autofocus属性没用，改用js代码自动获取焦点-->" +
        "                            <input type=\"text\" class=\"form-control\" id=\"register-nickname\" name=\"register-nickname\"" +
        "                                   placeholder=\"请输入账号昵称\" autocomplete=\"off\"/>" +
        "                        </div>" +
        "                    </div>" +
        "                    <!--账号id-->" +
        "                    <div class=\"form-group\">" +
        "                        <label for=\"register-userid\" class=\"col-sm-2 control-label\">账号</label>" +
        "                        <div class=\"col-sm-9\">" +
        "                            <input type=\"text\" class=\"form-control\" id=\"register-userid\" name=\"register-userid\"" +
        "                                   placeholder=\"请输入账号\" autocomplete=\"off\">" +
        "                        </div>" +
        "                    </div>" +
        "                    <!--密码-->" +
        "                    <div class=\"form-group\">" +
        "                        <label for=\"register-pw1\" class=\"col-sm-2 control-label\">密码</label>" +
        "                        <div class=\"col-sm-9\">" +
        "                            <input type=\"password\" class=\"form-control\" id=\"register-pw1\" name=\"register-pw1\"" +
        "                                   placeholder=\"请输入密码\" autocomplete=\"off\">" +
        "                        </div>" +
        "                    </div>" +
        "                    <!--再次输入密码-->" +
        "                    <div class=\"form-group\">" +
        "                        <label for=\"register-pw2\" class=\"col-sm-2 control-label\">再次输入</label>" +
        "                        <div class=\"col-sm-9\">" +
        "                            <input type=\"password\" class=\"form-control\" id=\"register-pw2\" name=\"register-pw2\"" +
        "                                   placeholder=\"请重复您的密码\" autocomplete=\"off\">" +
        "                        </div>" +
        "                    </div>" +
        "                    <!--生日-->" +
        "                    <div class=\"form-group\">" +
        "                        <label for=\"register-birthday\" class=\"col-sm-2 control-label\">生日</label>" +
        "                        <div class=\"col-sm-9\">" +
        "                            <input type=\"date\" id=\"register-birthday\" name=\"register-birthday\" class=\"form-control\"" +
        "                                   autocomplete=\"off\">" +
        "                        </div>" +
        "                    </div>" +
        "                    <!--性别-->" +
        "                    <div class=\"form-group\">" +
        "                        <label class=\"col-sm-2 col-xs-3 control-label\" style=\"margin-top: 7px\">性别</label>" +
        "                        <div class=\"radio\">" +
        "                            <label class=\"radio-inline col-sm-2 col-sm-offset-2 col-xs-3 col-xs-offset-1\">" +
        "                                <input type=\"radio\" name=\"register-sex\" id=\"male-radio\" value=\"male\" checked> 小哥哥" +
        "                            </label>" +
        "                            <label class=\"radio-inline col-sm-offset-2 col-xs-3\">" +
        "                                <input type=\"radio\" name=\"register-sex\" id=\"female-radio\" value=\"female\"> 小姐姐" +
        "                            </label>" +
        "                        </div>" +
        "                    </div>" +
        "                    <!-----提示框----->" +
        "                    <div class=\"form-group\" id=\"register-tip\"></div>" +
        "                    <!-----底部按钮----->" +
        "                    <div class=\"form-group\">" +
        "                        <div class=\"row\">" +
        "                            <!--注册按钮-->" +
        "                            <div class=\"col-xs-2 col-xs-offset-4 col-sm-2 col-sm-offset-4\">" +
        "                                <!--注册按钮，点击时执行validate函数判断非空，通过ajax提交数据并判断-->" +
        "                                <button type=\"button\" class=\"form-control btn btn-primary\" id=\"register-button\"" +
        "                                        onclick=\"validateRegister()\" style=\"margin-bottom: 5px\">" +
        "                                    注册" +
        "                                </button>" +
        "                            </div>" +
        "                            <!--重置按钮-->" +
        "                            <div class=\"col-xs-2 col-sm-2\">" +
        "                                <!--通过getRegisterTipDiv函数清空表单提示框样式-->" +
        "                                <button type=\"reset\" class=\"form-control btn btn-default\" id=\"reset-button\"" +
        "                                        style=\"margin-bottom: 5px\" onclick=\"registerReset()\">" +
        "                                    重置" +
        "                                </button>" +
        "                            </div>" +
        "                        </div>" +
        "                    </div>" +
        "                </form>" +
        "            </div>" +
        "        </div>" +
        "    </div>" +
        "</div>";
    $("body").append(str);
    //注册模态框中输入框自动获取焦点
    $('#register-modal').on('shown.bs.modal', function () {
        $("#register-nickname").focus();
    });
    //注册时输入框自动检查输入
    addAutoCheck();
}

/**
 * 注册成功模态框
 */
function addRegisterSuccessModal() {
    if ($("#register-success-modal") != null){
        //模态框已创建
        return;
    }
    var str = "<div class=\"modal fade bs-example-modal-sm\" id=\"register-success-modal\" tabindex=\"-1\" role=\"dialog\"" +
        "     aria-labelledby=\"success-register-modal\">" +
        "    <!--小模态框-->" +
        "    <div class=\"modal-dialog modal-sm\">" +
        "        <div class=\"modal-content\">" +
        "            <div class=\"modal-header\">" +
        "                <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\"><span" +
        "                        aria-hidden=\"true\">&times;</span></button>" +
        "                <h4 class=\"modal-title\">注册成功</h4>" +
        "            </div>" +
        "            <!--提示用户注册成功，可以选择打开登录窗口-->" +
        "            <div class=\"modal-body\">" +
        "                <p>注册成功！要登录吗？</p>" +
        "            </div>" +
        "            <div class=\"modal-footer\">" +
        "                <button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">关闭</button>" +
        "                <!--打开登录窗口的按钮-->" +
        "                <button type=\"button\" class=\"btn btn-primary\" onclick=\"toLogin()\">登录</button>" +
        "            </div>" +
        "        </div>" +
        "    </div>" +
        "</div>";
    $("body").append(str);
}

/**
 * 创建删除账号模态框
 */
function addDeleteModal() {
    //代码和登录的模态框差不多
    var str = "<div class=\"modal fade\" id=\"delete-modal\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"delete-modal\"" +
        "     data-backdrop=\"static\">" +
        "    <div class=\"modal-dialog\" role=\"document\">" +
        "        <div class=\"modal-content\">" +
        "            <div class=\"modal-header\">" +
        "                <button type=\"button\" class=\"close\" id=\"delete-close\" data-dismiss=\"modal\" aria-label=\"Close\"" +
        "                        onclick=\"document.getElementById('delete-form').reset();\">" +
        "                    <span aria-hidden=\"true\">&times;</span>" +
        "                </button>" +
        "                <h4 class=\"modal-title\" id=\"delete-modal-label\">删除该账号</h4>" +
        "            </div>" +
        "            <div class=\"modal-body\">" +
        "                <form class=\"form-horizontal\" id=\"delete-form\" role=\"form\" method=\"get\">" +
        "                    <div class=\"form-group\">" +
        "                        <label for=\"delete-userid\" class=\"col-sm-2 control-label\">账号</label>" +
        "                        <div class=\"col-sm-9\">" +
        "                            <input type=\"text\" class=\"form-control\" id=\"delete-userid\" name=\"delete-userid\"" +
        "                                   placeholder=\"请输入当前账号\" autofocus autocomplete=\"off\"/>" +
        "                        </div>" +
        "                    </div>" +
        "                    <div class=\"form-group\">" +
        "                        <label for=\"delete-password\" class=\"col-sm-2 control-label\">密码</label>" +
        "                        <div class=\"col-sm-9\">" +
        "                            <input type=\"password\" class=\"form-control\" id=\"delete-password\" name=\"delete-password\"" +
        "                                   placeholder=\"请输入该账号的密码\" autocomplete=\"off\">" +
        "                        </div>" +
        "                    </div>" +
        "                    <div class=\"form-group\" id=\"delete-tip\"></div>" +
        "                    <div class=\"form-group\">" +
        "                        <div class=\"col-xs-2 col-xs-offset-5 col-sm-2 col-sm-offset-5\">" +
        "                            <button type=\"button\" class=\"form-control btn btn-default\" onclick=\"deleteSubmit();\"" +
        "                                    style=\"margin-bottom: 5px\">删除</button>" +
        "                        </div>" +
        "                        <div class=\"col-xs-2 col-xs-offset-5 col-sm-2 col-sm-offset-5\">" +
        "                            <button type=\"button\" class=\"form-control btn btn-primary\"  data-dismiss=\"modal\">取消</button>" +
        "                        </div>" +
        "                    </div>" +
        "                </form>" +
        "            </div>" +
        "        </div>" +
        "    </div>" +
        "</div>";
    //添加模态框html
    $("body").append(str);
}