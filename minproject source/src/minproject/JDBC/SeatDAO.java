package minproject.JDBC;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.List;

public class SeatDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	// 싱글톤 패턴
	private static SeatDAO dao = new SeatDAO();

	public SeatDAO() {

	}

	public static SeatDAO getInstance() {
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

	// 시간표 별로 좌석 생성
	public void seatMethod(List<SeatDTO> seatList) {
		try {
			conn = init();
			
			String sql = "INSERT INTO seat(seat_code,seat_date,seat_time,seat_room,seat_seat) VALUES(seat_code_seq.nextval,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			
			int count = 0;
			for(SeatDTO dto : seatList) {
				pstmt.setDate(1, dto.getSeat_date());
				pstmt.setString(2, dto.getSeat_time());
				pstmt.setString(3, dto.getSeat_room());
				pstmt.setString(4, dto.getSeat_seat());
				pstmt.addBatch();
				pstmt.clearParameters();
				if((count % 10000) == 0){
	                // Batch 실행
	                pstmt.executeBatch();
	                // Batch 초기화
	                pstmt.clearBatch();
	                // 커밋
	                conn.commit();    
	            }
			}
			pstmt.executeBatch();
			conn.commit();
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

	// 좌석 예매 시 상태 변경
	public void reservMethod(Object[] obj) {
		try {
			conn = init();

			String sql = "UPDATE seat SET mem_code = ? WHERE seat_date = ? AND seat_time = ? AND seat_room = ? AND seat_seat = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (int) obj[0]);
			String[] stringDate = String.valueOf(obj[1]).split("-");
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(stringDate[0]),Integer.parseInt(stringDate[1])-1,Integer.parseInt(stringDate[2]));
			Date date = new Date(cal.getTimeInMillis());
			pstmt.setDate(2, date);
			pstmt.setString(3, String.valueOf(obj[2]));
			pstmt.setString(4, String.valueOf(obj[3]).substring(0,1));
			pstmt.setString(5, String.valueOf(obj[4]));
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
	
	// 좌석 상태 받아옴
	public boolean[] checkMethod(List<Object[]> objList) {
		boolean[] isReserved = new boolean[objList.size()];

		try {
			conn = init();

			int index = 0;
			
			for(Object[] obj : objList) {
				String sql = "SELECT mem_code FROM seat WHERE seat_date = ? AND seat_time = ? AND seat_room = ? AND seat_seat = ?";
				pstmt = conn.prepareStatement(sql);
				String[] stringDate = String.valueOf(obj[0]).split("-");
				Calendar cal = Calendar.getInstance();
				cal.set(Integer.parseInt(stringDate[0]),Integer.parseInt(stringDate[1])-1,Integer.parseInt(stringDate[2]));
				Date date = new Date(cal.getTimeInMillis());
				pstmt.setDate(1, date);
				pstmt.setString(2, String.valueOf(obj[1]));
				pstmt.setString(3, String.valueOf(obj[2]).substring(0,1));
				pstmt.setString(4, String.valueOf(obj[3]));
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					if(rs.getInt("mem_code")==0) {						
						isReserved[index] = false;
					}
					else {
						isReserved[index] = true;
					}
				}
				
				index++;
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
		return isReserved;
	}
	
	// 잔여 좌석 수 받아옴
	public int countMethod(Object[] obj) {
		int seat_count = 0;

		try {
			conn = init();

			String sql = "SELECT * FROM seat WHERE seat_date = ? AND seat_time = ? AND seat_room = ? AND mem_code IS NULL";
			pstmt = conn.prepareStatement(sql);
			String[] stringDate = String.valueOf(obj[0]).split("-");
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(stringDate[0]),Integer.parseInt(stringDate[1])-1,Integer.parseInt(stringDate[2]));
			Date date = new Date(cal.getTimeInMillis());
			pstmt.setDate(1, date);
			pstmt.setString(2, String.valueOf(obj[1]));
			pstmt.setString(3, String.valueOf(obj[2]).substring(0,1));
			rs = pstmt.executeQuery();
			
			while(rs.next())
				seat_count++;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				exit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return seat_count;
	}
	
	// seat 테이블에 좌석이 등록되어있는지 확인
	public boolean isExistMethod() {
		boolean isExist = false;
		
		try {
			conn = init();

			String sql = "SELECT * FROM seat";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next())
				isExist = true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				exit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isExist;
	}
	
	// 영화 편성 후 좌석 배치가 끝났는지 확인
	public boolean isOrganizedMethod() {
		boolean isOrganized = false;
		
		try {
			conn = init();

			String sql = "SELECT * FROM seat WHERE mem_code IS NOT NULL";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next())
				isOrganized = true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				exit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isOrganized;
	}
	
	// 좌석 편성
	public void organizeMethod(List<SeatDTO> seatList) {
		try {
			conn = init();

			String sql = "UPDATE seat SET mem_code = ? WHERE seat_date = ? AND seat_time = ? AND seat_room = ?";
			pstmt = conn.prepareStatement(sql);
			
			for(SeatDTO dto : seatList) {
				pstmt.setInt(1, dto.getMem_code());
				pstmt.setDate(2, dto.getSeat_date());
				pstmt.setString(3, dto.getSeat_time());
				pstmt.setString(4, dto.getSeat_room());
				pstmt.addBatch();
			}
			pstmt.executeBatch();
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
	
	// 좌석 예매 취소
	public void cancelMethod(int mem_code) {
		try {
			conn = init();
			
			String sql = "DELETE FROM seat WHERE mem_code = ?";
			//String sql = "UPDATE seat SET mem_code = NULL WHERE seat_date = ? AND seat_time = ? AND seat_room = ? AND seat_seat = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, mem_code);
			/*String[] stringDate = String.valueOf(obj[0]).split("-");
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(stringDate[0]),Integer.parseInt(stringDate[1])-1,Integer.parseInt(stringDate[2]));
			Date date = new Date(cal.getTimeInMillis());
			pstmt.setDate(1, date);
			pstmt.setString(2, String.valueOf(obj[1]));
			pstmt.setString(3, String.valueOf(obj[1]));
			pstmt.setString(4, String.valueOf(obj[1]));*/
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
