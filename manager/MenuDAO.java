package manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuDAO {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:orcl1";
	private String id = "system";
	private String pass = "1111";
	private Connection conn = null;
	private static MenuDAO menudao = null;

	public MenuDAO() {
		try {
			Class.forName(driver);
		} catch (Exception e) {

		}

	}

	public static MenuDAO getInstance() { // 싱글톤
		if (menudao == null) {
			menudao = new MenuDAO();
		}
		return menudao;
	}

	private Connection getConnection() {
		try {
			conn = DriverManager.getConnection(url, id, pass);
		} catch (Exception e) {

		}
		return conn;
	}

	public void insert(Super ss) {
		PreparedStatement pstm = null;
		try {
			if (getConnection() != null) {
				String sql = "insert into C_menu values(?,?,?,?)";
				pstm = conn.prepareStatement(sql);
				pstm.setString(1, ss.getName());
				pstm.setInt(2, ss.getPrice());
				pstm.setString(3, ss.getTag());
				pstm.setString(4, ss.getType());
				pstm.executeUpdate();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				pstm.close();
			} catch (Exception e) {

			}
		}
	}

	public ArrayList<Super> select() {
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<Super> menuList = new ArrayList<>();
		try {
			if (getConnection() != null) {
				String sql = "select * from C_menu";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					Super ss = new Coffee();
					ss.setName(rs.getString("m_name"));
					ss.setPrice(rs.getInt("price"));
					ss.setTag(rs.getString("tag"));
					ss.setType(rs.getString("type"));
					menuList.add(ss);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				stmt.close();
				rs.close();
			} catch (Exception e) {

			}
		}
		return menuList;
	}

	public void delete(String deletename) {
		PreparedStatement pstm = null;
		try {
			if (getConnection() != null) {
				String sql = "delete from C_menu where m_name=?";
				pstm = conn.prepareStatement(sql);
				pstm.setString(1, deletename);
				pstm.executeUpdate();

			}

		} catch (Exception e) {

		} finally {
			try {
				conn.close();
				pstm.close();
			} catch (Exception e) {

			}
		}
	}

	public void update(String modname) {
		Scanner s = new Scanner(System.in);
		System.out.println("원하는 수정메뉴를 선택하세요");
		System.out.println("1.메뉴명수정  2.가격수정  3.태그수정");
		PreparedStatement pstm = null;
		try {
			if (getConnection() != null) {
				int num = s.nextInt();
				s.nextLine();
				switch (num) {
				case 1:
					System.out.println("수정하고 싶은 메뉴명을 입력하세요");
					String newname = s.nextLine();
					String sql = "update C_menu set m_name=? where m_name=?";
					pstm = conn.prepareStatement(sql);
					pstm.setString(1, newname);
					pstm.setString(2, modname);
					pstm.executeUpdate();
					break;
				case 2:
					System.out.println("수정하고 싶은 가격을 입력하세요");
					int newprice = s.nextInt();
					s.nextLine();
					sql = "update C_menu set price=? where m_name=?";
					pstm = conn.prepareStatement(sql);
					pstm.setInt(1, newprice);
					pstm.setString(2, modname);
					pstm.executeUpdate();
					break;
				case 3:
					System.out.println("수정하고 싶은 태그을 입력하세요");
					System.out.println("1.Best 2.New  3.SoldOut 4.없음");
					int newtag = s.nextInt();
					s.nextLine();
					switch (newtag) {
					case 1:
						sql = "update C_menu set tag='Best' where m_name=?";
						pstm = conn.prepareStatement(sql);
						pstm.setString(1, modname);
						pstm.executeUpdate();
						break;
					case 2:
						sql = "update C_menu set tag='New' where m_name=?";
						pstm = conn.prepareStatement(sql);
						pstm.setString(1, modname);
						pstm.executeUpdate();
						break;
					case 3:
						sql = "update C_menu set tag='SoldOut' where m_name=?";
						pstm = conn.prepareStatement(sql);
						pstm.setString(1, modname);
						pstm.executeUpdate();
						break;
					case 4:
						sql = "update C_menu set tag=null where m_name=?";
						pstm = conn.prepareStatement(sql);
						pstm.setString(1, modname);
						pstm.executeUpdate();
						break;
					}

				}
			}
		} catch (Exception e) {

		} finally {
			try {
				pstm.close();
				conn.close();
			} catch (Exception e) {

			}
		}

	}

	public ArrayList<Super> search(String searchname) {
		ArrayList<Super> menulist = new ArrayList<>();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			if (getConnection() != null) {
				String sql = "select * from C_menu where m_name like ?";
				pstm = conn.prepareStatement(sql);
				pstm.setString(1, "%" + searchname + "%");
				rs = pstm.executeQuery();
				while (rs.next()) {
					Super ss = new Coffee();
					ss.setName(rs.getString("m_name"));
					ss.setPrice(rs.getInt("price"));
					ss.setTag(rs.getString("tag"));
					ss.setType(rs.getString("type"));
					menulist.add(ss);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				pstm.close();
				rs.close();
			} catch (Exception e) {

			}
		}
		return menulist;
	}

}