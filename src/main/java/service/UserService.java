package service;

import bean.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author 寒洲
 * @description 规范用户操作的接口
 * @date 2020/8/17
 */
public interface UserService {

    /**
     * 检查和提交注册的user对象
     * @param user
     * @return 遇到意外返回false
     */
    Boolean register(User user);

    /**
     * 通过数据库判断登录情况，获取登录结果
     * @param id
     * @param password
     * @return 包含登录结果的HashMap
     */
    Map<String, Object> login(Long id, String password);

    /**
     * @param userid
     * @param byteImg 图片的数据
     * @return 遇到意外返回false
     */
    Boolean changeAvatar(Long userid, byte[] byteImg);

    /**
     * 修改用户信息
     * @param user 新的用户信息包
     * @param originalId 原始的用户id
     * @return 遇到意外返回false
     */
    Boolean changeInfo(User user, Long originalId);

    /**
     * 操作数据库
     * @param userid
     * @param originalPw
     * @param newPw
     * @return 密码错误返回false，不修改密码；遇到意外返回null
     */
    Boolean changePw(Long userid, String originalPw, String newPw);

    /**
     * 删除账号
     * @param user 包含了账号ID和密码的
     * @return 出现异常返回null，账号或密码错误返回false，成功为true
     */
    Boolean deleteUser(User user);

    /**
     * 获取用户的个人信息
     * @param userid
     * @return
     */
    User getUserInfo(Long userid);

    /**
     * 获取头像数据
     * @param id
     * @return 图片的byte[]数据
     */
    byte[] getUserAvatar(Long id);

    /**
     * 检查ID是否存在
     * @param userid 用户ID
     * @return 存在true，不存在false，查询时出现错误返回null
     */
    Boolean validateUserId(Long userid);

    /**
     * 用户退出登录
     * @param userid
     */
    void logout(Long userid);

    /**
     * 服务器关闭，所有用户下线
     */
    void logoutAllUser();

}
