package minproject;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class Main extends JFrame implements ActionListener, ChangeListener{
	JPanel reservPane, eventPane;
	
	public static JTabbedPane jtp;
	public static JPanel moviePane, myPane;
	public static int myCode;
	public static CardLayout card;
	public static JLabel pointLabel;
	public static JButton logInOutBtn, chargeBtn;
	public static boolean chk = false;
	
	public Main() {
		jtp = new JTabbedPane();
		card = new CardLayout();
		
		moviePane = new JPanel();
		moviePane.setName("movie");
		moviePane.setLayout(card);
		moviePane.add(new Login(moviePane,"movie"),"login");
		moviePane.add(new Movie(),"movie");
		
		card.show(moviePane,"movie");
		
		reservPane = new JPanel();
		reservPane.setName("reserv");
		reservPane.setLayout(card);
		reservPane.add(new Login(reservPane,"reserv"),"login");
		if(myCode!=0)
			reservPane.add(new Reservation(),"reserv");
		
		myPane = new JPanel();
		myPane.setName("my");
		myPane.setLayout(card);
		myPane.add(new Login(myPane,"my"),"login");
		if(myCode!=0)
			myPane.add(new Mypage(),"my");
		
		eventPane = new JPanel();
		eventPane.setName("event");
		eventPane.setLayout(card);
		eventPane.add(new Login(eventPane,"event"),"login");
		if(myCode!=0)
			eventPane.add(new Event(),"event");
		
		jtp.addTab("영화", moviePane);
		jtp.addTab("예매", reservPane);
		jtp.addTab("마이페이지", myPane);
		jtp.addTab("이벤트", eventPane);
		
		JPanel optionPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pointLabel = new JLabel();
		optionPane.setBackground(new Color(255,255,230));
		optionPane.setName("loginout");
		chargeBtn = new JButton("충전");
		logInOutBtn = new JButton("로그인");
		chargeBtn.setPreferredSize(new Dimension(100,25));
		logInOutBtn.setPreferredSize(new Dimension(100,25));
		
		chargeBtn.setFocusPainted(false);
		logInOutBtn.setFocusPainted(false);
		
		optionPane.add(pointLabel);
		optionPane.add(chargeBtn);
		optionPane.add(logInOutBtn);
		
		add(optionPane,BorderLayout.NORTH);
		add(jtp,BorderLayout.CENTER);
		
		logInOutBtn.addActionListener(this);
		chargeBtn.addActionListener(this);
		jtp.addChangeListener(this);
		
		setResizable(false);
		setSize(1200, 900);
		setLocationRelativeTo(null);
		setTitle("minproject");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {	
		Object obj = e.getSource();
		
		if(obj == logInOutBtn) {
			if(chk) {
				int result = JOptionPane.showConfirmDialog(this, "로그아웃하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
				if(result==JOptionPane.YES_OPTION) {
					chk = false;
					pointLabel.setText("");
					logInOutBtn.setText("로그인");
					jtp.setSelectedIndex(0);
					
					myCode = 0;
					
					changePane(moviePane, "movie");
				}
			}
			else
				changePane(moviePane, "login");
		} else if(obj == chargeBtn) {
			if(myCode==0) {
				JOptionPane.showMessageDialog(this, "로그인을 먼저 해주세요.");
				return;
			}
			// 결제창 띄움
			PointPayment.getInstance(this);
		}
	}
	
	public static void changePane(JPanel pane, String paneName) {
		pane.removeAll();
		pane.add(new Login(pane,pane.getName()),"login",0);
		if(pane.getName().equals("movie"))
			pane.add(new Movie(), "movie");
		
		if(myCode!=0) {
			if(pane.getName().equals("reserv"))
				pane.add(new Reservation(), "reserv");
			else if(pane.getName().equals("my"))
				pane.add(new Mypage(), "my");
			else if(pane.getName().equals("event"))
				pane.add(new Event(), "event");
		}
		card.show(pane,paneName);
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		Object obj = e.getSource();
		
		if(obj==jtp) {
			if(jtp.getSelectedComponent()==jtp.getComponentAt(0))
				changePane(moviePane, "movie");
			if(jtp.getSelectedComponent()==jtp.getComponentAt(1))
				if(chk)
					changePane(reservPane, "reserv");
				else
					changePane(reservPane, "login");
			else if(jtp.getSelectedComponent()==jtp.getComponentAt(2))
				if(chk)
					changePane(myPane, "my");
				else
					changePane(myPane, "login");
			else if(jtp.getSelectedComponent()==jtp.getComponentAt(3))
				if(chk)
					changePane(eventPane, "event");
				else
					changePane(eventPane, "login");
		}
	}
	
	public static void main(String[] args) {
		new Main();
	}

}

