package form;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class l_quiz extends aframe {
	
	int check = 0, chance = 3;
	String answer = "";
	boolean ox;
	JLabel tl, cl;
	JButton b;
	JTextField jt;
	Thread th = new Thread(this);
	
	public l_quiz(int qno) {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				int re = JOptionPane.showConfirmDialog(null, "게임 도중 나갈 시 게임은 다시 진행할 수 없습니다\n나가시겠습니까?", "경고", 0);
				if (re == 1) {
					if (VQ.t_time > 0) {
						stack.add(l_quiz.this);
						l_quiz.this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
					}
				} else {
					dispose();
					sqlup("update reservation set r_attend = 1 where r_no = " + VQ.r_no);
					stack.pop();
				}
			}
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				th.interrupt();
			}
		});
		fs("Q" + qno);
		
		try {
			rs = DB.rs("select * from quiz where q_no = " + qno);
			if (rs.next()) {
				answer = rs.getString(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		np.add(jl = new JLabel("퀴즈번호 : " + qno, 0), BorderLayout.NORTH);
		np.setBackground(Color.black);
		font(jl, 0, 30);
		color(jl);
		
		np.add(cl = new JLabel("기회 : " + chance + "번"), BorderLayout.WEST);
		font(cl, 0, 16);
		color(cl);
		np.add(tl = new JLabel("남은 시간 : "), BorderLayout.EAST);
		font(tl, 0, 16);
		color(tl);
		
		cp.add(ap = new JPanel(new BorderLayout()));
		ap.add(jl = new JLabel(new ImageIcon(new ImageIcon("datafiles/퀴즈/" + qno + ".jpg").getImage().getScaledInstance(600, 500, 4))) {
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.red);
				g2.setStroke(new BasicStroke(10));
				if (check != 0) {
					if (ox) {
						g2.drawArc(50, 20, 500, 450, 0, 360);
					} else {
						g2.drawLine(0, 0, 600, 500);
						g2.drawLine(600, 0, 0, 500);
					}
				}
			}
		});
		sp.setBackground(Color.black);
		sp.setLayout(new FlowLayout());
		sp.add(jl = new JLabel("답 입력 : "));
		font(jl, 0, 22);
		color(jl);
		
		sp.add(jt = new JTextField(14));
		sp.add(b = new JButton("확인"));
		b.addActionListener(e -> {
			if (jt.getText().replace(" ", "").trim().equals(answer.replace(" ", "").trim())) {
				th.interrupt();
				check = 1;
				ox = true;
				ap.repaint();
				ap.revalidate();
				imsg("Q" + qno + "번 문제를 통과하셨습니다.");
				sqlup("update reservation set r_attend = 1 where r_no = " + VQ.r_no);
				dispose();
				stack.pop().setVisible(true);
			} else {
				chance--;
				cl.setText("기회 : " + chance + "회");
			}
			if (chance == 0) {
				th.interrupt();
				check = 1;
				ox = false;
				ap.repaint();
				ap.revalidate();
				wmsg("남은 기회가 없으므로 종료합니다.");
				sqlup("update reservation set r_attend = 1 where r_no = " + VQ.r_no);
				dispose();
				stack.pop().setVisible(true);
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
			while (VQ.t_time >= 0) {
				tl.setText("남은 시간 : " + VQ.t_time / 60 + ":" + zz.format(VQ.t_time % 60));
				th.sleep(1000);
				VQ.t_time--;
			}
			check = 1;
			ox = false;
			ap.repaint();
			ap.revalidate();
			wmsg("제한시간 초과로 종료합니다.");
			dispose();
			stack.pop().setVisible(true);
			th.interrupt();
		} catch (Exception e) {

		}
	}
	
	public static void main(String[] args) {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
