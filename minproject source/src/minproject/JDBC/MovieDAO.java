package minproject.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	// 싱글톤 패턴
	private static MovieDAO dao = new MovieDAO();

	public MovieDAO() {

	}

	public static MovieDAO getInstance() {
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
	
	// 웹에서 크롤링해온 영화 리스트를 테이블에 등록
	public void saveMethod(ArrayList<String[]> arr) {		
		try {
			conn = init();
			/*
			 * 0 : 영화명
			 * 1 : 장르
			 * 2 : 러닝타임
			 * 3 : 개봉일
			 * 4 : 출연
			 * 5 : 감독
			 * 6 : 연령
			 * 7 : 예매율
			 * 8 : 줄거리
			 */
			
			String[] movie_table = {
					"2018-05-01/2018-05-15/A",
					"2018-05-16/2018-05-30/A",
					"2018-05-01/2018-05-10/B",
					"2018-05-11/2018-05-20/B",
					"2018-05-21/2018-05-30/B",
					"2018-05-01/2018-05-10/C",
					"2018-05-11/2018-05-20/C",
					"2018-05-21/2018-05-30/C"};

			int index = 0;
			for (String[] dto : arr) {
				String sql = "INSERT INTO movie(mov_code,mov_title,mov_date,mov_genre,mov_time,mov_age,mov_rating,mov_scenario,mov_img,mov_actor,mov_director) VALUES(mov_code_seq.nextval,?,?,?,?,?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, dto[0]);
				pstmt.setString(2, movie_table[index]);
				pstmt.setString(3, dto[1]);
				pstmt.setInt(4, 2);	
				pstmt.setString(5, dto[6]);
				pstmt.setString(6, dto[7]);
				pstmt.setString(7, dto[8]);
				pstmt.setString(8, String.format("src/minproject/img/movie%d.jpg", index++));
				pstmt.setString(9, dto[4]);
				pstmt.setString(10, dto[5]);
				pstmt.executeUpdate();
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
	}

	// 테이블에서 모든 영화 리스트 받아옴
	public List<MovieDTO> loadMethod() {
		List<MovieDTO> movList = new ArrayList<MovieDTO>();

		try {
			conn = init();
			stmt = conn.createStatement();

			String sql = "SELECT * FROM movie";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {				
				MovieDTO dto = new MovieDTO();
				dto.setMov_code(rs.getInt("mov_code"));
				dto.setMov_title(rs.getString("mov_title"));
				dto.setMov_date(rs.getString("mov_date"));
				dto.setMov_genre(rs.getString("mov_genre"));
				dto.setMov_time(rs.getInt("mov_time"));
				dto.setMov_age(rs.getString("mov_age"));
				dto.setMov_rating(rs.getString("mov_rating"));
				dto.setMov_scenario(rs.getString("mov_scenario"));
				dto.setMov_img(rs.getString("mov_img"));
				dto.setMov_actor(rs.getString("mov_actor"));
				dto.setMov_director(rs.getString("mov_director"));
				movList.add(dto);
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
		return movList;
	}
	
	// 테이블에서 특정 장르의 영화 리스트 받아옴
	public List<MovieDTO> genreMethod(String mov_genre) {
		List<MovieDTO> movList = new ArrayList<MovieDTO>();

		try {
			conn = init();
			stmt = conn.createStatement();

			String sql = "SELECT * FROM movie WHERE mov_genre LIKE '%' || ? || '%'";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mov_genre);
			rs = pstmt.executeQuery();			
			while (rs.next()) {				
				MovieDTO dto = new MovieDTO();
				dto.setMov_code(rs.getInt("mov_code"));
				dto.setMov_title(rs.getString("mov_title"));
				dto.setMov_date(rs.getString("mov_date"));
				dto.setMov_genre(rs.getString("mov_genre"));
				dto.setMov_time(rs.getInt("mov_time"));
				dto.setMov_age(rs.getString("mov_age"));
				dto.setMov_rating(rs.getString("mov_rating"));
				dto.setMov_scenario(rs.getString("mov_scenario"));
				dto.setMov_img(rs.getString("mov_img"));
				dto.setMov_actor(rs.getString("mov_actor"));
				dto.setMov_director(rs.getString("mov_director"));
				movList.add(dto);
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
		return movList;
	}
	
	// 영화탭에서 선택한 영화정보 또는 테이블에서 예매한 영화 정보 받아옴
	public MovieDTO movieMethod(int mov_code) {
		MovieDTO dto = new MovieDTO();

		try {
			conn = init();

			String sql = "SELECT * FROM movie WHERE mov_code = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, mov_code);
			rs = pstmt.executeQuery();			
			
			if(rs.next()) {
				dto.setMov_code(rs.getInt("mov_code"));
				dto.setMov_title(rs.getString("mov_title"));
				dto.setMov_date(rs.getString("mov_date"));
				dto.setMov_genre(rs.getString("mov_genre"));
				dto.setMov_time(rs.getInt("mov_time"));
				dto.setMov_age(rs.getString("mov_age"));
				dto.setMov_rating(rs.getString("mov_rating"));
				dto.setMov_scenario(rs.getString("mov_scenario"));
				dto.setMov_img(rs.getString("mov_img"));
				dto.setMov_actor(rs.getString("mov_actor"));
				dto.setMov_director(rs.getString("mov_director"));
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
	
	// 예매 화면에 띄울 영화 포스터 받아옴
	public String imageMethod(String mov_title) {
		String imagePath = "";
		try {
			conn = init();

			String sql = "SELECT mov_img FROM movie WHERE mov_title = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mov_title);
			rs = pstmt.executeQuery();			
			
			if(rs.next())
				imagePath = rs.getString("mov_img");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				exit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return imagePath;
	}
	
	// 영화제목으로 영화코드 받아옴
	public int movieCodeMethod(String mov_title) {
		int mov_code = 0;
		try {
			conn = init();

			String sql = "SELECT mov_code FROM movie WHERE mov_title = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mov_title);
			rs = pstmt.executeQuery();			
			
			if(rs.next())
				mov_code = rs.getInt("mov_code");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				exit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return mov_code;
	}	
	
	// 영화 제목으로 상영기간 받아옴
	public String movieTableMethod(String mov_title) {
		String mov_date = "";
		try {
			conn = init();

			String sql = "SELECT mov_date FROM movie WHERE mov_title = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mov_title);
			rs = pstmt.executeQuery();
			
			if(rs.next())
				mov_date = rs.getString("mov_date");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				exit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return mov_date;
	}	
}
