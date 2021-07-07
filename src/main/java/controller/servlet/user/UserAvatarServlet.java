package controller.servlet.user;

import bean.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author 寒洲
 * @description 获取用户头像并输出
 * @date 2020/8/6
 */
@WebServlet("/UserAvatarServlet")
public class UserAvatarServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger("simpleAsyncLogger");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.trace("获取用户头像并输出!");
        //设置编码
        req.setCharacterEncoding("utf-8");
        //获取请求参数
        String idStr = req.getParameter("id");
        byte[] avatarData = null;
        if ("cancelled".equals(idStr)) {
            //显示“已注销”头像
            InputStream fis = this.getServletContext().getResourceAsStream("images/cancelled.png");
            try {
                avatarData = User.getbytes(fis);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //显示用户头像
            Long id = Long.valueOf(idStr);
            //获取头像数据
            UserService us = new UserServiceImpl();
            avatarData = us.getUserAvatar(id);
        }
        if (avatarData != null) {
            //输出头像数据
            ServletOutputStream sos = resp.getOutputStream();
            sos.write(avatarData);
        } else {
            logger.info("无用户头像：userId = " + idStr);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

}
