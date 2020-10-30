/**
 * @ignore  =====================================================================================
 * @fileoverview 首页登录注册的操作
 * @author  寒洲
 * @date 2020/8/4
 * @ignore  =====================================================================================
 */
/* ------------------------------------注册------------------------------------ */
/**
 * 注册时的自动检测功能
 */
addAutoCheck = function () {
    //获取注册提示框对象
    var regiTip = $("#register-tip");

    //获取昵称输入框
    var nickObj = $("#register-nickname");
    //输入改变时检查
    nickObj.on("change", function () {
        var nickVal = nickObj.val();
        if (nickVal != null && nickVal !== "") {
            //昵称非空时检查用户昵称是否可用
            validateNickname(regiTip, nickVal);
        }
    })

    /*
    自动检测两次密码是否相同，同时会检测密码的合法性
    因为注册和修改密码都会有相同的操作所以写在函数里
    注意！提示框会在函数中调用getTipDiv()再次获取，以清除原有样式
    因此这里只需要传入提示框对象即可
     */
    pwOnchangeCheck(regiTip, $("#register-pw1"), $("#register-pw2"));

    //注册时ID输入框输入改变就判断用户ID是否已存在
    var useridObj = $("#register-userid");
    useridObj.on("change", function () {
        validateUserId(regiTip, useridObj.val());
    })
}

/**
 * 注册时检查输入的数据
 * 如果通过验证则调用函数，提交注册数据
 */
function validateRegister() {
    //获取提示框对象
    var tipObj = $("#register-tip");
    /*
     * 这里会判断用户id是否包含非数字字符 或者已存在
     * 虽然在id输入框输入改变时会自动调用该方法检测，
     * 但是用户忽略提示提交代码，
     * 也可能因为触发其他错误导致该错误被覆盖
     * 所以还是要在提交时再一次检查id
     */
    var id = $("#register-userid").val();
    if (!validateUserId(tipObj, id)) {
        /*
         * id不可用时，函数返回值为false，这里会进入该if语句
         * 相应的提示语句已经在validateUserId()中给出，此处不必再次提示
         */
        return;
    }

    var nickname = $("#register-nickname").val();
    if (nickname == null || nickname === "") {
        setTip(tipObj, "请选择一个昵称", "warning");
        return;
    } else if (!validateNickname(tipObj, nickname)) {
        //昵称非空时检查用户昵称是否可用
        return;
    }

    //如果输入了密码，则再次验证密码是否规范
    //不这么做的话，密码不符合规定依旧可以提交
    if (!validatePw(tipObj, $("#register-pw1").val(), $("#register-pw2").val())) {
        //validatePw()函数返回false，说明验证不通过
        //提示信息在validatePw函数中已给出
        return;
    }

    var birthday = $("#register-birthday").val();
    //判断是否为合理的生日日期。
    if (!reasonalDate(tipObj, birthday)) {
        //如果生日早于1970-01-01或者晚于修改时的时间则给出提示并返回false
        return;
    }
    //所有信息都没有问题，调用函数通过ajax提交数据给后台
    registerSubmit();
}

/**
 * 注册数据提交后台
 */
function registerSubmit() {
    $.ajax({
        //请求类型
        type: "POST",
        //预期服务器返回的数据类型
        dataType: "json",
        url: "/RegisterServlet",
        data: $("#register-form").serialize(),
        /**
         * @param data
         * @param {Boolean} data.register
         * @param {String} data.msg
         */
        success: function (data) {
            if (data.register) {
                //注册成功
                //关闭注册窗口
                $("#register-close").click();
                //弹出确认窗口
                $("#register-success-modal").modal();
            } else {
                //注册失败
                setTip($("#register-tip"), data.msg, "danger");
            }
        },
        error: function () {
            //注册失败
            setTip($("#register-tip"), "注册出现异常，请重试！", "danger");
        }
    });
}

/**
 * 注册成功时选择登录，关闭注册窗口和提示窗口，打开登录窗口
 */
function toLogin() {
    $("#register-success-modal").modal("hide");
    $('#login-modal').modal();
}

/**
 * 重置注册模态框
 */
function registerReset() {
    //重置表单
    document.getElementById("register-form").reset();
    //重置提示框
    getPreparedTip($("#register-tip"));
}

/* ------------------------------------登录------------------------------------ */
/**
 * 登录时检查输入框是否为空
 * 如果通过验证则调用函数提交登录数据
 */
function validateLogin() {
    //获取提示框对象
    var tipObj = $("#login-tip");

    var id = $("#login-userid").val();
    if (id == null || id === "") {
        //设置提示框内容
        setTip(tipObj, "请输入账号ID", "warning");
        return;
    }else if (!standardId(id)) {
        //检查是否为标准id
        //进入if语句则不是标准id
        //用户名不存在，可以使用
        setTip(tipObj, "账号ID仅能包含数字且在6~20个字符内！", "warning");
        return;
    }

    var password = $("#login-password").val();
    if (password == null || password === "") {
        //设置提示框内容
        setTip(tipObj, "请输入账号密码", "warning");
        return;
    }else if (!standardPw(password)) {
        //密码已输入，但不符合规定
        setTip(tipObj,"密码由6~20个大小写英文字母、数字或者下划线组成，请检查哦","warning");
        return;
    }

    //通过验证，调用函数登录。
    loginSubmit();
}

/**
 * 登录，数据提交后台
 */
function loginSubmit() {
    $.ajax({
        //请求类型
        type: "POST",
        //预期服务器返回的数据类型
        dataType: "json",
        //请求的后端接口（Servlet）
        url: "/LoginServlet",
        //form-data
        //整个表单提交
        data: $("#login-form").serialize(),
        /**
         * 下面这些是返回参数的文档注释
         * @param data
         * @param {Boolean} data.login 是否登录成功
         * @param {String} data.msg 登录提示信息
         * @param {Object} data.user 用户信息
         */
        success: function (data) {
            if (data.login) {
                //登录成功，获取id
                var userid = $("#login-userid").val();
                //关闭登录窗口
                $("#login-close").click();
                //给出提示框
                toastr.success(data.msg);
                //登录后跳转
                setDirection(userid);

            } else {
                //登录失败，提示框显示登录原因
                setTip($("#login-tip"), data.msg, "danger");
            }
        }
        ,
        error: function () {
            //登录失败
            setTip($("#login-tip"), "登录失败，请重试！", "danger");
        }
    });
}
