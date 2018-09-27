package minproject;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import minproject.JDBC.MovieDAO;
import minproject.JDBC.MovieDTO;
import minproject.JDBC.SeatDAO;
import minproject.JDBC.SeatDTO;

public class Ticketing extends JPanel implements ActionListener {
	MoviePanel moviePanel = new MoviePanel();
	DatePanel datePanel = new DatePanel();
	TimePanel timePanel = new TimePanel();
	SeatPanel seatPanel = new SeatPanel(this);
	JLabel[] label = new JLabel[3];

	final int MAX_MOVIE_CNT = 8; // 영화 최대 갯수
	final int MAX_CINEMA_CNT = 3; // 상영관 최대 갯수
	final int MAX_TIME_CNT = 5; // 상영시간 최대한 갯수
	
	final int MAX_SEAT_ROW = 10;
	final int MAX_SEAT_COLUMN = 18;
	final int MAX_DATE = 30;
	final int CURRENT_YEAR = 2018;
	final int CURRENT_MONTH = 5;
	String[] movie_table = {
			"2018-05-01/2018-05-15/A",
			"2018-05-16/2018-05-30/A",
			"2018-05-01/2018-05-10/B",
			"2018-05-11/2018-05-20/B",
			"2018-05-21/2018-05-30/B",
			"2018-05-01/2018-05-10/C",
			"2018-05-11/2018-05-20/C",
			"2018-05-21/2018-05-30/C"};
	
	JButton selectedMovieBtn, selectedDateBtn;

	JPanel panel = null;
	String str = null;
	
	String[][] seat_num;
	String[] seat_date;
	char[] row = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J' };
	
	// 선택된 영화 및 시간 정보
	String[] data = new String[4];
	
	public static final Color color = new Color(255,255,230);

	public Ticketing() {
		panel = new JPanel();
		
		clearSeat();
		tableSeat();
		
		setactionlistener();
		setBackground(new Color(205, 193, 151));
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.BOTH;

		gbc.weightx = 0.3;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(moviePanel, gbc);
		gbc.weightx = 0.2;
		add(datePanel, gbc);
		gbc.weightx = 0.4;
		gbc.weighty = 0.1;
		add(timePanel, gbc);
		////////////////////////////////
		//gbc.weightx = 0.1;
		for (int i = 0; i < 3; i++) {
			label[i] = new JLabel("null");
			//panel.add(label[i]);
		}
		//add(panel, gbc);
		////////////////////////////////
	}// end Ticketing()

	private void setactionlistener() {
		for (int i = 0; i < MAX_MOVIE_CNT; i++) {
			moviePanel.movie_B[i].addActionListener(this);
		}
		
		/*for(int i=0;i<datePanel.date_2.length;i++) {
			datePanel.date_2[i].addActionListener(this);
		}*/
		
		for (int i = 0; i < MAX_TIME_CNT; i++) {
			timePanel.temp_A[i].addActionListener(this);
			timePanel.temp_B[i].addActionListener(this);
			timePanel.temp_C[i].addActionListener(this);
		}

		seatPanel.seat_Btn.addActionListener(this);
	}
	
	// 좌석 생성
	public void clearSeat() {
		SeatDAO dao = SeatDAO.getInstance();
		if (!dao.isExistMethod()) {
			seat_num = new String[MAX_SEAT_ROW][MAX_SEAT_COLUMN];
			seat_date = new String[MAX_DATE];
			
			for(int i=0; i<MAX_DATE; i++) {
				if(i<9)
					seat_date[i] = String.format("%d-0%d-0%d", CURRENT_YEAR, CURRENT_MONTH, i+1);
				else
					seat_date[i] = String.format("%d-0%d-%d", CURRENT_YEAR, CURRENT_MONTH, i+1);
			}

			for (int i = 0; i < 10; i++)
				for (int j = 0; j < 18; j++)
					seat_num[i][j] = String.format("(%c,%d)", row[i], j + 1);
			
			List<SeatDTO> seatList = new ArrayList<SeatDTO>();
			
			for(int i=0; i<MAX_DATE; i++) {
				for(int j=0; j<timePanel.time.length; j++) {
					for(int k=0; k<timePanel.Cinema_name.length; k++) {
						for(int l=0; l<MAX_SEAT_ROW; l++) {
							for(int m=0; m<MAX_SEAT_COLUMN; m++) {
								SeatDTO dto = new SeatDTO();
								String[] stringDate = seat_date[i].split("-");
								Calendar cal = Calendar.getInstance();
								cal.set(Integer.parseInt(stringDate[0]), Integer.parseInt(stringDate[1]) - 1,
										Integer.parseInt(stringDate[2]));
								Date date = new Date(cal.getTimeInMillis());
								dto.setSeat_date(date);
								dto.setSeat_time(timePanel.time[j]);
								dto.setSeat_room(timePanel.Cinema_name[k]);
								dto.setSeat_seat(seat_num[l][m]);
								dto.setSeat_check(0);
								seatList.add(dto);
							}
						}
					}
				}
			}
			dao.seatMethod(seatList);
		}
	}
	
