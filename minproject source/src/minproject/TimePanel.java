package minproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

import javafx.scene.layout.Border;
import minproject.JDBC.SeatDAO;
import minproject.JDBC.SeatDTO;

public class TimePanel extends JPanel implements ActionListener {
	final int MAX_CINEMA_CNT = 3;
	final int MAX_TIME_CNT = 5;

	JButton[] temp_A, temp_B, temp_C;
	JButton releasedBtn;
	
	String[] Cinema_name = new String[] { "A", "B", "C" };
	String[] time = { "06:00", "10:00", "14:00", "18:00", "22:00" }; // 시간을 String으로 받아와서, 배열로 저장
	Font l_font = new Font("굴림", Font.BOLD, 15);
	
	JPanel Root_P;
	GridBagConstraints gbc;

	public TimePanel() {
		setLayout(new GridLayout(1, 1));
		Auditorium();
	}// end TimePanel()

	// 아이콘 크기 조절
	public JLabel[] iconSize() {
		ImageIcon[] icon = new ImageIcon[2];
		BufferedImage j_image, n_image;
		Image j_dimg, n_dimg;

		try {
			JLabel[] icon_L = new JLabel[2];

			for (int i = 0; i < icon_L.length; i++) {
				icon_L[i] = new JLabel();
			}

			j_image = ImageIO.read(new File("./src/minproject/img/jojo.png"));
			n_image = ImageIO.read(new File("./src/minproject/img/night.png"));

			j_dimg = j_image.getScaledInstance(30, 30, j_image.SCALE_SMOOTH);
			n_dimg = n_image.getScaledInstance(30, 30, n_image.SCALE_SMOOTH);

			icon[0] = new ImageIcon(j_dimg);
			icon[1] = new ImageIcon(n_dimg);

			icon_L[0].setIcon(icon[0]);
			icon_L[1].setIcon(icon[1]);

			return icon_L;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}// end iconSize()

	private void Auditorium() {
		Root_P = new JPanel(new GridBagLayout());
		Root_P.setBackground(new Color(255, 255, 230));
		gbc = new GridBagConstraints();

		JLabel[] Info_icon = new JLabel[2];
		JLabel jojo = new JLabel("조조");
		JLabel night = new JLabel("심야");

		Font font = new Font("굴림", Font.BOLD, 25);

		jojo.setFont(l_font);
		night.setFont(l_font);

		Info_icon = iconSize();

		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;

		JPanel icon_P = new JPanel(new FlowLayout(FlowLayout.LEFT));
		icon_P.setBackground(new Color(205, 193, 151));
		icon_P.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 2), "정보", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, l_font));

		icon_P.add(Info_icon[0]);
		icon_P.add(jojo);
		icon_P.add(Info_icon[1]);
		icon_P.add(night);

		gbc.weightx = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0.0;
		Root_P.add(icon_P, gbc);

		JPanel[] time_P = new JPanel[MAX_CINEMA_CNT];
		temp_A = new JButton[MAX_TIME_CNT];
		temp_B = new JButton[MAX_TIME_CNT];
		temp_C = new JButton[MAX_TIME_CNT];

		for (int i = 0; i < MAX_CINEMA_CNT; i++) {
			String str = Cinema_name[i] + "상영관";

			time_P[i] = new JPanel(new GridLayout(3, 3));
			time_P[i].setBackground(new Color(255, 255, 230));
			time_P[i].setBorder(new TitledBorder(new LineBorder(Color.BLACK, 2), str, TitledBorder.LEFT,
					TitledBorder.ABOVE_TOP, font));

			switch (i) {
			case 0:
				for (int j = 0; j < MAX_TIME_CNT; j++) {
					temp_A[j] = new JButton();
					temp_A[j].setBackground(null);
					temp_A[j].setFocusPainted(false);
					temp_A[j].addActionListener(this);
					temp_A[j].setName("A영화관");
					if (j < 3) {
						time_P[i].add(Time_Select_Panel(j, 0, time[j], temp_A[j])); // x, y, String, int
					} else {
						time_P[i].add(Time_Select_Panel(j - 3, 1, time[j], temp_A[j]));
					}
				}
				break;
			case 1:
				for (int j = 0; j < MAX_TIME_CNT; j++) {
					temp_B[j] = new JButton();
					temp_B[j].setBackground(null);
					temp_B[j].setFocusPainted(false);
					temp_B[j].addActionListener(this);
					temp_B[j].setName("B영화관");
					if (j < 3) {
						time_P[i].add(Time_Select_Panel(j, 0, time[j],temp_B[j])); // x, y, String, int
					} else {
						time_P[i].add(Time_Select_Panel(j - 3, 1, time[j], temp_B[j]));
					}
				}
				break;
			case 2:
				for (int j = 0; j < MAX_TIME_CNT; j++) {
					temp_C[j] = new JButton();
					temp_C[j].setBackground(null);
					temp_C[j].setFocusPainted(false);
					temp_C[j].addActionListener(this);
					temp_C[j].setName("영화관");
					if (j < 3) {
						time_P[i].add(Time_Select_Panel(j, 0, time[j],temp_C[j])); // x, y, String, int
					} else {
						time_P[i].add(Time_Select_Panel(j - 3, 1, time[j], temp_C[j]));
					}
				}
				break;
			}

			gbc.weightx = 1;
			gbc.gridy = i + 1;
			gbc.weighty = 0.3;
			Root_P.add(time_P[i], gbc);
		}
		
		add(Root_P);
	}// end Auditorium()

	// 시간 select 하기 위한 패널 (Seat에서 값을 가져옴)
	public JPanel Time_Select_Panel(int x, int y, String time, JButton btn) {
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 230));

		btn.setText(time);

		JLabel[] icon = new JLabel[2];
		icon = iconSize();

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();

		panel.setLayout(gbl);

		gbc.weightx = 0.5;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(15, 5, 0, 0);
		panel.add(btn, gbc);

		// icon[0] : jojo, icon[1] : night
		if (time.equals("06:00")) {
			gbc.weightx = 0.2;
			gbc.gridx = x + 1;
			gbc.gridy = y;
			gbc.fill = GridBagConstraints.VERTICAL;
			gbc.insets = new Insets(15, 5, 0, 0);
			panel.add(icon[0], gbc);
		} else if(time.equals("22:00")) {
			gbc.weightx = 0.2;
			gbc.gridx = x + 1;
			gbc.gridy = y;
			gbc.fill = GridBagConstraints.VERTICAL;
			gbc.insets = new Insets(15, 5, 0, 0);
			panel.add(icon[1], gbc);
		} else {
			gbc.weightx = 0.2;
			gbc.gridx = x + 1;
			gbc.gridy = y;
			gbc.fill = GridBagConstraints.VERTICAL;
			gbc.insets = new Insets(15, 5, 0, 0);
			panel.add(new JLabel("           "), gbc); // 크기조절
		}

		return panel;
	}// end Time_Select_Panel()

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		JButton selectedBtn = (JButton) obj;

		if(releasedBtn!=null) {
			if(releasedBtn==selectedBtn) {
				selectedBtn.setBackground(Ticketing.color);
				releasedBtn = null;
				return;
			} else
				releasedBtn.setBackground(Ticketing.color);
		}
		selectedBtn.setBackground(Color.GRAY);
		
		releasedBtn = selectedBtn;
	}// end actionPerformed();

}// end class
