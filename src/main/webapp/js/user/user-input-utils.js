/**
 * @ignore  =====================================================================================
 * @fileoverview 用户注册、修改信息时的通用操作
 * @author  寒洲
 * @date 2020/8/7
 * @ignore  =====================================================================================
 */

/**
 * 检查id的字符和长度：
 * 1、仅包含数字
 * 2、6~20个字符
 * @param id
 * @returns {boolean} 符合标准返回true
 */
function standardId(id) {
    var reg = new RegExp("^\\d{6,20}$");
    //符合规定返回true
    return reg.test(id);
}

/**
 * 检查密码的字符和长度
 * 1、仅包含大小写英文字母、数字和下划线
 * 2、6~20个字符
 * @param password
 * @returns {boolean} 符合标准返回true
 */
function standardPw(password) {
    var reg = new RegExp("^\\w{6,20}$");
    //符合规定返回true
    return reg.test(password);
}

/**
 * 检查昵称的字符和长度
 * 1~8个汉字、英文、数字，可以包含下划线
 * @param nickname
 * @returns {boolean} 符合标准返回true
 */
function standardNickname(nickname) {
    var reg = new RegExp("^[\u4e00-\u9fa5_a-zA-Z0-9]{1,8}$");
    //符合规定返回true
    return reg.test(nickname);

}

/**
 * 检查两次密码：
 *      1、是否有输入
 *      2、输入是否一致
 *      3、密码是否符合规定
 * 会通过提示框给出对应提示
 * @param tipObj 提示框
 * @param pw1Val 第一次输入密码的<strong>值</strong>
 * @param pw2Val 重复输入密码的<strong>值</strong>
 * @returns {boolean} 所有数据均符合要求返回true，否则为false
 */
function validatePw(tipObj, pw1Val, pw2Val) {
    //判断第一次密码有没有输入
    if (pw1Val == null || pw1Val === "") {
        setTip(tipObj,"请输入你的账号密码","warning");
        return false;
    } else {
        //检查密码规范
        if (!standardPw(pw1Val)) {
            //密码已输入，但不符合规定
            setTip(tipObj,"密码由6~20个大小写英文字母、数字或者下划线组成，请检查哦","warning");
            return false;
        }
    }
    //对比两次输入的密码
    if (pw2Val != null && pw2Val !== "") {
        //已经第二次输入了密码
        if (pw1Val === pw2Val) {
            //密码相同
            setTip(tipObj,"两次密码相同~","success");
            return true;
        } else {
            //两次密码不相同
            setTip(tipObj,"两次密码<strong>不相同</strong>","danger");
            return false;
        }
    } else {
        //没有再次输入密码
        setTip(tipObj,"啊嘞？你没有再次输入密码","warning");
        return false;
    }
}

/**
 * 当两个密码输入框之一输入改变时，
 * 判断判断两次密码是否相同，并检查其输入规范
 * @param tip 提示框
 * @param pw1Obj 首次输入密码的<strong>输入框对象</strong>
 * @param pw2Obj 再次输入密码的<strong>输入框对象</strong>
 */
function pwOnchangeCheck(tip, pw1Obj, pw2Obj) {
    //自动检测两次密码是否相同，同时会检测密码的输入规范
    //两个输入框都绑定
    pw1Obj.on("change", function () {
        //在输入改变时才获取值，而不能再事件外获取
        validatePw(tip, pw1Obj.val(), pw2Obj.val());
    });
    pw2Obj.on("change", function () {
        validatePw(tip, pw1Obj.val(), pw2Obj.val());
    });
}

/**
 * 判断用户ID是否可用
 * @returns {boolean} 如果为空、包含非数字字符或者已被注册则返回false，表示不可用
 */
