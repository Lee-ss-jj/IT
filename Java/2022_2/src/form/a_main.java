package form;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class a_main extends aframe {

	int run = 1;
	Thread th;
	JComboBox jc = new JComboBox<>();
	JButton b[] = new JButton[6];
	JPanel bp[] = new JPanel[1000];

	public a_main() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				set();
				up();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				run = 0;
				System.exit(0);
			}
		});
		fs("메인");

		np.setBorder(new EmptyBorder(5, 5, 5, 5));
		np.setLayout(new FlowLayout(0));
		np.add(jl = new JLabel("예약별 TOP5"));
		font(jl, 0, 20);

		String cn[] = "지점 테마".split(" ");
		np.add(jc = new JComboBox<>(cn));
		jc.addActionListener(e -> {
			up();
		});

		sp.setBorder(new EmptyBorder(5, 5, 5, 5));
		sp.setLayout(new FlowLayout());
		String bn[] = "로그인 마이페이지 검색 게시판 방탈출게임 예약현황".split(" ");
		for (int i = 0; i < bn.length; i++) {
			sp.add(b[i] = new JButton(bn[i]));
			int n = i;
			b[i].addActionListener(e -> {
				switch (n) {
				case 0: {
					if (b[n].getText().equals("로그인")) {
						stack.add(this);
						dispose();
						new b_login();
					} else {
						VQ.log = 0;
						imsg("로그아웃이 완료되었습니다.");
						set();
					}
					break;
				}
				case 1: {
					stack.add(this);
					dispose();
					new d_mypage();
					break;
				}
				case 2: {
					stack.add(this);
					dispose();
					new e_search();
					break;
				}
				case 3: {
					stack.add(this);
					dispose();
					new h_noti();
					break;
				}
				case 4: {
					stack.add(this);
					dispose();
					new j_list();
				}
				case 5: {
					stack.add(this);
					dispose();
					new m_map();
					break;
				}
				}
			});
		}

		cp.add(ap = new JPanel(null));
		size(ap, 800, 500);
		
		up();
		sh();
	}

	void set() {
		for (int i = 0; i < b.length; i++) {
			int n = i;
			if (VQ.log == 0) {
				b[0].setText("로그인");
				if (n == 0 || n == 5) {
					b[i].setEnabled(true);
				} else {
					b[i].setEnabled(false);
				}
			} else {
				b[0].setText("로그아웃");
				b[i].setEnabled(true);
			}
		}
	}

	void up() {
		ap.removeAll();
		
		repaint();
		revalidate();
		try {
			int k = 0;
			if (jc.getSelectedIndex() == 0) {
				rs = DB.rs("select *, count(1) as cnt from cafe c inner join reservation r on c.c_no = r.c_no group by c.c_no order by cnt desc, c.c_name limit 5");
			} else {
				rs = DB.rs("select *, count(1) as cnt from theme t inner join reservation r on t.t_no = r.t_no group by t.t_no order by cnt desc, t.t_name limit 5");
			}
			while (rs.next()) {
				bp[k] = new JPanel(new BorderLayout());
				bp[k].add(jl = new JLabel());
				if (jc.getSelectedIndex() == 0) {
					jl.setIcon(new ImageIcon(new ImageIcon("datafiles/지점/" + rs.getString("c_name").split(" ")[0] + ".jpg").getImage().getScaledInstance(800, 400, 4)));
					bp[k].add(jl = new JLabel(rs.getString("c_name"), 0), BorderLayout.SOUTH);
				} else {
					jl.setIcon(new ImageIcon(new ImageIcon("datafiles/테마/" + rs.getString("t_no") + ".jpg").getImage().getScaledInstance(800, 400, 4)));
					bp[k].add(jl = new JLabel(rs.getString("t_name"), 0), BorderLayout.SOUTH);
				}
				ap.add(bp[k]);
				bp[k].setBounds(0, 7000, 800, 400);
				bp[k].setBorder(new EmptyBorder(10, 10, 10, 10));
				font(jl, 0, 24);
				k++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(th != null) th.interrupt();
		th = new Thread(() -> {
			try {
				while (run == 1) {
					for (int i = 0; i > -2000; i--) {
						for (int j = 0; j < 5; j++) {
							bp[j % 5].setBounds(0, i + 400 * j, 800, 400);
						}
						th.sleep(2);
					}
				}
			} catch (Exception e) {
				
			}
		});
		th.start();
	}

	public static void main(String[] args) {
		try {
			new a_main();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
