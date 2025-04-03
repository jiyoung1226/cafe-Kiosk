package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import manager.Data;
import manager.MenuDAO;
import manager.OrderDAO;
import manager.OrderDTO;
import manager.SalesDAO;
import manager.SalesDTO;
import manager.Super;
import manager.UserDAO;
import manager.UserDTO;

public class KioskMenu extends JFrame implements ActionListener {
	private MenuDAO menudao = MenuDAO.getInstance();
	private OrderDAO orderdao = OrderDAO.getInstance();
	private UserDAO userdao = UserDAO.getInstance();
	private SalesDAO salesdao = SalesDAO.getInstance();
	private UserDTO udto = new UserDTO();
	private OrderDTO odto = new OrderDTO();
	private JPanel categoryPanel, controlPanel, menuPanel, orderPanel, totalPanel, pricePanel, upPanel, timerPanel;
	private JButton[] categoryButtons;
	private JButton btnPay, btnReset, btnBack, btn, btnMinus, btnPlus, btnX;
	private DefaultListModel<String> model = new DefaultListModel<>();
	private JList<String> orderList = new JList<>(model);
	private int totalPrice = 0; // 총금액, 총금액표시라벨
	private int totalPrice1;
	private JLabel totalLabel, timerLabel;
	private TextField amount;
	private boolean timeExpired = false; // 시간 초과 여부 확인
	private Thread timerThread; // 타이머를 위한 스레드
	private Data datapoi = new Data();
	String[] categories = { "Coffee", "NonCoffee", "Smoothie", "Ade", "Tea", "Dessert" };
	int cnt;

	public KioskMenu() {
		setTitle("카페 메뉴 선택");
		this.setSize(600, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// 1. 카테고리 버튼 패널
		categoryPanel = new JPanel(new GridLayout(2, 3, 10, 10));
		categoryButtons = new JButton[categories.length];

		categoryButtons[0] = new JButton(categories[0]);
		categoryButtons[0].setFont(new Font("맑은 고딕", Font.BOLD, 14));
		categoryButtons[0].addActionListener(e -> menuC(((JButton) e.getSource()).getText()));
		categoryPanel.add(categoryButtons[0]);

		categoryButtons[1] = new JButton(categories[1]);
		categoryButtons[1].setFont(new Font("맑은 고딕", Font.BOLD, 14));
		categoryButtons[1].addActionListener(e -> menuN(((JButton) e.getSource()).getText()));
		categoryPanel.add(categoryButtons[1]);

		categoryButtons[2] = new JButton(categories[2]);
		categoryButtons[2].setFont(new Font("맑은 고딕", Font.BOLD, 14));
		categoryButtons[2].addActionListener(e -> menuS(((JButton) e.getSource()).getText()));
		categoryPanel.add(categoryButtons[2]);

		categoryButtons[3] = new JButton(categories[3]);
		categoryButtons[3].setFont(new Font("맑은 고딕", Font.BOLD, 14));
		categoryButtons[3].addActionListener(e -> menuA(((JButton) e.getSource()).getText()));
		categoryPanel.add(categoryButtons[3]);

		categoryButtons[4] = new JButton(categories[4]);
		categoryButtons[4].setFont(new Font("맑은 고딕", Font.BOLD, 14));
		categoryButtons[4].addActionListener(e -> menuT(((JButton) e.getSource()).getText()));
		categoryPanel.add(categoryButtons[4]);

		categoryButtons[5] = new JButton(categories[5]);
		categoryButtons[5].setFont(new Font("맑은 고딕", Font.BOLD, 14));
		categoryButtons[5].addActionListener(e -> menuD(((JButton) e.getSource()).getText()));
		categoryPanel.add(categoryButtons[5]);

		// 2. 메뉴 표시 패널 (초기에는 빈 버튼)
		menuPanel = new JPanel();

		// 3. 하단 버튼 패널 (결제, 초기화, 뒤로가기)

		controlPanel = new JPanel(new FlowLayout());
		btnPay = new JButton("결제하기");
		btnReset = new JButton("초기화");
		btnBack = new JButton("뒤로가기");

		btnPay.addActionListener(e -> {
			timerThread.interrupt(); // 타이머 중지
			processPayment();
		});
		btnReset.addActionListener(e -> resetMenu1());
		btnBack.addActionListener(e -> {
			timerThread.interrupt();
			new Kiosk(); // 메인 화면으로 돌아가기
			dispose(); // 현재 창 닫기
		});

		// 패널안에 버튼저장
		controlPanel.add(btnPay);
		controlPanel.add(btnReset);
		controlPanel.add(btnBack);
		// controlPanel.add(timerLabel);

		totalPanel = new JPanel(new BorderLayout());

		// 주문내역패널
		orderPanel = new JPanel(new BorderLayout());
		orderPanel.setPreferredSize(new Dimension(600, 200));
		orderPanel.setBorder(BorderFactory.createTitledBorder("주문 내역"));

		orderList.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		orderList.setFixedCellHeight(25);
		JScrollPane scrollPane = new JScrollPane(orderList);
		scrollPane.setPreferredSize(new Dimension(600, 180)); // 주문 내역 크기 조정
		orderPanel.add(scrollPane, BorderLayout.WEST);

		// 총금액 표시 패널
		totalLabel = new JLabel("총금액: 0원");
		totalLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));

