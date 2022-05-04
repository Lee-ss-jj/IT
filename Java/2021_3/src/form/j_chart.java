package form;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import DB.DB;
import aframe.aframe;

public class j_chart extends aframe {

	JComboBox<String> jc;
	JButton b[] = new JButton[2];
	int sm = 1;
	int t = -1;
	int cnt = 0;
	int k = 0;
	int t1[] = new int[1000];
	String t2[] = new String[1000];

	Color col[] = { Color.black, Color.blue, Color.red };
	
	Timer tm = new Timer(30, new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			chart();
			revalidate();
			repaint();
		}
	});


	public j_chart() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if (stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}
		});
		
		fs("인기 웨딩홀");

		String ss[] = "인기 웨딩 종류, 인기 식사 종류".split(", ");
		np.setLayout(new FlowLayout(FlowLayout.LEFT));
		np.add(jc = new JComboBox<String>(ss));
		size(jc, 250, 30);
		size(cp, 400, 420);

		sp.setLayout(new FlowLayout());
		String bn[] = "◀,▶".split(",");
		for (int i = 0; i < bn.length; i++) {
			sp.add(b[i] = new JButton(bn[i]));
			int n = i;
			b[i].addActionListener(e -> {
				if (n == 0) {
					col[0] = Color.black;
					col[1] = Color.blue;
					col[2] = Color.red;
					k--;
					ep.removeAll();
					repaint();
					pack();
					setLocationRelativeTo(null);
				} else {
					col[0] = Color.black;
					col[1] = Color.blue;
					col[2] = Color.red;
					k++;
					ep.removeAll();
					repaint();
					pack();
					setLocationRelativeTo(null);
				}
			});
		}

		jc.addActionListener(e -> {
			col[0] = Color.black;
			col[1] = Color.blue;
			col[2] = Color.red;
			ep.removeAll();
			repaint();
			pack();
			dataset();
		});

		dataset();
		sh();
	}

	void dataset() {
		String sql[] = {
				"select s.wtyname, count(s.wtyname) as cnt from 2021지방_2.sm s inner join payment p on p.wh_no = s.wno group by s.wtyname order by cnt desc, s.wtyname asc",
				"select s.mname, count(s.mname) as cnt from 2021지방_2.sm s inner join payment p on p.wh_no = s.wno group by s.mname order by cnt desc" };
		cnt = 0;
		k = 0;
		try {
			rs = DB.rs(sql[jc.getSelectedIndex()]);
			while (rs.next()) {
				t2[cnt] = rs.getString(1);
				System.out.println(rs.getString(1));
				t1[cnt] = rs.getInt(2);
				System.out.println(rs.getInt(2));
				cnt++;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if (tm.isRunning()) {
			sm = 1;
			t = -1;
			tm.restart();
		} else {
			sm = 1;
			t = -1;
			tm.start();
		}
	}

	void chart() {
		cp.removeAll();
		cp.add(ap = new JPanel() {

			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				if (t < 50) {
					sm++;
					t++;
				} else {
					tm.stop();
				}

				int kk = 0;
				for (int i = k; i < k + 3; i++) {
					g.setColor(col[kk]);
					g.fillRect(40 + kk * 60, 350 - (350 / t1[0] * t1[i]), 40, (350 / t1[0] * t1[i]) * (sm - 1) / 50);
					g.fillRect(250, 180 + kk * 30, 15, 15);

					g.setColor(Color.black);
					g.drawRect(40 + kk * 60, 350 - (350 / t1[0] * t1[i]), 40, (350 / t1[0] * t1[i]) * (sm - 1) / 50);
					g.drawString(t2[i], 40 + kk * 60, 370);
					g.drawString(t2[i] + " : " + t1[i] + "개", 270, 190 + kk * 30);

					kk++;
				}
				if (k == 0) {
					b[0].setEnabled(false);
				} else {
					b[0].setEnabled(true);
				}
				if (k == cnt - 3) {
					b[1].setEnabled(false);
				} else {
					b[1].setEnabled(true);
				}
			}
		});
		size(ap, 400, 420);

		ap.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.getX() >= 250 && e.getX() <= 265) {
					int x = -1;
					if (e.getY() >= 180 && e.getY() <= 195) {
						col[0] = Color.magenta;
						col[1] = Color.blue;
						col[2] = Color.red;
						x = 0;
					} if (e.getY() >= 210 && e.getY() <= 225) {
						col[0] = Color.black;
						col[1] = Color.magenta;
						col[2] = Color.red;
						x = 1;
					} if (e.getY() >= 240 && e.getY() <= 255) {
						col[0] = Color.black;
						col[1] = Color.blue;
						col[2] = Color.magenta;
						x = 2;
					} if (x == -1) return;

					repaint();

					ep.removeAll();
					ep.setLayout(new FlowLayout());
					String tn[] = "이름, 주소, 홀사용료".split(", ");
					ep.add(jsp = new JScrollPane(jta = new JTable(dtm = new DefaultTableModel(null, tn) {
						public boolean isCellEditable(int row, int column) {
							return false;
						};
					})));
					jta.getTableHeader().setReorderingAllowed(false);
					jta.getTableHeader().setResizingAllowed(false);
					size(jsp, 500, 350);

					String sql[] = { "select wname, wadd, format(wprice, '#,##0') from 2021지방_2.sm where wtyname like '%" + t2[k + x] + "%'", 
							"select wname, wadd, format(wprice, '#,##0') from 2021지방_2.sm where mname like '%" + t2[k + x] + "%'" };
					try {
						rs = DB.rs(sql[jc.getSelectedIndex()]);
						while (rs.next()) {
							dtm.addRow(new String[] { rs.getString(1), rs.getString(2), rs.getString(3) + "원" });
						}
					} catch (Exception e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
					pack();
					setLocationRelativeTo(null);
				}
			}
		});
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
			new j_chart();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
