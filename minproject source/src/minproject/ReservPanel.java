package minproject;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import minproject.JDBC.MovieDAO;


public class ReservPanel extends JPanel {
	// 전체 패널과버튼 패널과 정보 패널
	JPanel btn_panel, info_panel;

	// 버튼 패널의 버튼 및 아이콘
	JButton prev_btn, next_btn;
	ImageIcon prev_img, next_img;

	// info_panel의 정보 패널
	JPanel info_reserv, info_seat, info_pay, info_reserv_tit, info_reserv_detail, info_reserv_con, info_seat_tit,
			info_seat_detail, info_seat_con, info_pay_tit, info_pay_detail, info_pay_con;

	GridBagLayout seat_info;
	Ticketing ticket;
	String price="";
	String seat_count="";
	JLabel price_label,seat_label,tot_label,total_label;
	
	//0=영화 제목 , 1=상영일 ,  2=상영시간 , 3=상영관 , 4= 좌석 ,  5= 총 가격
	String[] next_data = new String[6];
	
	//버그수정 
	String[] day = {"일","월"};
	
	public ReservPanel(Ticketing tic) {
		this.ticket = tic;
		
		//좌석값 초기화
		next_data[4]="";
		
		this.setVisible(true);
		this.setSize(1000, 280);
		seat_info = new GridBagLayout();
		GridBagConstraints tot_layout = new GridBagConstraints();
		this.setLayout(seat_info);

		btn_panel = setBtn();

		// test color
		btn_panel.setBackground(new Color(205, 193, 151));

		tot_layout.weightx = 0.1;
		tot_layout.weighty = 0.15;
		tot_layout.anchor = GridBagConstraints.NORTH;
		tot_layout.fill = GridBagConstraints.BOTH;
		tot_layout.gridwidth = GridBagConstraints.REMAINDER;
		tot_layout.gridheight = 1;
		seat_info.setConstraints(btn_panel, tot_layout);
		
		
		this.add(btn_panel);

		info_panel = setInfo();
		
		// test color
		info_panel.setBackground(new Color(85, 85, 85));

		tot_layout.anchor = GridBagConstraints.CENTER;

		tot_layout.fill = GridBagConstraints.BOTH;
		tot_layout.gridwidth = 4;
		tot_layout.gridheight = 2;
		tot_layout.weightx = 0.1;
		tot_layout.weighty = 0.85;
		seat_info.setConstraints(info_panel, tot_layout);
		this.add(info_panel);
		btn_panel.revalidate();
		btn_panel.repaint();
		
	}
	
	//btn_box
	private JPanel setBtn() {
		GridBagLayout btn_grid=new GridBagLayout();
		JPanel btn_box = new JPanel(btn_grid);
		GridBagConstraints btn_layout = new GridBagConstraints();

		prev_img = new ImageIcon("./src/minproject/btn_prev.png");
		prev_btn = new JButton("이전단계", prev_img);
		prev_btn.setBackground(new Color(205, 193, 151));
		prev_btn.setBorder(new LineBorder(new Color(205, 193, 151)));
		prev_btn.setFont(new Font("돋움",3,15));
		
		btn_layout.gridx=0;
		btn_layout.gridy=0;
		btn_layout.weightx=1;
		btn_layout.ipadx=20;
		
		
		btn_layout.anchor = GridBagConstraints.WEST;
		btn_grid.setConstraints(prev_btn, btn_layout);
		
		
		next_img = new ImageIcon("./src/minproject/btn_next.png");
		next_btn = new JButton("다음단계", next_img);
		next_btn.setBackground(new Color(205, 193, 151));
		next_btn.setBorder(new LineBorder(new Color(205, 193, 151)));
		next_btn.setFont(new Font("돋움",3,15));
		next_btn.setHorizontalTextPosition(JButton.LEFT);
		
		btn_layout.gridx=1;
		btn_layout.gridy=0;
		btn_layout.weightx=1;
		btn_layout.ipadx=20;
		
		
		btn_layout.anchor = GridBagConstraints.EAST;
		btn_layout.fill = GridBagConstraints.REMAINDER;
		btn_grid.setConstraints(next_btn, btn_layout);
				
		btn_box.add(prev_btn);
		btn_box.add(next_btn);
		
		
		return btn_box;
	}
	
