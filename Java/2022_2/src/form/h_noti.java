package form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class h_noti extends aframe {
	
	int nno, cnt;
	int page, maxpage;
	JLabel pl, ml;
	JButton b[] = new JButton[4], bb;
	JComboBox jc = new JComboBox<>();
	JTextField jt[] = new JTextField[2];
	JTextArea jtx;
	boolean me;
	
	public h_noti() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				up();
			}
		});
		fs("게시판");
		
		page = 1;
		np.add(p = new JPanel(new FlowLayout(0)), BorderLayout.WEST);
		p.add(new JLabel("페이지 정보 : "));
		p.add(pl = new JLabel(page + ""));
		p.add(new JLabel("/"));
		p.add(ml = new JLabel());
		p.add(b[0] = new JButton("◀"));
		p.add(b[1] = new JButton("▶"));
		b[0].setEnabled(false);
		b[0].addActionListener(e -> {
			page--;
			b[1].setEnabled(true);
			if (page == 1) {
				b[0].setEnabled(false);
			}
			up();
		});
		b[1].addActionListener(e -> {
			page++;
			b[0].setEnabled(true);
			if (page == maxpage) {
				b[1].setEnabled(false);
			}
			up();
		});
		
		np.add(p = new JPanel(new FlowLayout(2)), BorderLayout.EAST);
		p.add(setcomp(new JLabel("분류 : ", 4), 200, 25));
		p.add(jc = new JComboBox<>());
		p.add(jt[0] = new JTextField(10));
		p.add(b[2] = new JButton("검색"));
		p.add(b[3] = new JButton("게시물 작성"));
		b[2].addActionListener(e -> {
			page = 1;
			size(ep, 0, 0);
			ep.removeAll();
			pack();
			ep.repaint();
			up();
		});
		b[3].addActionListener(e -> {
			stack.add(this);
			dispose();
			new i_ins();
		});
		
		String tn[] = "번호 제목 아이디 등록일 조회".split(" ");
		cp.add(jsp = new JScrollPane(jta = new JTable(dtm = new DefaultTableModel(null, tn) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		})));
		size(jsp, 700, 380);
		cell = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				// TODO Auto-generated method stub
				var comp = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (dtm.getValueAt(row, 2).equals(VQ.u_id)) {
					comp.setBackground(Color.blue);
					comp.setForeground(Color.white);
				} else {
					comp.setBackground(null);
					comp.setForeground(null);
				}
				return comp;
			}
		};
		
		int sz[] = { 30, 200, 70, 70, 30 };
		cell.setHorizontalAlignment(0);
		for (int i = 0; i < sz.length; i++) {
			jta.getColumnModel().getColumn(i).setPreferredWidth(sz[i]);
			jta.getColumnModel().getColumn(i).setCellRenderer(cell);
		}
		jta.setRowHeight(35);
		jta.getTableHeader().setReorderingAllowed(false);
		jta.getTableHeader().setResizingAllowed(false);

		jc.addItem("제목");
		jc.addItem("아이디");
		
		jta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				me = false;
				if (jta.getValueAt(jta.getSelectedRow(), 2).equals(VQ.u_id)) {
					me = true;
				}
				if (nno == (int) jta.getValueAt(jta.getSelectedRow(), 0)) {
					ep.removeAll();
					nno = 0;
					size(ep, 0, 0);
					page = 1;
					jt[0].setText("");
					up();
					pack();
					repaint();
					return;
				}
				nno = (int) jta.getValueAt(jta.getSelectedRow(), 0);
				pageUp();
			}
		});
		
		up();
		sh();
	}
	
	void up() {
		String title = "", id = "";
		
		if (jc.getSelectedIndex() == 0 && !jt[0].getText().equals("")) {
			title = " and n.n_title like '%" + jt[0].getText() + "%'";
		}
		if (jc.getSelectedIndex() == 1 && !jt[0].getText().equals("")) {
			id = " and u.u_id like '%" + jt[0].getText() + "%'";
		}
		dtm.setRowCount(0);
		b[1].setEnabled(true);
		
		int k = 0;
		cnt = 10 * page - 10;
		pl.setText(page + "");
		try {
			rs = DB.rs("select * from notice n inner join user u on u.u_no = n.u_no" + title + id + 
					" where n.n_open = 1 or u.u_no = " + VQ.u_no + " order by n.n_no asc");
			while (rs.next()) {
				k++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (k == 0) {
			b[0].setEnabled(false);
			b[1].setEnabled(false);
		}
		try {
			rs = DB.rs("select * from notice n inner join user u on u.u_no = n.u_no" + title + id +
					" where n.n_open = 1 or u.u_no = " + VQ.u_no + " order by n.n_no asc limit " + cnt + ", 10");
			while (rs.next()) {
				if (jc.getSelectedIndex() == 0 && !rs.getString("n_title").contains(jt[0].getText())) {
					continue;
				} else if (jc.getSelectedIndex() == 1 && !rs.getString("u_id").contains(jt[0].getText())) {
					continue;
				}
				dtm.addRow(new Object[] { rs.getInt("n_no"), rs.getString("n_title"), rs.getString("u_id"), rs.getString("n_date"), rs.getInt("n_viewcount") });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		maxpage = k % 10 == 0 ? k / 10 : (k / 10) + 1;
		if (maxpage == 0) {
			maxpage = 1;
		}
		if (page == maxpage) {
			b[1].setEnabled(false);
		} else if (page == 1) {
			b[0].setEnabled(false);
		}
		ml.setText(maxpage + "");
	}
	
	void pageUp() {
		ep.removeAll();
		ep.setBorder(new EmptyBorder(5, 5, 5, 5));
		ep.add(p = new JPanel(new FlowLayout()), BorderLayout.NORTH);
		p.add(jl = new JLabel("제목 : "));
		font(jl, 0, 20);
		p.add(jt[1] = new JTextField(16));
		ep.add(jtx = new JTextArea());
		if (me) {
			jt[1].setEnabled(true);
			jtx.setEnabled(true);
			p.add(bb = new JButton("수정"));
			bb.addActionListener(e -> {
				psqlup("update notice set n_title = ?, n_content = ? where n_no = ?", jt[1].getText(), jtx.getText(), nno + "");
				imsg("수정이 완료되었습니다.");
				up();
				pageUp();
				return;
			});
		} else {
			jt[1].setEnabled(false);
			jtx.setEnabled(false);
			sqlup("update notice set n_viewcount = n_viewcount + 1 where n_no = " + nno);
		}
		jtx.setLineWrap(true);
		ep.add(p = new JPanel(new FlowLayout(2)), BorderLayout.SOUTH);
		try {
			rs = DB.rs("select * from notice where n_no = " + nno);
			if (rs.next()) {
				jt[1].setText(rs.getString("n_title"));
				jtx.setText(rs.getString("n_content"));
				p.add(new JLabel("작성일 : " + rs.getString("n_date")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		size(ep, 330, 300);
		up();
		pack();
	}
	
	public static void main(String[] args) {
		try {
			new h_noti();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
