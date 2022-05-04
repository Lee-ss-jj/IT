package form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import DB.VQ;
import aframe.aframe;

public class k_game extends aframe {
	
	int squiz, run = 1;
	int onimg[] = new int[5], img[] = new int[9];
	JLabel bl[] = new JLabel[9];
	JPanel bp[] = new JPanel[9];
	JButton b;
	Thread th = new Thread(this);
	Random r = new Random();
	
	public k_game() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if (run == 0) {
					int re = JOptionPane.showConfirmDialog(null, "게임 도중 나갈 시 게임은 다시 진행할 수 없습니다\n나가시겠습니까?", "경고", 0);
					if (re == 1) {
						stack.add(k_game.this);
						k_game.this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
					} else {
						dispose();
						sqlup("update reservation set r_attend = 1 where r_no = " + VQ.r_no);
					}
				}
			}
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				run = 0;
			}
		});
		fs("방탈출");
		
		for (int i = 0; i < img.length; i++) {
			img[i] = r.nextInt(30) + 1;
			for (int j = 0; j < i; j++) {
				if (img[i] == img[j]) {
					i--;
					break;
				}
			}
		}
		for (int i = 0; i < onimg.length; i++) {
			onimg[i] = r.nextInt(9);
			for (int j = 0; j < i; j++) {
				if (onimg[i] == onimg[j]) {
					i--;
					break;
				}
			}
		}
		
		cp.setLayout(new GridLayout(0, 3, 5, 5));
		cp.setBorder(new EmptyBorder(10, 10, 10, 10));
		for (int i = 0; i < bl.length; i++) {
			cp.add(bp[i] = new JPanel(new BorderLayout()));
			bp[i].setBorder(new LineBorder(Color.black));
			bp[i].add(bl[i] = new JLabel(new ImageIcon(new ImageIcon("datafiles/퀴즈/" + img[i] + ".jpg").getImage().getScaledInstance(150, 150, 4))));
			size(bl[i], 150, 150);
			bl[i].setEnabled(false);
		}
		for (int i = 0; i < 5; i++) {
			bl[onimg[i]].setEnabled(true);
		}
		
		sp.add(b = new JButton("선택"));
		b.addActionListener(e -> {
			if (b.getText().equals("선택")) {
				run = 0;
				b.setText("게임시작");
			} else {
				dispose();
				new l_quiz(squiz);
			}
		});
		
		th.start();
		sh();
	}
	
	void up() {
		
	}
	
	@Override
	public void run() {
		try {
			int k = 0;
			while (run == 1) {
				for (int i = 0; i < 5; i++) {
					bl[onimg[i]].setBorder(new LineBorder(Color.black));
					bl[onimg[i]].repaint();
					bl[onimg[i]].revalidate();
				}
				bl[onimg[k % 5]].setBorder(new LineBorder(Color.red, 3));
				squiz = img[onimg[k % 5]];
				if (run == 0) {
					break;
				}
				bl[onimg[k % 5]].repaint();
				bl[onimg[k % 5]].revalidate();
				th.sleep(50);
				k++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			new k_game();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
