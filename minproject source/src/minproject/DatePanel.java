package minproject;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class DatePanel extends JPanel implements ActionListener {
	final int MONTH_CNT = 1; // month count
	final int MAX_DATE_CNT = 30;
	
	JTabbedPane jtp;
	JScrollPane js2;
	JPanel jp2;
	JButton[] date_2;
	String[] day = {"일","월","화","수","목","금","토"};
	JButton releasedBtn;

	//List<String> month1, month2, month3; // 4,5,6월에 대한 것들을 넣기 위함
	
	public DatePanel() {
		setLayout(new GridLayout(1, 1));
		Calendar cal = Calendar.getInstance();
		cal.set(2018, 4, 1);
		
		jtp = new JTabbedPane();
		
		jp2 = new JPanel(new GridLayout(30, 1, 0, 5));
		jp2.setBackground(new Color(255,255,230));
		js2 = new JScrollPane(jp2);
		js2.getVerticalScrollBar().setUnitIncrement(16);
		jtp.addTab("5월", js2);
		
		add(jtp);
	}
	
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
	}
}// end class
