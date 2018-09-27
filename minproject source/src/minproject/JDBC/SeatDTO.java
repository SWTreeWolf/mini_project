package minproject.JDBC;

import java.sql.Date;

public class SeatDTO {
	private int seat_code;
	private Date seat_date;
	private String seat_time;
	private String seat_room;
	private String seat_seat;
	private int seat_check;
	private int mem_code;

	public SeatDTO() {
		
	}

	public int getSeat_code() {
		return seat_code;
	}

	public void setSeat_code(int seat_code) {
		this.seat_code = seat_code;
	}

	public Date getSeat_date() {
		return seat_date;
	}

	public void setSeat_date(Date seat_date) {
		this.seat_date = seat_date;
	}

	public String getSeat_time() {
		return seat_time;
	}

	public void setSeat_time(String seat_time) {
		this.seat_time = seat_time;
	}

	public String getSeat_room() {
		return seat_room;
	}

	public void setSeat_room(String seat_room) {
		this.seat_room = seat_room;
	}

	public int getSeat_check() {
		return seat_check;
	}

	public void setSeat_check(int seat_check) {
		this.seat_check = seat_check;
	}
	
	public String getSeat_seat() {
		return seat_seat;
	}

	public void setSeat_seat(String seat_seat) {
		this.seat_seat = seat_seat;
	}
	
	public int getMem_code() {
		return mem_code;
	}

	public void setMem_code(int mem_code) {
		this.mem_code = mem_code;
	}
}
