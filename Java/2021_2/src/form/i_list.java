package form;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class i_list extends aframe {
	
	JComboBox<String> jc;
	
	public i_list() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if(stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}
		});
		
		fs("상품관리");
		
		String tn[] = "상품 번호, 상품 카테고리, 상품명, 상품 가격, 상품 재고, 상품 설명".split(", ");
		cp.setLayout(new FlowLayout());
		cp.add(jsp = new JScrollPane(jta = new JTable(dtm = new DefaultTableModel(null, tn) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		}) {
			public java.awt.Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if(jta.getSelectedRow() == row) {
					c.setBackground(Color.yellow);
				} else {
					c.setBackground(null);
				}
				return c;
			};
		}));
		jta.getTableHeader().setReorderingAllowed(false);
		jta.getTableHeader().setResizingAllowed(false);
		size(jsp, 500, 400);
		
		sp.setLayout(new FlowLayout(FlowLayout.RIGHT));
		sp.add(jc = new JComboBox<String>());
		
		jc.addItem("전체");
		try {
			rs = DB.rs("select * from category");
			while(rs.next()) {
				jc.addItem(rs.getString(2));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		jc.addActionListener(e -> {
			up();
		});
		
		cell.setHorizontalAlignment(0);
		for (int i = 0; i < tn.length; i++) {
			jta.getColumnModel().getColumn(i).setCellRenderer(cell);
		}
		
		jta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.getClickCount() == 2) {
					VQ.in = jta.getSelectedRow();
					stack.add(i_list.this);
					dispose();
					new j_su();
				}
			}
		});
		
		up();
		sh();
	}
	
	void up() {
		int cnt = 0;
		if (jc.getSelectedIndex() == 0) {
			cnt = 0;
		} else {
			cnt = 1;
		}

		dtm.setRowCount(0);

		String sql[] = {
				"select p.p_no, c.c_name, p.p_name, p.p_price, p.p_stock, p.p_explanation from 2021지방_1.product p inner join category c on c.c_no = p.c_no order by p.p_no asc",
				"select p.p_no, c.c_name, p.p_name, p.p_price, p.p_stock, p.p_explanation from 2021지방_1.product p inner join category c on c.c_no = p.c_no where c.c_name = '" + jc.getSelectedItem().toString() + "' order by p.p_no asc" };
		try {
			int k = 0;
			rs = DB.rs(sql[cnt]);
			while (rs.next()) {
				VQ.index[k] = rs.getInt(1);
				dtm.addRow(new String[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), won.format(rs.getInt(5)), rs.getString(6) });
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
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		try {
			new i_list();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
