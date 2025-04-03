package manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class UserAdmin {
	private UserDAO userdao = UserDAO.getInstance();
	private Data datapoi = new Data();

	public UserAdmin() {

	}

	public void menu() {
		Scanner s = new Scanner(System.in);
		boolean flag = true;
		UserDAO userdao = new UserDAO();
		while (flag) {
			System.out.println("1.회원조회  2.회원검색  3.뒤로가기  ");
			int num = s.nextInt();
			s.nextLine();
			switch (num) {
			case 1:
				userView();
				break;
			case 2:
				userSearch();
				break;
			case 3:
				flag = false;
				break;
			default:
				System.out.println("올바른 번호를 입력해주세요.");
			}
		}
	}

	private void userView() {
		ArrayList<UserDTO> userList = userdao.select();
		for (UserDTO u : userList) {
			System.out.println("전화번호 :" + u.getPhoneNum());
			System.out.println("스탬프 :" + u.getStamp() + "개");
		}
	}

	private void userSearch() {
		Scanner s = new Scanner(System.in);
		System.out.println("검색할 번호를 입력하세요");
		String searchPhoneNum = s.nextLine();
		ArrayList<UserDTO> userList = userdao.search(searchPhoneNum);
		for (UserDTO u : userList) {
			System.out.println("전화번호 :" + u.getPhoneNum());
			System.out.println("스탬프 :" + u.getStamp() + "개");
		}
		userSearchmenu(searchPhoneNum);
	}

	private void userDelete() {
		Scanner s = new Scanner(System.in);
		System.out.println("삭제할번호를 입력하세요");
		String deletePhoneNum = s.nextLine();
		userdao.delete(deletePhoneNum);
		// 엑셀 업로드
		try {
			datapoi.downloadExcel();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void userModify() {
		Scanner s = new Scanner(System.in);
		System.out.println("수정할 번호를 입력하세요");
		String modPhoneNum = s.nextLine();
		userdao.update(modPhoneNum);
		// 엑셀 업로드
		try {
			datapoi.downloadExcel();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void userSearchmenu(String searchPhoneNum) {
		Scanner s = new Scanner(System.in);
		boolean flag = true;
		while (flag) {
			System.out.println("1.수정 2.삭제 3.뒤로가기");
			int num = s.nextInt();
			s.nextLine();
			switch (num) {
			case 1:
				userModify();
				break;
			case 2:
				userDelete();
				break;
			case 3:
				flag = false;
				break;
			default:
				System.out.println("잘못된 입력입니다.");
				return;
			}
		}

	}
}
