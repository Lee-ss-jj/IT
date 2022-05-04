package form;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class h_friend extends aframe {

	String tt = "";
	String name = "asc";
	String date = "asc";
	String phone = "asc";
	String title = "u_no asc";
	JTextField jt;
	JCheckBox jch;
	JButton jb[] = new JButton[3];

	public h_friend() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if (stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}
		});

		fs("친구목록");

		np.setLayout(new GridLayout(0, 2));
		np.add(p = new JPanel(new FlowLayout(FlowLayout.LEFT)));
		p.add(new JLabel("이름"));
		p.add(jt = new JTextField(14));
		p.add(jb[0] = new JButton("검색"));

		np.add(p = new JPanel(new FlowLayout(FlowLayout.RIGHT)));
		p.add(jch = new JCheckBox("전체 선택"));
		p.add(jb[1] = new JButton("초기화"));

		cp.setBorder(new EmptyBorder(3, 3, 3, 3));
		String tn[] = " , 이름, 생년월일, 전화번호".split(", ");
		cp.add(jsp = new JScrollPane(jta = new JTable(dtm = new DefaultTableModel(null, tn) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				// TODO Auto-generated method stub
				if (columnIndex == 0) {
					return Boolean.class;
				} else {
					return String.class;
				}
			}
		}) {
			public java.awt.Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row,
					int column) {
				Component c = super.prepareRenderer(renderer, row, column);

				if (jta.getSelectedRow() == row) {
					c.setBackground(Color.yellow);
				} else {
					c.setBackground(null);
				}
				return c;
			};
		}));
		jta.getTableHeader().setReorderingAllowed(false);
		jta.getTableHeader().setResizingAllowed(false);
		size(jsp, 500, 300);

		sp.setLayout(new FlowLayout(FlowLayout.RIGHT));
		sp.add(jb[2] = new JButton("보내기"));

		cell.setHorizontalAlignment(0);
		
	    int w[] = { 40, 130, 130, 300 };
	    for (int i = 0; i < w.length; i++) {
	    	if (i != 0) {
	    		jta.getColumnModel().getColumn(i).setCellRenderer(cell);
	    	}
	    	jta.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
	    }

		jta.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				int c = jta.columnAtPoint(e.getPoint());
				if (c == 1) {
					if (name.equals("asc")) {
						name = "desc";
					} else {
						name = "asc";
					}
					tt = "u_name ";
					title = tt + name;
				} else if (c == 2) {
					if (date.equals("asc")) {
						date = "desc";
					} else {
						date = "asc";
					}
					tt = "u_date ";
					title = tt + date;
				} else if (c == 3) {
					if (phone.equals("asc")) {
						phone = "desc";
					} else {
						phone = "asc";
					}
					tt = "u_phone ";
					title = tt + phone;
				}
				up();
			}
		});

		jb[0].addActionListener(e -> {
			jch.setSelected(false);
			up();
		});

		jb[1].addActionListener(e -> {
			title = "u_no asc";
			name = "asc";
			date = "asc";
			phone = "asc";
			tt = "";
			jch.setSelected(false);
			jt.setText("");
			
			up();
		});

		jb[2].addActionListener(e -> {
			int cnt = 0;
			for (int i = 0; i < jta.getRowCount(); i++) {
				if (jta.getValueAt(i, 0).equals(true)) {
					cnt++;
				}
			}
			if (cnt == 0) {
				wmsg("청첩장을 보낼 친구를 선택해주세요.");
				return;
			}

			for (int i = 0; i < jta.getRowCount(); i++) {
				if (jta.getValueAt(i, 0).equals(true)) {
					sqlup("insert into invitation values('0','" + VQ.p_no + "','" + VQ.no + "','" + VQ.index[i] + "')");
				}
			}
			stack.pop();
			imsg("청첩장을 보냈습니다.");
			dispose();
			stack.pop().setVisible(true);
		});

		jch.addActionListener(e -> {
			int cnt = 0;
			for (int i = 0; i < jta.getRowCount(); i++) {
				if (jch.isSelected()) {
					jta.setValueAt(true, i, 0);
				} else {
					jta.setValueAt(false, i, 0);
				}
			}
		});

		jta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				int cnt = 0;
				for (int i = 0; i < jta.getRowCount(); i++) {
					if (jta.getValueAt(i, 0).equals(true)) {
						cnt++;
					}
				}

				if (cnt == jta.getRowCount()) {
					jch.setSelected(true);
				} else {
					jch.setSelected(false);

				}
			}
		});

		up();
		sh();
	}

	void up() {
		int k = 0;
		dtm.setRowCount(0);
		try {
			rs = DB.rs("select u_no, u_name, u_date, u_phone from 2021지방_2.user where u_no !=  '" + VQ.no + "' and u_name like '%" + jt.getText() + "%' order by " + title + "");
			while (rs.next()) {
				VQ.index[k] = rs.getInt(1);
				dtm.addRow(new Object[] { false, rs.getString(2), rs.getString(3), rs.getString(4) });

				k++;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
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
			new h_friend();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
