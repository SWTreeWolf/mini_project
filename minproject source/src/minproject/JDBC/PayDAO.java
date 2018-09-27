package minproject.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PayDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	// 싱글톤 패턴
	private static PayDAO dao = new PayDAO();
	public PayDAO() {
		
	}
	
	public static PayDAO getInstance() {
		return dao;
	}
	
	private Connection init() throws ClassNotFoundException, SQLException {
		// 1. 드라이버 로딩
		Class.forName("oracle.jdbc.OracleDriver");
		
		// 2. 서버 연결
		String url="jdbc:oracle:thin://@127.0.0.1:1521:xe";
		String username="hr";
		String password="a1234";
		return DriverManager.getConnection(url ,username ,password);
	}
	
	private void exit() throws SQLException {
		if(rs!=null)
			rs.close();
		if(stmt!=null)
			stmt.close();
		if(pstmt!=null)
			pstmt.close();
		if(conn!=null)
			conn.close();
	}
	
	// 결제 완료되면 테이블에 등록
	public void payMethod(PayDTO dto) {
		try {
			conn=init();

			String sql = "INSERT INTO pay(pay_check,pay_code,pay_credit,pay_point,mem_code) VALUES(1,pay_code_seq.nextval,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getPay_credit());
			pstmt.setInt(2, dto.getPay_point());
			pstmt.setInt(3, dto.getMem_code());
			pstmt.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				exit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
