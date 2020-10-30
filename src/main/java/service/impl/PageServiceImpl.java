package service.impl;

import bean.Blog;
import bean.PageBean;
import dao.BlogDao;
import dao.impl.BlogDaoImpl;
import dao.utils.JDBCUtils;
import service.BlogService;
import service.PageService;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 寒洲
 * @description 获取页面信息
 * @date 2020/8/15
 */
public class PageServiceImpl implements PageService {

	private final BlogDao blogDao = new BlogDaoImpl();

	private static final String BLACK_SPACE = " ";
	@Override
	public PageBean<Blog> getBlogPageBean(String type, String msg, int currentPage, int rows) {

		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			//存入当前页码和每页显示的记录数
			PageBean<Blog> pb = new PageBean<>(currentPage, rows);

			//计算查询索引
			int start = (currentPage - 1) * rows;
			//初始化
			List<Blog> list = null;
			int totalCount = 0;
			//获取并存入博客总数以及博客详细数据
			if (type == null) {
				//所有博客都可以查询
				list = blogDao.getBlogListByPage(conn, start, rows);
				totalCount = blogDao.getBlogCount(conn);
			} else {
				msg = java.net.URLDecoder.decode(msg, "UTF-8");
				System.out.println("PageService:msg转码：" + msg);
				switch (type) {
					case "category":
						//按博客类别查询
						System.out.println("按博客类别查询" + msg);
						list = blogDao.getBlogListByCategory(conn, Integer.parseInt(msg), start, rows);
						totalCount = blogDao.getBlogCountByCategory(conn, Integer.parseInt(msg));
						break;
					case "mlabel":
						//按主要标签查询
						System.out.println("按主要标签查询：" + msg);
						list = blogDao.getBlogListByMlabel(conn, msg, start, rows);
						totalCount = blogDao.getBlogCountByMlabel(conn, msg);
						break;
					case "slabel":
						//按次要标签查询
						System.out.println("按次要标签查询：" + msg);
						list = blogDao.getBlogListBySlabel(conn, msg, start, rows);
						totalCount = blogDao.getBlogCountBySlabel(conn, msg);
						break;
					default:
				}
			}
			pb.setList(list);
			pb.setTotalCount(totalCount);
			//计算总页码数
			//如果 总记录数可以整除以每页显示的记录数，那么总页数就是它们的商
			//否则 说明有几条数据要另开一页显示，总页数+1
			int page = totalCount / rows;
			int totalPage = totalCount % rows == 0 ? page : page + 1;
			if (currentPage > totalPage) {
				//url错误，说！是不是你乱改的！(不是的话当我没说)
				//这里改成0，ajax那里会判断之后提示“无匹配项”
				pb.setTotalPage(0);
			} else {
				pb.setTotalPage(totalPage);
			}
			return pb;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			JDBCUtils.closeConnection(conn);
		}
	}

	@Override
	public PageBean<Blog> searchBlogByWord(int currentPage, int rows, String searchWord) {

		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			//encodeURIComponent解码
			searchWord = java.net.URLDecoder.decode(searchWord, "UTF-8");
			String[] words;
			if (searchWord.contains(BLACK_SPACE)) {
				//搜索词包含空格，
				words = searchWord.split(BLACK_SPACE);
			} else {
				words = new String[]{searchWord};
			}
			//存入当前页码和每页显示的记录数
			PageBean<Blog> pb = new PageBean<>(currentPage, rows);
			//查询到的博客编号，用set避免重复
			//从下面的for循环独立出来是为了初始化，避免addAll空指针
			Set<Integer> blogSet = getSearchBlogNo(conn, words[0]);
			//如果还有其他要查询的字段，则循环获取
			for (int i = 1; i < words.length; i++) {
				Set<Integer> searchBlogNo = getSearchBlogNo(conn, words[i]);
				blogSet.addAll(searchBlogNo);
			}


			List<Blog> blogDetailList = new ArrayList<>();
			//查询所有需要查询的博客
			for (Integer blogNo : blogSet) {
				//自动拆箱，获取博客
				Blog blogByNo = blogDao.getBlogByNo(conn, blogNo);
				blogDetailList.add(blogByNo);
			}
			//获取并存入总记录数
			int totalCount = blogDetailList.size();
			pb.setTotalCount(totalCount);

			//获取并存入博客数据
			//计算索引
			int start = (currentPage - 1) * rows;
			//最终要返回给客户端的数据
			List<Blog> returnList = new ArrayList<>();
			//按索引存储需要的数据
			int last = start + rows;
			while (start < last) {
				//判断是否会超出链表长度
				//在这里判断可以避免链表为空
				if (start >= totalCount) {
					break;
				}
				returnList.add(blogDetailList.get(start++));
			}
			pb.setList(returnList);
			//计算总页码数
			//如果 总记录数可以整除以每页显示的记录数，那么总页数就是它们的商
			//否则 说明有几条数据要另开一页显示，总页数+1
			int page = totalCount / rows;
			int totalPage = totalCount % rows == 0 ? page : page + 1;
			pb.setTotalPage(totalPage);
			return pb;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			JDBCUtils.closeConnection(conn);
		}
	}

	/**
	 * 根据搜索词获取需要查询的博客编号
	 *
	 * @param conn    数据库连接
	 * @param search  搜索词
	 * @return
	 */
	private Set<Integer> getSearchBlogNo(Connection conn,  String search) {
		//需要查询的博客id的Set（不重复）
		Set<Integer> blogNoSet = new HashSet<>();
		//获取主要标签和对应的博客编号
		List<Blog> mlabels = blogDao.getAllBlogMlabel(conn);
		//次要标签
		List<Blog> slabels = blogDao.getAllBlogSlabel(conn);
		//获取博客标题和对应博客编号
		List<Blog> titles = blogDao.getAllBlogTitle(conn);

		//与search比对，匹配的就存入编号
		//主要标签
		for (Blog blog : mlabels) {
			//如果和搜索词匹配就存入博客编号
			if (search.contains(blog.getMlabel()) || blog.getMlabel().contains(search)) {
				blogNoSet.add(blog.getBlogNo());
			}
		}
		//次要
		for (Blog blog : slabels) {
			if (search.contains(blog.getSlabel()) || blog.getSlabel().contains(search)) {
				blogNoSet.add(blog.getBlogNo());
			}
		}
		//标题
		for (Blog blog : titles) {
			if (search.contains(blog.getTitle()) || blog.getTitle().contains(search)) {
				blogNoSet.add(blog.getBlogNo());
			}
		}

		//获取博客分类名
		BlogService bs = new BlogServiceImpl();
		List<String> category = bs.getCategory();
		//遍历博客分类名并和搜索词比对
		for (int i = 0; i < category.size(); i++) {
			//搜索词包含该分类名才匹配成功
			if (search.contains(category.get(i))) {
				//匹配该分类，查询该分类下的所有博客
				//博客分类索引从1开始
				List<Blog> categoryBlog = blogDao.getBlogNoByCategory(conn, i + 1);
				//添加编号
				for (Blog blog : categoryBlog) {
					blogNoSet.add(blog.getBlogNo());
				}
			}
		}
		return blogNoSet;
	}


	@Override
	public PageBean<Blog> getUserBlogPageBean(Long userid, int currentPage, int rows) {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			//存入当前页码和每页显示的记录数
			PageBean<Blog> pb = new PageBean<>(currentPage, rows);
			//获取并存入总记录数
			int totalCount = blogDao.getUserBlogCount(conn, userid);
			pb.setTotalCount(totalCount);
			//获取并存入博客数据
			//计算索引
			int start = (currentPage - 1) * rows;
			List<Blog> list = blogDao.getUserBlogListByPage(conn, userid, start, rows);
			pb.setList(list);
			//计算总页码数
			//如果 总记录数可以整除以每页显示的记录数，那么总页数就是它们的商
			//否则 说明有几条数据要另开一页显示，总页数+1
			int page = totalCount / rows;
			int totalPage = totalCount % rows == 0 ? page : page + 1;
			pb.setTotalPage(totalPage);
			return pb;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			JDBCUtils.closeConnection(conn);
		}
	}
}
