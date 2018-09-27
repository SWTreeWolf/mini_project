package minproject;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import minproject.JDBC.MovieDAO;
import minproject.JDBC.MovieDTO;

@SuppressWarnings("serial")
public class MoviePanel extends JPanel implements ActionListener{
	JPanel movie_P;
	JScrollPane movie_SP;
	JLabel jojo, night;
	JButton[] movie_B;
	JTabbedPane movie_tabbed_P;
	JButton releasedBtn;
	
	private final int MAX_MOVIE_CNT = 8;
	
	public MoviePanel() {
		setLayout(new GridLayout(1, 1));
		movie_tabbed_P = new JTabbedPane();
		movie_P = new JPanel();
		movie_SP = new JScrollPane();
		movie_SP.setBackground(Ticketing.color);
		movie_B = new JButton[MAX_MOVIE_CNT];
		movie_P.setLayout(new GridLayout(12,1,0,1));
		movie_P.setBackground(Ticketing.color);
		movie_P.setBorder(new LineBorder(Color.BLACK, 2));
		
		MovieDAO dao = MovieDAO.getInstance();
		List<MovieDTO> movList = dao.loadMethod();
		
		for (int i = 0; i < MAX_MOVIE_CNT; i++) {
			movie_B[i] = new JButton(movList.get(i).getMov_title());
			movie_B[i].setName("movie");
			movie_B[i].setBackground(Ticketing.color);
			movie_B[i].addActionListener(this);
			movie_B[i].setFocusPainted(false);
			movie_P.add(movie_B[i]);
		}
		
		movie_SP.setViewportView(movie_P);
		movie_SP.getVerticalScrollBar().setUnitIncrement(16);
		movie_tabbed_P.add("순위",movie_SP);
		add(movie_tabbed_P);
	}// end moviePanel()
	
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
	}//end actionPerformed()
	
}//end class
