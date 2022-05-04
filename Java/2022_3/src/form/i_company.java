package form;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class i_company extends aframe {

	File sfile;
	JButton b;
	JTextField jt[] = new JTextField[5];

	public i_company(int cno, int from) {
		if (from == 0)
			fs("기업상세정보");
		else
			fs("기업정보수정");

		np.setBorder(new EmptyBorder(5, 5, 5, 5));
		np.add(jl = new JLabel());
		jl.setOpaque(false);
		jl.setBorder(new LineBorder(Color.black));
		np.setBackground(Color.white);
		cp.setBackground(Color.white);

		cp.setLayout(new GridLayout(0, 1, 5, 5));
		String s[] = "기업명 대표자 주소 직종 직원수".split(" ");
		for (int i = 0; i < s.length; i++) {
			cp.add(p = new JPanel(new FlowLayout()));
			p.add(setcomp(new JLabel(s[i]), 80, 30));
			if (i == 3) {
				p.add(setcomp(VQ.jtt = new JTextField(15), 150, 30));
				VQ.jtt.setEnabled(false);
			} else {
				p.add(setcomp(jt[i] = new JTextField(15), 150, 30));
				jt[i].setEnabled(false);
			}
			p.setBackground(Color.white);
		}
		cp.setBackground(Color.white);

		try {
			rs = DB.rs("select * from company where c_no = " + cno);
			if (rs.next()) {
				jl.setIcon(new ImageIcon(new ImageIcon("datafiles/기업/" + rs.getString("c_name") + "1.jpg").getImage().getScaledInstance(250, 200, 4)));
				jt[0].setText(rs.getString("c_name"));
				jt[1].setText(rs.getString("c_ceo"));
				jt[2].setText(rs.getString("c_address"));
				VQ.jtt.setText(NoToStr(rs.getString("c_category")));
				jt[4].setText(rs.getString("c_employee"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sp.setBorder(new EmptyBorder(5, 5, 5, 5));
		sp.setBackground(Color.white);
		sp.add(b = new JButton());

		if (from == 0) {
			b.setText("닫기");
			b.addActionListener(e -> {
				dispose();
				stack.pop().setVisible(true);
			});
		} else {
			jl.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					JFileChooser choo = new JFileChooser();
					choo.setAcceptAllFileFilterUsed(false);
					choo.setFileFilter(new FileNameExtensionFilter("JPG Images", "jpg"));
					int re = choo.showOpenDialog(null);
					if (re != 1) {
						try {
							sfile = choo.getSelectedFile();
							jl.setIcon(new ImageIcon(new ImageIcon(sfile.toString()).getImage().getScaledInstance(250, 200, 4)));
							repaint();
							revalidate();
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				}
			});
			jt[1].setEnabled(true);
			jt[2].setEnabled(true);
			VQ.jtt.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					stack.add(i_company.this);
					dispose();
					new e_select(1);
				}
			});
			b.setText("수정");
			b.addActionListener(e -> {
				if (jt[1].getText().equals("") || jt[2].getText().equals("")) {
					wmsg("빈칸이 있습니다.");
					return;
				}
				psqlup("update company set c_ceo = ?, c_address = ?, c_category = ? where c_no = " + cno, jt[1].getText(), jt[2].getText(), StrToNo(VQ.jtt.getText()));
				try {
					if (sfile != null) {
						ImageIO.write(ImageIO.read(sfile), "jpg", new File("datafiles/기업/" + jt[0].getText() + "1.jpg"));
					}
					var ps = DB.con.prepareStatement("update company set c_img = ? where c_no = " + cno);
					ps.setBlob(1, new FileInputStream("datafiles/기업/" + jt[0].getText() + "1.jpg"));
					ps.executeUpdate();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				imsg("수정이 완료되었습니다.");
				dispose();
				stack.pop().setVisible(true);
			});
		}

		sh();
	}

	public static void main(String[] args) {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
