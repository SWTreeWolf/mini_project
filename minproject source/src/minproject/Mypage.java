package minproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import javax.swing.table.DefaultTableModel;

import minproject.JDBC.BookingDAO;
import minproject.JDBC.BookingDTO;
import minproject.JDBC.MemberDAO;
import minproject.JDBC.MemberDTO;
import minproject.JDBC.MovieDAO;
import minproject.JDBC.MovieDTO;
import minproject.JDBC.SeatDAO;

public class Mypage extends JPanel implements ActionListener {
	// mem = member (줄임)
	// member Info
	JLabel memNumber, memName, memAge, memPhone, memId, labelText;
	JLabel[] text_tab;
	JTextField memNumberF, memNameF, memAgeF, memPhoneF, memIdF;
	JPasswordField passF, passChkF, memPassF;
	JPanel memInfo, reservRecord, reservRecordSec, reserv;
	JPanel memInfoFir, memInfoSec, memInfoThr, memInfoFour;
	JButton infoUpdate, memExit, cancelReser;

	String image;
	BufferedImage imageInfo;
	// BufferedImage image, imageInfo;
	boolean updating = false;

	JTable table;
	JScrollPane couponScroll;
	// tabel model
	DefaultTableModel couponTable;

	// reserv Info
	JLabel reservName, reservLocal, reservDate, reservCount, reservChk;

	// 정규식
	Pattern infoChk = Pattern.compile("[-a-z][A-Z][0-9][ㄱ-ㅎ][가-힣]");
	Matcher infoCheck;

	// coupon table column
	String[] couponCol = { "쿠폰명", "쿠폰 번호", "쿠폰 기한" };

	String[] test = new String[4];
	String[] reservTest = new String[5];
	boolean reservCk;
	Font font;
	// test Info
	/*
	 * String[] test = { "정승호", "25", "010-5494-4358", "kukuff" }; String[]
	 * reservTest = { "어벤져스 : 인피니티 워", "승호영화관/3관", "2015-04-25", "성인 2명/R열25/R열26",
	 * "무통장입금" };
	 */

