package form;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class g_mypage extends aframe {
	
	JButton b;
	JPopupMenu pop = new JPopupMenu();
	JMenuItem item;
	
	public g_mypage() {
		fs("마이페이지");
		
		np.setBorder(new EmptyBorder(10, 10, 10, 10));
		np.add(jl = new JLabel("Mypage", 0), BorderLayout.NORTH);
		font(jl, 0, 30);
		
		np.add(p = new JPanel(new GridLayout(0, 1)));
		p.add(jl = new JLabel("성명 : " + VQ.u_name));
		font(jl, 0, 20);
		
		p.add(jl = new JLabel("성별 : " + VQ.genderlist[VQ.u_gender]));
		font(jl, 0, 16);
		
		p.add(jl = new JLabel("최종학력 : " + VQ.graduatelist[VQ.u_graduate]));
		font(jl, 0, 16);
		
		cp.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		String tn[] = "no 번호 기업명 모집정보 시급 모집정원 최종학력 성별 합격여부".split(" ");
		cp.add(jsp = new JScrollPane(jta = new JTable(dtm = new DefaultTableModel(null, tn) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		})));
		jta.setSelectionMode(0);
		jta.removeColumn(jta.getColumn("no"));
		jta.getTableHeader().setReorderingAllowed(false);
		jta.getTableHeader().setResizingAllowed(false);
		size(jsp, 800, 150);
		
		int sz[] = { 20, 80, 200, 80, 30, 50, 30, 30 };
		cell.setHorizontalAlignment(0);
		for (int i = 0; i < sz.length; i++) {
			jta.getColumnModel().getColumn(i).setPreferredWidth(sz[i]);
			jta.getColumnModel().getColumn(i).setCellRenderer(cell);
		}
		
		sp.setBorder(new EmptyBorder(10, 10, 10, 10));
		sp.setLayout(new FlowLayout(3));
		sp.add(b = new JButton("PDF 인쇄"));
		
		item = new JMenuItem("삭제");
		pop.add(item);
		
		item.addActionListener(e -> {
			int ano = Integer.parseInt((String) dtm.getValueAt(jta.getSelectedRow(), 0));
			sqlup("delete from applicant where a_no = " + ano);
			imsg("삭제가 완료되었습니다.");
			up();
		});
		jta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.getButton() == 3) {
					int row = jta.rowAtPoint(e.getPoint());
					jta.setRowSelectionInterval(row, row);
					if (jta.getValueAt(row, 7).equals("불합격")) {
						pop.show(jta, e.getX(), e.getY());
					}
				}
			}
		});
		b.addActionListener(e -> {
			try {
				jta.print(JTable.PrintMode.FIT_WIDTH, null, null);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		});
		
		up();
		sh();
	}
	
	void up() {
		dtm.setRowCount(0);
		int cnt = 1;
		try {
			rs = DB.rs("select * from employment e inner join applicant a on e.e_no = a.e_no inner join company c on c.c_no = e.c_no"
					+ " where u_no = " + VQ.u_no);
			while (rs.next()) {
				dtm.addRow(new Object[] { rs.getString("a_no"), cnt, rs.getString("c_name"), rs.getString("e_title"), won.format(rs.getInt("e_pay")), rs.getInt("e_people"),
						VQ.graduatelist[rs.getInt("e_graduate")], VQ.genderlist[rs.getInt("e_gender")], VQ.applylist[rs.getInt("a_apply")]});
				cnt++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			new g_mypage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
