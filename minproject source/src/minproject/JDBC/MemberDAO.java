package minproject.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import minproject.Main;

public class MemberDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	// 싱글톤 패턴
	private static MemberDAO dao = new MemberDAO();

	public MemberDAO() {

	}

	public static MemberDAO getInstance() {
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
	
	// 로그인 정보 확인, 회원정보 수정 시 비밀번호 확인
	public boolean loginMethod(String[] str) {
		boolean isCorrected = false;
		try {
			conn = init();

			String sql = "SELECT mem_code FROM member WHERE mem_id = ? AND mem_pw = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, str[0]);
			pstmt.setString(2, str[1]);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				isCorrected = true;	
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
		return isCorrected;
	}
	
	// 탈퇴된 회원의 계정인지 확인
	public boolean isExitedMethod(String[] str) {
		boolean isExited = false;
		try {
			conn = init();

			String sql = "SELECT mem_check FROM member WHERE mem_id = ? AND mem_pw = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, str[0]);
			pstmt.setString(2, str[1]);
			rs = pstmt.executeQuery();
			
			if(rs.next())
				if(rs.getInt("mem_check") == 0)
					isExited = true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				exit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isExited;
	}
	
	// 이미 가입된 회원인지 확인
	public boolean isJoinedMethod(String id) {
		boolean isCorrected = false;
		try {
			conn = init();

			String sql = "SELECT mem_id FROM member WHERE mem_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next())
				isCorrected = true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				exit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isCorrected;
	}

	// 회원코드 받아옴
	public int codeMethod(String[] str) {
		int mem_code = 0;
		try {
			conn = init();

			String sql = "SELECT mem_code, mem_name FROM member WHERE mem_id = ? AND mem_pw = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, str[0]);
			pstmt.setString(2, str[1]);
			rs = pstmt.executeQuery();
			
			if(rs.next())
				mem_code = rs.getInt("mem_code");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				exit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return mem_code;
	}
	
	// 회원가입 완료되면 테이블에 등록
	public void joinMethod(MemberDTO dto) {
		try {
			conn = init();
			
			String sql = "INSERT INTO member(mem_check,mem_code,mem_name,mem_local,mem_phone,mem_id,mem_pw,mem_point) VALUES(1,mem_code_seq.nextval,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getMem_name());
			pstmt.setInt(2, dto.getMem_local());
			pstmt.setString(3, dto.getMem_phone());
			pstmt.setString(4, dto.getMem_id());
			pstmt.setString(5, dto.getMem_pw());
			pstmt.setInt(6, 5000);
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

	// 예약이 완료되면 고객 정보에 예약 코드 추가
	public void bookMethod(int book_code) {
		try {
			conn = init();

			String sql = "INSERT INTO member(book_code) VALUES(?)";
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

	// 테이블에서 마이페이지에 띄울 정보 받아옴
	public MemberDTO myPageMethod(int mem_code) {
		MemberDTO dto = new MemberDTO();
		try {
			conn = init();

			String sql = "SELECT * FROM member WHERE mem_code = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, mem_code);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				dto.setMem_name(rs.getString("mem_name"));
				dto.setMem_local(rs.getInt("mem_local"));
				dto.setMem_phone(rs.getString("mem_phone"));
				dto.setMem_id(rs.getString("mem_id"));
				dto.setBook_code(rs.getInt("book_code"));
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
	
	// 회원정보 수정 시 입력한 비밀번호와 동일한지 확인
	public boolean checkMethod(Object[] obj) {
		boolean isCorrected = false;
		try {
			conn = init();

			String sql = "SELECT mem_pw FROM member WHERE mem_code = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (int) obj[0]);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String password = rs.getString("mem_pw");			
				isCorrected = obj[1].equals(password) ? true : false ;
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
		return isCorrected;
	}
	
	// 회원정보 수정
	public void changeMethod(String[] str) {
		try {
			conn = init();
			
			String sql = "UPDATE member SET mem_phone=?, mem_pw=? WHERE mem_code=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, str[0].replace("-", ""));
			pstmt.setString(2, str[1]);
			pstmt.setInt(3, Main.myCode);
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
	
	// 회원정보에서 예약코드 받아옴
	public int bookCodeMethod(int mem_code) {
		int book_code=0;
		
		try {
			conn = init();

			String sql = "SELECT book_code FROM member WHERE mem_code = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, mem_code);
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
	
	// 회원탈퇴
	public void quitMethod(int mem_code) {
		try {
			conn = init();
			
			String sql = "DELETE FROM member WHERE mem_code = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, mem_code);
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
	
	// 포인트 조회
	public int pointMethod(int mem_code) {
		int mem_point=0;
		
		try {
			conn = init();

			String sql = "SELECT mem_point FROM member WHERE mem_code = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, mem_code);
			rs = pstmt.executeQuery();
			
			if(rs.next())
				mem_point = rs.getInt("mem_point");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				exit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return mem_point;
	}
	
	// 예매 시 포인트 차감 및 예매코드 등록
	public void reservMethod(int[] obj) {
		try {
			conn = init();
			
			String sql = "UPDATE member SET mem_point=?, book_code=? WHERE mem_code=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, obj[0]);
			pstmt.setInt(2, obj[1]);
			pstmt.setInt(3, obj[2]);
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
	
	// 포인트 충전
	public void chargeMethod(int[] data) {
		try {
			conn = init();
			
			String sql = "UPDATE member SET mem_point=? WHERE mem_code=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, data[0]);
			pstmt.setInt(2, data[1]);
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
	
	// 회원 탈퇴
	// 1이면 가입상태, 0이면 탈퇴상태
	public void exitMethod(int mem_code) {
		try {
			conn = init();
			
			String sql = "UPDATE member SET mem_check=0 WHERE mem_code=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, mem_code);
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
	
	// 예매 취소하기
	public void cancelMethod(int mem_code) {
		try {
			conn = init();
			
			String sql = "UPDATE member SET book_code = null WHERE mem_code=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, mem_code);
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
	
	public boolean isBookedMethod(int mem_code) {
		boolean isBookedMethod = true;
		
		try {
			conn = init();

			String sql = "SELECT book_code FROM member WHERE mem_code = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, mem_code);
			rs = pstmt.executeQuery();
			
			if(rs.next())
				if(String.valueOf(rs.getInt("book_code")).equals(null))
					isBookedMethod = false;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				exit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isBookedMethod;
	}	
}
