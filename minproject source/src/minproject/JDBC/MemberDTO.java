package minproject.JDBC;

public class MemberDTO {
	private int mem_check;
	private int mem_code;
	private String mem_name;
	private int mem_local;
	private String mem_phone;
	private String mem_id;
	private String mem_pw;
	private int point;
	private int book_code;
	
	public MemberDTO() {
		
	}
	
	public int getMem_check() {
		return mem_check;
	}

	public void setMem_check(int mem_check) {
		this.mem_check = mem_check;
	}

	public int getMem_code() {
		return mem_code;
	}

	public void setMem_code(int mem_code) {
		this.mem_code = mem_code;
	}

	public String getMem_name() {
		return mem_name;
	}

	public void setMem_name(String mem_name) {
		this.mem_name = mem_name;
	}

	public int getMem_local() {
		return mem_local;
	}

	public void setMem_local(int mem_local) {
		this.mem_local = mem_local;
	}

	public String getMem_phone() {
		return mem_phone;
	}

	public void setMem_phone(String mem_phone) {
		this.mem_phone = mem_phone;
	}

	public String getMem_id() {
		return mem_id;
	}

	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}

	public String getMem_pw() {
		return mem_pw;
	}

	public void setMem_pw(String mem_pw) {
		this.mem_pw = mem_pw;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getBook_code() {
		return book_code;
	}

	public void setBook_code(int book_code) {
		this.book_code = book_code;
	}
}
