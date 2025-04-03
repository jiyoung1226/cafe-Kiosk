package manager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class OrderDAO {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:orcl1";
	private String id = "system";
	private String pass = "1111";
	private Connection conn = null;
	private static OrderDAO orderdao = null;
	private SalesDAO salesdao = new SalesDAO();
	

	public OrderDAO() {
		try {
			Class.forName(driver);
		} catch (Exception e) {

		}

	}

	public static OrderDAO getInstance() { // 싱글톤
		if (orderdao == null) {
			orderdao = new OrderDAO();
		}
		return orderdao;
	}

	private Connection getConnection() {
		try {
			conn = DriverManager.getConnection(url, id, pass);
		} catch (Exception e) {

		}
		return conn;
	}

	public void insert(String name, int price, int amount, int orderNumber) {
		PreparedStatement pstm = null;
		try {
			if (getConnection() != null) {
				String sql = "insert into c_order values(?,?,?,?,?,sysdate)";
				pstm = conn.prepareStatement(sql);
				pstm.setString(1, name);
				pstm.setInt(2, price);
				pstm.setInt(3, amount);
				pstm.setInt(4, price * amount);
				pstm.setInt(5, orderNumber);
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

	public ArrayList<OrderDTO> select() {
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<OrderDTO> orderList = new ArrayList<>();
		try {
			if (getConnection() != null) {
				String sql = "select * from c_order";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					OrderDTO orderdto = new OrderDTO();
					orderdto.setName(rs.getString("m_name"));
					orderdto.setPrice(rs.getInt("price"));
					orderdto.setAmount(rs.getInt("amount"));
					orderdto.setTotalprice(rs.getInt("t_price"));
					orderdto.setOrderNum(rs.getInt("order_num"));
					orderdto.setIndate(rs.getTimestamp("indate"));
					orderList.add(orderdto);
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
		return orderList;
	}

	public void delete(String deletenum) {
		PreparedStatement pstm = null;
		try {
			if (getConnection() != null) {
				String sql = "delete from c_order where order_num=?";
				pstm = conn.prepareStatement(sql);
				pstm.setString(1, deletenum);
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

	public ArrayList<OrderDTO> search(String searchnum) {
		ArrayList<OrderDTO> orderList = new ArrayList<>();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			if (getConnection() != null) {
				String sql = "select * from c_order where order_num like ?";
				pstm = conn.prepareStatement(sql);
				pstm.setString(1, "%" + searchnum + "%");
				rs = pstm.executeQuery();
				while (rs.next()) {
					OrderDTO orderdto = new OrderDTO();
					orderdto.setName(rs.getString("m_name"));
					orderdto.setPrice(rs.getInt("price"));
					orderdto.setAmount(rs.getInt("amount"));
					orderdto.setTotalprice(rs.getInt("t_price"));
					orderdto.setOrderNum(rs.getInt("order_num"));
					orderList.add(orderdto);
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
		return orderList;
	}

}