	public Mypage() {
		setLayout(new GridLayout(1, 1));
		loadMyPage();
		// personal Info
		memName = new JLabel("     " + "회원 이름 : ");
		memAge = new JLabel("     " + "회원 나이 : ");
		memPhone = new JLabel("     " + "회원 전화번호 : ");
		memId = new JLabel("     " + "회원 아이디 : ");

		// reserv Info
		reservName = new JLabel("     " + "예매 영화 명 : ");
		reservLocal = new JLabel("     " + "상영관 : ");
		reservDate = new JLabel("     " + "관람 일시 : ");
		reservCount = new JLabel("     " + "관람 인원 및 좌석 : ");
		reservChk = new JLabel("");

		font = new Font("sansSerif", 1, 15);

		memName.setFont(font);
		memAge.setFont(font);
		memPhone.setFont(font);
		memId.setFont(font);

		// 수정시 필드변환
		memNameF = new JTextField(10);
		memAgeF = new JTextField(10);
		memPhoneF = new JTextField(10);
		memIdF = new JTextField(10);
		memPassF = new JPasswordField(15);
		passF = new JPasswordField(15);
		passChkF = new JPasswordField(15);

		memPhoneF.setDocument(new JTextFieldLimit(11));
		passF.setDocument(new JTextFieldLimit(12));
		passChkF.setDocument(new JTextFieldLimit(12));
		
		// 개인 정보
		memInfo = new JPanel(new GridLayout(1, 4));
		memInfo.setSize(new Dimension(1200, 250));
		createTable(1);
		createTable(2);
		createTable(3);
		createTable(4);
		memInfo.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 2), "개인 정보"));

		// image = "./src/minproject/aven.jpg";

		/*
		 * try { image = ImageIO.read(new File("./src/minproject/aven.jpg")); } catch
		 * (IOException e) {
		 * 
		 * e.printStackTrace(); }
		 */
		reserv = new JPanel(new GridLayout(1, 4));
		reserv.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 2), "예매 내역"));
		reserv.setSize(new Dimension(1200, 250));
		
		if (reservCk == true) {
			// 예매 정보 패널 컨테이너
			JPanel reservImg = new JPanel(new BorderLayout());
			reservImg.add(new JLabel(new ImageIcon(image)));
			reservImg.setSize(new Dimension(300, 250));

			// 1의 경우 예매 내역 틀
			// 2의 경우 예매 정보
			createReservTable(1);
			createReservTable(2);

			reserv.add(reservImg);
			reserv.add(reservRecord);
			reserv.add(reservRecordSec);
			reserv.add(cancelReserv());
		} else {
			reserv.setLayout(new BorderLayout());
			JLabel reserv_not = new JLabel("예매한 내역이 없습니다.");
			reserv_not.setHorizontalAlignment(JLabel.CENTER);
			reserv_not.setFont(new Font("돋움", 3, 20));

			reserv.add(reserv_not, BorderLayout.CENTER);

		}

		// 쿠폰 내역 정보 패널 컨테이너
		JPanel coupon = new JPanel(new BorderLayout());

		coupon.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 2), "쿠폰내역"));

		coupon.setSize(new Dimension(1200, 250));

		// table model
		couponTable = new DefaultTableModel(couponCol, 4);

		table = new JTable(couponTable);
		table.setPreferredSize(new Dimension(800, 200));
		table.getTableHeader().setBackground(new Color(179, 218, 255));
		couponScroll = new JScrollPane(table);

		// setBackground(new Color(230, 255, 255));
		table.setRowHeight(50);
		table.getColumnModel().getColumn(0).setPreferredWidth(200);
		table.setBackground(new Color(230, 255, 255));
		table.getColumnModel().getColumn(1).setPreferredWidth(300);
		table.getColumnModel().getColumn(2).setPreferredWidth(300);
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);

		coupon.add("Center", couponScroll);
		// 전체 컨테이너
		JPanel contain = new JPanel(new GridLayout(3, 1));
		contain.setSize(1200, 800);
		contain.add(memInfo);
		contain.add(reserv);
		contain.add(coupon);

		// 마우스 이벤트

		infoUpdate.addActionListener(this);
		memExit.addActionListener(this);

		this.add(contain);

	}// end constructor

	// 비밀번호 체크
	private boolean checkPassword() {
		MemberDAO memDao = MemberDAO.getInstance();
		String[] str = new String[2];

		str[0] = test[3];
		str[1] = String.valueOf(memPassF.getPassword());

		return memDao.loginMethod(str);
	}

	// 화면에 정보 뿌려줌
	private void loadMyPage() {
		MemberDAO memDao = MemberDAO.getInstance();

		// 회원정보 받아옴
		MemberDTO memDto = memDao.myPageMethod(Main.myCode);
		test[0] = memDto.getMem_name();

		int bornYear = Integer.parseInt(String.valueOf(memDto.getMem_local()).substring(0, 2));
		Calendar cal = Calendar.getInstance();
		int currentYear = cal.get(Calendar.YEAR);
		if (bornYear >= currentYear / 100)
			bornYear += 1900;
		else
			bornYear += 2000;
		test[1] = String.valueOf(currentYear - bornYear + 1);
		String phone = memDto.getMem_phone();
		test[2] = phone.substring(0, 3) + "-" + phone.substring(3, 7) + "-" + phone.substring(7, 11);
		test[3] = memDto.getMem_id();

		// 예약정보 받아옴
		int book_code = memDao.bookCodeMethod(Main.myCode);

		if (book_code == 0) {
			reservCk = false;
		} else {
			reservCk = true;
			BookingDAO bookDao = BookingDAO.getInstance();
			BookingDTO bookDto = bookDao.myBookMethod(book_code);
			int mov_code = bookDto.getMov_code();

			MovieDAO movDao = MovieDAO.getInstance();
			MovieDTO movDto = movDao.movieMethod(mov_code);

			image = movDto.getMov_img();

			reservTest[0] = movDto.getMov_title();
			reservTest[1] = bookDto.getBook_room();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			reservTest[2] = sdf.format(bookDto.getBook_date());
			// 좌석 고려해야됨
			reservTest[3] = bookDto.getBook_seat();
			reservTest[4] ="";
			
		}
	}
	//예매 취소 버튼
	private JPanel cancelReserv() {
		JPanel jp = new JPanel(new GridLayout(4, 2));
		JPanel jpChild = new JPanel(new GridLayout(2, 2));
		jp.add(new JLabel(""));
		jp.add(new JLabel(""));
		jp.add(new JLabel(""));
		jp.add(new JLabel(""));
		jp.add(new JLabel(""));
		jp.add(new JLabel(""));
		jp.add(new JLabel(""));
		
		
		
		cancelReser = new JButton("취소");
		cancelReser.setFont(new Font("돋움", 1, 12));
		jpChild.add(cancelReser);
		jpChild.add(new JLabel(""));
		jpChild.add(new JLabel(""));
		jpChild.add(new JLabel(""));
		jp.add(jpChild);
		cancelReser.addActionListener(this);
		
		return jp;
				
	}
	
	private void changeInform() {
		// 휴대폰번호와 비밀번호만 변경되게 하면 어떨지
		MemberDAO dao = MemberDAO.getInstance();

		String[] str = new String[2];
		str[0] = test[2];
		str[1] = String.valueOf(passF.getPassword());
		dao.changeMethod(str);
	}

	private void createTable(int num) {

		if (num == 1) {
			memInfoFir = new JPanel(new GridLayout());
			try {
				imageInfo = ImageIO.read(new File("./src/minproject/img/index.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			memInfoFir.add(new JLabel(new ImageIcon(imageInfo)));
			memInfo.add(memInfoFir);

		} else if (num == 2) {
			memInfoSec = new JPanel(new GridLayout(6, 2));
			for (int i = 1; i <= 12; i++) {

				JLabel label = new JLabel("");
				label.setSize(new Dimension(150, 40));
				label.setOpaque(true);
				memInfoSec.add(label);

			}
			memInfo.add(memInfoSec);

		} else if (num == 3) {
			memInfoThr = new JPanel(new GridLayout(6, 2));
			memInfo.add(memInfoThr);
			memInfoFour = new JPanel(new GridLayout(6, 2));

			for (int i = 1; i <= 12; i++) {
				switch (i) {
				case 3:
					memName.setSize(new Dimension(150, 40));
					memName.setOpaque(true);
					memName.setBackground(new Color(179, 218, 255));
					memInfoThr.add(memName);
					break;
				case 4:

					labelText = new JLabel("        " + test[0]);
					labelText.setSize(new Dimension(150, 40));
					labelText.setOpaque(true);
					labelText.setBackground(new Color(230, 255, 255));
					memInfoThr.add(labelText);

					break;
				case 5:
					memAge.setSize(new Dimension(150, 40));
					memAge.setOpaque(true);
					memAge.setBackground(new Color(179, 218, 255));
					memInfoThr.add(memAge);

					break;
				case 6:

					labelText = new JLabel("        " + test[1]);
					labelText.setSize(new Dimension(150, 40));
					labelText.setOpaque(true);
					labelText.setBackground(new Color(230, 255, 255));
					memInfoThr.add(labelText);

					break;
				case 7:
					if (updating == true) {
						JLabel password_txt = new JLabel("     " + " 변경 비밀번호 :");
						password_txt.setFont(font);
						password_txt.setSize(new Dimension(150, 40));
						password_txt.setOpaque(true);
						password_txt.setBackground(new Color(179, 218, 255));
						memInfoThr.add(password_txt);

					} else {
						JLabel label = new JLabel(" ");
						label.setSize(new Dimension(150, 40));
						label.setOpaque(true);
						memInfoThr.add(label);
					}
					break;

				case 8:

					if (updating == true) {
						passF.setSize(new Dimension(150, 40));
						passF.setOpaque(true);
						memInfoThr.add(passF);

					} else {
						JLabel label = new JLabel(" ");
						label.setSize(new Dimension(150, 40));
						label.setOpaque(true);
						memInfoThr.add(label);
					}
					break;
				case 11:
					labelText = new JLabel("      " + "비밀번호 :");
					labelText.setSize(new Dimension(150, 40));
					labelText.setOpaque(true);
					labelText.setBackground(new Color(179, 218, 255));
					memInfoThr.add(labelText);
					break;
				case 12:
					if (updating == false) {
						memPassF.setSize(new Dimension(150, 40));
						memPassF.setOpaque(true);
						memInfoThr.add(memPassF);
					} else {
						JLabel label = new JLabel(" ");
						label.setSize(new Dimension(150, 40));
						label.setOpaque(true);
						memInfoThr.add(label);
					}

					break;
				default:
					JLabel label = new JLabel(" ");
					label.setSize(new Dimension(150, 40));
					label.setOpaque(true);
					memInfoThr.add(label);
				}

			}

		} else if (num == 4) {

			memInfo.add(memInfoFour);

			for (int i = 1; i <= 12; i++) {
				switch (i) {
				case 3:
					memId.setSize(new Dimension(150, 40));
					memId.setOpaque(true);
					memId.setBackground(new Color(179, 218, 255));
					memInfoFour.add(memId);
					break;
				case 4:

					labelText = new JLabel("        " + test[3]);
					labelText.setSize(new Dimension(150, 40));
					labelText.setOpaque(true);
					labelText.setBackground(new Color(230, 255, 255));
					memInfoFour.add(labelText);

					break;
				case 5:
					memPhone.setSize(new Dimension(150, 40));
					memPhone.setOpaque(true);
					memPhone.setBackground(new Color(179, 218, 255));
					memInfoFour.add(memPhone);
					break;
				case 6:
					if (updating == false) {
						labelText = new JLabel("        " + test[2]);
						labelText.setSize(new Dimension(150, 40));
						labelText.setOpaque(true);
						labelText.setBackground(new Color(230, 255, 255));
						memInfoFour.add(labelText);
					} else {
						memPhoneF.setText(test[2].replace("-",""));
						memInfoFour.add(memPhoneF);
					}

					break;
				case 7:
					if (updating == true) {
						JLabel password_txt = new JLabel(" 변경 비밀번호 확인 :");
						password_txt.setFont(font);
						password_txt.setSize(new Dimension(150, 40));
						password_txt.setOpaque(true);
						password_txt.setBackground(new Color(179, 218, 255));
						memInfoFour.add(password_txt);

					} else {
						JLabel label = new JLabel(" ");
						label.setSize(new Dimension(150, 40));
						label.setOpaque(true);
						memInfoFour.add(label);
					}
					break;

				case 8:

					if (updating == true) {
						passChkF.setSize(new Dimension(150, 40));
						passChkF.setOpaque(true);
						memInfoFour.add(passChkF);

					} else {
						JLabel label = new JLabel(" ");
						label.setSize(new Dimension(150, 40));
						label.setOpaque(true);
						memInfoFour.add(label);
					}
					break;
				case 11:
					// button
					infoUpdate = new JButton("수정하기");
					memInfoFour.add(infoUpdate);
					break;

				case 12:
					memExit = new JButton("회원 탈퇴");
					memInfoFour.add(memExit);
					break;

				default:
					JLabel label = new JLabel(" ");
					label.setSize(new Dimension(150, 40));
					label.setOpaque(true);
					memInfoFour.add(label);
				}

			}

		}

	}

	private void createReservTable(int chk) {
		if (chk == 1) {
			reservRecord = new JPanel(new GridLayout(5, 1));
			reservRecord.setSize(1000, 250);
		} else {
			reservRecordSec = new JPanel(new GridLayout(5, 1));
			reservRecordSec.setSize(1000, 250);
		}

		for (int i = 1; i <= 5; i++) {

			switch (i) {
			case 1:
				if (chk == 1) {

					reservRecord.add(reservName);
				} else {
					reservRecordSec.add(new JLabel(reservTest[0]));
				}
				break;

			case 2:
				if (chk == 1) {

					reservRecord.add(reservLocal);
				} else {
					reservRecordSec.add(new JLabel(reservTest[1]));
				}
				break;
			case 3:
				if (chk == 1) {

					reservRecord.add(reservDate);
				} else {
					reservRecordSec.add(new JLabel(reservTest[2]));
				}
				break;
			case 4:
				if (chk == 1) {

					reservRecord.add(reservCount);
				} else {
					reservRecordSec.add(new JLabel(reservTest[3]));
				}
				break;
			case 5:
				if (chk == 1) {

					reservRecord.add(new JLabel(""));
				} else {
					reservRecordSec.add(new JLabel(reservTest[4]));
				}
				break;
			}
		}

	}

	private void Exit_member() {
		int val = JOptionPane.showConfirmDialog(this, "정말 탈퇴하시겠습니까 ?", "회원탈퇴", JOptionPane.OK_CANCEL_OPTION);
		if (val == JOptionPane.OK_OPTION) {
			// 회원 탈퇴 처리
			MemberDAO dao = MemberDAO.getInstance();
			dao.exitMethod(Main.myCode);
			
			JOptionPane.showMessageDialog(this, "탈퇴 처리가 완료되었습니다.");
			
			Main.chk = false;
			Main.pointLabel.setText("");
			Main.logInOutBtn.setText("로그인");
			Main.jtp.setSelectedIndex(0);
			Main.myCode = 0;
			Main.changePane(Main.moviePane, "movie");
		} else {
			return;
		}
	}

	private void Cancel_movie() {
		int val = JOptionPane.showConfirmDialog(this, "정말 취소하시겠습니까 ?", "예매취소", JOptionPane.OK_CANCEL_OPTION);
		if (val == JOptionPane.OK_OPTION) {
			// 예매 취소 처리
			MemberDAO memDao = MemberDAO.getInstance();
			int book_code = memDao.bookCodeMethod(Main.myCode);

			int point=Integer.parseInt(Main.pointLabel.getText().split("P")[0]);

			BookingDAO bookDao = BookingDAO.getInstance();
			
			point += bookDao.priceMethod(book_code);
			Main.pointLabel.setText(String.valueOf(point) + "P");
			
			int mem_data[] = new int[2];
			mem_data[0] = point;
			mem_data[1] = Main.myCode;
			memDao.chargeMethod(mem_data);
			
			bookDao.cancelMethod(book_code);
			
			memDao.cancelMethod(Main.myCode);
			
			SeatDAO seatDao = SeatDAO.getInstance();
			seatDao.cancelMethod(Main.myCode);
			
			JOptionPane.showMessageDialog(this, "예매를 취소했습니다.");
			
			Main.jtp.setSelectedIndex(2);
			Main.changePane(Main.myPane, "my");
			
		} else {
			return;
		}		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();

		if (obj.equals(infoUpdate)) {
			if (updating == false) {
				// 비밀번호 체크하는 부분
				if (!checkPassword()) {
					JOptionPane.showMessageDialog(this, "비밀번호가 틀렸습니다.");
					return;
				}
				updating = true;
				memInfo.removeAll();
				createTable(1);
				createTable(2);
				createTable(3);
				createTable(4);
				infoUpdate.setText("수정완료");
				infoUpdate.addActionListener(this);

			} else if (updating == true) {

				int val = JOptionPane.showConfirmDialog(this, "정말 수정하시겠습니까?", "수정", JOptionPane.OK_CANCEL_OPTION);
				updating = false;
				if (val == JOptionPane.OK_OPTION) {
					memPassF.setText("");
					test[2] = memPhoneF.getText();
					// 공백 입력해도 저장됨
					for (String i : test) {
						infoCheck = infoChk.matcher(i);
						if (infoCheck.matches()) {
							JOptionPane.showMessageDialog(this, "빈칸없이 다시 입력해 주세요");
							return;
						}
					}

					memInfo.removeAll();
					test[2] = memPhoneF.getText().substring(0, 3) + "-" + memPhoneF.getText().substring(3, 7) + "-" + memPhoneF.getText().substring(7, 11);
					createTable(1);
					createTable(2);
					createTable(3);
					createTable(4);

					changeInform();

					infoUpdate.addActionListener(this);
				} else {
					return;

				}
			}

			memInfo.revalidate();
			memInfo.repaint();
		} else if (obj == memExit) {
			if (updating == false) {
				Exit_member();
			}
		} else if (obj == cancelReser) {
			if (updating == false) {
				Cancel_movie();
			}
		}
	}
}