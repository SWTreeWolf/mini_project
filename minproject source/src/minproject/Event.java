package minproject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import minproject.JDBC.MemberDAO;

@SuppressWarnings("serial")
public class Event extends JPanel implements ActionListener {
	JLabel label;
	JButton[] btns;
	JButton btn_reset;
	ImageIcon icon, icon_entered, icon_win, icon_lose;
	int width, height;
	boolean isSelected;
	int max = 9;
	
	Color color = new Color(255,255,230);
	
	public Event() {	
		JPanel jp1 = new JPanel();
		label = new JLabel("도전하세요!!",SwingConstants.CENTER);
		label.setFont(new Font("굴림", Font.BOLD, 50));
		jp1.setBackground(new Color(205, 193, 151));
		jp1.add(label);

		JPanel jp2 = new JPanel(new GridLayout(3, 3, 10, 10));
		btns = new JButton[max];
		for(int i=0; i<btns.length; i++) {
			btns[i] = new JButton();
			btns[i].setName("event");
			btns[i].setBackground(Color.WHITE);
			btns[i].setFocusPainted(false);
			btns[i].setBorder(new LineBorder(Color.BLACK, 5));
			jp2.add(btns[i]);
			btns[i].addActionListener(this);
		}
		jp2.setBackground(color);
		
		JPanel jp3 = new JPanel();
		btn_reset = new JButton("리셋");
		btn_reset.setName("reset");
		btn_reset.setFont(new Font("굴림", Font.BOLD, 30));
		btn_reset.setPreferredSize(new Dimension(200,100));
		btn_reset.setBackground(Color.WHITE);
		btn_reset.setFocusPainted(false);
		jp3.add(btn_reset);
		jp3.setBackground(color);

		btn_reset.addActionListener(this);
		
		setBackground(color);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		add(jp1, gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weighty = 1;
		add(jp2, gbc);
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weighty = 1;
		add(jp3, gbc);
				
		resize();
		random();
	}
	
	private void resize() {
		icon = new ImageIcon("src\\minproject\\img\\box.jpg");
		icon_win = new ImageIcon("src\\minproject\\img\\box_win.jpg");
		icon_lose = new ImageIcon("src\\minproject\\img\\box_lose.jpg");
	}

	private void random() {
		Random ran = new Random();
		int win = ran.nextInt(9)+1;
		for(int i=0; i<btns.length; i++) {
			btns[i].setIcon(icon);
			if(win==i+1)
				btns[i].setSelectedIcon(icon_win);
			else
				btns[i].setSelectedIcon(icon_lose);
		}
	}
	
	public void check(JButton btn) {
		btn.setBorder(new LineBorder(Color.RED, 5));
		btn.setBorderPainted(true);
		if(btn.getSelectedIcon()==icon_win) {
			label.setText("10000p 당첨!!");
			int point=Integer.parseInt(Main.pointLabel.getText().split("P")[0]);
			point += 10000;
			Main.pointLabel.setText(String.valueOf(point) + "P");
			MemberDAO dao = MemberDAO.getInstance();
			int num[] = new int[2];
			num[0] = point;
			num[1] = Main.myCode;
			dao.chargeMethod(num);
		}
		else
			label.setText("꽝입니다..");
		isSelected = true;
	}
	
	public void reset() {
		label.setText("도전하세요!!");
		isSelected = false;
		for(int i=0; i<btns.length; i++) {
			btns[i].setSelected(false);
			btns[i].setBorder(new LineBorder(Color.BLACK, 5));
		}
		random();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		JButton btn = (JButton) obj;
		
		if(btn.getName().equals("reset"))
			reset();
		else if(btn.getName().equals("event")) {
			check(btn);
			for(int i=0; i<btns.length; i++)
				btns[i].setSelected(true);
		} 
	}

}
