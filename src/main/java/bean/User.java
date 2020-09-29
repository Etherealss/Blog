package bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;

/**
 * @Description 用户类
 * @Author 寒洲
 * @Date 2020/7/24
 */
public class User {
    private Long id;
    private String password;
    private String nickname;
    private byte[] avatar;
    /** true为小哥哥，false为小姐姐 */
    private Boolean sex;

    //JSON数据格式化
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        String result;
        result = "User{ " + " id = '" + id;
        if (password != null){
            result += ", password = '" + password + '\'';
        }
        result += ", nickname = '" + nickname + '\'';
        if (avatar != null){
            result +=  ", avatar[0~3] = '" + avatar[0] + avatar[1] + avatar[2] + avatar[3] + '\'';
        }
        result += ", sex='" + sex + '\'' + ", birth=" + birthday + '}';
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public Boolean getSex() {
        return sex;
    }

    /**
     * @param sex true为小哥哥，false为小姐姐
     */
    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public User() {}

    /**
     * @param id
     * @param password
     */
    public User(Long id, String password) {
        this.id = id;
        this.password = password;
    }

    /**
     * @param id
     * @param password
     * @param nickname
     * @param sex
     * @param birthday
     */
    public User(Long id, String password, String nickname,  Boolean sex, Date birthday) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.sex = sex;
        this.birthday = birthday;
    }

    /**
     * @param id
     * @param password
     * @param nickname
     * @param avatar
     * @param sex
     * @param birthday
     */
    public User(Long id, String password, String nickname, byte[] avatar, Boolean sex, Date birthday) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.avatar = avatar;
        this.sex = sex;
        this.birthday = birthday;
    }

    /**
     * 工具方法
     * 将InputStream转换为byte[]返回，用于将文件转为byte[]传入数据库
     * @param is 输入流对象
     * @return InputStream转换后的byte[]
     * @throws Exception
     */
    public static byte[] getbytes(InputStream is) throws Exception{
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        byte[] buffer=new byte[1024];
        int len;
        while((len=is.read(buffer))!=-1){
            bos.write(buffer,0,len);
        }
        is.close();
        bos.flush();
        return bos.toByteArray();
    }
}

