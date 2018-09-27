package minproject;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SeatPanel extends JPanel implements ActionListener{
	JButton seat_Btn;
	Ticketing tic;

	public SeatPanel(Ticketing tic) {
		this.tic=tic;
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		seat_Btn = new JButton("좌석선택");
		seat_Btn.addActionListener(this);
		add(seat_Btn);
	}//end SeatPanel()
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object obj = e.getSource();
		
		if(obj.equals(seat_Btn)) {
			if(tic.label[0].getText().equals("null"))
				JOptionPane.showMessageDialog(tic, "영화를 선택하세요");
			else if(tic.label[1].getText().equals("null"))
				JOptionPane.showMessageDialog(tic, "날짜를 선택하세요");
			else if(tic.label[2].getText().equals("null"))
				JOptionPane.showMessageDialog(tic, "시간을 결정하세요");
			else createSeatPanel();
		}
	}

	public void createSeatPanel() {
		new Seat(tic);
	}//end createSeatPanel()
}// end class