	//Info_box
	private JPanel setInfo() {
		
		JPanel info_box = new JPanel(new GridLayout(1, 3));
		//test
	
		GridBagConstraints grid_info= new GridBagConstraints();
		GridBagLayout info_lay = new GridBagLayout();
		
		info_reserv = new JPanel(info_lay);
	
		info_reserv.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.white));
		info_reserv.setBackground(new Color(85,85,85));
		info_reserv_tit =new JPanel(new FlowLayout(FlowLayout.LEFT));
		info_seat_tit = new JPanel(new FlowLayout(FlowLayout.LEFT));
		info_seat_tit.setBackground(new Color(85,85,85));
		info_pay_tit = new JPanel(new FlowLayout(FlowLayout.LEFT));
		info_pay_tit.setBackground(new Color(85,85,85));
		
		JLabel label = new JLabel("  영화");
		label.setForeground(new Color(205, 193, 151));
		label.setFont(new Font("돋움", 3, 15));
		info_reserv_tit.add(label);
		JLabel seat_tit_txt = new JLabel("  예매 정보");
		seat_tit_txt.setForeground(label.getForeground());
		seat_tit_txt.setFont(label.getFont());
		info_seat_tit.add(seat_tit_txt);
		JLabel pay_tit_txt = new JLabel("  총 결제 금액");
		pay_tit_txt.setForeground(label.getForeground());
		pay_tit_txt.setFont(label.getFont());
		info_pay_tit.add(pay_tit_txt);
		info_reserv_tit.setBackground(new Color(85,85,85));
		info_reserv_tit.setForeground(Color.WHITE);
		
		grid_info.gridx=1;
		grid_info.gridy=0;
		grid_info.weightx=1.0;
		grid_info.weighty=0.0;
		grid_info.gridwidth=GridBagConstraints.REMAINDER;
		grid_info.gridheight=1;

		grid_info.fill = GridBagConstraints.HORIZONTAL;
		grid_info.anchor= GridBagConstraints.NORTHWEST;
		
		info_lay.setConstraints(info_reserv_tit, grid_info);
		info_lay.setConstraints(info_seat_tit, grid_info);
		info_lay.setConstraints(info_pay_tit, grid_info);
		info_reserv.add(info_reserv_tit);

		info_reserv_detail =new JPanel();
		
		MovieDAO dao = MovieDAO.getInstance();
		String imagePath = dao.imageMethod(ticket.label[0].getText());
		
		try {
			BufferedImage img = new BufferedImage(50, 150, BufferedImage.TYPE_INT_RGB);
			img = ImageIO.read(new File(imagePath));
			
			ImageIcon icon = new ImageIcon(img);
			Image reimg = icon.getImage().getScaledInstance(80, 120, Image.SCALE_SMOOTH);
			
			info_reserv_detail.add(new JLabel(new ImageIcon(reimg)));
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		info_reserv_detail.setBackground(new Color(85,85,85));
		info_seat_detail = new JPanel(new GridLayout(5, 1));
		info_seat_detail.setBackground(new Color(85,85,85));
		info_pay_detail = new JPanel(new GridLayout(5, 1));
		info_pay_detail.setBackground(new Color(85,85,85));
		
		//detail 정보
		JLabel seat_detail_txt = new JLabel("    상영일   ");
		seat_detail_txt.setFont(new Font("돋움",1,12));
		seat_detail_txt.setForeground(new Color(167, 169, 172));
		info_seat_detail.add(seat_detail_txt);
		
		seat_detail_txt = new JLabel("    상영시간   ");
		seat_detail_txt.setFont(new Font("돋움",1,12));
		seat_detail_txt.setForeground(new Color(167, 169, 172));
		info_seat_detail.add(seat_detail_txt);
		
		seat_detail_txt = new JLabel("    상영관   ");
		seat_detail_txt.setFont(new Font("돋움",1,12));
		seat_detail_txt.setForeground(new Color(167, 169, 172));
		info_seat_detail.add(seat_detail_txt);
		
		seat_detail_txt = new JLabel("    좌석   ");
		seat_detail_txt.setFont(new Font("돋움",1,12));
		seat_detail_txt.setForeground(new Color(167, 169, 172));
		info_seat_detail.add(seat_detail_txt);
		
		JLabel pay_detail_txt = new JLabel("    영화예매   ");
		pay_detail_txt.setFont(new Font("돋움",1,12));
		pay_detail_txt.setForeground(new Color(167, 169, 172));
		info_pay_detail.add(pay_detail_txt);
		
		grid_info.gridx=0;
		grid_info.gridy=1;
		grid_info.weightx=0.3;
		grid_info.weighty=1.0;
		grid_info.gridwidth=GridBagConstraints.REMAINDER;

		grid_info.fill=GridBagConstraints.VERTICAL;
		grid_info.anchor= GridBagConstraints.WEST;
		info_lay.setConstraints(info_reserv_detail, grid_info);
		info_lay.setConstraints(info_seat_detail, grid_info);
		info_lay.setConstraints(info_pay_detail, grid_info);
		info_reserv.add(info_reserv_detail);

		info_reserv_con =new JPanel(new GridLayout(4, 1));
		
		//넘길 데이터 
		next_data[0]= ticket.label[0].getText();
		
		//영화 제목 받는곳
		JLabel ch_label = new JLabel("                   "+ticket.label[0].getText());
		ch_label.setFont(new Font("돋움",3,13));
		ch_label.setForeground(Color.white);
		info_reserv_con.add(ch_label);
		   
		ch_label = new JLabel("                        "+"2D");
		ch_label.setFont(new Font("돋움",1,10));
		ch_label.setForeground(Color.white);
		info_reserv_con.add(ch_label);
		
		//연령
		ch_label = new JLabel("                        "+"12세 이상 관람가");
		ch_label.setFont(new Font("돋움",1,10));
		ch_label.setForeground(Color.white);
		info_reserv_con.add(ch_label);
		
		info_reserv_con.add(new JLabel(""));
		
		info_seat_con = new JPanel(new GridLayout(5, 1));
		
		String[] seat_date = ticket.label[1].getText().split("월|일");
		
		if(new Integer(seat_date[1]) < 10) {
			seat_date[1]="0"+seat_date[1];
		}
		
		//버그수정
		if(seat_date.length < 4) {
		//test binding text 바꿔야 함..
		ch_label = new JLabel("                   "+"2018-0"+seat_date[0]+"-"+seat_date[1]+seat_date[2]);
		ch_label.setFont(new Font("돋움",1,12));
		ch_label.setForeground(Color.WHITE);
		info_seat_con.add(ch_label);
		} else {
			Calendar cal = Calendar.getInstance();
			cal.set(2018, Integer.parseInt(seat_date[0])-1, Integer.parseInt(seat_date[1]));
			seat_date[2] = seat_date[2] + day[cal.get(Calendar.DAY_OF_WEEK)-1] + seat_date[3];
			ch_label = new JLabel("                   "+"2018-0"+seat_date[0]+"-"+seat_date[1]+seat_date[2]);
			ch_label.setFont(new Font("돋움",1,12));
			ch_label.setForeground(Color.WHITE);
			info_seat_con.add(ch_label);
		}
		
		//넘길 데이터
		next_data[1]="2018-0"+seat_date[0]+"-"+seat_date[1]+seat_date[2];
		
		String[] running = ticket.label[2].getText().split("/");
		String[] running_time = running[1].split(":");
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		date.setHours(new Integer(running_time[0]));
		date.setMinutes(new Integer(running_time[1]));
		cal.setTime(date);
		
		//90분은 러닝 타임
		cal.add(Calendar.MINUTE, 120);
		SimpleDateFormat simple_date = new SimpleDateFormat("HH:mm");
		
		ch_label = new JLabel("                   "+running[1]+"~"+simple_date.format(cal.getTime()));
		ch_label.setFont(new Font("돋움",1,12));
		ch_label.setForeground(Color.white);
		info_seat_con.add(ch_label);
		
		//넘길 데이터
		next_data[2]=running[1]+"~"+simple_date.format(cal.getTime());
		
		ch_label = new JLabel("                   "+running[0]);
		ch_label.setFont(new Font("돋움",1,12));
		ch_label.setForeground(Color.white);
		info_seat_con.add(ch_label);
		
		//넘길데이터
		next_data[3]=running[0];
		
		seat_label = new JLabel("                   "+seat_count);
		seat_label.setFont(new Font("돋움",1,12));
		seat_label.setForeground(Color.white);
		info_seat_con.add(seat_label);
		
		price_label = new JLabel(price+"원    ");
		
		price_label.setFont(new Font("돋움",1,15));
		price_label.setForeground(Color.white);
		price_label.setHorizontalAlignment(SwingConstants.RIGHT);
		info_seat_con.add(price_label);
		
		
		
		info_pay_con = new JPanel(new GridLayout(5, 1));
		tot_label = new JLabel("￦ "+price+"    ");
		tot_label.setFont(new Font("돋움",1,12));
		tot_label.setForeground(Color.white);
		tot_label.setHorizontalAlignment(SwingConstants.RIGHT);
		info_pay_con.add(tot_label);
		
		info_pay_con.add(new JLabel(""));
		info_pay_con.add(new JLabel(""));
		info_pay_con.add(new JLabel(""));
		
		total_label = new JLabel(price+" 원   ");
		total_label.setFont(new Font("돋움",3,15));
		total_label.setForeground(Color.white);
		total_label.setHorizontalAlignment(SwingConstants.RIGHT);
		info_pay_con.add(total_label);
		
		

		info_reserv_con.setBackground(new Color(85,85,85));
		info_seat_con.setBackground(new Color(85,85,85));
		info_pay_con.setBackground(new Color(85,85,85));
		
		grid_info.gridx=0;
		grid_info.gridy=1;
		grid_info.weightx=0.7;
		grid_info.weighty=1.0;
		
		grid_info.gridwidth=GridBagConstraints.REMAINDER;
		grid_info.fill=GridBagConstraints.BOTH;
		grid_info.anchor= GridBagConstraints.WEST;
		info_lay.setConstraints(info_reserv_con, grid_info);
		info_lay.setConstraints(info_seat_con, grid_info);
		info_lay.setConstraints(info_pay_con, grid_info);
		info_reserv.add(info_reserv_con);
		
		info_seat = new JPanel(info_lay);
		info_seat.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.white));
		info_seat.setBackground(new Color(85,85,85));
		info_seat.add(info_seat_tit);
		info_seat.add(info_seat_detail);
		info_seat.add(info_seat_con);
		info_pay = new JPanel(info_lay);
		info_pay.setBackground(new Color(85,85,85));
		info_pay.add(info_pay_tit);
		info_pay.add(info_pay_detail);
		info_pay.add(info_pay_con);
		
		info_box.add(info_reserv);
		info_box.add(info_seat);
		info_box.add(info_pay);
		
		return info_box;
	}

}