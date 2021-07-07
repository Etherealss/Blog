package controller.servlet.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description 用户注册时判断用户ID是否已被占用
 * @author 寒洲
 * @date 2020/8/4
 */
@WebServlet("/ValidateUserIdServlet")
public class ValidateUserIdServlet extends HttpServlet {

	private final Logger logger = LoggerFactory.getLogger("simpleAsyncLogger");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.trace("检查用户id是否存在");
		//设置编码
		req.setCharacterEncoding("utf-8");
		//设置响应的数据格式为json
		resp.setContentType("application/json;charset=utf-8");

		Long userid = Long.valueOf(req.getParameter("userId"));
		//返回信息的map
		Map<String, Object> info = new HashMap<String, Object>();
		Boolean validate = new UserServiceImpl().validateUserId(userid);
		if (validate != null){
			if (validate) {
				//存在该用户
				info.put("userExist", true);
				info.put("msg", "这个用户ID太受欢迎了，请换一个叭~");
			}else {
				//不存在
				info.put("userExist", false);
				info.put("msg", "哎哟不错哦，这个用户ID独一无二！");
			}
		}else{
			//出现意外状况
			//不存在
			logger.trace("检查用户ID出现意外");
			info.put("userExist", false);
			info.put("msg", "检查ID时出现意外状况，请重试或刷新页面");
		}
		//将map转为JSON
		ObjectMapper mapper = new ObjectMapper();
		//发送给客户端
		mapper.writeValue(resp.getWriter(), info);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}


//	private Boolean validate(Long userid){
//		Connection conn = null;
//		try{
//			conn = JDBCUtils.getConnection();
//			UserDao userDao = new UserDaoImpl();
//			//查询用户id，不存在为false
//			return userDao.existUserById(conn, userid);
//		}catch (Exception e){
//			e.printStackTrace();
//			//出现异常返回null
//			return null;
//			/*
//			按道理说，Boolean非黑即白，返回null好像说不过去
//			不过我觉得可以解释为：出现了异常所以用户ID是否存在并不清楚
//			也就是说null蕴含着：不清楚是true还是false的意思
//			正好可以说明出现异常的处境
//			正常情况下Boolean肯定只有true和false啦
//			这里感觉可能会有“把null当成第三个布尔值来判断”的嫌疑
//			所以狡辩一下o(￣ˇ￣)o
//			 */
//		}finally {
//			JDBCUtils.closeConnection(conn);
//		}
//	}
}
