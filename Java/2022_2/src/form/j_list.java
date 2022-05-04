package form;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class j_list extends aframe {
	
	public j_list() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				up();
			}
		});
		fs("게임리스트");
		
		np.setBorder(new EmptyBorder(10, 10, 10, 10));
		np.add(jl = new JLabel("회원명 : " + VQ.u_name, 0));
		font(jl, 0, 36);
		
		cp.setBorder(new EmptyBorder(5, 5, 5, 5));
		String tn[] = "번호 날짜 지점명 장르 테마명".split(" ");
		cp.add(jsp = new JScrollPane(jta = new JTable(dtm = new DefaultTableModel(null, tn) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		})));
		jta.getTableHeader().setReorderingAllowed(false);
		jta.getTableHeader().setResizingAllowed(false);
		size(jsp, 700, 350);
		
		int sz[] = { 50, 80, 100, 50, 100 };
		cell.setHorizontalAlignment(0);
		for (int i = 0; i < sz.length; i++) {
			jta.getColumnModel().getColumn(i).setPreferredWidth(sz[i]);
			jta.getColumnModel().getColumn(i).setCellRenderer(cell);
		}
		
		jta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				String date = (String) jta.getValueAt(jta.getSelectedRow(), 1);
				VQ.r_no = (int) jta.getValueAt(jta.getSelectedRow(), 0);
				try {
					Date day = ymd.parse(date);
					int d = day.compareTo(now);
					if (d > 0) {
						throw new Exception();
					}
				} catch (Exception e2) {
					wmsg("미래로 예약된 게임은 실행할 수 없습니다.");
					return;
				}
				try {
					rs = DB.rs("select * from theme where t_name = '" + (String) jta.getValueAt(jta.getSelectedRow(), 4) + "'");
					if (rs.next()) {
						VQ.t_time = rs.getInt("t_time");
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				stack.add(j_list.this);
				dispose();
				new k_game();
			}
		});
		
		up();
		sh();
	}
	
	void up() {
		dtm.setRowCount(0);
		try {
			rs = DB.rs("select * from reservation r inner join user u on u.u_no = r.u_no inner join cafe c on c.c_no = r.c_no inner join theme t on t.t_no = r.t_no inner join genre g on g.g_no = t.g_no"
					+ " where r.r_attend = 0 and u.u_no = " + VQ.u_no);
			while (rs.next()) {
				dtm.addRow(new Object[] { rs.getInt("r_no"), rs.getString("r_date"), rs.getString("c_name"), rs.getString("g_name"), rs.getString("t_name") });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			new j_list();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
