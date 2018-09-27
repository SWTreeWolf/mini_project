package minproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Reservation extends JPanel{
	public Reservation() {
		setLayout(new BorderLayout());
		Ticketing T_panel = new Ticketing();
		SeatPanel seatPanel = new SeatPanel(T_panel);
		add(T_panel,BorderLayout.CENTER);
		add(seatPanel,BorderLayout.SOUTH);
	}//end MainFrame()
}//end class
