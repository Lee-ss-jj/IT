package form;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class f_buylist extends aframe {

	JButton jb[] = new JButton[2];
	JLabel ll = new JLabel();
	JTextField jt;

	public f_buylist() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if (stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				up();
			}
		});

		fs("구매리스트");

		np.setLayout(new GridLayout(0, 2));
		np.add(p = new JPanel(new FlowLayout(FlowLayout.LEFT)));
		p.add(new JLabel(VQ.name));
		p.add(ll = new JLabel());
		p.add(jb[0] = new JButton("월 선택"));

		np.add(p = new JPanel(new FlowLayout(FlowLayout.RIGHT)));
		p.add(jb[1] = new JButton("전체보기"));

		String tn[] = "구매날짜, 상품 번호, 상품명, 상품 가격, 주문 개수, 금액".split(", ");
		cp.setBorder(new EmptyBorder(3, 3, 3, 3));
		cp.add(jsp = new JScrollPane(jta = new JTable(dtm = new DefaultTableModel(null, tn) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		})));
		jta.getTableHeader().setReorderingAllowed(false);
		jta.getTableHeader().setResizingAllowed(false);
		size(jsp, 500, 300);

		sp.setLayout(new FlowLayout(FlowLayout.RIGHT));
		sp.add(new JLabel("총 금액"));
		sp.add(jt = new JTextField(10));
		jt.setHorizontalAlignment(JTextField.RIGHT);

		jb[0].addActionListener(e -> {
			setVisible(false);
			VQ.ecnt = 1;
			new e_month();
		});

		jb[1].addActionListener(e -> {
			VQ.sus = 12;
			up();
		});

		JPopupMenu pop = new JPopupMenu();
		JMenuItem t = new JMenuItem("구매취소");
		JPopupMenu pop2 = new JPopupMenu();
		JMenuItem tt = new JMenuItem("구매내역 삭제");

		pop.add(t);
		pop2.add(tt);

		jta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.getButton() == 3) {
					int r = jta.rowAtPoint(e.getPoint());

					try {
						Date day = ymd.parse(jta.getValueAt(r, 0).toString());
						if (ymd.format(day).equals(ymd.format(now))) {
							pop.show(e.getComponent(), e.getX(), e.getY());
						} else {
							pop2.show(e.getComponent(), e.getX(), e.getY());
						}
					} catch (Exception e2) {
						// TODO: handle exception
					}

					if (r >= 0 && r <= jta.getRowCount()) {
						jta.setRowSelectionInterval(r, r);
					} else {
						jta.clearSelection();
					}
				}
				if (e.getClickCount() == 2) {
					int re = JOptionPane.showConfirmDialog(null, "재구매 하시겠습니까?", "정보", JOptionPane.YES_NO_OPTION);
					if (re != 0)
						return;

					int p_no = rei(jta.getValueAt(jta.getSelectedRow(), 1).toString());

					try {
						rs = DB.rs("select c.c_name from 2021지방_1.product p inner join category c on c.c_no= p.c_no where p.p_no = '" + p_no + "'");
						if (rs.next()) {
							VQ.cate = rs.getString(1);
						}
					} catch (Exception e2) {
						// TODO: handle exception
					}

					int k = 0;
					try {
						rs = DB.rs("select * from 2021지방_1.product p inner join category c on c.c_no= p.c_no where c.c_name = '" + VQ.cate + "'");
						while (rs.next()) {
							VQ.index[k] = rs.getInt(1);
							if (rs.getInt(1) == p_no) {
								VQ.in = k;
							}

							k++;
						}
					} catch (Exception e2) {
						// TODO: handle exception
					}

					dispose();
					stack.add(f_buylist.this);
					new d_buy();
				}
			}
		});

		t.addActionListener(e -> {
			int p_no = 0;
			int cnt = 0;
			int coupon = 0;
			try {
				rs = DB.rs("select p_no, pu_count, coupon from 2021지방_1.purchase where pu_no = '" + index[jta.getSelectedRow()] + "'");
				if (rs.next()) {
					p_no = rs.getInt(1);
					cnt = rs.getInt(2);
					coupon = rs.getInt(3);
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
			if (coupon == 1) {
				VQ.per10++;
			} else if (coupon == 2) {
				VQ.per30++;
			}
			sqlup("update user set u_10percent = '" + VQ.per10 + "', u_30percent = '" + VQ.per30 + "' where u_no = '" + VQ.no + "'");
			sqlup("update product set p_stock = p_stock + " + cnt + " where p_no = '" + p_no + "'");
			sqlup("delete from purchase where pu_no = '" + index[jta.getSelectedRow()] + "'");
			imsg("취소되었습니다.");
			up();
		});

		tt.addActionListener(e -> {
			sqlup("delete from purchase where pu_no = '" + index[jta.getSelectedRow()] + "'");
			imsg("삭제되었습니다.");
			up();
		});

		cell.setHorizontalAlignment(0);
		for (int i = 0; i < tn.length; i++) {
			jta.getColumnModel().getColumn(i).setCellRenderer(cell);
		}

		up();
		sh();
	}

	int index[] = new int[1000];

	void up() {
		bl = "";
		if (VQ.sus == 12) {
			ll.setText("전체");
			for (int i = 0; i < VQ.mon.length; i++) {
				VQ.mon[i] = i + 1 + "월";
			}
		} else {
			for (int i = 0; i < VQ.sus; i++) {
				bl = bl + VQ.mon[i] + ",";
			}
			ll.setText(bl.substring(0, bl.length() - 1));
		}

		dtm.setRowCount(0);

		int pay = 0;
		int total = 0;

		int k = 0;
		for (int i = 0; i < VQ.sus; i++) {
			try {
				rs = DB.rs("select pu.pu_no, p.p_no, pu.pu_date, p.p_name, p.p_price, pu.pu_count, (pu.pu_count * p.p_price), pu.coupon from 2021지방_1.product p inner join purchase pu on pu.p_no = p.p_no where date_format(pu.pu_date , '%Y년%m월%d일') like '%" + VQ.mon[i] + "%' and pu.u_no = '" + VQ.no + "' order by pu.pu_date asc");
				while (rs.next()) {
					index[k] = rs.getInt(1);
					pay = rs.getInt(7);
					if (rs.getInt(8) == 1) {
						pay = (int) (pay * 0.9);
					} else if (rs.getInt(8) == 2) {
						pay = (int) (pay * 0.7);
					}
					dtm.addRow(new String[] { rs.getString(3), rs.getString(2), rs.getString(4), won.format(rs.getInt(5)), rs.getString(6), won.format(pay) });
					total += pay;
					k++;
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		jt.setText(won.format(total));
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
			new e_month();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
