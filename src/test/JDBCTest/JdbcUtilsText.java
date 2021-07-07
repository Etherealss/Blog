package JDBCTest;

import org.junit.Test;
import dao.utils.JdbcUtils;

import java.sql.Connection;

/**
 * 测试JDBC类
 * @Author 寒洲
 * @Date 2020/7/24
 */
public class JdbcUtilsText {

    /**
     * 测试获取数据库连接
     * @throws Exception
     */
    @Test
    public void getConnection() throws Exception{
        Connection connection = JdbcUtils.getConnection();
        System.out.println(connection);
    }
}
