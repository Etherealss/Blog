/**
 * @ignore  =====================================================================================
 * @fileoverview 用户设置功能有关的js操作
 * @author  寒洲
 * @date 2020/8/7
 * @ignore  =====================================================================================
 */
var userid;
/*用于判断前后选择的头像图片是否相同*/
var originalPath;
/*--------------------------------------初 始 化--------------------------------------*/
$(function () {
    var params = getParams();
    if (params.userid != null) {
        //其实如果没有userid会被filter拦截无法访问的，所以应该可以去掉if
        userid = params.userid;
    }
    //回显用户信息
    echoInfo();
    // 回显用户头像
    echoAvatar();

    /*
    切换页面板块
     */
    $(".nav li").click(function () {
        //获取点击的目标元素对象，三个板块都有body类属性
        var bodys = $("#content").children(".body");
        //判断当前对象是否被选中（active），如果没选中的话进入if循环
        if (!$(this).hasClass("active")) {
            //获取当前对象的索引
            var i = $(this).index();
            //当前对象添加选中样式并且将其同胞移除选中样式；
            //siblings() 方法返回被选元素的所有同级元素
            $(this).addClass("active").siblings('li').removeClass("active");
            //先隐藏后显示，避免出现滚动条导致页面“震动”
            //索引对应的div块的同胞隐藏
            $(bodys[i]).siblings('.body').removeClass("show").addClass("hidden");
            //索引对应的div块显示
            $(bodys[i]).removeClass("hidden").addClass("show");
        }
    });

    /*
    移动端展开导航栏后点击自动收回
     */
    $(".navbar-nav li a").click(function () {
        var navbar = $(".navbar-toggle");
        if (!navbar.hasClass("collapsed")) {
            navbar.click();
        }
    });

    /*
    点击取消按钮回到上一页
     */
    $(".cancel").click(function () {
        history.back();
    })

    /*
    设置生日日期的最大日期
     */
    var birthday = $("#manage-birthday");
    birthday.prop("max", getCurrentDate());

    /*
    输入改变检查格式
     */
    /* ------   账号设置  -------- */
    //获取提示框对象
    //昵称输入框输入改变
    var nickObj = $("#manage-nickname");
    nickObj.on("change", function () {
        var nickVal = nickObj.val();
        if (nickVal != null && nickVal !== "") {
            //昵称非空时检查用户昵称是否可用，并给出提示
            validateNickname($("#manage-info-tip"), nickVal);
        }
    })
    //ID输入框输入改变
    var useridObj = $("#manage-userid");
    useridObj.on("change", function () {
        validateUserId($("#manage-info-tip"), useridObj.val());
    })

    /* ------   修改密码  -------- */
    /*
    新密码和重复输入密码输入框输入改变时，
    检查它们的输入是否相同，并检查输入规范
    注意！提示框会在函数中调用getTipDiv()再次获取，以清除原有样式
    因此这里只需要传入提示框对象即可
     */
    pwOnchangeCheck($("#manage-pw-tip"), $("#manage-pw1"), $("#manage-pw2"));

});

/*--------------------------------------账号设置--------------------------------------*/

/**
 * 修改个人信息时回显用户信息
 */
function echoInfo() {
    //获取cookie，截取用户信息
    var cookies = getCookieValue(userid);
    //顺序为 昵称、生日、性别
    $("#manage-nickname").val(cookies.nickname);
    $("#manage-birthday").val(cookies.birthday);
    if (cookies.sex) {
        //true为男
        $("#male-radio").prop("checked", true);
    } else {
        //false为女
        $("#female-radio").prop("checked", true);
    }
    //id回显
    $("#manage-userid").val(userid);
}

/**
 * 确认修改用户信息时，判断输入情况
 */
