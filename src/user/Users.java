package user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Users {
	/**
	 * UserMessage.txt引入
	 */
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
	private Users() {}

	/**
	 * 存档
	 * @param f
	 * @param list
	 * @return
	 */
		public static List<User> objectOutput(File f, List<User> list) {
			ObjectOutputStream os = null;
			try {
				os = new ObjectOutputStream(new FileOutputStream(f));
				os.writeObject(list);
//				System.out.println("存档成功!");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return list;
		}

	/**
	 *读档
	 * @param f
	 * @return
	 */
		public static List<User> objectInput(File f) {
			List<User> list = new ArrayList<>();
			ObjectInputStream is = null;
			try {
				is = new ObjectInputStream(new FileInputStream(f));
				Object obj = is.readObject();
				list = (List<User>) obj;
//				System.out.println("读档成功!");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return list;
		}

	/**
	 * 登录验证
	 * @param list
	 * @param admin
	 * @param psw
	 * @return
	 */
		public static boolean login(List<User> list, String admin, String psw) {
			boolean res = false;
			for (User u : list) {
				if (u.getUsername().equals(admin) && u.getPassword().equals(psw)) {
					res = true;
					break;
				} else {
					res = false;
				}
			}
			return res;
		}

	/**
	 * 注册方法
	 * @param list
	 * @param admin
	 * @param psw
	 * @return
	 */
		public static boolean regiest(List<User> list, String admin, String psw) {
			boolean res = false;
			File f = new File(pro.getProperty("file"));
			List<String> uname = new ArrayList<>();
			if (list.size() != 0) {
				for (User user : Users.objectInput(f)) {
					uname.add(user.getUsername());
				}
			}
			// 用户名使用数字，字母，下划线组成，必须6位(包含6位)以上
			boolean illadmin = admin.matches("\\w{6,}");
			// 密码 6位（包含6位）以上字母，数字 组成
			boolean illpsw = admin.matches("[A-Za-z0-9]{6,}");
			if (illadmin == true) {
				if (uname.contains(admin)) {
					System.out.println("该用户名已存在!");
					res = false;
				} else {
					if (illpsw == true) {
						list.add(new User(admin, psw));
						list = Users.objectOutput(f, list);// 存档
						res= true;
					} else {
						System.out.println("密码格式不合法! (密码格式:6位或6位以上的数字，字母组成)");
						res= false;
					}
				}
			} else {
				System.out.println("用户名格式不合法! (用户名格式:6位或6位以上的数字，字母，下划线组成)");
				return false;
			}
			return res;
		}
}
