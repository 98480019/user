package user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;


public class Admin {
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
	private Admin() {
	}

	// 管理员端注销用户
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
				break;
			}
		}
		Users.objectOutput(new File(pro.getProperty("file")), list);
		return res;
	}

	// 管理员端修改用户密码
	public static void changPsw(String username, String newPsw) {
		List<User> read = Users.objectInput(new File(pro.getProperty("file")));
		List<User> list = read;
		Iterator<User> it = list.iterator();
		while (it.hasNext()) {
			User user = it.next();
			if (user.getUsername().equals(username)) {
				user.setPassword(newPsw);
				break;
			}
		}
		System.err.println("提示:密码修改成功");
		Users.objectOutput(new File(pro.getProperty("file")), list);
	}

	// 显示已注册用户
	public static void showUsers() {
		List<User> read = Users.objectInput(new File(pro.getProperty("file")));
		List<User> list = read;
		if (list.size() == 0) {
			System.err.println("提示:暂无用户");
		} else {
			Iterator<User> it = list.iterator();
			while (it.hasNext()) {
				System.err.println(it.next());
			}
		}
	}
}
