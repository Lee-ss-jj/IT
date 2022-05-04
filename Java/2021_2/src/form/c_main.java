package form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class c_main extends aframe {

	JLabel img[] = new JLabel[1000];
	JLabel cl[] = new JLabel[1000];
	JButton jb[] = new JButton[3];
	JTextField jt[] = new JTextField[3];

	public c_main() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if (stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}
		});
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				cl[0].setForeground(Color.red);
				VQ.cate = cl[0].getText();
				up();
			}
		});

		fs("상품목록");

		np.setLayout(new GridLayout(0, 2));
		np.add(p = new JPanel(new FlowLayout(FlowLayout.LEFT)));
		p.add(jl = new JLabel("회원 : " + VQ.name));
		jl.setFont(new Font("", 1, 24));

		np.add(p = new JPanel(new FlowLayout(FlowLayout.RIGHT)));
		p.add(jb[0] = new JButton("출석 이벤트"));

		wp.add(p = new JPanel(new GridLayout(0, 1)), BorderLayout.NORTH);
		p.setBorder(new EmptyBorder(0, 20, 0, 20));
		String ln[] = "상품명, 최저 가격, 최대 가격".split(", ");
		for (int i = 0; i < ln.length; i++) {
			p.add(pp = new JPanel(new GridLayout(0, 2)));
			pp.add(new JLabel(ln[i]));
			pp.add(jt[i] = new JTextField(10));
			pp.setBorder(new EmptyBorder(5, 5, 5, 5));
		}
		p.add(jb[1] = new JButton("검색"));

		wp.add(jsp = new JScrollPane(jp1 = new JPanel(new GridLayout(0, 1, 15, 15))), BorderLayout.CENTER);
		jp1.add(jl = new JLabel("카테고리"));
		jl.setFont(new Font("", 1, 20));

		try {
			int k = 0;
			rs = DB.rs("select * from category");
			while (rs.next()) {
				jp1.add(cl[k] = new JLabel(rs.getString(2)));
				cl[k].setFont(new Font("", 1, 16));
				cl[k].addMouseListener(this);

				k++;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		size(jsp, 300, 350);

		wp.add(p = new JPanel(), BorderLayout.SOUTH);
		p.add(jb[2] = new JButton("구매목록"));

		cp.setBorder(new EmptyBorder(5, 5, 5, 5));
		cp.add(jsp = new JScrollPane(ap = new JPanel(new GridLayout(0, 3))), BorderLayout.CENTER);
		size(jsp, 750, 420);

		String tn[] = "상품번호, 상품 카테고리, 상품 이름, 상품 가격, 상품 재고, 상품 설명".split(", ");
		cp.add(jsp = new JScrollPane(jta = new JTable(dtm = new DefaultTableModel(null, tn) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		})), BorderLayout.SOUTH);
		jta.getTableHeader().setReorderingAllowed(false);
		jta.getTableHeader().setResizingAllowed(false);
		size(jsp, 750, 100);

		VQ.cate = cl[0].getText();

		jb[0].addActionListener(e -> {
			stack.add(this);
			dispose();
			new g_cal();
		});
		jb[1].addActionListener(e -> {
			up();
		});
		jb[2].addActionListener(e -> {
			VQ.ecnt = 0;
			stack.add(this);
			dispose();
			new e_month();
		});

		jta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.getClickCount() == 2) {
					VQ.in = jta.getSelectedRow();
					stack.add(c_main.this);
					dispose();
					new d_buy();
				}
			}
		});

		up();
		sh();
	}

	void up() {
		int max = Integer.MAX_VALUE, min = Integer.MIN_VALUE;

		if (!jt[1].getText().equals("")) {
			min = rei(jt[1].getText());
		} if (!jt[2].getText().equals("")) {
			max = rei(jt[2].getText());
		}

		for (int i = 0; i < jt.length; i++) {
			if (jt[i].getText().equals(""))
				continue;
			try {
				int d = Integer.parseInt(jt[i].getText());
			} catch (Exception e) {
				// TODO: handle exception
				wmsg("최대 가격과 최저 가격은 숫자로 입력해주세요.");
				jt[i].setText("");
				return;
			}
		}

		if (min > max) {
			wmsg("최대 가격은 최저 가격보다 커야 합니다.");
		}
		int k = 0;
		
		ap.removeAll();
		dtm.setRowCount(0);
		try {
			rs = DB.rs(
					"select p.p_no, c.c_name, p.p_name, p.p_price, p.p_stock, p.p_explanation from 2021지방_1.product p inner join category c on c.c_no = p.c_no where c_name = '"
							+ VQ.cate + "' and p_price between '" + min + "' and '" + max + "' and p.p_name like '%"
							+ jt[0].getText() + "%'");
			while (rs.next()) {
				VQ.index[k] = rs.getInt(1);

				ap.add(p = new JPanel(new BorderLayout()));
				p.setBorder(new LineBorder(Color.black));

				p.add(img[k] = new JLabel(), BorderLayout.CENTER);
				img[k].setIcon(new ImageIcon(new ImageIcon("datafile2/이미지/" + rs.getString(3) + ".jpg").getImage()
						.getScaledInstance(240, 180, Image.SCALE_SMOOTH)));
				size(img[k], 240, 180);

				img[k].addMouseListener(this);
				img[k].setToolTipText(rs.getString(6));
				ToolTipManager tool = ToolTipManager.sharedInstance();
				tool.setInitialDelay(0);

				p.add(new JLabel(rs.getString(3), JLabel.CENTER), BorderLayout.SOUTH);
				dtm.addRow(new String[] { rs.getString(1), rs.getString(2), rs.getString(3), won.format(rs.getInt(4)),
						rs.getString(5), rs.getString(6) });
				k++;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		revalidate();
		repaint();
		
		if (k == 0) {
			imsg("검색결과가 없습니다.");
			return;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		for (int i = 0; i < cl.length; i++) {
			try {
				if (e.getSource() == cl[i] && e.getClickCount() == 2) {
					jt[0].setText("");
					jt[1].setText("");
					jt[2].setText("");
					VQ.cate = cl[i].getText();
					cl[i].setForeground(Color.red);
					up();
				} else if (e.getClickCount() == 2) {
					cl[i].setForeground(Color.black);
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		for (int i = 0; i < img.length; i++) {
			if (e.getSource() == img[i] && e.getClickCount() == 2) {
				VQ.in = i;
				stack.add(c_main.this);
				dispose();
				new d_buy();
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
