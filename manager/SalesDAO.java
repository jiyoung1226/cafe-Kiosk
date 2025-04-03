package manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.time.LocalDate;

public class SalesDAO {

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:orcl1";
	private String id = "system";
	private String pass = "1111";
	private Connection conn = null;
	private static SalesDAO salesdao = null;
	// private SalesDTO sdto = new SalesDTO();

	public SalesDAO() {
		try {
			Class.forName(driver);
		} catch (Exception e) {

		}

	}

	public static SalesDAO getInstance() { // 싱글톤
		if (salesdao == null) {
			salesdao = new SalesDAO();
		}
		return salesdao;
	}

	private Connection getConnection() {
		try {
			conn = DriverManager.getConnection(url, id, pass);
		} catch (Exception e) {

		}
		return conn;
	}

	public void insert(SalesDTO sdto) {
		PreparedStatement pstm = null;
		try {
			if (getConnection() != null) {
				String sql = "insert into Sales values(?,?)";
				pstm = conn.prepareStatement(sql);
				pstm.setInt(1, sdto.getT_Price());
				pstm.setDate(2, sdto.getIndate());
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

	public ArrayList<SalesDTO> select() {
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<SalesDTO> sList = new ArrayList<>();
		try {
			if (getConnection() != null) {
				String sql = "SELECT indate AS indate, SUM(t_price) AS t_price "
						+ "FROM Sales GROUP BY indate ORDER BY indate DESC";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					SalesDTO s = new SalesDTO();
					s.setT_Price(rs.getInt("t_price")); // 총 매출
					s.setIndate(rs.getDate("indate")); // 날짜 그대로 저장
					sList.add(s);
				}
			}

		} catch (Exception e) {
			System.out.println("매출 조회 중 오류 발생");

		}
		return sList;
	}

}
