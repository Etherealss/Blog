package DaoText;

import dao.UserDao;
import org.junit.Test;
import dao.utils.JDBCUtils;
import bean.User;
import dao.impl.UserDaoImpl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * UserDaoImpl测试类
 */
public class UserDaoImplTest {

    private final UserDao dao = new UserDaoImpl();
    private Connection conn;

    {
        try {
            conn = JDBCUtils.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入
     */
    @Test
    public void insertNewUser() {
        User user = new User();
        user.setId(333L);
        user.setPassword("234");
        user.setNickname("李四");
        user.setBirthday(new Date());
        user.setSex(true);
        try {
            /*  以InputStream获取将头像文件，转为byte[]
                byte[]才可以以Blob储存在数据库中   */
            InputStream is = new FileInputStream("src\\main\\webapp\\images\\boy.png");
            byte[] bytes = User.getbytes(is);
            user.setAvatar(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dao.insertNewUser(conn, user);
        System.out.println("insert测试完成");
        JDBCUtils.closeConnection(conn);
    }

    /**
     * 删除
     */
    @Test
    public void deleteById() {
        dao.deleteById(conn,333L);
        System.out.println("deleteById测试完成");
        JDBCUtils.closeConnection(conn);
    }

    @Test
    public void updatePw() {
        dao.updatePw(conn,567L,"999","888");
        System.out.println("updatePw测试完成");
        JDBCUtils.closeConnection(conn);
    }

    @Test
    public void updateInfo(){
        User user = new User(567L,null,"阿哈",null,true,new Date());
        dao.updateInfo(conn, user ,567L);
        System.out.println("updateInfo测试完成");
    }

//    @Test
//    public void getUserById() {
//        User userById = dao.getUserById(conn, 123L);
//        System.out.println(userById);
//        System.out.println("getUserById测试完成");
//        JDBCUtils.closeConnection(conn);
//    }

    @Test
    public void getUserByInfo(){
        User userById = dao.getUserById(conn, 1231L);
        System.out.println(userById);
        System.out.println("getUserByInfo测试完成");
        JDBCUtils.closeConnection(conn);
    }

    @Test
    public void getAllUser() {
        List<User> allUser = dao.getAllUser(conn);
        for (User s : allUser) {
            System.out.println(s);
        }
        System.out.println("getAllUser测试完成");
        JDBCUtils.closeConnection(conn);
    }

    @Test
    public void existUserById() {
        System.out.println(dao.existUserById(conn, 1231L));
        JDBCUtils.closeConnection(conn);
    }

    @Test
    public void getUserAvatarById() {
    }

	@Test
	public void validatePw() {
        System.out.println("getUserByInfo测试 = " + dao.validatePw(conn, 123123L,"123123"));
        JDBCUtils.closeConnection(conn);
	}

	@Test
	public void validateState() {
        System.out.println(dao.validateState(conn, 123123L));
	}

	@Test
	public void updateAvatar() {
	}

	@Test
	public void getUserById() {
	}

	@Test
	public void getUserNameById() {
	}
}