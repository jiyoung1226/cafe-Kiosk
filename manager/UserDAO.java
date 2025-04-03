package manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class UserDAO {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:orcl1";
	private String id = "system";
	private String pass = "1111";
	private Connection conn = null;
	private static UserDAO userdao = null;

	public UserDAO() {
		try {
			Class.forName(driver);
		} catch (Exception e) {

		}
	}

	public static UserDAO getInstance() { // 싱글톤
		if (userdao == null) {
			userdao = new UserDAO();
		}
		return userdao;
	}

	private Connection getConnection() {
		try {
			conn = DriverManager.getConnection(url, id, pass);
		} catch (Exception e) {

		}
		return conn;
	}

	public void insert(String phoneNumber, int stamp) {
		PreparedStatement pstm = null;
		try {
			if (getConnection() != null) {
				String sql = "insert into C_Member values(?,?)";
				pstm = conn.prepareStatement(sql);
				pstm.setString(1, phoneNumber);
				pstm.setInt(2, stamp);
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

	public ArrayList<UserDTO> select() {
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<UserDTO> userList = new ArrayList<>();
		try {
			if (getConnection() != null) {
				String sql = "select * from C_Member";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					UserDTO udto = new UserDTO();
					udto.setPhoneNum(rs.getString("p_num"));
					udto.setStamp(rs.getInt("stamp"));
					userList.add(udto);
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
		return userList;
	}

	public void delete(String deletePhoneNum) {
		PreparedStatement pstm = null;
		try {
			if (getConnection() != null) {
				String sql = "delete from C_Member where p_num=?";
				pstm = conn.prepareStatement(sql);
				pstm.setString(1, deletePhoneNum);
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

	public void update(String modPhoneNum) {
		Scanner s = new Scanner(System.in);
		System.out.println("새로운 번호를 입력하세요");
		String newPhoneNum = s.nextLine();
		PreparedStatement pstm = null;
		try {
			if (getConnection() != null) {
				String sql = "update C_Member set p_num=? where p_num=?";
				pstm = conn.prepareStatement(sql);
				pstm.setString(1, newPhoneNum);
				pstm.setString(2, modPhoneNum);
				pstm.executeUpdate();
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

	public void updateStamp(String PhoneNum, int stamp) {
		PreparedStatement pstm = null;
		try {
			if (getConnection() != null) {
				String sql = "update C_Member set stamp=? where p_num=?";
				pstm = conn.prepareStatement(sql);
				pstm.setInt(1, stamp);
				pstm.setString(2, PhoneNum);
				pstm.executeUpdate();
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

	public ArrayList<UserDTO> search(String searchPhoneNum) {
		ArrayList<UserDTO> userlist = new ArrayList<>();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			if (getConnection() != null) {
				String sql = "select * from C_Member where p_num like ? ";
				pstm = conn.prepareStatement(sql);
				pstm.setString(1, "%" + searchPhoneNum + "%");
				rs = pstm.executeQuery();
				while (rs.next()) {
					UserDTO udto = new UserDTO();
					udto.setPhoneNum(rs.getString("p_num"));
					udto.setStamp(rs.getInt("stamp"));
					userlist.add(udto);
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
		return userlist;
	}

}
