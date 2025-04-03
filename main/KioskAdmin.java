package main;

import java.util.Scanner;

import javax.swing.JFrame;

import manager.Manager;


public class KioskAdmin {
	public KioskAdmin() {
		System.out.println("1.주문하기 2.관리자");
		Scanner s = new Scanner(System.in);
		Manager manager = new Manager();
		
		while(true) {
			int num=s.nextInt();
			s.nextLine();
			switch(num) {
			case 1 :
				new Kiosk();
				break;
			case 2 :
				manager.check();
				break;
			}
		}
	}
}
