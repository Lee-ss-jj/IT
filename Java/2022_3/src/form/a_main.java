package form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class a_main extends aframe {
	
	int cnt;
	Thread th;
	JButton b[] = new JButton[5];
	JTextField jt;
	JComboBox jc;
	JPanel bp[] = new JPanel[1000];
	
	public a_main() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				set();
			}
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		fs("Main");
		
		np.setBorder(new EmptyBorder(5, 5, 5, 5));
		np.add(ap = new JPanel(new FlowLayout()), BorderLayout.NORTH);
		
		np.add(p = new JPanel(new FlowLayout()));
		p.add(jl = new JLabel("기업검색"));
		p.add(jt = new JTextField(14));
		p.add(b[0] = new JButton("검색"));
		font(jl, 0, 16);
		
		b[0].addActionListener(e -> {
			if (jt.getText().equals("")) {
				wmsg("검색할 기업명을 입력하세요.");
				return;
			}
			try {
				rs = DB.rs("select * from company where c_name like '%" + jt.getText().toUpperCase() + "%'");
				if (rs.next()) {
					int cno = rs.getInt("c_no");
					sqlup("update company set c_search = c_search + 1 where c_no = " + cno);
					up();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		});
		cp.setBorder(new EmptyBorder(5, 5, 5, 5));
		cp.add(jl = new JLabel(" 인기기업", 2), BorderLayout.NORTH);
		font(jl, 0, 16);
		
		cp.add(jp1 = new JPanel(new GridLayout(0, 1, 5, 5)), BorderLayout.CENTER);
		cp.add(jp2 = new JPanel(new GridLayout(0, 1, 5, 5)), BorderLayout.EAST);
		
		sp.setBorder(new EmptyBorder(5, 5, 5, 5));
		sp.add(p = new JPanel(new FlowLayout(0)));
		p.add(jl = new JLabel("지역"));
		font(jl, 0, 16);
		
		String area[] = "전체 서울 부산 대구 인천 광주 대전 울산 세종 경기 강원 충북 충남 전북 전남 경남 제주".split(" ");
		p.add(jc = new JComboBox<>(area));
		jc.setSelectedIndex(0);
		
		sp.add(jp3 = new JPanel(null), BorderLayout.SOUTH);
		size(jp3, 350, 100);
		
		jc.addActionListener(e -> {
			slide();
		});
		
		slide();
		up();
		sh();
	}
	
	void set() {
		ap.removeAll();
		jp2.removeAll();
		
		for (int i = 0; i < 4; i++) {
			jp2.add(setcomp(b[i] = new JButton(), 130, 30));
		}
		ap.add(jl = new JLabel("아르바이트", 0), BorderLayout.NORTH);
		font(jl, 0, 24);
		
		if (VQ.log == true) {
			ap.add(jl = new JLabel(new ImageIcon(new ImageIcon("datafiles/회원사진/" + VQ.u_no + ".jpg").getImage().getScaledInstance(30, 30, 4))));
			jl.setBorder(new LineBorder(Color.black));
			ap.add(jl = new JLabel(VQ.u_name + "님 환영합니다."));
			
			String s[] = "로그아웃 채용정보 마이페이지 닫기".split(" ");
			for (int i = 0; i < s.length; i++) {
				b[i].setText(s[i]);
			}
			b[3].setVisible(true);
			b[0].addActionListener(e -> {
				VQ.log = false;
				imsg("로그아웃 되었습니다.");
				jt.setText("");
				jc.setSelectedIndex(0);
				up();
			});
			b[1].addActionListener(e -> {
				stack.add(this);
				dispose();
				new d_info();
			});
			b[2].addActionListener(e -> {
				stack.add(this);
				dispose();
				new g_mypage();
			});
			b[3].addActionListener(e -> {
				dispose();
			});
		} else {
			String s[] = "로그인 회원가입 닫기".split(" ");
			for (int i = 0; i < s.length; i++) {
				b[i].setText(s[i]);
			}
			b[3].setVisible(false);
			b[0].addActionListener(e -> {
				stack.add(this);
				dispose();
				new b_login();
			});
			b[1].addActionListener(e -> {
				stack.add(this);
				dispose();
				new c_reg();
			});
			b[2].addActionListener(e -> {
				dispose();
			});
		}
		repaint();
		revalidate();
	}
	
	void up() {
		jp1.removeAll();
		
		try {
			int k = 1;
			rs = DB.rs("select * from company order by c_search desc, c_no asc limit 5");
			while (rs.next()) {
				jp1.add(p = new JPanel(new FlowLayout(0)));
				p.add(setcomp(new JLabel(" " + k), 30, 25));
				p.add(setcomp(new JLabel(rs.getString("c_name")), 100, 25));
				p.add(setcomp(new JLabel(rs.getString("c_search")), 30, 25));
				k++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		repaint();
		revalidate();
		if (!jt.getText().equals("")) {
			try {
				rs = DB.rs("select * from company where c_name like '%" + jt.getText().toUpperCase() + "%' order by c_no asc");
				if (rs.next()) {
					stack.add(this);
					dispose();
					new i_company(rs.getInt(1), 0);
				} else {
					throw new Exception();
				}
			} catch (Exception e) {
				wmsg("검색한 기업이 없습니다.");
				jt.setText("");
				jt.requestFocus();
				return;
			}
		}
	}
	
	void slide() {
		jp3.removeAll();
		
		repaint();
		revalidate();
		try {
			int k = 0;
			rs = DB.rs("select * from company");
			while (rs.next()) {
				if (jc.getSelectedIndex() != 0 && !rs.getString("c_address").contains(jc.getSelectedItem().toString())) {
					continue;
				}
				jp3.add(bp[k] = new JPanel(new BorderLayout()));
				Image img = ImageIO.read(rs.getBlob("c_img").getBinaryStream()).getScaledInstance(100, 100, 4);
				bp[k].add(jl = new JLabel());
				jl.setIcon(new ImageIcon(img));
				bp[k].setBorder(new LineBorder(Color.black));
				bp[k].setBounds(0, 7000, 100, 100);
				bp[k].add(jl = new JLabel(rs.getString("c_name"), 0), BorderLayout.SOUTH);
				k++;
			}
			cnt = k;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cnt == 0) {
			wmsg("선택한 기업정보가 없습니다.");
			jc.setSelectedIndex(0);
			slide();
			return;
		}
		if (th != null) {
			th.interrupt();
		}
		th = new Thread(() -> {
			try {
				int k = 1;
				int n = cnt;
				if (n > 5) {
					n = 5;
				}
				while (true) {
					for (int i = 0; i > -100; i--) {
						for (int j = 0; j < n; j++) {
							bp[(j + k) % cnt].setBounds(i + j * 100, 0, 100, 100);
						}
						th.sleep(20);
					}
					k++;
				}
			} catch (Exception e) {
				
			}
		});
		th.start();
		repaint();
		revalidate();
	}
	
	public static void main(String[] args) {
		try {
			new a_main();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
