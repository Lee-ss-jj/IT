package form;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class j_su extends aframe {

	JLabel img;
	JComboBox jc;
	BufferedImage buf;
	JButton jb[] = new JButton[2], b;
	JTextField jt[] = new JTextField[5], jtt;

	public j_su() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if(stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}
		});

		fs("상품수정");

		np.add(img = new JLabel(), BorderLayout.NORTH);
		size(img, 330, 200);

		np.add(b = new JButton("사진 넣기"));
		b.addActionListener(e -> {
			try {
				JFileChooser choo = new JFileChooser();
				int re = choo.showOpenDialog(this);
				if (re == 0) {
					buf = ImageIO.read(choo.getSelectedFile());
					img.setIcon(new ImageIcon(
							new ImageIcon(buf).getImage().getScaledInstance(330, 200, Image.SCALE_SMOOTH)));
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		});

		cp.setLayout(new GridLayout(0, 1));
		String ln[] = "상품명: 카테고리: 가격: 재고: 설명:".split(" ");
		for (int i = 0; i < ln.length; i++) {
			cp.add(p = new JPanel(new FlowLayout(FlowLayout.LEFT)));
			p.add(setcomp(new JLabel(ln[i], JLabel.LEFT), 65, 25));
			if (i == 1) {
				p.add(jc = new JComboBox());
				size(jc, 105, 25);
				p.add(jtt = new JTextField(10));
			} else if (i == 4) {
				p.add(jt[i] = new JTextField(20));
			} else {
				p.add(jt[i] = new JTextField(10));
			}
		}

		sp.setLayout(new FlowLayout(FlowLayout.RIGHT));
		String bn[] = "수정, 취소".split(", ");
		for (int j = 0; j < bn.length; j++) {
			sp.add(jb[j] = new JButton(bn[j]));
			int n = j;
			jb[j].addActionListener(e -> {
				if (n == 0) {
					if (jt[2].getText().equals("") || jt[3].getText().equals("") || jt[4].getText().equals("")) {
						wmsg("빈칸이 있습니다.");
						return;
					}
					boolean tf = jc.getSelectedItem().toString().equals("기타");
					try {
						rs = DB.rs("select * from category where c_name = '" + jtt.getText() + "'");
						if (rs.next()) {
							wmsg("이미 있는 카테고리입니다.");
							return;
						}
					} catch (Exception e2) {
						// TODO: handle exception
					}
					if (tf && jtt.getText().equals("")) {
						wmsg("카테고리를 입력해 주세요.");
						return;
					}
					try {
						int d = Integer.parseInt(jt[2].getText());
						int dd = Integer.parseInt(jt[3].getText());
					} catch (Exception e2) {
						// TODO: handle exception
						wmsg("숫자로 입력하세요.");
						return;
					}
					int ok1 = 0, ok2 = 0;
					if (buf == null) {
						ok1 = 1;
					}
					String t = "";
					if (tf) {
						t = jtt.getText();
					} else {
						t = jc.getSelectedItem().toString();
					}

					try {
						rs = DB.rs(
								"select * from 2021지방_1.product p inner join category c on c.c_no = p.c_no where p.p_price = '" + jt[2].getText() + "' and p.p_stock = '" + jt[3].getText() + "' and p.p_explanation = '" + jt[4].getText() + "' and c.c_name = '" + t + "' and p_no = '" + VQ.index[VQ.in] + "'");
						if (rs.next()) {
							ok2 = 1;
						}
					} catch (Exception e2) {
						// TODO: handle exception
					}
					if (ok1 == 1 && ok2 == 1) {
						wmsg("수정한 내용이 없습니다.");
						return;
					}

					try {
						ImageIO.write(buf, "jpg", new File("datafile2/이미지/" + jt[0].getText() + ".jpg"));
					} catch (Exception e2) {
						// TODO: handle exception
					}

					if (tf) {
						sqlup("insert into category values('0','" + jtt.getText() + "')");
					}
					int c_no = 0;
					try {
						rs = DB.rs("select * from category where c_name = '" + t + "'");
						if (rs.next()) {
							c_no = rs.getInt(1);
						}
					} catch (Exception e2) {
						// TODO: handle exception
					}
					sqlup("update product set c_no = '" + c_no + "', p_price = '" + jt[2].getText() + "', p_stock = '" + jt[3].getText() + "', p_explanation = '" + jt[4].getText() + "' where p_no = '" + VQ.index[VQ.in] + "'");
					imsg("상품정보가 수정되었습니다.");
					dispose();
					stack.pop().setVisible(true);
				} else {
					dispose();
					stack.pop().setVisible(true);
				}
			});
		}
		try {
			rs = DB.rs("select * from category");
			while (rs.next()) {
				jc.addItem(rs.getString(2));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		jc.addItem("기타");

		try {
			rs = DB.rs("select p.p_name, c.c_name, p.p_price, p.p_stock, p.p_explanation from 2021지방_1.product p inner join category c on c.c_no = p.c_no where p.p_no = '" + VQ.index[VQ.in] + "'");
			if (rs.next()) {
				img.setIcon(new ImageIcon(new ImageIcon("datafile2/이미지/" + rs.getString(1) + ".jpg").getImage() .getScaledInstance(300, 200, Image.SCALE_SMOOTH)));
				jt[0].setText(rs.getString(1));
				jt[0].setEnabled(false);
				jc.setSelectedItem(rs.getString(2));
				jt[2].setText(rs.getString(3));
				jt[3].setText(rs.getString(4));
				jt[4].setText(rs.getString(5));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		jtt.setVisible(false);
		jc.addActionListener(e -> {
			if (jc.getSelectedItem().toString().equals("기타")) {
				jtt.setVisible(true);
			} else {
				jtt.setVisible(false);
			}
			revalidate();
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
		super.mouseClicked(e);
	}

	public static void main(String[] args) {
		try {
			new j_su();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}