	// 영화 편성 후 좌석에 적용
	private void tableSeat() {
		SeatDAO seatDao = SeatDAO.getInstance();
		if (!seatDao.isOrganizedMethod()) {
			seat_num = new String[MAX_SEAT_ROW][MAX_SEAT_COLUMN];
			seat_date = new String[MAX_DATE];
			
			MovieDAO movDao = MovieDAO.getInstance();
			List<MovieDTO> movList = movDao.loadMethod();
			int[] mov_codes = new int[MAX_MOVIE_CNT];
			
			for(int i=0; i<MAX_MOVIE_CNT; i++)
				mov_codes[i] = movList.get(i).getMov_code();
			
			List<SeatDTO> seatList = new ArrayList<SeatDTO>();
			
			for(int i=0; i<movie_table.length; i++) {
				String[] temp = movie_table[i].split("/");
				String start = temp[0];
				String end = temp[1];
				String room = temp[2];
				
				int startDate = Integer.parseInt(start.split("-")[2]);
				int endDate = Integer.parseInt(end.split("-")[2]);
				
				String[] startFormat = start.split("-");
				
				int differ = endDate - startDate+1;
				
				Calendar cal = Calendar.getInstance();
				cal.set(Integer.parseInt(startFormat[0]), Integer.parseInt(startFormat[1]) - 1,
						Integer.parseInt(startFormat[2]));
				
				for(int j=0; j<differ; j++) {
					for(int k=0; k<timePanel.time.length; k++) {
						SeatDTO seatDto = new SeatDTO();
						Date date = new Date(cal.getTimeInMillis());
						seatDto.setSeat_date(date);
						seatDto.setSeat_time(timePanel.time[k]);
						seatDto.setSeat_room(room);
						seatList.add(seatDto);
					}
					cal.add(Calendar.DATE, 1);
				}
			}
		}		
	}
	
	// 특정 영화 클릭 시 해당 영화의 편성표에 있는 날짜만 띄워줌
	private void showDateList(String title) {
		datePanel.jp2.removeAll();
		
		MovieDAO dao = MovieDAO.getInstance();
		String[] tableArr = dao.movieTableMethod(title).split("/");
		String start = tableArr[0];
		String end = tableArr[1];
		
		int startDate = Integer.parseInt(start.split("-")[2]);
		int endDate = Integer.parseInt(end.split("-")[2]);
		
		String[] startFormat = start.split("-");
		
		int differ = endDate - startDate + 1;
		
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(startFormat[0]), Integer.parseInt(startFormat[1]) - 1,
				Integer.parseInt(startFormat[2]));
		
		datePanel.date_2 = new JButton[differ];
		for(int i=0; i<differ; i++) {
			datePanel.date_2[i] = new JButton(String.format("%d일(%s)",(startDate+i),datePanel.day[cal.get(Calendar.DAY_OF_WEEK)-1]));
			datePanel.date_2[i].setName("date");
			if(i % 7 == 5) {
				datePanel.date_2[i].setForeground(Color.red);
			}else if(i % 7 == 4) {
				datePanel.date_2[i].setForeground(Color.BLUE);
			}
			datePanel.date_2[i].setBackground(null);
			datePanel.date_2[i].setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
			cal.add(Calendar.DATE, 1);
			datePanel.date_2[i].setFocusPainted(false);
			datePanel.jp2.add(datePanel.date_2[i]);
			datePanel.date_2[i].addActionListener(this);
			datePanel.date_2[i].addActionListener(datePanel);
		}
		datePanel.repaint();
	}

	// 데이터를 주기 위함
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		JButton btn = (JButton) obj;
		
		if(btn.getName().equals("movie")) {
			if(btn.getBackground().equals(Color.GRAY)) {
				str = btn.getText();
				label[0].setText("null");
				btn.setBackground(color);
			} else {
				label[0].setText(btn.getText());
				showDateList(label[0].getText());
			}
		} else if(btn.getName().equals("date")) {
			if(!btn.getBackground().equals(Color.GRAY)) {
				str += "5월" + btn.getText();
				label[1].setText("null");
			} else {
				label[1].setText("5월" + btn.getText());
			}
		} else {
			if(btn.getBackground().equals(Color.GRAY)) {
				str += btn.getName() + btn.getText();
				label[2].setText("null");
				btn.setBackground(color);
			} else {
				label[2].setText(btn.getName()+ "/" + btn.getText());
			}
		}
	}// end actionPerformed()

}// end class
