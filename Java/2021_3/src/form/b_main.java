package form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import DB.DB;
import aframe.aframe;

public class b_main extends aframe {

	JButton jb[] = new JButton[5];

	public b_main() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if (stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}
		});

		fs("메인");

		np.setLayout(new FlowLayout(FlowLayout.CENTER));
		np.add(jl = new JLabel("결혼식장 예약 프로그램"));
		jl.setFont(new Font("", 1, 28));

		wp.setLayout(new GridLayout(0, 1, 3, 3));
		wp.setBorder(new EmptyBorder(3, 3, 3, 3));
		String bn[] = "웨딩홀 검색, 결제확인, 인기 웨딩홀, 로그아웃, 종료".split(", ");
		for (int i = 0; i < bn.length; i++) {
			wp.add(jb[i] = new JButton(bn[i]));
			int n = i;
			jb[i].addActionListener(e -> {
				stack.add(this);
				switch (n) {
				case 0: {
					dispose();
					new c_search();
					break;
				}
				case 1: {
					dispose();
					new i_paycheck1();
					break;
				}
				case 2: {
					dispose();
					new j_chart();
					break;
				}
				case 3: {
					dispose();
					new a_login();
					break;
				}
				case 4: {
					dispose();
					break;
				}
				}
			});
		}

		for (int i = 0; i < 12; i++) {
			wp.add(new JLabel());
		}

		cp.setBorder(new EmptyBorder(5, 5, 5, 5));
		cp.add(jsp = new JScrollPane(ap = new JPanel(new GridLayout(0, 1))));
		size(jsp, 800, 500);

		try {
			rs = DB.rs("select count(w.wno) as cnt, w.wname, w.wprice, w.wadd from 2021지방_2.sm w inner join payment p on w.wno=p.wh_no group by w.wno order by cnt desc, w.wno asc limit 10");
			while (rs.next()) {
				ap.add(p = new JPanel(new BorderLayout()));
				p.setBorder(new LineBorder(Color.black));

				p.add(jl = new JLabel(), BorderLayout.WEST);
				jl.setIcon(new ImageIcon(new ImageIcon("datafile3/웨딩홀/" + rs.getString(2) + "/" + rs.getString(2) + "1.jpg").getImage().getScaledInstance(250, 150, Image.SCALE_SMOOTH)));

				p.add(pp = new JPanel(new GridLayout(0, 1)));
				pp.add(l = new JLabel("예약 : " + rs.getString(1) + "건"));
				l.setFont(new Font("", 1, 15));
				pp.add(l = new JLabel("이름 : " + rs.getString(2)));
				l.setFont(new Font("", 1, 15));
				pp.add(l = new JLabel("가격 : " + won.format(rs.getInt(3))));
				l.setFont(new Font("", 1, 15));
				pp.add(l = new JLabel("주소 : " + rs.getString(4)));
				l.setFont(new Font("", 1, 15));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		sh();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		super.actionPerformed(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		super.mouseClicked(e);
	}

	public static void main(String[] args) {
		try {
			new b_main();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
