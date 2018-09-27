package minproject.JDBC;

import java.sql.Date;

public class BookingDTO {
	private int book_check;
	private int book_code;
	private String book_room;
	private Date book_date;
	private String book_time;
	private String book_seat;
	private int book_count;
	private int book_price;

	private int mov_code;
	
	public BookingDTO() {
		
	}
	
	public int getBook_check() {
		return book_check;
	}

	public void setBook_check(int book_check) {
		this.book_check = book_check;
	}

	public int getBook_code() {
		return book_code;
	}

	public void setBook_code(int book_code) {
		this.book_code = book_code;
	}

	public String getBook_room() {
		return book_room;
	}

	public void setBook_room(String book_room) {
		this.book_room = book_room;
	}

	public Date getBook_date() {
		return book_date;
	}

	public void setBook_date(Date book_date) {
		this.book_date = book_date;
	}

	public String getBook_time() {
		return book_time;
	}

	public void setBook_time(String book_time) {
		this.book_time = book_time;
	}

	public String getBook_seat() {
		return book_seat;
	}

	public void setBook_seat(String book_seat) {
		this.book_seat = book_seat;
	}

	public int getBook_count() {
		return book_count;
	}

	public void setBook_count(int book_count) {
		this.book_count = book_count;
	}

	public int getBook_price() {
		return book_price;
	}

	public void setBook_price(int book_price) {
		this.book_price = book_price;
	}

	public int getMov_code() {
		return mov_code;
	}

	public void setMov_code(int mov_code) {
		this.mov_code = mov_code;
	}
}
