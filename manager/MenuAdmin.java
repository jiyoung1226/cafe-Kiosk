package manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuAdmin {

	private MenuDAO menudao = MenuDAO.getInstance();
	private Data datapoi = new Data();

	public MenuAdmin() {

	}

	public void menu() {
		Scanner s = new Scanner(System.in);
		boolean flag = true;
		while (flag) {
			System.out.println("1.메뉴등록  2.메뉴조회  3.메뉴검색  4.뒤로가기 ");
			int num = s.nextInt();
			s.nextLine();
			switch (num) {
			case 1:
				menuRegister();
				break;
			case 2:
				menuView();
				break;
			case 3:
				menuSearch();
				break;
			case 4:
				flag=false;	
				break;
			default:
				System.out.println("올바른 번호를 입력해주세요.");
				return;
			}break;
		}
	}

	private void menuView() {
		ArrayList<Super> menuList = menudao.select();
		for (Super ss : menuList) {
			ss.prt();
		}

	}

	private void menuSearch() {
		Scanner s = new Scanner(System.in);
		System.out.println("검색하고 싶은 메뉴명을 입력하세요");
		String searchname = s.nextLine();
		ArrayList<Super> menulist = menudao.search(searchname);
		for (Super ss : menulist) {
			ss.prt();
		}
		searchmenu(searchname);

	}

	private void searchmenu(String m_name) {
		Scanner s = new Scanner(System.in);
		boolean flag = true;
		while (flag) {
			System.out.println("1.수정 2.삭제 3.뒤로가기");
			int a = s.nextInt();
			s.nextLine();
			switch (a) {
			case 1:
				 menuModify();
				break;
			case 2:
				menuDelete();
				break;
			case 3:
				menu();
				break;
			default:
				System.out.println("잘못된 입력입니다.");
				return;
			}
			break;
		}
	}

	private void menuDelete() {
		Scanner s = new Scanner(System.in);
		System.out.println("삭제할 메뉴명을 입력하세요");
		String deletename = s.nextLine();
		menudao.delete(deletename);
		System.out.println("삭제가 완료되었습니다");
		// 엑셀 업로드
		try {
			datapoi.downloadExcel();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	private void menuModify() {
		Scanner s = new Scanner(System.in);
		System.out.println("수정할 메뉴명을 입력하세요");
		String modname = s.nextLine();
		menudao.update(modname);
		// 엑셀 업로드
		try {
			datapoi.downloadExcel();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void menuRegister() {
		Scanner s = new Scanner(System.in);
		boolean flag = true;
		Super sup = null;
		while (flag) {
			System.out.println("1.커피  2.논커피  3.스무디  4.에이드  5.티  6.디저트 7.등록종료");
			int num = s.nextInt();
			s.nextLine();
			switch (num) {
			case 1:
				sup = new Coffee();
				break;
			case 2:
				sup = new NonCoffee();
				break;
			case 3:
				sup = new Smoothie();
				break;
			case 4:
				sup = new Ade();
				break;
			case 5:
				sup = new Tea();
				break;
			case 6:
				sup = new Dessert();
				break;
			case 7:
				menu();
			}
			sup.register();
			menudao.insert(sup);
			// 엑셀 업로드
			try {
				datapoi.downloadExcel();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

}