function infoChange() {
    //修改信息后如果再次修改，需要获取新的cookie，所以这里再次获取
    var cookies = getCookieValue(userid);
    //获取提示框对象
    var tip = $("#manage-info-tip");
    var nickname = $("#manage-nickname").val();
    var id = $("#manage-userid").val();
    var birthday = $("#manage-birthday").val();
    //如果选择了男，sex=true，匹配cookie
    var sexVal = $("input[type=radio]:checked").val();
    var sex = (sexVal.indexOf("female") == -1);
    // console.log(typeof sex);
    // console.log(typeof cookies.sex);
    //经过测试，sex为Boolean，要转为字符串才能与cookie比较
    //注意indexOf的话， female是包含male的
    if (id == userid && nickname == cookies.nickname &&
        birthday == cookies.birthday && JSON.stringify(sex) == cookies.sex) {
        setTip(tip, "你的信息都没有改还要折腾一下服务器嘛？", "warning");
        return;
    }

    if (nickname == null || nickname === "") {
        setTip(tip, "请选择一个昵称", "warning");
        return;
    } else if (!validateNickname(tip, nickname)) {
        //昵称非空时检查用户昵称是否可用
        return;
    }

    //如果用户没有改ID，则不用判断ID是否可用
    if (id !== userid) {
        //用户id不为空时且不为原ID时判断用户ID是否可用
        if (!validateUserId(tip, id)) {
            /*
             * id不可用时，函数返回值为false，这里会进入该if语句
             * 相应的提示语句已经在validateUserId()中给出，此处不必再次提示
             */
            return;
        }
    }

    //判断是否为合理的生日日期。
    if (!reasonalDate(tip, birthday)) {
        //如果生日日期为空、早于1970-01-01或者晚于修改时的时间则给出提示并返回false
        return;
    }

    //格式化提示框
    getPreparedTip(tip);
    //提交
    infoSumbit(userid);
}

/**
 * 修改用户信息，提交数据
 * @param originalId
 */
function infoSumbit(originalId) {
    $.ajax({
        //请求类型
        type: "POST",
        url: "/ChangeInfoServlet",
        data: {
            "nickname": $("#manage-nickname").val(),
            "userid": $("#manage-userid").val(),
            "birthday": $("#manage-birthday").val(),
            "sex": $("input[name='manage-sex']:checked").val(),
            "original-id": originalId
        },
        success: function () {
            //给出提示框
            toastr.success("修改用户信息成功！");
        },
        error: function () {
            //修改失败
            //给出提示框
            toastr.error("修改失败！请重试！");
        }
    });
}


/*--------------------------------------修改头像--------------------------------------*/
function echoAvatar() {
    //访问servlet获取头像数据并显示
    $("#manage-avatar img").prop("src", "/UserAvatarServlet?id=" + userid);
}

/**
 * 修改头像时，选择图片后在输入框显示图片文件名称
 */
function showFileName() {
    //获取文件路径 包含fakepath
    var filePath = $("#manage-avatar-file").val();
    //截取文件名
    var index = filePath.lastIndexOf("\\");
    var fileName = filePath.substring(index + 1);
    //显示文件名
    $("#manage-avatar-filename").val(fileName);

    //读取图片
    var file = document.getElementById("manage-avatar-file").files[0];
    var pathReader = new FileReader();
    pathReader.readAsDataURL(file);
    pathReader.onloadend = function (oFRevent) {
        var src = oFRevent.target.result;
        //修改头像时，选择图片后在头像框显示图片预览
        $("#manage-avatar-show").prop("src", src);
    }
}

/**
 * 修改头像时，确定修改图片后判断、获取图片文件
 * @returns {boolean} 没有选择图片、文件类型不是支持的图片类型、
 *                    文件大于4M时返回false
 */