function validateUserId(tipObj, userid) {
    //判断用户id是否可用的变量
    var bool;
    //检查是否为空
    if (userid == null || userid === "") {
        setTip(tipObj, "请选择一个账号ID", "warning");
        bool = false;
    } else if (!standardId(userid)) {
        //检查是否为标准id
        //进入if语句则不是标准id
        //用户名不存在，可以使用
        setTip(tipObj, "账号ID仅能包含数字且在6~20个字符内！", "warning");
        bool = false;
    } else {
        /*
         * 发送ajax请求
         * 服务器返回的信息：
         *  userExist：true；用户存在
         *  userExist：false；用户不存在
         */
        $.ajax({
            type: "POST",
            //同步
            async: false,
            url: "/ValidateUserIdServlet",
            dataType: "json",
            data: {userId: userid},
            /**
             * @param data
             * @param {Boolean} data.userExist
             * @param {String} data.msg
             */
            success: function (data) {
                if (data.userExist) {
                    //用户名存在，提示用户换一个ID
                    setTip(tipObj, data.msg, "danger");
                    bool = false;

                } else {
                    //用户名不存在，可以使用
                    setTip(tipObj, data.msg, "success");
                    bool = true;
                }
            },
            error: function () {
                toastr.error("检查id是否重复时发生错误！请稍后重试");
                bool = false;
            }
        });
    }
    return bool;
}

/**
 * 昵称非空时检查用户昵称是否可用，并给出提示
 * 昵称由3~10个汉字、英文或数字组成，可以包含下划线
 * @param nickVal
 * @param tipObj
 * @returns {boolean}
 */
function validateNickname(tipObj, nickVal) {
    if (!standardNickname(nickVal)) {
        // 密码已输入，且不符合规定
        setTip(tipObj,"昵称由3~10个汉字、英文或数字组成，可以包含下划线","warning");
        return false;
    }
    return true;
}

/**
 * 得到当前时间
 * @returns {string} 日期格式为yyyy-MM-dd
 */
function getCurrentDate() {
    //得到当前时间
    var date_now = new Date();
    //得到当前年份
    var year = date_now.getFullYear();
    //得到当前月份
    //  注意！js中获取Date中的month时结果为0~11
    //  判断当前月份是否小于10，如果小于，那么就在月份的前面补0
    var month = date_now.getMonth() + 1 < 10 ? "0" + (date_now.getMonth() + 1) : (date_now.getMonth() + 1);
    //得到当前几号
    var day = date_now.getDate() < 10 ? "0" + date_now.getDate() : date_now.getDate();
    return year + "-" + month + "-" + day;
}

/**
 * 比较日期是否超出范围
 * @param date 生日日期
 * @param compareDate 被比较的日期
 * @returns {boolean} 大于等于返回true，小于返回false
 */
function compareDate(date, compareDate) {
    //初始化数组
    var arr1 = [];
    var arr2 = [];
    if (date != null && compareDate != null) {
        //切割字符串
        arr1 = date.split('-');
        var date1 = new Date(arr1[0], parseInt(arr1[1]) - 1, arr1[2]);
        arr2 = compareDate.split('-');
        var date2 = new Date(arr2[0], parseInt(arr2[1]) - 1, arr2[2]);
        //大于等于返回true，小于返回false
        return (date1 >= date2);
    }
}

/**
 * 判断是否为合理的生日日期。
 * @param birthday 生日日期
 * @param tip 提示框
 * @returns {boolean} 如果生日日期为空、早于1970-01-01或者晚于修改时的时间则给出提示并返回false
 */
function reasonalDate(tip, birthday) {
    if (birthday == null || birthday === "") {
        setTip(tip,"请选择你的生日","warning");
        return false;
    }
    if (compareDate(birthday, "1970-01-01")) {
        //大于等于最小日期，合理
        if (compareDate(birthday, getCurrentDate())) {
            //大于等于当前日期，不合理
            setTip(tip,"那时候才出生，太晚了！","warning");
            return false;
        }

        return true;
    } else {
        //小于最小日期
        setTip(tip,"生日不能早于1970-01-01","warning");
        return false;
    }
}