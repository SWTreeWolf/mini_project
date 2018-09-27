package minproject;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import minproject.JDBC.BookingDAO;
import minproject.JDBC.BookingDTO;
import minproject.JDBC.MemberDAO;
import minproject.JDBC.MovieDAO;
import minproject.JDBC.SeatDAO;

public class Seat extends JFrame implements ActionListener {
	JPanel rowpanel_w, rowpanel_e, panel_s;
	Ticketing ticket;
	JLabel[] rowLabels;
	JButton[][] leftSeats;
	JButton[][] centerSeats;
	JButton[][] rightSeats;
	ReservPanel rp;
	
	List<JButton> seatList = new ArrayList<JButton>();
	char[] row = {'A','B','C','D','E','F','G','H','I','J'};
	
	public Seat(Ticketing tic) {
		ticket=tic;
		JPanel jp1 = new JPanel();
		jp1.add(new JLabel("스크린"));

		int i, j;
		
		data_s();
		seatWest();
		
		JPanel jp3 = new JPanel(new GridLayout(10, 10));
		centerSeats = new JButton[10][10];
		for (i = 0; i < centerSeats.length; i++) {
			for (j = 0; j < centerSeats[i].length; j++) {
				centerSeats[i][j] = new JButton(String.valueOf(j+5));
				centerSeats[i][j].setName("("+row[i]+","+(j+5)+")");
				centerSeats[i][j].setFocusPainted(false);
				jp3.add(centerSeats[i][j]);
			}
		}
		checkSeat(centerSeats);
		jp3.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		seatEast();

		add(jp1, BorderLayout.NORTH);
		add(rowpanel_w, BorderLayout.WEST);
		add(jp3, BorderLayout.CENTER);
		add(rowpanel_e, BorderLayout.EAST);
		add(panel_s, BorderLayout.SOUTH);

		setSize(1000, 800);
		setResizable(false);
		setLocationRelativeTo(this);
		setVisible(true);
		rp.prev_btn.addActionListener(this);
	}
	
	private void checkSeat(JButton[][] btns) {
		SeatDAO dao = SeatDAO.getInstance();
		
		List<Object[]> objList = new ArrayList<Object[]>();
		
		for(int i=0; i<btns.length; i++) {
			for(int j=0; j<btns[i].length; j++) {
				Object[] obj = new Object[4];
				obj[0] = rp.next_data[1].substring(0, 10);
				obj[1] = rp.next_data[2].split("~")[0];
				obj[2] = rp.next_data[3];
				obj[3] = btns[i][j].getName();
				objList.add(obj);
			}
		}
		
		boolean[] isReserved = dao.checkMethod(objList);
		
		int index = 0;
		
		for(int i=0; i<btns.length; i++) {
			for(int j=0; j<btns[i].length; j++) {
				if(isReserved[index])
					btns[i][j].setBackground(Color.GRAY);
				else
					btns[i][j].addActionListener(this);
				index++;
			}
		}
	}

	private void data_s() {
		JPanel jp5 = new JPanel(new GridLayout(1, 1));
		
		rp = new ReservPanel(ticket);
		rp.next_btn.addActionListener(this);
		jp5.add(rp);

		panel_s = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		panel_s.add(jp5,gbc);
	}
	
	private void seatWest() {
		JPanel panel = new JPanel(new GridLayout(10, 1));
		JLabel[] row_name = new JLabel[10];
		for(int i =0; i< row_name.length;i++) {
			row_name[i] = new JLabel(String.valueOf((char)(i+65)));
			panel.add(row_name[i]);
		}
		
		JPanel jp2 = new JPanel(new GridLayout(10, 4));
		leftSeats = new JButton[10][4];
		for (int i = 0; i < leftSeats.length; i++) {
			for (int j = 0; j < leftSeats[i].length; j++) {
				leftSeats[i][j] = new JButton(String.valueOf(j+1));
				leftSeats[i][j].setName("("+row[i]+","+(j+1)+")");
				leftSeats[i][j].setFocusPainted(false);
				jp2.add(leftSeats[i][j]);
			}
		}
		checkSeat(leftSeats);
		jp2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		rowpanel_w = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		gbc.weighty = 1;
		gbc.insets = new Insets(0, 10, 0, 0);
		rowpanel_w.add(panel,gbc);
		
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridx = 1;
		gbc.weightx = 0.7;
		rowpanel_w.add(jp2,gbc);
	}
	