function validateFile() {

    //获取文件路径 包含fakepath
    var filePath = $("#manage-avatar-file").val();
    //判断前后提交的头像是否相同，或者是否没有重新选择图片就点击修改按钮
    if (originalPath != null) {
        if (originalPath == filePath) {
            setTip($("#manage-avatar-tip"), "你好像没有换一张图片呀？", "warning");
            return false;
        }
    }
    //更新变量，之后可以比较
    originalPath = filePath;

    //获取文件
    var $c = document.querySelector('#manage-avatar-file'),
        file = $c.files[0];
    var tip = $("#manage-avatar-tip");
    if (file == null) {
        setTip(tip, "你还没选择你的新头像呢", "warning");
        return false;
    }
    var fileName = $("#manage-avatar-file").val();
    //截取文件后缀名，判断文件类型是否支持
    var t = fileName.substring(fileName.lastIndexOf("."), fileName.length).toLocaleLowerCase();
    if (t !== "") {
        // - jpg - jpeg - png - gif - bmp
        if (t !== ".jpg" && t !== ".jpeg" && t !== ".png" && t !== ".bmp" && t !== ".psd" && t !== ".gif") {
            setTip(tip, "暂不支持此类文件", "warning");
            return false;
        }
    }

    //检查文件大小：不超过4M
    var fileSize = file.size;
    var maxSize = 1024 * 1024 * 4;
    if (fileSize > maxSize) {
        //文件太大了
        setTip(tip, "文件太大了！请不要超过4M", "warning");
        return false;
    }

    //读取文件
    var reader = new FileReader();
    reader.readAsDataURL(file);
    var newAvatarImg;
    reader.onload = function (e) {
        newAvatarImg = e.target.result;
        //文件提交
        upload(newAvatarImg);
        //清空提示框
        getPreparedTip(tip);
    };
    return true;
}

/**
 * 将图片上传至服务器
 * @param img 图片数据
 */
function upload(img) {
    $.ajax({
        //请求类型
        type: "POST",
        //预期服务器返回的数据类型
        dateType: "json",
        url: "/UploadServlet",
        data: {
            "userid": userid,
            "img": img
        },
        success: function (data) {
            if (data.change) {
                //成功
                toastr.success("修改头像成功！");
                /*
                更新页眉头像
                如果调用方法showAvatar()，会因为src没有修改而不更新图片
                （前后src相同，不访问Servlet）
                而且浏览器有缓存还是怎么回事，移除img后重新添加也不访问Servlet
                不过我先移除了原有的src属性，再把img数据添加上去之后就可以了
                就不用访问servlet
                 */
                $(".avatar-btn img").removeAttr("src");
                $(".avatar-btn img").attr("src", img);
            } else {
                //失败
                toastr.error("修改头像失败！请重试");
            }
        },
        error: function (data) {
            toastr.error("修改头像失败！请重试");
        }
    });
}


/*--------------------------------------修改密码--------------------------------------*/
/**
 * 点击修改按钮修改密码时，判断输入情况
 */
function pwChange() {
    var tip = $("#manage-pw-tip");
    var pw0 = $("#manage-pw0").val();
    var pw1 = $("#manage-pw1").val();
    if (pw0 == null || pw0 === "") {
        setTip(tip, "请输入原密码", "warning");
    } else if (validatePw(tip, pw1, $("#manage-pw2").val())) {
        if (pw1 === pw0) {
            //新旧密码一样
            setTip(tip, "新密码和旧密码一样诶，这就不用再折腾服务器了吧？", "warning");
        } else {
            //检查新密码的输入
            //来到这里说明输入没有问题，可以提交后台
            pwSubmit(userid);
        }
    }
}

/**
 * 修改密码数据提交后台
 */
function pwSubmit(userId) {
    $.ajax({
        //请求类型
        type: "POST",
        //预期服务器返回的数据类型
        dataType: "json",
        url: "/ChangePwServlet",
        // contentType: "application/json",
        data: {
            "original-pw": $("#manage-pw0").val(),
            "new-pw": $("#manage-pw1").val(),
            "userid": userId
        },
        /**
         * @param data
         * @param data.change
         * @param data.msg
         */
        success: function (data) {
            if (data.change) {
                //密码修改成功
                toastr.success(data.msg);
            } else {
                //原密码错误或者遇到异常，修改失败
                setTip($("#manage-pw-tip"), data.msg, "danger");
            }
        },
        error: function () {
            setTip($("#manage-pw-tip"), "哎呀！出现预期之外的问题！请重试！", "danger");
        }
    });
}