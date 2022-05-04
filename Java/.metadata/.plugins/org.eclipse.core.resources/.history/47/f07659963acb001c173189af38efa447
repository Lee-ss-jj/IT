package form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class j_adinfo extends aframe {
	
	int cno;
	String cname;
	JButton b;
	
	public j_adinfo() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				up();
			}
		});
		fs("관리자 채용정보");
		
		np.setBorder(new EmptyBorder(10, 10, 10, 10));
		np.add(jl = new JLabel("관리자 채용정보", 0));
		font(jl, 0, 30);
		np.add(b = new JButton("공고수정"), BorderLayout.EAST);
		
		cp.setBorder(new EmptyBorder(10, 10, 10, 10));
		String tn[] = "번호 이름 이미지 공고명 모집인원 시급 직종 지역 학력 성별".split(" ");
		cp.add(jsp = new JScrollPane(jta = new JTable(dtm = new DefaultTableModel(null, tn)) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				// TODO Auto-generated method stub
				if (column == 0) {
					return (JLabel) jta.getValueAt(row, column);
				}
				return super.prepareRenderer(renderer, row, column);
			}
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		}));
		jta.setRowHeight(50);
		jta.setSelectionMode(0);
		jta.removeColumn(jta.getColumn("번호"));
		jta.removeColumn(jta.getColumn("이름"));
		jta.getTableHeader().setReorderingAllowed(false);
		jta.getTableHeader().setResizingAllowed(false);
		size(jsp, 800, 450);
		
		int sz[] = { 40, 120, 40, 10, 120, 120, 50, 10 };
		cell.setHorizontalAlignment(0);
		for (int i = 0; i < sz.length; i++) {
			jta.getColumnModel().getColumn(i).setPreferredWidth(sz[i]);
			jta.getColumnModel().getColumn(i).setCellRenderer(cell);
		}
		
		b.addActionListener(e -> {
			if (jta.getSelectedRow() == -1) {
				wmsg("수정할 공고를 선택하세요.");
				return;
			}
			cno = (int) dtm.getValueAt(jta.getSelectedRow(), 0);
			cname = (String) jta.getValueAt(jta.getSelectedRow(), 1);
			stack.add(this);
			dispose();
			new k_noti(cno, cname);
		});
		
		sh();
	}
	
	void up() {
		dtm.setRowCount(0);
		int k = 0;
		try {
			rs = DB.rs("select *, count(e.e_no) as cnt from employment e inner join applicant a on e.e_no = a.e_no inner join company c on c.c_no = e.c_no"
					+ " where a_apply != 2 group by e.e_no");
			while (rs.next()) {
				if(rs.getInt("cnt") == rs.getInt("e_people")) continue;
				dtm.addRow(new Object[] { rs.getInt("c_no"), rs.getString("c_name"), jl = new JLabel(), rs.getString("e_title"), rs.getString("e_people"), won.format(rs.getInt("e_pay")),
						NoToStr(rs.getString("c_category")), rs.getString("c_address"), VQ.graduatelist[rs.getInt("e_graduate")], VQ.genderlist[rs.getInt("e_gender")]});
				jl.setIcon(new ImageIcon(new ImageIcon("datafiles/기업/" + rs.getString("c_name") + "2.jpg").getImage().getScaledInstance(70, 50, 4)));
				k++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			rs = DB.rs("select * from employment e inner join company c on e.c_no = c.c_no limit " + k + ", 5");
			while (rs.next()) {
				dtm.addRow(new Object[] { rs.getInt("c_no"), rs.getString("c_name"), jl = new JLabel(), rs.getString("e_title"), rs.getString("e_people"), won.format(rs.getInt("e_pay")),
						NoToStr(rs.getString("c_category")), rs.getString("c_address"), VQ.graduatelist[rs.getInt("e_graduate")], VQ.genderlist[rs.getInt("e_gender")]});
				jl.setIcon(new ImageIcon(new ImageIcon("datafiles/기업/" + rs.getString("c_name") + "2.jpg").getImage().getScaledInstance(70, 50, 4)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		repaint();
		revalidate();
	}
	
	public static void main(String[] args) {
		try {
			new j_adinfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
