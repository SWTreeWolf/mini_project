package minproject;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import minproject.JDBC.MemberDAO;

@SuppressWarnings("serial")
public class Login extends JPanel implements ActionListener {
	JTextField idTxt;
	JLabel idLabel, pwLabel;
	JButton loginBtn, joinBtn;
	JPasswordField pwTxt;
	JPanel pane;
	String paneName;

	public Login(JPanel pane, String paneName) {
		this.pane = pane;
		this.paneName = paneName;
		
		JPanel topPanel = new JPanel();
		JLabel topLabel = new JLabel("로그인");
		topLabel.setFont(new Font("굴림", Font.BOLD, 30));
		topPanel.add(topLabel);
		topPanel.setBackground(new Color(205, 193, 151));
		topPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		
		idLabel = new JLabel("아이디 ");
		pwLabel = new JLabel("비밀번호 ");
		idTxt = new JTextField();
		pwTxt = new JPasswordField();

		loginBtn = new JButton("로그인");
		joinBtn = new JButton("회원가입");
		idLabel.setFont(new Font("굴림", Font.BOLD, 20));
		pwLabel.setFont(new Font("굴림", Font.BOLD, 20));
		idTxt.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		pwTxt.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		loginBtn.setFont(new Font("굴림", Font.BOLD, 20));
		joinBtn.setFont(new Font("굴림", Font.BOLD, 20));

		loginBtn.setFocusPainted(false);
		joinBtn.setFocusPainted(false);
		
		idTxt.setDocument(new JTextFieldLimit(12));
		pwTxt.setDocument(new JTextFieldLimit(12));

		idLabel.setBounds(450, 300, 300, 30);
		idTxt.setBounds(550, 300, 200, 30);
		pwLabel.setBounds(450, 350, 300, 30);
		pwTxt.setBounds(550, 350, 200, 30);
		loginBtn.setBounds(450, 450, 130, 50);
		joinBtn.setBounds(620, 450, 130, 50);
		topPanel.setBounds(0, 0, 1200, 65);

		add(topPanel);
		add(idLabel);
		add(idTxt);
		add(pwLabel);
		add(pwTxt);
		add(loginBtn);
		add(joinBtn);

		loginBtn.addActionListener(this);
		idTxt.addActionListener(this);
		pwTxt.addActionListener(this);
		joinBtn.addActionListener(this);

		setBackground(new Color(255,255,230));
		setLayout(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();

		if (obj == loginBtn || obj == pwTxt || obj == idTxt) {
			String id = idTxt.getText();
			String password = String.valueOf(pwTxt.getPassword());

			MemberDAO dao = MemberDAO.getInstance();
			String[] str = new String[2];
			str[0] = id;
			str[1] = password;
			if (dao.loginMethod(str) && !dao.isExitedMethod(str)) {
				JOptionPane.showMessageDialog(loginBtn, "로그인 되었습니다.");
				Main.chk = true;
				// 회원코드와 이름을 메인에서 계속 관리함
				Main.myCode = dao.codeMethod(str);
				Main.changePane(pane, paneName);
				Main.pointLabel.setText(String.valueOf(dao.pointMethod(Main.myCode))+"P");
				Main.logInOutBtn.setText("로그아웃");

			} else if(dao.isExitedMethod(str)) {
				JOptionPane.showMessageDialog(loginBtn, "탈퇴된 회원입니다.");
			} else {
				JOptionPane.showMessageDialog(loginBtn, "아이디 또는 비밀번호가 틀렸습니다.");
			}
		} else if (obj == joinBtn) {
			new Join();
		}
	}
}