	private void seatEast() {
		JPanel panel = new JPanel(new GridLayout(10, 1));
		JLabel[] row_name = new JLabel[10];
		for(int i =0; i< row_name.length;i++) {
			row_name[i] = new JLabel(String.valueOf((char)(i+65)));
			panel.add(row_name[i]);
		}
		
		JPanel jp4 = new JPanel(new GridLayout(10, 4));
		rightSeats = new JButton[10][4];
		for (int i = 0; i < rightSeats.length; i++) {
			for (int j = 0; j < rightSeats[i].length; j++) {
				rightSeats[i][j] = new JButton(String.valueOf(j+15));
				rightSeats[i][j].setName("("+row[i]+","+(j+15)+")");
				rightSeats[i][j].setFocusPainted(false);
				jp4.add(rightSeats[i][j]);
			}
		}
		checkSeat(rightSeats);
		jp4.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		rowpanel_e = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		gbc.weighty = 1;
		gbc.insets = new Insets(0, 0, 0, 10);
		rowpanel_e.add(panel,gbc);
		
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridx = 0;
		gbc.weightx = 0.7;
		rowpanel_e.add(jp4,gbc);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		JButton selectedBtn = (JButton) obj;
		
		if (obj == rp.prev_btn) {
			int chk_option = JOptionPane.showConfirmDialog(this, "이 창을 벗어나면 정보가 지워집니다.", "확인",
					JOptionPane.OK_CANCEL_OPTION);
			if (chk_option == JOptionPane.OK_OPTION) {
				this.dispose();
			} else {
				return;
			}
		} else if(obj.equals(rp.next_btn)) {
			int mem_point = Integer.parseInt(Main.pointLabel.getText().split("P")[0]);
			int reserv_price = Integer.parseInt(rp.total_label.getText().split(" ")[0]);
			
			if(reserv_price==0) {
				JOptionPane.showMessageDialog(this, "좌석을 선택해주세요.");
				return;
			}
				
			if(mem_point < reserv_price)
				PointPayment.getInstance(this);
			else {
				int chk_option = JOptionPane.showConfirmDialog(this, "예매를 진행하시겠습니까?", "확인",
						JOptionPane.OK_CANCEL_OPTION);
				if(chk_option == JOptionPane.OK_OPTION) {
					mem_point -= reserv_price;
					rp.next_btn.setText(mem_point+"P");
					
					// booking에 예매 정보 등록
					BookingDAO bookDao = BookingDAO.getInstance();
					BookingDTO bookDto = new BookingDTO();
					
					MovieDAO movDao = MovieDAO.getInstance();
					int mov_code = movDao.movieCodeMethod(ticket.label[0].getText());
					
					bookDto.setBook_room(rp.next_data[3]);
					String[] stringDate = rp.next_data[1].substring(0,10).split("-");
					Calendar cal = Calendar.getInstance();
					cal.set(Integer.parseInt(stringDate[0]),Integer.parseInt(stringDate[1])-1,Integer.parseInt(stringDate[2]));
					Date date = new Date(cal.getTimeInMillis());
					bookDto.setBook_date(date);
					bookDto.setBook_time(rp.next_data[2].split("~")[0]);
					bookDto.setBook_seat(rp.next_data[4]);
					bookDto.setBook_count(seatList.size());
					bookDto.setBook_price(reserv_price);
					bookDto.setMov_code(mov_code);
					bookDao.bookingMethod(bookDto);
					
					// member에 예매코드 연결
					Object[] book_data = new Object[5];
					book_data[0] = rp.next_data[3];
					book_data[1] = rp.next_data[1].substring(0, 10);
					book_data[2] = rp.next_data[2].split("~")[0];
					book_data[3] = rp.next_data[4];
					book_data[4] = mov_code;
					
					int book_code = bookDao.bookCodeMethod(book_data);
					
					int[] mem_data = new int[3];
					mem_data[0] = mem_point;
					mem_data[1] = book_code;
					mem_data[2] = Main.myCode;
					
					MemberDAO memDao = MemberDAO.getInstance();
					memDao.reservMethod(mem_data);
					Main.pointLabel.setText(mem_point+"P");
					
					// 좌석 예매상태 변경
					String[] seatArr = rp.next_data[4].split("/");
					SeatDAO seatDao = SeatDAO.getInstance();
					Object[] seat_data = new Object[5];
					seat_data[0] = 1;
					seat_data[1] = rp.next_data[1].substring(0,10);
					seat_data[2] = rp.next_data[2].split("~")[0];
					seat_data[3] = rp.next_data[3];
					
					for(String seat : seatArr) {
						seat_data[4] = seat;
						seatDao.reservMethod(seat_data);
					}
					
					
					JOptionPane.showMessageDialog(this, "예매가 완료되었습니다.");
					
					this.dispose();
				} else
					return;
			}
		} else {
			if (selectedBtn.getBackground() != Color.GRAY) {
				selectedBtn.setBackground(Color.GRAY);
				seatList.add(selectedBtn);
			} else if (selectedBtn.getBackground() == Color.GRAY) {
				selectedBtn.setBackground(null);
				seatList.remove(selectedBtn);
			}
			
			rp.next_data[4]= "";
			for (JButton btn : seatList) {
				rp.next_data[4] += btn.getName();
				rp.next_data[4] += "/";
			}
			
			if(rp.next_data[4].length()!=0)
				rp.next_data[4] = rp.next_data[4].substring(0, rp.next_data[4].length()-1);
			
			rp.seat_label.setText("                   " + rp.next_data[4]);
			
			if(rp.next_data[2].split("~")[0].equals("06:00") || rp.next_data[2].split("~")[0].equals("22:00")) {
				rp.price_label.setText((seatList.size() * 5000) + "원    ");
				rp.tot_label.setText("￦ " + (seatList.size() * 5000) + "    ");
				rp.total_label.setText((seatList.size() * 5000) + " 원   ");
			} else {
				rp.price_label.setText((seatList.size() * 10000) + "원    ");
				rp.tot_label.setText("￦ " + (seatList.size() * 10000) + "    ");
				rp.total_label.setText((seatList.size() * 10000) + " 원   ");
			}
			rp.info_seat.revalidate();
			rp.info_pay.revalidate();
			rp.info_seat.repaint();
			rp.info_pay.repaint();
		}
	}//end actionPerformed()
}//end class
