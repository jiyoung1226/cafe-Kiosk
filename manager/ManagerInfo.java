package manager;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class ManagerInfo {
	private String code = "1234"; // 관리자 코드
	private OrderDAO orderdao = OrderDAO.getInstance();
	private SalesDAO salesdao = SalesDAO.getInstance();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void dailySales() {
		ArrayList<OrderDTO> orders = orderdao.select(); // 주문 목록 가져오기
		if (orders.isEmpty()) {
			System.out.println("등록된 주문이 없습니다.");
			return;
		}

		HashMap<LocalDate, Integer> salesMap = new HashMap<>(); // 날짜별 매출 저장할 맵

		for (OrderDTO order : orders) {
			LocalDate orderDate = order.getIndate().toLocalDateTime().toLocalDate(); // 날짜 변환// 주문 날짜 가져오기
			int totalPrice = order.getTotalprice(); // 주문 총 가격 가져오기

			// 해당 날짜의 매출이 이미 있으면 더하고, 없으면 새로 추가
			salesMap.put(orderDate, salesMap.getOrDefault(orderDate, 0) + totalPrice);
		}

		// 매출 출력
		System.out.println("===== 일별 매출 내역 =====");
		for (LocalDate date : salesMap.keySet()) {
			System.out.println("날짜: " + date + " | 총 매출: " + salesMap.get(date) + "원");
		}

	}

	public ManagerInfo() {

	}

	public void orderList() {
		ArrayList<OrderDTO> order = orderdao.select();
		System.out.println("===== 주문내역 =====");
		for (OrderDTO odto : order) {
			System.out.println("메뉴이름: " + odto.getName());
			System.out.println("단가: " + odto.getPrice());
			System.out.println("수량:" + odto.getAmount());
			System.out.println("총가격: " + odto.getTotalprice());
			System.out.println("주문번호: " + odto.getOrderNum());
			System.out.println("구매시간: " + odto.getIndate());
			System.out.println();

		}

	}

}
