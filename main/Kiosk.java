package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Kiosk extends JFrame implements ActionListener { // ActionListener:버튼 눌렸을때 동작하도록 해주는것
	int count = 0;
	JFrame frame = new JFrame();
	private JButton btn1 = new JButton();
	private JPanel jp1 = new JPanel();

	public Kiosk() {
		this.setSize(600, 700); // 크기
		setLocationRelativeTo(null); // 윈도우창 중앙배치
		setDefaultCloseOperation(EXIT_ON_CLOSE);//
		this.setTitle("Cafe Kiosk");
		// 이미지 첨부
		ImageIcon icon = new ImageIcon("D:\\javasrc\\CafeProject\\imgs/cafe3.jpg");
		JLabel lb1 = new JLabel(icon, JLabel.CENTER);

		jp1.setLayout(null); // 레이아웃 없이 사용자가 원하는 위치에 배치가능
		btn1 = new JButton("주문하기");
		//btn1.setBackground(Color.white);
		btn1.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		btn1.setSize(170, 150); // 버튼 사이즈
		btn1.setLocation(220, 200); // 버튼 위치
		lb1.setBounds(0, 0, 600, 700);
		jp1.add(btn1);
		jp1.add(lb1);

		this.add(jp1);
		this.setVisible(true);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);

		btn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new KioskMenu();
				setVisible(false);
			}
		});

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
