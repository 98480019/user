package user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Main {
	//***
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

	public static void main(String[] args) throws ParseException {
		Scanner scan = new Scanner(System.in);
		File file = new File(pro.getProperty("file"));
		System.out.println(" ************************");
		System.out.println(" *\t欢迎来到本系统 \t*");
		System.out.println(" ************************");
		// 系统主体start
		while (true) {
			// 系统启动读档
			List<User> record = Users.objectInput(file);
			// 把读出来的信息放到list集合,用于后期修改;
			List<User> list = record;
			// 获取所有用户名,在登录模块用于匹配是否存在该用户
			List<String> uname = new ArrayList<>();
			// 主界面
			System.out.println("\t1.注册");
			System.out.println("\t2.登录");
			System.out.println("\t3.管理员登录\n\t  ***管理员功能***\n\t  ①查看已注册该系统的用户\n\t  ②修改用户密码\n\t  ③注销指定用户");
			System.out.println("\texit 退出该系统");
			System.out.print("指令:");
			String code = scan.nextLine();

			// regiest 注册模块start
			if (code.equals("1")) {
				while (true) {
					System.out.println("------用户注册------");
					// 输入账号密码
					System.out.print("新用户:");
					String radmin = scan.nextLine();
					System.out.print("新密码:");
					String rpsw = scan.nextLine();
					boolean res = Users.regiest(list, radmin, rpsw);
					// 注册成功,返回主界面 (注册失败,方法内打印失败信息,继续停留在注册模块)
					if (res == true) {
						System.err.println("提示:注册成功!");
						break;
					}
				}
			}
			// 注册模块end

			// login 登录模块start
			if (code.equals("2")) {
				// 登录验证模块 start
				for (User username : list) {// 把所有已注册用户名存到一个String类型的的集合.用于登录时匹配是否存在该用户
					uname.add(username.getUsername());
				}
				int i = 3;
				ql: while (true) {
					System.out.println("------用户登录------");
					System.out.print("用户名:");
					String ladmin = scan.nextLine();
					System.out.print("密    码:");
					String lpsw = scan.nextLine();
					// 调用login方法匹配集合中该用户名和密码是否正确
					if (Users.login(list, ladmin, lpsw)) {
						while (true) {
							System.err.println("提示:登陆成功");
							// 用户功能模块start
							System.out.println("\t1.查看注册时间");
							System.out.println("\t2.修改密码");
							System.out.println("\t3.注销该用户");
							System.out.println("exit 退出");
							System.out.print("指令:");
							String lg = scan.nextLine();
							// 查看注册时间
							if (lg.equals("1")) {
								System.err.println("提示:您的注册时间为:" + User.getRegTime(ladmin));
							}
							// 修改密码
							if (lg.equals("2")) {
								System.out.print("新密码:");
								String newPsw = scan.nextLine();
								User.changePsw(ladmin, newPsw);
								break ql;
							}
							// 注销该账户(从内存中删除该用户无法恢复)
							if (lg.equals("3")) {
								System.err.println("警告:一旦注销将不可恢复!确定注销该用户?");
								System.err.println("Y or N");
								String select = scan.nextLine();
								if (select.equalsIgnoreCase("Y")) {
									boolean res = User.removeUser(ladmin);
									// 注销后返回主界面
									if (res == true) {
										System.err.println("提示:该用户已注销");
										break ql;
									}
								}
								if (select.equalsIgnoreCase("N")) {
									System.err.println("提示:本次操作取消");
								}
							}
							// 退出登录
							if (lg.equalsIgnoreCase("exit")) {
								System.err.println("提示:退出登录   Y or N");
								String select = scan.nextLine();
								// 退出登录,返回主界面
								if (select.equalsIgnoreCase("Y")) {
									System.err.println("提示:"+ladmin+"再见!");
									break ql;
								}
								if (select.equalsIgnoreCase("N")) {

								}
							}
						} // 用户功能模块end
					} else {// 登录失败判定,三次输入机会
						i--;
						// 通过输入的用户名,判断该用户是否注册;
						if (!uname.contains(ladmin)) {
							System.err.println("提示:该用户不存在,请重试!  ");
							break;
						}
						// 存在该用户,但密码错误
						if (i == 0) {// 次数用完 i=0
							System.err.println("提示:今日次数用完!");
							break;
						} else {// 密码错误,i!=0
							System.err.println("提示:用户名或密码错误!\t今日还有" + i + "次机会");
						}
					}
				} // 登录验证模块 end
			} // 登录模块end

			// 管理员模块start
			if (code.equals("3")) {
				qa: while (true) {
					// 输入管理员账号和密码
					System.out.print("root name:");
					String radmin = scan.nextLine();
					System.out.print("root password:");
					String rpsw = scan.nextLine();
					while (true) {
						// 管理员账号 root 管理员密码 123456 (暂时固定)
						if (radmin.equals("root") && rpsw.equals("123456")) {
							// 管理员操作界面
							System.out.println("进入管理员界面");
							System.out.println("\t1.查看所有用户");
							System.out.println("\t2.修改用户密码");
							System.out.println("\t3.注销指定用户");
							System.out.println("\texit 退出");
							System.out.print("指令:");
							String rootcode = scan.nextLine();
							// 管理员功能实现
							if (rootcode.equals("1")) {
								Admin.showUsers();
							}
							if (rootcode.equals("2")) {
								// 若读取的存档集合list为无元素,无需操作
								if (list.size() == 0) {
									System.err.println("提示:无用户可操作");
								} else {
									System.out.print("用户名:");
									String username = scan.nextLine();
									System.out.print("新密码:");
									String newPsw = scan.nextLine();
									Admin.changPsw(username, newPsw);
								}
							}
							if (rootcode.equals("3")) {
								// 若读取的存档集合list为无元素,无需操作
								if (list.size() == 0) {
									System.err.println("提示:无用户可操作!");
								} else {
									System.err.print("要注销的用户名:");
									String username = scan.nextLine();
									System.err.println("警告:一旦注销将不可恢复!确定注销该用户?");
									System.err.println("Y or N");
									String select = scan.nextLine();
									if (select.equalsIgnoreCase("Y")) {
										boolean res = Admin.removeUser(username);
										if(res == true) {
											System.err.println("提示:该用户已注销!");
										}else {
											System.err.println("提示:该用户不存在!");
										}
									}
									if (select.equalsIgnoreCase("N")) {
										System.err.println("提示:本次操作取消");
									}
								}
							}
							if (rootcode.equals("exit")) {
								System.err.println("提示:退出管理员界面   Y or N");
								String select = scan.nextLine();
								if (select.equalsIgnoreCase("Y")) {
									break qa;
								}
								if (select.equalsIgnoreCase("N")) {

								}
							}
						} else {// 管理员账户密码错误,返回
							System.err.println("提示:输入有误");
							break;
						}
					}
				}
			} // 管理员模块end

			// 退出系统判定
			if (code.equalsIgnoreCase("exit")) {
				System.err.println("提示:退出本系统\n   Y or N");
				String select = scan.nextLine();
				if (select.equalsIgnoreCase("Y")) {
					System.err.println("提示:系统已关闭,感谢您的使用!");
					// 关闭系统前把修改后的list存档
					Users.objectOutput(file, list);
					System.exit(0);
					scan.close();
				}
				if (select.equalsIgnoreCase("N")) {

				}
			}
		} // 系统主体end
	}
}
