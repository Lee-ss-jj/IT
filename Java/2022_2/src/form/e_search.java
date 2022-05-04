package form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class e_search extends aframe {

	JComboBox jc;
	JTextField jt;
	JButton b;
	JLabel img[] = new JLabel[1000], cl[] = new JLabel[1000];

	public e_search() {
		fs("검색");

		np.setBorder(new EmptyBorder(5, 5, 5, 5));
		np.add(jl = new JLabel("방탈출 카페 검색", 0));
		font(jl, 0, 28);

		cp.setBorder(new EmptyBorder(0, 10, 0, 10));
		cp.add(p = new JPanel(new FlowLayout(2)));
		p.add(new JLabel("장르"));
		p.add(jc = new JComboBox<>());
		p.add(new JLabel("테마"));
		p.add(jt = new JTextField(12));
		p.add(b = new JButton("검색"));

		sp.setBorder(new EmptyBorder(0, 10, 0, 10));
		sp.add(jsp = new JScrollPane(jta = new JTable(dtm = new DefaultTableModel("no 지역".split(" "), 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		})), BorderLayout.WEST);
		jta.removeColumn(jta.getColumn("no"));
		jta.getTableHeader().setReorderingAllowed(false);
		jta.getTableHeader().setResizingAllowed(false);
		size(jsp, 120, 400);

		cell.setHorizontalAlignment(0);
		for (int i = 0; i < jta.getColumnCount(); i++) {
			jta.getColumnModel().getColumn(i).setCellRenderer(cell);
		}

		try (var rs = DB.rs("select * from area")) {
			dtm.addRow(new Object[] { 0, "전국" });
			while (rs.next()) {
				dtm.addRow(new Object[] { rs.getInt(1), rs.getString(2) });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try (var rs = DB.rs("select * from genre")) {
			jc.addItem("전체");
			while (rs.next()) {
				jc.addItem(rs.getString(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		jta.setSelectionMode(0);
		jta.setRowSelectionInterval(0, 0);

		sp.setBorder(new EmptyBorder(5, 5, 5, 5));
		sp.add(jsp = new JScrollPane(ap = new JPanel(new GridLayout(0, 3))), BorderLayout.CENTER);
		size(jsp, 780, 400);

		b.addActionListener(e -> {
			up();
		});
		jta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				jc.setSelectedIndex(0);
				jt.setText("");
				up();
			}
		});

		up();
		sh();
	}

	void up() {
		int k = 0;
		int ano = (Integer) dtm.getValueAt(jta.getSelectedRow(), 0), gno = jc.getSelectedIndex();
		String aa = "", gg = "";

		if (ano != 0) aa = " and c.a_no = " + ano;
		if (gno != 0) gg = " and t.g_no = " + gno;

		ap.removeAll();
		try {
			rs = DB.rs("select * from cafe c cross join theme t where c.t_no like concat(t.t_no, ',%') or c.t_no like concat('%,', t.t_no) or c.t_no like concat('%,', t.t_no ,',%') or c.t_no like t.t_no"
							+ " group by c.c_no having count(if(t.t_name like '%" + jt.getText() + "%', 1, null)) > 0" + aa + gg);
			while (rs.next()) {
				String cname = rs.getString("c_name");
				ap.add(p = new JPanel(new BorderLayout()));
				p.setBorder(new LineBorder(Color.black));
				
				p.add(img[k] = new JLabel(), BorderLayout.CENTER);
				img[k].setIcon(new ImageIcon(new ImageIcon("datafiles/지점/" + rs.getString("c_name").split(" ")[0] + ".jpg").getImage().getScaledInstance(250, 180, 4)));
				img[k].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
						try {
							if (e.getClickCount() == 2) {
								VQ.c_name = cname;
								stack.add(e_search.this);
								dispose();
								new f_info();
							}
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				});
				p.add(new JLabel(rs.getString("c_name"), 0), BorderLayout.SOUTH);
				k++;
			}
			if (k == 0) {
				wmsg("검색결과가 없습니다.");
				jc.setSelectedIndex(0);
				jta.setRowSelectionInterval(0, 0);
				jt.setText("");
				up();
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			new e_search();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
