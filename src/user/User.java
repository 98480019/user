package user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

public class User implements Serializable {
	static Properties pro = new Properties();
	static {
		try {
			pro.load(new FileInputStream("property.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static final long serialVersionUID = 18777524800534807L;
	private String username;
	private String password;
	private Date regiestTime;
	private String dateStr;
	DateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public User(String username, String password) {
		regiestTime = new Date();
		this.username = username;
		this.password = password;
		this.dateStr = date.format(regiestTime);
	}

	public Date getRegiestTime() {
		return regiestTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		if (username.matches("\\w{6,}")) {
			this.username = username;
		} else {
			System.err.println("用户名格式不合法! (用户名格式:6位或6位以上的数字，字母，下划线组成)");
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if (password.matches("[A-Za-z0-9]{6,}")) {
			this.password = password;
		} else {
			System.err.println("提示:密码格式不合法! (密码格式:6位或6位以上的数字，字母组成)");
		}

	}

	public String getDateStr() {
		return dateStr;
	}

	@Override
	public String toString() {
		return "User [用户名:" + username + ", 密码:" + password + ", 注册时间:" + dateStr + "]";
	}

	// 获取注册时间;
	public static String getRegTime(String username) {
		String time = "";
		List<User> read = Users.objectInput(new File(pro.getProperty("file")));
		List<User> list = read;
		for (User user : list) {
			if (user.getUsername().equals(username)) {
				time = user.getDateStr();
			}
		}
		return time;
	}

	// 修改密码
	public static void changePsw(String username, String newPsw) {
		List<User> read = Users.objectInput(new File(pro.getProperty("file")));
		List<User> list = read;
		Iterator<User> it = list.iterator();
		while (it.hasNext()) {
			User user = it.next();
			if (user.getUsername().equals(username)) {
				user.setPassword(newPsw);
			}
		}
		System.err.println("提示:密码修改成功,请重新登录!");
		Users.objectOutput(new File(pro.getProperty("file")), list);
	}

	// 注销该用户
	public static boolean removeUser(String username) {
		List<User> read = Users.objectInput(new File(pro.getProperty("file")));
		List<User> list = read;
		boolean res = false;
		ListIterator<User> lit = list.listIterator();
		while (lit.hasNext()) {
			User user = lit.next();
			if (user.getUsername().equals(username)) {
				lit.remove();
				res = true;
			} else {
				System.err.println("提示:操作失败,无法找到该用户!");
			}
		}
		Users.objectOutput(new File(pro.getProperty("file")), list);
		return res;
	}
}
