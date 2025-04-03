package manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Data {

	private static UserDAO userdao = UserDAO.getInstance();
	private static OrderDAO orderdao = OrderDAO.getInstance();
	private static MenuDAO menudao = MenuDAO.getInstance();
	private static SalesDAO salesdao = SalesDAO.getInstance();

	public Data() {

	}

	public void downloadExcel() throws IOException {
		// UserDAO 객체 생성하여 데이터베이스에서 유저 목록 가져오기
		List<UserDTO> userList = userdao.select(); // DB에서 유저 목록 가져오기
		ArrayList<OrderDTO> orderList = orderdao.select();
		List<Super> menuList = menudao.select();
		List<SalesDTO> salesList = salesdao.select();
		// 파일을 저장할 폴더 경로 설정
		String folderPath = "C:\\excel";
		File folder = new File(folderPath);
		if (!folder.exists()) {
			folder.mkdirs(); // 폴더가 없으면 생성
		}

		// 저장할 파일 경로 (확장자 .xls로 설정)
		String filePath = folderPath + "\\CafeData.xls";

		// 워크북 및 파일 저장
		try (Workbook workbook = new HSSFWorkbook(); 
				FileOutputStream fileOut = new FileOutputStream(filePath)) {
			Sheet userSheet = workbook.createSheet("회원 목록");
			Sheet orderSheet = workbook.createSheet("주문 목록");
			Sheet menuSheet = workbook.createSheet("메뉴 목록");
			Sheet priceSheet = workbook.createSheet("일일 매출");

			int userRowNo = 0;
			int orderRowNo = 0;
			int menuRowNo = 0;
			int priceRowNo = 0;

			// 회원헤더 생성
			Row userHeaderRow = userSheet.createRow(userRowNo++);
			userHeaderRow.createCell(0).setCellValue("핸드폰번호");
			userHeaderRow.createCell(1).setCellValue("스탬프갯수");

			// 주문목록헤더생성
			Row orderHeaderRow = orderSheet.createRow(orderRowNo++);
			orderHeaderRow.createCell(0).setCellValue("메뉴");
			orderHeaderRow.createCell(1).setCellValue("가격");
			orderHeaderRow.createCell(2).setCellValue("구매개수");
			orderHeaderRow.createCell(3).setCellValue("총가격");
			orderHeaderRow.createCell(4).setCellValue("주문번호");
			orderHeaderRow.createCell(5).setCellValue("구매시간");

			// 메뉴헤더생성
			Row menuHeaderRow = menuSheet.createRow(menuRowNo++);
			menuHeaderRow.createCell(0).setCellValue("메뉴이름");
			menuHeaderRow.createCell(1).setCellValue("단가");
			menuHeaderRow.createCell(2).setCellValue("태그");
			menuHeaderRow.createCell(3).setCellValue("타입");

			// 매출헤더생성
			Row priceHeaderRow = priceSheet.createRow(priceRowNo++);
			priceHeaderRow.createCell(0).setCellValue("매출");
			priceHeaderRow.createCell(1).setCellValue("날짜");

			// DB에서 가져온 데이터로 엑셀에 데이터 입력(회원)
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd a HH:mm:ss");
			for (UserDTO user : userList) {
				Row userRow = userSheet.createRow(userRowNo++);
				userRow.createCell(0).setCellValue(user.getPhoneNum());
				userRow.createCell(1).setCellValue(user.getStamp());
			}

			// DB에서 가져온 데이터로 엑셀에 데이터 입력(주문목록)
			for (OrderDTO odto : orderList) {
				Row orderRow = orderSheet.createRow(orderRowNo++);
				orderRow.createCell(0).setCellValue(odto.getName());
				orderRow.createCell(1).setCellValue(odto.getPrice());
				orderRow.createCell(2).setCellValue(odto.getAmount());
				orderRow.createCell(3).setCellValue(odto.getTotalprice());
				orderRow.createCell(4).setCellValue(odto.getOrderNum());
				orderRow.createCell(5).setCellValue(simpleDateFormat.format(odto.getIndate()));
			}

			// DB에서 가져온 데이터로 엑셀에 데이터 입력(메뉴)
			for (Super s : menuList) {
				Row row = menuSheet.createRow(menuRowNo++);
				row.createCell(0).setCellValue(s.getName());
				row.createCell(1).setCellValue(s.getPrice());
				row.createCell(2).setCellValue(s.getTag());
				row.createCell(3).setCellValue(s.getType());
			}

			// DB에서 가져온 데이터로 엑셀에 데이터 입력(일일매출)
			SimpleDateFormat Format = new SimpleDateFormat("yyyy/MM/dd");
			for (SalesDTO sdto : salesList) {
				Row row = priceSheet.createRow(priceRowNo++);
				row.createCell(0).setCellValue(sdto.getT_Price());
				row.createCell(1).setCellValue(Format.format(sdto.getIndate()));
			}

			// 파일 저장
			workbook.write(fileOut);
		}

		System.out.println("엑셀 파일이 성공적으로 저장되었습니다: " + filePath); // 성공 메시지 출력
	}

}
