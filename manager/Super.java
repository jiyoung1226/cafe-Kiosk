package manager;

import java.util.Scanner;

public abstract class Super {
	private String name;
	private int price;
	private String tag;
	private String type;

	public Super() {

	}

	public void register() {
		Scanner s = new Scanner(System.in);
		System.out.println("메뉴명을 입력하세요");
		String name = s.nextLine();
		System.out.println("가격을 입력하세요");
		int price = s.nextInt();
		s.nextLine();
		System.out.println("1.Best 2.New  3.SoldOut 4.없음");
		int num = s.nextInt();
		s.nextLine();
		String tag = null;
		switch (num) {
		case 1:
			this.setTag("Best");
			break;
		case 2:
			this.setTag("New");
			break;
		case 3:
			this.setTag("SoldOut");
			break;
		case 4:
			this.setTag(null);
			break;
		}
		this.setName(name);
		this.setPrice(price);
	}

	public void prt() {
		System.out.println(this.type);
		System.out.println(this.tag);
		System.out.println("메뉴명 :" + this.name);
		System.out.println("가격 :" + this.price);

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
