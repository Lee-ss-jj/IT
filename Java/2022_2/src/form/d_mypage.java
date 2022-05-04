package form;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class d_mypage extends aframe {
	
	JButton b;
	JComboBox jc;
	
	public d_mypage() {
		fs("����������");
		
		np.setBorder(new EmptyBorder(5, 5, 5, 5));
		np.add(p = new JPanel(new FlowLayout(0)));
		p.add(jl = new JLabel("��¥ : "));
		font(jl, 0, 20);
		p.add(jc = new JComboBox<>());
		np.add(b = new JButton("����"), BorderLayout.EAST);
		
		cp.setBorder(new EmptyBorder(5, 5, 5, 5));
		String tn[] = "no, ��¥, �ð�, ī�� �̸�, �׸���, �ο���, ����".split(", ");
		cp.add(jsp = new JScrollPane(jta = new JTable(dtm = new DefaultTableModel(null, tn) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		})));
		jta.setSelectionMode(0);
		jta.getColumnModel().getColumn(0).setWidth(0);
		jta.getColumnModel().getColumn(0).setMinWidth(0);
		jta.getColumnModel().getColumn(0).setMaxWidth(0);
		jta.getTableHeader().setReorderingAllowed(false);
		jta.getTableHeader().setResizingAllowed(false);
		size(jsp, 700, 250);
		
		int sz[] = { 0, 30, 30, 130, 130, 30, 50 };
		cell.setHorizontalAlignment(0);
		for (int i = 0; i < sz.length; i++) {
			jta.getColumnModel().getColumn(i).setPreferredWidth(sz[i]);
			jta.getColumnModel().getColumn(i).setCellRenderer(cell);
		}
		
		jc.addItem("��ü");
		for (int i = 1; i < 13; i++) {
			jc.addItem(i + "��");
		}
		jc.addActionListener(e -> {
			up();
		});
		
		sp.setBorder(new EmptyBorder(5, 5, 5, 5));
		sp.setLayout(new FlowLayout(2));
		sp.add(l = new JLabel());
		
		b.addActionListener(e -> {
			if (jta.getSelectedRow() == -1) {
				wmsg("������ ���ڵ带  �����ϼ���.");
				return;
			}
			int no = (Integer) jta.getValueAt(jta.getSelectedRow(), 0);
			String date = (String) jta.getValueAt(jta.getSelectedRow(), 1);
			try {
				Date day = ymd.parse(date);
				int d = day.compareTo(now);
				if (d < 0) {
					wmsg("���� ������ ������ �� �����ϴ�.");
					return;
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			sqlup("delete from reservation where r_no = " + no);
			imsg("������ �Ϸ�Ǿ����ϴ�.");
			up();
		});
		
		up();
		sh();
	}
	
	void up() {
		int pay = 0;
		String month = "";
		
		dtm.setRowCount(0);
		if (jc.getSelectedIndex() != 0) {
			month = " and month(r.r_date) = "+  jc.getSelectedIndex();
		}
		try {
			rs = DB.rs("select * from reservation r inner join cafe c on r.c_no = c.c_no inner join theme t on t.t_no = c.t_no where u_no = " + VQ.u_no + month);
			while (rs.next()) {
				dtm.addRow(new Object[] { rs.getInt("r_no"), rs.getString("r_date"), rs.getString("r_time"), rs.getString("c_name"), rs.getString("t_name"), 
						rs.getInt("r_people"), won.format(rs.getInt("r_people") * rs.getInt("c_price"))});
				pay += rs.getInt("r_people") * rs.getInt("c_price");
			}
			if (pay == 0 && jc.getSelectedIndex() != 0) {
				wmsg("������Ȳ�� �����ϴ�.");
				jc.setSelectedIndex(0);
				up();
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		l.setText("�� �ݾ� : " + won.format(pay));
	}
	
	public static void main(String[] args) {
		try {
			VQ.u_id = "roo1";
			VQ.u_no = 1;
			new d_mypage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
