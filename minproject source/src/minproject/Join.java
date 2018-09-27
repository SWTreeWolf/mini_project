package minproject;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import minproject.JDBC.*;

@SuppressWarnings("serial")
public class Join extends JFrame implements ActionListener {
	JTextField nameTxt, localTxt, phoneTxt, idTxt;
	JPasswordField pwTxt;
	JButton submitBtn;

	public Join() {
		super("회원가입");
		nameTxt = new JTextField(20);
		localTxt = new JTextField(20);
		phoneTxt = new JTextField(20);
		idTxt = new JTextField(20);
		pwTxt = new JPasswordField(20);
		submitBtn = new JButton("가입하기");
		
		submitBtn.setFocusPainted(false);
	
		nameTxt.setDocument(new JTextFieldLimit(10));
		localTxt.setDocument(new JTextFieldLimit(6));
		phoneTxt.setDocument(new JTextFieldLimit(11));
		idTxt.setDocument(new JTextFieldLimit(12));
		pwTxt.setDocument(new JTextFieldLimit(12));

		JPanel subPanel = new JPanel(null);

		
		JLabel nameLabel = new JLabel("회원이름");
		JLabel localLabel = new JLabel("주민등록번호");
		JLabel phoneLabel = new JLabel("휴대전화");
		JLabel idLabel = new JLabel("아이디");
		JLabel pwLabel = new JLabel("비밀번호");
		
		nameLabel.setFont(new Font("굴림", Font.BOLD, 13));
		localLabel.setFont(new Font("굴림", Font.BOLD, 13));
		phoneLabel.setFont(new Font("굴림", Font.BOLD, 13));
		idLabel.setFont(new Font("굴림", Font.BOLD, 13));
		pwLabel.setFont(new Font("굴림", Font.BOLD, 13));
		nameTxt.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		localTxt.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		phoneTxt.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		idTxt.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		pwTxt.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		submitBtn.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		
		nameLabel.setBounds(80, 90, 300, 30);
		nameTxt.setBounds(170, 92, 200, 30);
		localLabel.setBounds(80, 140, 300, 30);
		localTxt.setBounds(170, 142, 200, 30);
		phoneLabel.setBounds(80, 190, 300, 30);
		phoneTxt.setBounds(170, 192, 200, 30);
		idLabel.setBounds(80, 240, 300, 30);
		idTxt.setBounds(170, 242, 200, 30);
		pwLabel.setBounds(80, 290, 300, 30);
		pwTxt.setBounds(170, 292, 200, 30);
		submitBtn.setBounds(172, 380, 130, 50);

		subPanel.add(nameLabel);
		subPanel.add(nameTxt);
		subPanel.add(localLabel);
		subPanel.add(localTxt);
		subPanel.add(phoneLabel);
		subPanel.add(phoneTxt);
		subPanel.add(idLabel);
		subPanel.add(idTxt);
		subPanel.add(pwLabel);
		subPanel.add(pwTxt);
		subPanel.add(submitBtn);
		
		add(subPanel);
		
		submitBtn.addActionListener(this);
		subPanel.setBackground(new Color(205, 193, 151));
		setLayout(new GridLayout(1, 1));
		setSize(500, 500);
		setLocationRelativeTo(this);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if(obj == submitBtn) {
			MemberDAO dao = MemberDAO.getInstance();
			
			if(dao.isJoinedMethod(idTxt.getText())) {
				JOptionPane.showMessageDialog(submitBtn, "이미 사용 중인 아이디입니다.");
				return;
			}
			
			String name = nameTxt.getText();
			String local = localTxt.getText();
			String phone = phoneTxt.getText();
			String id = idTxt.getText();
			String password = String.valueOf(pwTxt.getPassword());
			
			if (!name.matches(".*[가-힣].*")) {
				JOptionPane.showMessageDialog(submitBtn, "회원이름은 한글로만 입력하시오.");
				return;
			} else if (!local.matches("[0-9]{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[1,2][0-9]|3[0,1])")) {
				JOptionPane.showMessageDialog(submitBtn, "주민등록번호는 앞 6자리만 입력하시오.");
				return;
			} else if (!phone.matches("01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{5}")) {
				JOptionPane.showMessageDialog(submitBtn, "전화번호는 '-' 없이 숫자 11자리만 입력하시오.");
				return;
			} else if (!id.matches(".*[a-z].*") || !id.matches(".*[0-9].*") || !id.matches("[\\w]{2,12}")) {
				JOptionPane.showMessageDialog(submitBtn, "아이디는 소문자/숫자 조합 2자리 이상 12자리 이하로 입력하시오.");
				return;
			} else if (!password.matches(".*[a-z].*") || !password.matches(".*[0-9].*") || !password.matches("[\\w]{4,12}")) {
				JOptionPane.showMessageDialog(submitBtn, "비밀번호는 소문자/숫자 조합 4자리 이상 12자리 이하로 입력하시오.");
				return;
			}
			
			MemberDTO dto = new MemberDTO();
			dto.setMem_name(name);
			dto.setMem_local(Integer.parseInt(local));
			dto.setMem_phone(phone);
			dto.setMem_id(id);
			dto.setMem_pw(password);
			
			dao.joinMethod(dto);
			
			JOptionPane.showMessageDialog(submitBtn, "회원가입이 완료되었습니다.");
			dispose();
		}
	}
	
}
