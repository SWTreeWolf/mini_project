package minproject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.LineBorder;

import java.util.List;

import minproject.JDBC.MovieDAO;
import minproject.JDBC.MovieDTO;

@SuppressWarnings("serial")
public class Movie extends JPanel implements ActionListener {
	JLabel genreLabel, movieLabel, chartLabel;
	JPanel moviePane, genrePane, chartPane;
	JPanel[] panes_movie;
	JButton[] btns_movie;
	JButton[] btns_genre;
	Font genreFont, movieFont, chartFont;
	List<MovieDTO> movList;
	GridBagConstraints gbc;
	String[] genreName = {"드라마","판타지","애니메이션","다큐멘터리","전체"};
	JButton selectedBtn;

	private final Color color = new Color(85,85,85);
	private final int max = 8;
	
	public Movie() {
		clearMovie();
		loadAllMovie();

		moviePane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		movieFont = new Font("굴림", Font.BOLD, 30);// 영화폰트
		movieLabel = new JLabel("\r\n영화\n");// 라벨변수에저장
		movieLabel.setFont(movieFont);// 영화폰트지정
		
		moviePane.setBackground(new Color(205, 193, 151)); // 백그라운드 색상 변환
		moviePane.add(movieLabel);
		moviePane.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		genrePane = new JPanel();
		genreFont = new Font("굴림", Font.BOLD, 15);// 장르폰트
		genreLabel = new JLabel(" 장르 >>");// 장르 라벨 변수에 저장
		genreLabel.setFont(genreFont);
		genrePane.add(genreLabel);// 장르
		
		btns_genre = new JButton[genreName.length];
		
		for(int i=0; i<btns_genre.length; i++) {
			btns_genre[i] = new JButton();
			btns_genre[i].setName("genre");
			btns_genre[i].setPreferredSize(new Dimension(120,30));
			btns_genre[i].setFocusPainted(false);
			btns_genre[i].setText(genreName[i]);
			genrePane.add(btns_genre[i]);
			btns_genre[i].addActionListener(this);
		}
		genrePane.setBackground(new Color(255,255,230));
		genrePane.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		chartPane = new JPanel(new FlowLayout(FlowLayout.LEFT));// 첫번째칸 세번째 그리드
		chartPane.setBackground(new Color(255,255,230)); // 백그라운드 컬러 설정
		chartFont = new Font("굴림", Font.BOLD, 30);// 영화폰트
		chartLabel = new JLabel("     무비차트\n");// 라벨변수에저장
		chartLabel.setFont(chartFont);// 영화폰트지정
		chartPane.add(chartLabel);// 세번째칸 자주색
		chartPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		
		setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.gridwidth = 4;
		add(moviePane, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 4;
		add(genrePane, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 4;
		add(chartPane, gbc);
		
		renewMovie();
	}
	
	private void renewMovie() {
		if(getComponentCount() > 4)
			for(int i=max+2; i>=3; i--)
				remove(i);
		
		for(int i=0; i<max; i++) {
			if(i<4) {
				gbc.gridx = i;
				gbc.gridy = 3;
			} else if(i<8) {
				gbc.gridx = i-4;
				gbc.gridy = 4;
			} else {
				gbc.gridx = i-8;
				gbc.gridy = 5;
			}
			gbc.gridwidth = 1;
			gbc.weighty = 1;
			add(panes_movie[i], gbc);
		}
		
		revalidate();
		repaint();
	}

	// 프로그램 실행 시 영화 리스트 초기화
	private void clearMovie() {
		MovieDAO dao = MovieDAO.getInstance();
		if(dao.loadMethod().size()==0) {
			Crawling craw = new Crawling();
			dao.saveMethod(craw.arr);
		}
		//dao.clearMethod();
	}
	
	// 모든 영화리스트 받아옴
	private void loadAllMovie() {
		MovieDAO dao = MovieDAO.getInstance();
		movList = dao.loadMethod();
		
		insertImage();
	}
	
	// 특정 장르의 영화 리스트 받아옴
	private void loadGenreMovie(String genre) {
		MovieDAO dao = MovieDAO.getInstance();
		movList = dao.genreMethod(genre);
		
		insertImage();
		renewMovie();
	}
	
	// 버튼에 이미지 및 MovieDTO 객체 넣어줌
	private void insertImage() {
		int listSize = movList.size();
		btns_movie = new JButton[max];
		panes_movie = new JPanel[max];
		
		for(int i=0; i<max; i++) {
			if(i<listSize) {
				btns_movie[i] = new JButton(new ImageIcon(movList.get(i).getMov_img()));
				btns_movie[i].setBackground(null);
				btns_movie[i].setBorder(new LineBorder(Color.BLACK, 3));
				btns_movie[i].setFocusPainted(false);
				btns_movie[i].setName(String.valueOf(movList.get(i).getMov_code()));
				panes_movie[i] = new JPanel(new GridBagLayout());
				GridBagConstraints gbc = new GridBagConstraints();
				panes_movie[i].add(btns_movie[i], gbc);
				panes_movie[i].setBackground(color);
				btns_movie[i].addActionListener(this);
			} else {
				panes_movie[i] = new JPanel(new GridBagLayout());
				GridBagConstraints gbc = new GridBagConstraints();
				panes_movie[i].add(new JButton(new ImageIcon("src/minproject/img/dummy.png")), gbc);
				JButton btn = (JButton) panes_movie[i].getComponent(0);
				btn.setBackground(color);
				btn.setBorder(new LineBorder(color, 3));
				panes_movie[i].setBackground(color);
			}
		}
	}
	
	// 이런식으로 바꿔야함
	private void showMovieInform(JButton btn) {
		int mov_code = Integer.parseInt(btn.getName());
			
		MovieDAO dao = MovieDAO.getInstance();
		MovieDTO dto = dao.movieMethod(mov_code);
			
		String title = dto.getMov_title();
			
		JFrame subWin = new JFrame(title);

		// 첫번째칸
		JPanel movieInfo = new JPanel(new GridLayout(1, 2));
		// 첫번째칸의 첫번째열
		JLabel imageLabel = new JLabel(btn.getIcon());

		// 첫번째칸의 두번째열
		JPanel moinforma = new JPanel(new GridLayout(10, 1));
		moinforma.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		chartFont = new Font("굴림", Font.BOLD, 15);
		JLabel bar1Label = new JLabel("------------------");
		bar1Label.setFont(chartFont);
		bar1Label.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		JLabel titleLabel = new JLabel(title, SwingConstants.LEFT);
		titleLabel.setFont(chartFont);
		//titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 30));

		JLabel bar2Label = new JLabel("------------------");
		bar2Label.setFont(chartFont);
		bar2Label.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

		JLabel timeLabel = new JLabel(String.format("상영시간 : %d시간", dto.getMov_time()));
		JLabel castingLabel = new JLabel(String.format("출연진 : %s", dto.getMov_actor()));
		JLabel directorLabel = new JLabel(String.format("감독 : %s", dto.getMov_director()));
		JLabel optionLabel = new JLabel(String.format("연령제한 : %s", dto.getMov_age()));
		JLabel genrLabel = new JLabel(String.format("장르 : %s", dto.getMov_genre()));

		moinforma.add(bar1Label);
		moinforma.add(titleLabel);
		moinforma.add(bar2Label);
		moinforma.add(timeLabel);
		moinforma.add(castingLabel);
		moinforma.add(directorLabel);
		moinforma.add(optionLabel);
		moinforma.add(genrLabel);

		movieInfo.add(BorderLayout.CENTER, imageLabel);
		movieInfo.add(moinforma);

		////////////////// 두번째칸
		JPanel scenario = new JPanel(new GridLayout(10, 1));
		JLabel empty = new JLabel("");
		
		JLabel scen = new JLabel("줄거리");
		scen.setBorder(BorderFactory.createEmptyBorder(0,170,0,0));
		
		JLabel s1=new JLabel(String.format("%s", dto.getMov_scenario().substring(0, 31)));
		s1.setBorder(BorderFactory.createEmptyBorder(0,30,0,0));
		
		JLabel s2=new JLabel(String.format("%s", dto.getMov_scenario().substring(31, 62)));
		s2.setBorder(BorderFactory.createEmptyBorder(0,30,0,0));
		
		JLabel s3=new JLabel(String.format("%s · · ·", dto.getMov_scenario().substring(62, 87)));
		s3.setBorder(BorderFactory.createEmptyBorder(0,30,0,0));
		
		scenario.add(empty);
		scenario.add(scen);
		scenario.add(empty);
		scenario.add(s1);
		scenario.add(s2);
		scenario.add(s3);
		
		moinforma.setBackground(new Color(205, 193, 151));
		movieInfo.setBackground(new Color(205, 193, 151));
		scenario.setBackground(new Color(255,255,230));
		
		subWin.add(movieInfo);
		subWin.add(scenario);

		subWin.setLayout(new GridLayout(2, 1));
		subWin.setSize(400, 600);
		subWin.setResizable(false);
		subWin.setVisible(true);
		subWin.setLocationRelativeTo(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if(obj instanceof JButton) {
			JButton btn = (JButton) obj;
			if(btn.getName().equals("genre"))
				if(btn.getText().equals("전체")) {
					loadAllMovie();
					renewMovie();
				}
				else
					loadGenreMovie(btn.getText());
			else {
				if(selectedBtn!=null)
					selectedBtn.setBorder(new LineBorder(Color.BLACK, 3));
				selectedBtn = btn;
				btn.setBorder(new LineBorder(Color.RED, 3));
				showMovieInform(btn);
			}
		}

	}// end actionPerformed
}