		totalPanel.add(totalLabel, BorderLayout.NORTH);
		totalPanel.add(orderPanel, BorderLayout.CENTER);
		totalPanel.add(controlPanel, BorderLayout.SOUTH);

		timerPanel = new JPanel();
		timerPanel.setPreferredSize(new Dimension(80, 60));
		timerLabel = new JLabel("100초");
		timerLabel.setFont(new Font("맑은 고딕", Font.BOLD, 23));
		timerPanel.add(timerLabel);
		// 상단에 카테고리패널+타이머
		upPanel = new JPanel(new BorderLayout());
		upPanel.add(categoryPanel, BorderLayout.CENTER);
		upPanel.add(timerPanel, BorderLayout.EAST);

		// 패널 추가
		add(upPanel, BorderLayout.NORTH);
		add(menuPanel, BorderLayout.CENTER);
		add(totalPanel, BorderLayout.SOUTH);
		setVisible(true);

		// 타이머 시작
		startTimer();

	}

	// 타이머
	private void startTimer() {
		timerThread = new Thread(() -> {
			int timeLeft = 100;

			while (timeLeft > 0 && !timeExpired) {
				try {
					Thread.sleep(1000); // 1초씩 멈춤
					timeLeft--; // 1초 간격으로 시간 감소
					System.out.println("남은 시간: " + timeLeft + "초");
					timerLabel.setText(timeLeft + "초");
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}

			if (timeLeft == 0) {
				timeExpired = true;
				JOptionPane.showMessageDialog(this, "시간 초과로 인해 메인화면으로 돌아갑니다.");
				new Kiosk(); // 시간이 초과되면 Kiosk로 자동 전환
				setVisible(false); // 현재 화면 숨기기
			}
		});
		timerThread.start(); // 타이머 스레드 시작

	}

	// 주문내역표시
	private void addToOrder1(Super menu, int cnt) {
		if (cnt != 0) {
			String orderItem = menu.getName() + "   " + menu.getPrice() + "원" + "   " + "수량 :" + cnt;
			model.addElement(orderItem);
			totalPrice +=menu.getPrice() * cnt;
			totalLabel.setText("총금액: " + totalPrice + "원");

		}

	}

	// 총금액표시
	private void updateTotalPrice() {
		totalLabel.setText("총금액: " + totalPrice + "원");
	}

	private void resetMenu1() {
		model.clear();
		totalPrice = 0;
		updateTotalPrice();

	}

	// 카테고리 선택 시 메뉴 불러오기 (DB 연동)
	private void loadMenu(String category) {
		menuPanel.removeAll(); // 기존 메뉴 삭제

	}

	// 수량선택
	private void count(Super menu) {
		cnt = 1;
		JDialog jd = new JDialog();
		jd.setLayout(new BorderLayout());
		jd.setBounds(450, 400, 400, 200);
		JLabel jLabel = new JLabel("수량을 선택해주세요");
		jLabel.setHorizontalAlignment(JLabel.CENTER);
		JPanel buttonPanel = new JPanel(new FlowLayout());
		amount = new TextField(3);
		amount.setText(String.valueOf(cnt));

		JButton btnMinus = new JButton("-");
		JButton btnPlus = new JButton("+");

		btnMinus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (cnt > 0) {
					cnt = cnt - 1;
					amount.setText(String.valueOf(cnt));
				}
			}
		});
		btnPlus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cnt = cnt + 1;
				amount.setText(String.valueOf(cnt));
			}
		});

		buttonPanel.add(btnMinus);
		buttonPanel.add(amount);
		buttonPanel.add(btnPlus);
		JButton btnCheck = new JButton("확인");

		btnCheck.addActionListener(e -> addToOrder1(menu, cnt));
		btnCheck.addActionListener(e -> jd.setVisible(false));

		jd.add(jLabel, BorderLayout.NORTH);
		jd.add(buttonPanel, BorderLayout.CENTER);
		jd.add(btnCheck, BorderLayout.SOUTH);

		jd.setVisible(true);
	}

	// 커피
	private void menuC(String category) {
		ArrayList<Super> menuList = menudao.select();
		menuPanel.removeAll();

		for (Super menu : menuList) {
			if (menu.getType().equals("Coffee")) {
				if (menu.getTag() == null) {
					JButton btn = new JButton("<html><body style='text-align:center;'>" + menu.getName() + "<br>" + menu.getPrice() + "원</body></html>");

					btn.setPreferredSize(new Dimension(150, 100));

					btn.addActionListener(e -> count(menu));

					menuPanel.add(btn);

				} else {
					JButton btn = new JButton(
							"<html><body style='text-align:center;'>" + menu.getTag() + "<br>" + menu.getName() + "<br>" + menu.getPrice() + "원</body></html>");
					btn.setPreferredSize(new Dimension(150, 100));

					if (menu.getTag().equals("SoldOut")) {
						btn.addActionListener(e -> soldOut());

					} else {
						btn.addActionListener(e -> count(menu));

					}
					menuPanel.add(btn);
				}

			}

			menuPanel.revalidate();
			menuPanel.repaint();
		}

	}

	// 논커피
	private void menuN(String category) {
		ArrayList<Super> menuList = menudao.select();
		menuPanel.removeAll();

		for (Super menu : menuList) {
			if (menu.getType().equals("NonCoffee")) {
				if (menu.getTag() == null) {
					JButton btn = new JButton("<html><body style='text-align:center;'>" + menu.getName() + "<br>" + menu.getPrice() + "원</body></html>");
					btn.setPreferredSize(new Dimension(150, 100));

					btn.addActionListener(e -> count(menu));

					menuPanel.add(btn);

				} else {
					JButton btn = new JButton(
							"<html><body style='text-align:center;'>" + menu.getTag() + "<br>" + menu.getName() + "<br>" + menu.getPrice() + "원</body></html>");
					btn.setPreferredSize(new Dimension(150, 100));

					if (menu.getTag().equals("SoldOut")) {
						btn.addActionListener(e -> soldOut());

					} else {
						btn.addActionListener(e -> count(menu));

					}
					menuPanel.add(btn);

				}
			}
			menuPanel.revalidate();
			menuPanel.repaint();
		}
	}

	// 스무디
	private void menuS(String category) {
		ArrayList<Super> menuList = menudao.select();
		menuPanel.removeAll();

		for (Super menu : menuList) {
			if (menu.getType().equals("Smoothie")) {
				if (menu.getTag() == null) {
					JButton btn = new JButton("<html><body style='text-align:center;'>" + menu.getName() + "<br>" + menu.getPrice() + "원</body></html>");
					btn.setPreferredSize(new Dimension(150, 100));

					btn.addActionListener(e -> count(menu));

					menuPanel.add(btn);

				} else {
					JButton btn = new JButton(
							"<html><body style='text-align:center;'>" + menu.getTag() + "<br>" + menu.getName() + "<br>" + menu.getPrice() + "원</body></html>");
					btn.setPreferredSize(new Dimension(150, 100));

					if (menu.getTag().equals("SoldOut")) {
						btn.addActionListener(e -> soldOut());

					} else {
						btn.addActionListener(e -> count(menu));

					}

					menuPanel.add(btn);

				}
			}
			menuPanel.revalidate();
			menuPanel.repaint();
		}
	}

	// 에이드
	private void menuA(String category) {
		ArrayList<Super> menuList = menudao.select();
		menuPanel.removeAll();

		for (Super menu : menuList) {
			if (menu.getType().equals("Ade")) {
				if (menu.getTag() == null) {
					JButton btn = new JButton("<html><body style='text-align:center;'>" + menu.getName() + "<br>" + menu.getPrice() + "원</body></html>");
					btn.setPreferredSize(new Dimension(150, 100));

					btn.addActionListener(e -> count(menu));

					menuPanel.add(btn);

				} else {
					JButton btn = new JButton(
							"<html><body style='text-align:center;'>" + menu.getTag() + "<br>" + menu.getName() + "<br>" + menu.getPrice() + "원</body></html>");
					btn.setPreferredSize(new Dimension(150, 100));

					if (menu.getTag().equals("SoldOut")) {
						btn.addActionListener(e -> soldOut());

					} else {
						btn.addActionListener(e -> count(menu));

					}

					menuPanel.add(btn);

				}
			}

			menuPanel.revalidate();
			menuPanel.repaint();
		}
	}

	// 티
	private void menuT(String category) {
		ArrayList<Super> menuList = menudao.select();
		menuPanel.removeAll();

		for (Super menu : menuList) {
			if (menu.getType().equals("Tea")) {
				if (menu.getTag() == null) {
					JButton btn = new JButton("<html><body style='text-align:center;'>" + menu.getName() + "<br>" + menu.getPrice() + "원</body></html>");
					btn.setPreferredSize(new Dimension(150, 100));
					btn.addActionListener(e -> count(menu));
					menuPanel.add(btn);

				} else {
					JButton btn = new JButton(
							"<html><body style='text-align:center;'>" + menu.getTag() + "<br>" + menu.getName() + "<br>" + menu.getPrice() + "원</body></html>");
					btn.setPreferredSize(new Dimension(150, 100));
					if (menu.getTag().equals("SoldOut")) {
						btn.addActionListener(e -> soldOut());

					} else {
						btn.addActionListener(e -> count(menu));
					}
					menuPanel.add(btn);
				}
			}
			menuPanel.revalidate();
			menuPanel.repaint();
		}
	}

	// 디저트
	private void menuD(String category) {
		ArrayList<Super> menuList = menudao.select();
		menuPanel.removeAll();

		for (Super menu : menuList) {
			if (menu.getType().equals("Dessert")) {
				if (menu.getTag() == null) {
					JButton btn = new JButton("<html><body style='text-align:center;'>" + menu.getName() + "<br>" + menu.getPrice() + "원</body></html>");
					btn.setPreferredSize(new Dimension(150, 100));
					btn.addActionListener(e -> count(menu));

					menuPanel.add(btn);

				} else {
					JButton btn = new JButton(
							"<html><body style='text-align:center;'>" + menu.getTag() + "<br>" + menu.getName() + "<br>" + menu.getPrice() + "원</body></html>");
					btn.setPreferredSize(new Dimension(150, 100));
					if (menu.getTag().equals("SoldOut")) {
						btn.addActionListener(e -> soldOut());

					} else {
						btn.addActionListener(e -> count(menu));
					}
					menuPanel.add(btn);
				}
			}
			menuPanel.revalidate();
			menuPanel.repaint();
		}
	}

	

	// 품절메뉴
	private void soldOut() {
		JOptionPane.showMessageDialog(null, "품절메뉴입니다 !", "SoldOut", JOptionPane.INFORMATION_MESSAGE);
	}

	// 결제 처리
	private void processPayment() {
		if (model.isEmpty()) {
			JOptionPane.showMessageDialog(this, "주문 내역이 없습니다.", "알림", JOptionPane.WARNING_MESSAGE);
			return;

		} else {
			// 새로운 다이얼로그 창 생성
			JFrame paymentFrame = new JFrame("결제하기");
			paymentFrame.setSize(300, 100);
			paymentFrame.setLayout(new FlowLayout());
			paymentFrame.setLocationRelativeTo(this);

			// 버튼 생성
			JButton btnMembership = new JButton("멤버십 적립");
			JButton btnConfirmPayment = new JButton("결제하기");

			// 멤버십 적립 버튼 클릭 시 핸드폰 번호 입력창 표시
			ArrayList<UserDTO> numlist = userdao.select();
			btnMembership.addActionListener(e -> {
				String phoneNumber = showKeypadDialog();
				int stamp;

				if (phoneNumber != null) {
					JOptionPane.showMessageDialog(paymentFrame, "스탬프 1개가 적립되었습니다! ", "확인",
							JOptionPane.INFORMATION_MESSAGE);
					JOptionPane.showMessageDialog(paymentFrame, "결제가 완료되었습니다! ", "결제 완료",
							JOptionPane.INFORMATION_MESSAGE);
					JOptionPane.showMessageDialog(paymentFrame, "주문 번호: " + orderNumber, "주문번호",
							JOptionPane.PLAIN_MESSAGE);
					for (UserDTO u : numlist) {
						// 이미 번호가 저장되어 있을떄
						if (u.getPhoneNum().equals(phoneNumber)) {
							stamp = (u.getStamp()) + 1;
							// 스탬프 10개 이상이면 2000원 할인 적용
							if (stamp >= 10) {
								totalPrice -= 2000;
								// if (totalPrice < 0) totalPrice = 0; // 총 금액이 음수가 되지 않도록 처리
								stamp -= 10; // 스탬프 10개 차감 (사용 후 초기화)
								JOptionPane.showMessageDialog(paymentFrame, "스탬프 10개 사용! 2000원 할인 적용!", "할인 적용",
										JOptionPane.INFORMATION_MESSAGE);
							}

							userdao.updateStamp(u.getPhoneNum(), stamp);
							saveOrder(orderNumber);
							saveSales(totalPrice);
							resetMenu1(); // 주문 내역 및 총금액 초기화
							paymentFrame.dispose(); // 결제창 닫기

						}
						// 새로 회원가입 될때
						else {
							stamp = 1;
							userdao.insert(phoneNumber, stamp);
							saveOrder(orderNumber);
							saveSales(totalPrice);
							resetMenu1(); // 주문 내역 및 총금액 초기화
							paymentFrame.dispose(); // 결제창 닫기
						}

					}
					try { // 엑셀저장
						datapoi.downloadExcel();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					setVisible(false); // 창닫기
					new Kiosk();

				}
			});

			// 결제하기 버튼 클릭 시 결제 완료 처리
			btnConfirmPayment.addActionListener(e -> {
				JOptionPane.showMessageDialog(paymentFrame, "결제가 완료되었습니다!", "결제 완료", JOptionPane.INFORMATION_MESSAGE);
				JOptionPane.showMessageDialog(paymentFrame, "주문 번호: " + orderNumber, "주문번호", JOptionPane.PLAIN_MESSAGE);
				saveOrder(orderNumber);
				saveSales(totalPrice);
				resetMenu1(); // 주문 내역 및 총금액 초기화
				paymentFrame.dispose(); // 결제창 닫기
				try { // 엑셀저장
					datapoi.downloadExcel();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				setVisible(false);
				new Kiosk();
			});

			// 다이얼로그 창에 버튼 추가
			paymentFrame.add(btnMembership);
			paymentFrame.add(btnConfirmPayment);
			paymentFrame.setVisible(true);

		}

	}

	// 주문 번호 카운터
	private static int orderNumberCounter = 100;

	// 주문 번호 생성
	private int generateOrderNumber() {
		// 간단히 주문 번호를 증가시키는 방식 (DB나 파일에 저장할 수 있음)
		// 주문 번호는 임시로 static 변수를 사용하여 증가
		return orderNumberCounter++; // 첫 주문 번호는 100부터 시작
	}

	// 결제 완료 후 주문 번호 생성
	private int orderNumber = generateOrderNumber();

	// 멤버십적립 키패드
	private String showKeypadDialog() {
		JDialog keypadDialog = new JDialog(this, "전화번호 입력", true);
		keypadDialog.setSize(250, 350);
		keypadDialog.setLayout(new BorderLayout());
		keypadDialog.setLocationRelativeTo(this);

		// 입력창
		JTextField inputField = new JTextField();
		inputField.setEditable(false);
		inputField.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		inputField.setHorizontalAlignment(JTextField.CENTER);
		keypadDialog.add(inputField, BorderLayout.NORTH);

		// 키패드 패널
		JPanel keypadPanel = new JPanel(new GridLayout(4, 3, 5, 5));
		String[] keys = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "←", "0", "확인" };

		for (String key : keys) {
			JButton btn = new JButton(key);
			btn.setFont(new Font("맑은 고딕", Font.BOLD, 16));
			btn.addActionListener(e -> {
				if (key.equals("←")) { // 백스페이스 기능
					String text = inputField.getText();
					if (!text.isEmpty()) {
						inputField.setText(text.substring(0, text.length() - 1));
					}

				} else if (key.equals("확인")) { // 입력 완료
					String phoneNumber = inputField.getText();
					if (phoneNumber.length() == 11) {
						keypadDialog.dispose();
					} else {
						JOptionPane.showMessageDialog(keypadDialog, "올바른 전화번호를 입력하세요.", "경고",
								JOptionPane.WARNING_MESSAGE);
					}
				} else { // 숫자 입력
					if (inputField.getText().length() < 11) { // 전화번호 길이 제한
						inputField.setText(inputField.getText() + key);
					}
				}
			});
			keypadPanel.add(btn);
		}
		keypadDialog.add(keypadPanel, BorderLayout.CENTER);
		keypadDialog.setVisible(true);
		return inputField.getText();
	}

	// 주문 정보 저장
	private void saveOrder(int orderNumber) {
		for (int i = 0; i < model.size(); i++) {
			String item = model.getElementAt(i);
			String[] parts = item.split("   ");
			String menuName = parts[0];
			int price = Integer.parseInt(parts[1].replace("원", "").trim());
			int amount = Integer.parseInt(parts[2].replace("수량 :", "").trim());
			orderdao.insert(menuName, price, amount, orderNumber); // DB에 주문 정보 저장
		}
	}

	private void saveSales(int totalPrice) {
		SalesDAO salesdao = SalesDAO.getInstance();
		java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis()); // 현재 날짜 가져오기

		SalesDTO sales = new SalesDTO();
		sales.setIndate(currentDate);
		sales.setT_Price(totalPrice);
		salesdao.insert(sales);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
