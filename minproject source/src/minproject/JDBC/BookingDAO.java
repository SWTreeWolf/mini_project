package minproject.JDBC;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class BookingDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	// 싱글톤 패턴
	private static BookingDAO dao = new BookingDAO();

	public BookingDAO() {

	}

	public static BookingDAO getInstance() {
		return dao;
	}

	private Connection init() throws ClassNotFoundException, SQLException {
		// 1. 드라이버 로딩
		Class.forName("oracle.jdbc.OracleDriver");

		// 2. 서버 연결
		String url = "jdbc:oracle:thin://@127.0.0.1:1521:xe";
		String username = "hr";
		String password = "a1234";
		return DriverManager.getConnection(url, username, password);
	}

	private void exit() throws SQLException {
		if (rs != null)
			rs.close();
		if (stmt != null)
			stmt.close();
		if (pstmt != null)
			pstmt.close();
		if (conn != null)
			conn.close();
	}

	// 예약 완료되면 테이블에 등록
	public void bookingMethod(BookingDTO dto) {
		try {
			conn = init();
			String sql = "INSERT INTO booking(book_check,book_code,book_room,book_date,book_time,book_seat,book_count,book_price,mov_code) VALUES(1,book_code_seq.nextval,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getBook_room().substring(0,1));
			pstmt.setDate(2, dto.getBook_date());
			pstmt.setString(3, dto.getBook_time());
			pstmt.setString(4, dto.getBook_seat());
			pstmt.setInt(5, dto.getBook_count());
			pstmt.setInt(6, dto.getBook_price());
			pstmt.setInt(7, dto.getMov_code());
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

	// 마이페이지에서 예약 목록 확인
	// 일단 1명이 예약할 수 있는 영화는 1개로 제한
	public BookingDTO myBookMethod(int book_code) {
		BookingDTO dto = new BookingDTO();

		try {
			conn = init();

			String sql = "SELECT * FROM booking WHERE book_code = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, book_code);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto.setBook_code(rs.getInt("book_code"));
				dto.setBook_room(rs.getString("book_room"));
				dto.setBook_date(rs.getDate("book_date"));
				dto.setBook_time(rs.getString("book_time"));
				dto.setBook_seat(rs.getString("book_seat"));
				dto.setBook_count(rs.getInt("book_count"));
				dto.setBook_price(rs.getInt("book_price"));
				dto.setMov_code(rs.getInt("mov_code"));
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				exit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dto;
	}
	
	// 좌석정보와 일치하는 예매 번호 받아옴
	public int bookCodeMethod(Object[] book_data) {
		int book_code = 0;

		try {
			conn = init();

			String sql = "SELECT book_code FROM booking WHERE book_room = ? AND book_date = ? AND book_time = ? AND book_seat = ? AND mov_code = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, String.valueOf(book_data[0]).substring(0,1));
			String[] stringDate = String.valueOf(book_data[1]).split("-");
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(stringDate[0]),Integer.parseInt(stringDate[1])-1,Integer.parseInt(stringDate[2]));
			Date date = new Date(cal.getTimeInMillis());
			pstmt.setDate(2, date);
			pstmt.setString(3, String.valueOf(book_data[2]));
			pstmt.setString(4, String.valueOf(book_data[3]));
			pstmt.setInt(5, (int) book_data[4]);
			rs = pstmt.executeQuery();
			
			if(rs.next())
				book_code = rs.getInt("book_code");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				exit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return book_code;
	}

	// 예매 취소
	public void cancelMethod(int book_code) {
		try {
			conn = init();
			
			String sql = "UPDATE booking SET book_check = 0 WHERE book_code = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, book_code);
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
	
	public int priceMethod(int book_code) {
		int price = 0;
		
		try {
			conn = init();

			String sql = "SELECT book_price FROM booking WHERE book_code = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, book_code);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				price = rs.getInt("book_price");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				exit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return price;
	}
}
