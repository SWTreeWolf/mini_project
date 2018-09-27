package minproject.JDBC;

public class PayDTO {
	private int pay_check;
	private int pay_code;
	private String pay_credit;
	private int pay_point;
	private int mem_code;
	
	public PayDTO() {
		
	}
	
	public int getPay_check() {
		return pay_check;
	}

	public void setPay_check(int pay_check) {
		this.pay_check = pay_check;
	}

	public int getPay_code() {
		return pay_code;
	}

	public void setPay_code(int pay_code) {
		this.pay_code = pay_code;
	}

	public String getPay_credit() {
		return pay_credit;
	}

	public void setPay_credit(String pay_credit) {
		this.pay_credit = pay_credit;
	}
	
	public int getPay_point() {
		return pay_point;
	}

	public void setPay_point(int pay_point) {
		this.pay_point = pay_point;
	}

	public int getMem_code() {
		return mem_code;
	}

	public void setMem_code(int mem_code) {
		this.mem_code = mem_code;
	}
}
