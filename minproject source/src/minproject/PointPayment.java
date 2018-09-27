package minproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import minproject.JDBC.MemberDAO;
import minproject.JDBC.PayDAO;
import minproject.JDBC.PayDTO;

public class PointPayment extends JFrame implements ActionListener, DocumentListener {
	Main main;
	Seat seat;
	JLabel price;
	JPanel txt_Panel;
	JButton agree_btn;
	JTextField price_txt;
	JTextField[] card_number;
	JRadioButton[] price_radio_btn, card;
	
	boolean isSelected = false;

	List<String> cardlist, pricelist;

	final private int MAX_COUNT_CARD = 9;
	private int variation = 0;
	private String[] str2;
	
	private static PointPayment pointPayment;
	
	private PointPayment(Object obj) {
		if(obj.equals(seat))
			this.seat = (Seat) obj;
		else if(obj.equals(main))
			this.main = (Main) obj;
		
		setTitle("포인트 충전");
		setLayout(new BorderLayout());

		JPanel btn_panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btn_panel.setBackground(new Color(95, 95, 95));
		agree_btn = new JButton("확인");
		agree_btn.addActionListener(this);
		btn_panel.add(agree_btn);

		infomation();

		JPanel card_name_panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		card_name_panel.setBackground(new Color(85, 85, 85));

		JLabel label1 = new JLabel("신용카드");
		label1.setFont(new Font("굴림", Font.BOLD, 20));
		label1.setForeground(Color.WHITE);

		JLabel label2 = new JLabel("Credit Card");
		label2.setFont(new Font("굴림", Font.PLAIN, 15));
		label2.setForeground(Color.WHITE);

		card_name_panel.add(label1);
		card_name_panel.add(label2);
		add(card_name_panel, BorderLayout.NORTH);
		add(btn_panel, BorderLayout.SOUTH);

		setSize(800, 500);
		setResizable(false);
		setLocationRelativeTo(main);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
 
	public static PointPayment getInstance(Object obj) {
		if(pointPayment == null) {
			pointPayment = new PointPayment(obj);
		}
		return pointPayment;
	}
	// border.center (gridbaglayout 이용) (금액, 포인트, 카드선택, 카드번호)
	private void infomation() {
		JPanel gbl_Panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		JPanel merge_Panel = new JPanel(new GridBagLayout());
		GridBagConstraints merge_gbc = new GridBagConstraints();

		JPanel product_panel = new JPanel(new GridLayout(1, 2));

		product_panel.add(new JLabel("금액"));
		price = new JLabel("0원");
		product_panel.add(price);

		JPanel price_Panel = new JPanel(new GridLayout(1, 4));
		price_radio_btn = new JRadioButton[4];
		ButtonGroup price_group = new ButtonGroup();

		pricelist = new ArrayList<String>();
		pricelist.add("1000P");
		pricelist.add("5000P");
		pricelist.add("10000P");
		pricelist.add("선택금액");

		for (int i = 0; i < price_radio_btn.length; i++) {
			price_radio_btn[i] = new JRadioButton(pricelist.get(i));
			price_radio_btn[i].setBackground(new Color(255, 255, 230));
			price_radio_btn[i].addActionListener(this);
			price_group.add(price_radio_btn[i]);

			if (i == price_radio_btn.length - 1) {
				JPanel sub_panel = new JPanel(new GridLayout(1, 2));
				price_txt = new JTextField(10);
				price_txt.getDocument().addDocumentListener(this);
				sub_panel.add(price_radio_btn[i]);
				sub_panel.add(price_txt);
				sub_panel.setBackground(new Color(255, 255, 230));
				price_Panel.add(sub_panel);
			} else
				price_Panel.add(price_radio_btn[i]);
		}

		////////////////////////////////////////////////////////////////
		JPanel card_kind_panel = new JPanel(new GridLayout(3, 3));
		card = new JRadioButton[MAX_COUNT_CARD];
		ButtonGroup card_group = new ButtonGroup();

		cardlist = new ArrayList<String>();
		cardlist.add("비씨카드");
		cardlist.add("신한카드");
		cardlist.add("KB국민카드");
		cardlist.add("NH농협은행");
		cardlist.add("현대카드");
		cardlist.add("삼성카드");
		cardlist.add("롯데카드");
		cardlist.add("하나카드");
		cardlist.add("우리은행");

		for (int i = 0; i < MAX_COUNT_CARD; i++) {
			card[i] = new JRadioButton(cardlist.get(i));
			card[i].addActionListener(this);
			card[i].setBackground(new Color(255, 255, 230));
			card_group.add(card[i]);
			card_kind_panel.add(card[i]);
		}

		JPanel sub_grid_bag = new JPanel(new GridBagLayout());
		GridBagConstraints sub_gbc = new GridBagConstraints();

		sub_gbc.fill = GridBagConstraints.BOTH;
		sub_gbc.anchor = GridBagConstraints.CENTER;
		sub_gbc.gridwidth = GridBagConstraints.REMAINDER;
		sub_gbc.weightx = 1;
		sub_gbc.gridx = 0;
		sub_gbc.gridy = 1;
		sub_gbc.insets = new Insets(0, 40, 0, 0);
		sub_grid_bag.setBackground(new Color(255, 255, 230));
		sub_grid_bag.setBorder(new TitledBorder(new LineBorder(Color.black), "카드사"));
		sub_grid_bag.add(card_kind_panel, sub_gbc);
		///////////////////////////////////////////////////////////

		txt_Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		txt_Panel.setVisible(false); // 최초에 안보이게

		card_number = new JTextField[4];

		for (int i = 0; i < card_number.length; i++) {
			card_number[i] = new JTextField(5);

			txt_Panel.add(card_number[i]);
			card_number[i].setDocument(new JTextFieldLimit(4));

			if (i < card_number.length - 1)
				txt_Panel.add(new JLabel("-"));
		}
		///////////////////////////////////////////////////////////
		merge_Panel.setBorder(new LineBorder(Color.BLACK));
		merge_Panel.setBackground(new Color(255, 255, 230));

		merge_gbc.fill = GridBagConstraints.BOTH;
		merge_gbc.anchor = GridBagConstraints.NORTH;
		merge_gbc.gridwidth = GridBagConstraints.REMAINDER;
		merge_gbc.gridx = 0;
		merge_gbc.gridy = 0;
		merge_gbc.insets = new Insets(0, 0, 0, 0);
		merge_Panel.add(product_panel, merge_gbc);

		merge_gbc.fill = GridBagConstraints.BOTH;
		merge_gbc.anchor = GridBagConstraints.CENTER;
		merge_gbc.gridwidth = GridBagConstraints.REMAINDER;
		merge_gbc.gridx = 0;
		merge_gbc.gridy = 1;
		merge_gbc.insets = new Insets(10, 0, 0, 0);
		merge_Panel.add(price_Panel, merge_gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 1;
		gbc.ipady = 10;
		gbc.weightx = 1;
		gbc.weighty = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(50, 30, 0, 50);
		gbl_Panel.add(merge_Panel, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		// gbc.ipady = 20;
		gbc.weightx = 1;
		gbc.weighty = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 30, 0, 50);
		gbl_Panel.add(sub_grid_bag, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		// gbc.ipady = 20;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0.2;
		gbc.insets = new Insets(0, 30, 50, 50);
		gbl_Panel.add(txt_Panel, gbc);

		product_panel.setBackground(new Color(255, 255, 230));
		card_kind_panel.setBackground(new Color(255, 255, 230));
		txt_Panel.setBackground(new Color(255, 255, 230));
		gbl_Panel.setBackground(new Color(255, 255, 230));

		add(gbl_Panel, BorderLayout.CENTER);
	}// end infomation()

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		str2 = price.getText().split("원");
		
		if (obj.equals(agree_btn)) {
			int result = JOptionPane.showConfirmDialog(this, "충전하시겠습니까?", "포인트 충전", 
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if(result == JOptionPane.YES_OPTION) {
				String[] str = Main.pointLabel.getText().split("P");
				int point = Integer.parseInt(str[0]);
				point += variation;
				Main.pointLabel.setText(String.valueOf(point) + "P");
				
				MemberDAO memDao = MemberDAO.getInstance();
				int[] data = new int[2];
				data[0] = point;
				data[1] = Main.myCode;
				memDao.chargeMethod(data);
				
				PayDAO payDao = PayDAO.getInstance();
				PayDTO payDto = new PayDTO();
				
				String credit = "";
				
				for(int i=0; i<card_number.length; i++) {
					credit += card_number[i].getText();
					if(i-1 == card_number.length)
						break;
					credit += "-";
				}
				
				payDto.setPay_credit(credit);
				payDto.setPay_point(variation);
				payDto.setMem_code(Main.myCode);
				payDao.payMethod(payDto);
				
				dispose();
			}
		}

		for (int i = 0; i < card.length; i++) {
			if (obj.equals(card[i])) {
				JRadioButton SelectedButton = (JRadioButton) e.getSource();
				if (SelectedButton != null) {
					txt_Panel.setBorder(new TitledBorder(new LineBorder(Color.black), SelectedButton.getText()));
					txt_Panel.setVisible(true);
					card_number[0].requestFocus();
				}
			}
		}

		for (int i = 0; i < price_radio_btn.length; i++) {
			if (obj.equals(price_radio_btn[i])) {
				if (price_radio_btn[3].isSelected()) {
					continue;
				} else if (obj.equals(price_radio_btn[2])) {
					price.setText(price_radio_btn[i].getText().substring(0, 5) + "원");
					str2 = price.getText().split("원");
					variation = Integer.parseInt(str2[0]);
				} else {
					price.setText(price_radio_btn[i].getText().substring(0, 4) + "원");
					str2 = price.getText().split("원");
					variation = Integer.parseInt(str2[0]);
				}
						
			}
		}
	}// end actionPerformed()

	@Override
	public void insertUpdate(DocumentEvent e) {
		try {
			price.setText(price_txt.getText() + "원");
			str2 = price.getText().split("원");
			variation = Integer.parseInt(str2[0]);
		} catch (ArrayIndexOutOfBoundsException e1) {
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		try {
			price.setText(price_txt.getText() + "원");
			str2 = price.getText().split("원");
			variation = Integer.parseInt(str2[0]);
		} catch (ArrayIndexOutOfBoundsException e1) {
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		try {
		price.setText(price_txt.getText() + "원");
		str2 = price.getText().split("원");
		variation = Integer.parseInt(str2[0]);
	} catch (ArrayIndexOutOfBoundsException e1) {
	}
	}

}// end class
