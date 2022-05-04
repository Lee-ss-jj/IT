package form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class g_cal extends aframe {

	int yy, mm;
	JButton b;
	JPanel bp[] = new JPanel[42];
	JLabel dl[] = new JLabel[42];
	Calendar cal = Calendar.getInstance();

	public g_cal() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if(stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}

		});

		fs("출석");

		yy = cal.get(Calendar.YEAR);
		mm = cal.get(Calendar.MONTH) + 1;

		np.setLayout(new FlowLayout(FlowLayout.LEFT));
		np.add(jl = new JLabel(yy + "년 " + mm + "월"));
		jl.setFont(new Font("", 1, 24));

		cp.setLayout(new GridLayout(7, 7));
		String ln[] = "일 월 화 수 목 금 토".split(" ");
		for (int i = 0; i < ln.length; i++) {
			cp.add(setcomp(jl = new JLabel(ln[i], JLabel.CENTER), 50, 50));
			if (i == 0) {
				jl.setForeground(Color.red);
			} else if (i == 6) {
				jl.setForeground(Color.blue);
			}
		}
		for (int i = 0; i < bp.length; i++) {
			cp.add(bp[i] = new JPanel(new BorderLayout()));
		}

		cal.set(yy, mm - 1, 0);
		int sday = cal.get(Calendar.DAY_OF_WEEK);
		cal.set(yy, mm, 0);
		int eday = cal.get(Calendar.DATE);

		for (int i = sday; i < sday + eday; i++) {
			int d = i - sday + 1;
			bp[i].setBorder(new LineBorder(Color.black));
			String bl = yy + "-" + zz.format(mm) + "-" + zz.format(d);
			bp[i].add(p = new JPanel(new BorderLayout()) {
				@Override
				protected void paintComponent(Graphics g) {
					// TODO Auto-generated method stub
					try {
						Date day = ymd.parse(bl);

						if (ymd.format(day).equals(ymd.format(now))) {
							g.setColor(Color.red);
							g.drawOval(5, 5, 40, 40);
						}

						rs = DB.rs("select * from 2021지방_1.attendance where u_no ='" + VQ.no
								+ "' and date_format(a_date, '%Y-%m-%d')  = '" + bl + "'");
						if (rs.next()) {
							g.setColor(Color.black);
							g.drawOval(5, 5, 40, 40);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			});
			size(p, 50, 50);
			p.add(dl[i] = new JLabel(d + "", JLabel.CENTER));
			dl[i].addMouseListener(this);
		}
		sp.setLayout(new FlowLayout(FlowLayout.RIGHT));
		sp.add(b = new JButton("쿠폰 받기"));

		b.addActionListener(e -> {
			String da = yy + "-" + zz.format(mm);

			int cnt = 0;
			int p10;
			int p30;
			int per10 = 0;
			int per30 = 0;

			try {
				rs = DB.rs("select count(*) from 2021지방_1.attendance where u_no ='" + VQ.no
						+ "' and date_format(a_date, '%Y-%m-%d')  like '%" + da + "%'");
				if (rs.next()) {
					cnt = rs.getInt(1);
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}

			int c_no = 0;
			int check = 0;

			try {
				rs = DB.rs("select * from 2021지방_1.coupon where c_date = '" + da + "' and u_no = '" + VQ.no + "'");
				if (rs.next()) {
					c_no = rs.getInt(1);
					per10 = rs.getInt(4);
					per30 = rs.getInt(5);
					check = 1;
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
			if (cnt >= 5) {
				if (per10 == 1 && per30 == 1) {
					wmsg("쿠폰을 이미 받았습니다.");
					return;
				} else {
					VQ.coupon = 1;
					per10 = 1 - per10;
					per30 = 1 - per30;
					p10 = 1;
					p30 = 1;
				}
			} else if (cnt >= 3) {
				if (per10 == 1) {
					wmsg("쿠폰을 이미 받았습니다.");
					return;
				} else {
					VQ.coupon = 0;
					per10 = 1 - per10;
					p10 = 1;
					p30 = 0;
				}
			} else {
				return;
			}

			VQ.per10 += per10;
			VQ.per30 += per30;

			if (check == 1) {
				sqlup("update coupon set c_10percent = '" + p10 + "', c_30percent = '" + p30 + "' where c_no = '" + c_no
						+ "'");
			} else {
				sqlup("insert into coupon values('0','" + VQ.no + "','" + da + "','" + p10 + "', '" + p30 + "')");
			}
			sqlup("update user set u_10percent = '" + VQ.per10 + "', u_30percent = '" + VQ.per30 + "' where u_no = '"
					+ VQ.no + "'");
			
			stack.add(this);
			dispose();
			new h_coupon();
		});

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
		for (int i = 0; i < dl.length; i++) {
			if (e.getSource() == dl[i]) {
				String bl = yy + "-" + zz.format(mm) + "-" + zz.format(rei(dl[i].getText()));

				try {
					Date day = ymd.parse(bl);
					if (ymd.format(now).equals(ymd.format(day))) {
						rs = DB.rs("select * from 2021지방_1.attendance where u_no ='" + VQ.no
								+ "' and date_format(a_date, '%Y-%m-%d')  = '" + bl + "'");
						if (rs.next()) {
							wmsg("이미 출석체크를 했습니다.");
							return;
						}
					} else {
						wmsg("출석체크가 불가능한 날짜입니다.");
						return;
					}
				} catch (Exception e2) {
					// TODO: handle exception
				}
				sqlup("insert into 2021지방_1.attendance values('0', '" + VQ.no + "', '" + bl + "')");
				repaint();
			}
		}
	}

	public static void main(String[] args) {
		try {
			new g_cal();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
