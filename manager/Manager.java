package manager;

import java.util.Scanner;
import main.KioskAdmin;

public class Manager {
	MenuAdmin menuadmin = new MenuAdmin();
	UserAdmin useradmin = new UserAdmin();
	ManagerInfo managerinfo = new ManagerInfo();

	public Manager() {

	}

	public void check() {
		Scanner s = new Scanner(System.in);
		System.out.println("관리자 코드를 입력하세요");
		String code = s.nextLine();
		if (code.equals(managerinfo.getCode())) {
			menu();
		} else {
			System.out.println("틀렸습니다.다시입력해주세요.");
		}

	}

	private void menu() {
		Scanner s = new Scanner(System.in);
		boolean flag = true;
		while (flag) {
			System.out.println("1.메뉴관리  2.회원관리  3.주문내역조회  4.일일매출  5.뒤로가기");
			int num = s.nextInt();
			s.nextLine();
			switch (num) {
			case 1:
				menuadmin.menu();
				break;
			case 2:
				useradmin.menu();
				break;
			case 3:
				managerinfo.dailySales();
				break;
			case 4:
				managerinfo.orderList();
				break;
			case 5:
				new KioskAdmin();
				break;

			}

		}

	}
}
