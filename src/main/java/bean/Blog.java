package bean;

import java.util.Date;

/**
 * @author 寒洲
 * @description 博客类
 * @date 2020/8/12
 */
public class Blog {
	/**博客序号*/
	private int blogNo;
	/**作者id*/
	private Long authorId;
	/**作者昵称*/
	private String authorName;
	/**博客标题*/
	private String title;
	/**博客内容*/
	private String content;
	/**博客类别*/
	private int category;
	/**博客主要标签main label*/
	private String mlabel;
	/**博客次要标签 secondary label*/
	private String slabel;
	/**创建日期*/
	private Date createdate;
	/**修改日期*/
	private Date updatedate;

	@Override
	public String toString() {
		return "Blog{" +
				"blogNo=" + blogNo +
				", authorId=" + authorId +
				", authorName='" + authorName + '\'' +
				", title='" + title + '\'' +
				", content='" + content + '\'' +
				", category=" + category +
				", mlabel='" + mlabel + '\'' +
				", slabel='" + slabel + '\'' +
				", createdate=" + createdate +
				", updatedate=" + updatedate +
				'}';
	}

	public Blog(){}

	/**
	 * @param blogNo 博客序号
	 * @param authorId 作者ID
	 * @param authorName 作者昵称
	 * @param title 博客标题
	 * @param content 博客内容
	 * @param category 博客类别
	 * @param mlabel 主要标签
	 * @param slabel 次要标签
	 * @param createdate 创建日期
	 * @param updatedate 修改日期
	 */
	public Blog(int blogNo, Long authorId, String authorName, String title, String content, int category, String mlabel, String slabel, Date createdate, Date updatedate) {
		this.blogNo = blogNo;
		this.authorId = authorId;
		this.authorName = authorName;
		this.title = title;
		this.content = content;
		this.category = category;
		this.mlabel = mlabel;
		this.slabel = slabel;
		this.createdate = createdate;
		this.updatedate = updatedate;
	}

	/**
	 * @param authorId 作者ID
	 * @param title 博客标题
	 * @param category 博客类别
	 * @param mlabel 主要标签
	 * @param slabel 次要标签
	 * @param createdate 创建日期
	 * @param updatedate 修改日期
	 */
	public Blog(Long authorId, String title, String content, int category, String mlabel, String slabel, Date createdate, Date updatedate) {
		this.authorId = authorId;
		this.title = title;
		this.content = content;
		this.category = category;
		this.mlabel = mlabel;
		this.slabel = slabel;
		this.createdate = createdate;
		this.updatedate = updatedate;
	}

	public int getBlogNo() {
		return blogNo;
	}

	public void setBlogNo(int blogNo) {
		this.blogNo = blogNo;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getMlabel() {
		return mlabel;
	}

	public void setMlabel(String mlabel) {
		this.mlabel = mlabel;
	}

	public String getSlabel() {
		return slabel;
	}

	public void setSlabel(String slabel) {
		this.slabel = slabel;
	}
}
