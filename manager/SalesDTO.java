package manager;

import java.sql.Date;

public class SalesDTO {
	private int t_Price;
	private Date indate;

	public int getT_Price() {
		return t_Price;
	}

	public void setT_Price(int t_Price) {
		this.t_Price = t_Price;
	}

	public Date getIndate() {
		return indate;
	}

	public void setIndate(Date indate) {
		this.indate = indate;
	}

	public SalesDTO() {
		// TODO Auto-generated constructor stub
	}

}
