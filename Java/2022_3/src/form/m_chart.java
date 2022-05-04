package form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DB.DB;
import aframe.aframe;

public class m_chart extends aframe {

	int cate, data[] = { 0, 0, 0, 0, 0 };
	JComboBox jc = new JComboBox();

	public m_chart() {
		fs("지원자 분석");

		np.setBorder(new EmptyBorder(10, 10, 10, 10));
		np.setBackground(Color.white);
		np.add(jl = new JLabel("회사별 지원자 [연령별]", JLabel.CENTER));
		jl.setFont(new Font("HY헤드라인M", 0, 24));
		np.add(jc = new JComboBox(), BorderLayout.EAST);

		cp.setBackground(Color.white);
		try {
			rs = DB.rs("SELECT * FROM company");
			while (rs.next()) {
				jc.addItem(rs.getString(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		int maxV = 250;
		Color c[] = { Color.black, Color.blue, Color.red, Color.green, Color.yellow };
		cp.add(ap = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				int max = Arrays.stream(data).max().getAsInt();
				for (int i = 0; i < data.length; i++) {
					float per = (float) data[i] / max;
					int x = 25 + i * 100;
					int y = 10 + (int) (maxV * (1f - per));
					int w = 40;
					int h = (int) (maxV * per);

					g.setColor(c[i]);
					g.fillRect(x, y, w, h);
					g.fillRect(490, 70 + i * 35, 12, 12);

					g.setColor(Color.black);
					g.drawRect(x, y, w, h);

					String age = String.format("%d0대", i + 1);
					g.drawString(age, 30 + i * 100, 280);
					g.drawString(String.format("%s:%d명", age, data[i]), 510, 80 + i * 35);
				}
			}
		});
		ap.setBackground(Color.white);
		size(cp, 600, 300);

		jc.addActionListener(e -> {
			cate = jc.getSelectedIndex() + 1;
			up();
		});

		cate = 1;
		up();
		sh();
	}

	void up() {
		try {
			rs = DB.rs("SELECT "
					+ "COUNT(IF(year(now()) - year(u_birth) BETWEEN 10 AND 19, 1, null)) cnt1, "
					+ "COUNT(IF(year(now()) - year(u_birth) BETWEEN 20 AND 29, 1, null)) cnt2, "
					+ "COUNT(IF(year(now()) - year(u_birth) BETWEEN 30 AND 39, 1, null)) cnt3, "
					+ "COUNT(IF(year(now()) - year(u_birth) BETWEEN 40 AND 49, 1, null)) cnt4, "
					+ "COUNT(IF(year(now()) - year(u_birth) BETWEEN 50 AND 59, 1, null)) cnt5 "
					+ "FROM company c inner join employment e on c.c_no = e.c_no inner join applicant a on e.e_no = a.e_no inner join user u on a.u_no = u.u_no where c.c_no = " + cate);
			if (rs.next()) {
				for (int i = 0; i < data.length; i++) {
					data[i] = rs.getInt(i + 1);
				}
				int sum = Arrays.stream(data).sum();
				if (sum > 0) {
					repaint();
				} else {
					wmsg("지원자 또는 공고가 없습니다.");
					cate = 1;
					jc.setSelectedIndex(0);
					up();
					return;

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new m_chart();
	}
}
