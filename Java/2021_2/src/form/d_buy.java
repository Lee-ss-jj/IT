package form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class d_buy extends aframe {

	int cnt = 0;
	int stock = 0;
	int index[] = new int[1000];
	JTextField jt[] = new JTextField[4];
	JCheckBox jch[] = new JCheckBox[2];
	JTextArea jtx;
	JPanel bp[] = new JPanel[1000];
	JLabel image, img[] = new JLabel[1000];
	Thread th = new Thread(this);
	JButton jb[] = new JButton[2];

	public d_buy() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if (stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				th.stop();
			}
		});

		fs("구매");

		np.setLayout(new FlowLayout(FlowLayout.LEFT));
		np.add(image = new JLabel());
		size(image, 200, 200);
		image.setBorder(new LineBorder(Color.black));

		np.add(p = new JPanel(new GridLayout(0, 1)));
		String ln[] = "제품명, 가격, , 수량".split(", ");
		for (int i = 0; i < ln.length; i++) {
			p.add(pp = new JPanel(new FlowLayout(FlowLayout.LEFT)));
			if (i == 2) {
				String ss[] = "10% 할인 쿠폰 적용, 30% 할인 쿠폰 적용".split(", ");
				for (int j = 0; j < ss.length; j++) {
					pp.add(jch[j] = new JCheckBox(ss[j]));
				}
			} else {
				pp.add(setcomp(new JLabel(ln[i], JLabel.LEFT), 55, 25));
				pp.add(jt[i] = new JTextField(20));
			}
		}

		cp.setLayout(new FlowLayout(FlowLayout.LEFT));
		cp.add(p = new JPanel(new BorderLayout()));
		p.add(new JLabel("상품 설명"), BorderLayout.NORTH);

		p.add(jtx = new JTextArea(), BorderLayout.CENTER);
		jtx.setBorder(new LineBorder(Color.black));
		jtx.setEnabled(false);
		jtx.setLineWrap(true);
		size(jtx, 350, 150);

		cp.add(p = new JPanel(new GridLayout(0, 1)));
		p.add(pp = new JPanel(new FlowLayout(FlowLayout.CENTER)));
		p.add(pp = new JPanel(new FlowLayout(FlowLayout.CENTER)));
		String bn[] = "구매하기 취소하기".split(" ");
		for (int i = 0; i < bn.length; i++) {
			pp.add(jb[i] = new JButton(bn[i]));
			int n = i;
			jb[i].addActionListener(e -> {
				if (n == 0) {
					if (rei(jt[3].getText()) < 1) {
						wmsg("1개 이상의 수량을 입력하세요.");
						return;
					} try {
						int d = Integer.parseInt(jt[3].getText());
					} catch (Exception e2) {
						// TODO: handle exception
						wmsg("1개 이상의 수량을 입력하세요.");
						return;
					} if (jch[0].isSelected() && jch[1].isSelected()) {
						wmsg("할인 쿠폰은 중복 사용이 불가능합니다.");
						return;
					} if (jch[0].isSelected() && VQ.per10 == 0 || jch[1].isSelected() && VQ.per30 == 0) {
						wmsg("해당 쿠폰이 없습니다.");
						return;
					} if (stock < rei(jt[3].getText())) {
						wmsg("재고가 없습니다.");
						return;
					} try {
						rs = DB.rs("select * from 2021지방_1.purchase where p_no = '" + VQ.index[VQ.in] + "' and u_no = '"
								+ VQ.no + "' and pu_date = '" + ymd.format(now) + "'");
						if (rs.next()) {
							wmsg("동일한 상품을 이미 구매하였습니다.");
							return;
						}
					} catch (Exception e2) {
						// TODO: handle exception
					}
					int check = 0;
					int pay = rei(jt[1].getText()) * rei(jt[3].getText());
					
					if(jch[0].isSelected()) {
						pay = (int) (pay * 0.9);
					} if(jch[1].isSelected()) {
						pay = (int) (pay * 0.7);
					}
					
					int select = JOptionPane.showConfirmDialog(null, "총 가격이 " + won.format(pay) + "원 입니다.\n결제하시겠습니까?", "결제", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					
					if(select == 0) {
						if (jch[0].isSelected()) {
							check = 1;
							VQ.per10--;
						} else if (jch[1].isSelected()) {
							check = 2;
							VQ.per30--;
						}						
						sqlup("insert into purchase values('0','" + VQ.index[VQ.in] + "','" + rei(jt[1].getText()) + "','"
								+ jt[3].getText() + "','" + check + "','" + VQ.no + "','" + ymd.format(now) + "')");
						sqlup("update user set u_10percent = '" + VQ.per10 + "', u_30percent = '" + VQ.per30
								+ "' where u_no = '" + VQ.no + "'");
						sqlup("update product set p_stock = P_stock - " + jt[3].getText() + " where p_no = '"
								+ VQ.index[VQ.in] + "'");
						
						imsg("결제가 완료되었습니다.");
						dispose();
						stack.pop().setVisible(true);
					}
				} else {
					dispose();
					stack.pop().setVisible(true);
				}
			});
		}
		size(pp, 300, 50);
		p.add(pp = new JPanel(new FlowLayout(FlowLayout.CENTER)));
		p.add(pp = new JPanel(new FlowLayout(FlowLayout.CENTER)));

		sp.add(new JLabel("같은 카테고리 목록"), BorderLayout.NORTH);
		sp.add(ap = new JPanel(null), BorderLayout.CENTER);
		size(ap, 700, 120);
		
		jt[0].setEnabled(false);
		jt[1].setEnabled(false);
		
		up();
		slide();
		
		th.start();
		sh();
	}
	
	void up() {
		try {
			rs = DB.rs("select * from 2021지방_1.product where p_no = '" + VQ.index[VQ.in] + "'");
			if (rs.next()) {
				jt[0].setText(rs.getString(3));
				image.setIcon(new ImageIcon(new ImageIcon("datafile2/이미지/" + rs.getString(3) + ".jpg").getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
				jt[1].setText(won.format(rs.getInt(4)));
				stock = rs.getInt(5);
				jtx.setText(rs.getString(6));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	void slide() {
		ap.removeAll();
		cnt = 0;
		try {
			rs = DB.rs("select * from 2021지방_1.product p inner join category c on c.c_no = p.c_no where p.p_no != '" + VQ.index[VQ.in] + "' and c.c_name = '" + VQ.cate + "'");
			while (rs.next()) {
				index[cnt] = rs.getInt(1);

				img[cnt] = new JLabel();
				img[cnt].setIcon(new ImageIcon(new ImageIcon("datafile2/이미지/" + rs.getString(3) + ".jpg").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
				img[cnt].addMouseListener(this);
				size(img[cnt], 100, 100);

				bp[cnt] = new JPanel(new BorderLayout());
				bp[cnt].add(img[cnt], BorderLayout.NORTH);
				bp[cnt].add(new JLabel(rs.getString(3), JLabel.CENTER), BorderLayout.CENTER);
				bp[cnt].setBounds(0, 7000, 100, 120);
				bp[cnt].setBorder(new LineBorder(Color.black));

				ap.add(bp[cnt]);
				cnt++;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		revalidate();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int k = 0;
		while(true) {
			try {
					int a = k % cnt;
					int b = (k + 1) % cnt;
					int c = (k + 2) % cnt;
					int d = (k + 3) % cnt;
					int e = (k + 4) % cnt;
					int f = (k + 5) % cnt;
					int g = (k + 6) % cnt;
					int h = (k + 7) % cnt;
					
					for (int i = 0; i > -100; i--) {
						bp[a].setBounds(i, 0, 100, 120);
						bp[b].setBounds(i + 100, 0, 100, 120);
						bp[c].setBounds(i + 200, 0, 100, 120);
						bp[d].setBounds(i + 300, 0, 100, 120);
						bp[e].setBounds(i + 400, 0, 100, 120);
						bp[f].setBounds(i + 500, 0, 100, 120);
						bp[g].setBounds(i + 600, 0, 100, 120);
						bp[h].setBounds(i + 700, 0, 100, 120);
						th.sleep(1);
					}
				th.sleep(1000);
				k++;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		for (int i = 0; i < img.length; i++) {
			if(e.getClickCount() == 2 && e.getSource() == img[i]) {
				if(i >= VQ.in) {
					VQ.in = i + 1;
				} else {
					VQ.in = i;
				}
				
				th.stop();
				
				up();
				slide();
				th = new Thread(this);
				th.start();
			}
		}
	}

	public static void main(String[] args) {
		try {
			new c_main();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
