package form;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class d_info extends aframe {
	
	int check = 0;
	JButton b;
	JTextField jt;
	JComboBox jc[] = new JComboBox[3];
	
	public d_info() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				up();
			}
		});
		fs("채용정보");
		
		np.setBorder(new EmptyBorder(5, 5, 5, 5));
		np.add(jl = new JLabel("채용정보", 0));
		font(jl, 0, 30);
		
		cp.setLayout(new GridLayout(0, 1));
		cp.add(p = new JPanel(new FlowLayout(0)));
		p.add(setcomp(jl = new JLabel("공고명", 4), 60, 25));
		p.add(jt = new JTextField(14));
		font(jl, 0, 14);
		
		cp.add(p = new JPanel(new FlowLayout(0)));
		p.add(setcomp(jl = new JLabel("직종", 4), 60, 25));
		p.add(VQ.jt = new JTextField(14));
		VQ.jt.setEnabled(false);
		font(jl, 0, 14);
		
		cp.add(p = new JPanel(new FlowLayout(0)));
		String s[] = "지역 학력 성별".split(" ");
		String ss[][] = { "전체 서울 부산 대구 인천 광주 대전 울산 세종 경기 강원 충북 충남 전북 전남 경남 제주".split(" "), "전체, 대학교 졸업, 고등학교 졸업, 중학교 졸업, 무관".split(", "), "전체 남자 여자 무관".split(" ") };
		for (int i = 0; i < ss.length; i++) {
			p.add(setcomp(jl = new JLabel(s[i], 4), 60, 25));
			font(jl, 0, 14);
			if (i == 2) {
				p.add(setcomp(jc[i] = new JComboBox<>(ss[i]), 60, 25));
			} else {
				p.add(setcomp(jc[i] = new JComboBox<>(ss[i]), 120, 25));
			}
		}
		p.add(setcomp(new JLabel(), 150, 25));
		p.add(b = new JButton("검색하기"));
		p.add(VQ.b = new JButton("지원하기"));
		
		String tn[] = "cno eno 이미지 공고명 모집정원 시급 직종 지역 학력 성별".split(" ");
		sp.add(jsp = new JScrollPane(jta = new JTable(dtm = new DefaultTableModel(null, tn)) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				// TODO Auto-generated method stub
				if (column == 0) {
					return (JLabel) jta.getValueAt(row, column);
				} else {
					return super.prepareRenderer(renderer, row, column);
				}
			}
		}));
		jta.getTableHeader().setReorderingAllowed(false);
		jta.getTableHeader().setResizingAllowed(false);
		jta.removeColumn(jta.getColumn("eno"));
		jta.removeColumn(jta.getColumn("cno"));
		jta.setRowHeight(50);
		size(jsp, 850, 400);
		
		int sz[] = { 40, 120, 50, 40, 120, 120, 70, 20 };
		cell.setHorizontalAlignment(0);
		for (int i = 0; i < sz.length; i++) {
			jta.getColumnModel().getColumn(i).setPreferredWidth(sz[i]);
			jta.getColumnModel().getColumn(i).setCellRenderer(cell);
		}
		
		b.addActionListener(e -> {
			up();
		});
		jta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				VQ.b.setEnabled(false);
				int cno = rei(jta.getValueAt(jta.getSelectedRow(), 0).toString());
				stack.add(d_info.this);
				dispose();
				new f_apply(cno);
			}
		});
		VQ.jt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				stack.add(d_info.this);
				dispose();
				new e_select(0);
			}
		});
		VQ.b.addActionListener(e -> {
			int cno = rei(jta.getValueAt(jta.getSelectedRow(), 0).toString());
			int eno = rei(jta.getValueAt(jta.getSelectedRow(), 1).toString());
			sqlup("insert into applicant values('0'" + eno + "','" + VQ.u_no + "','0')");
			imsg("신청이 완료되었습니다.");
			check = 1;
			up();
		});
		sp.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		up();
		sh();
	}
	
	void up() {
		if (check == 0) {
			VQ.b.setEnabled(false);
		}
		dtm.setRowCount(0);
		try {
			rs = DB.rs("select *, (select count(1) from applicant a where a.e_no = e.e_no and a.a_apply < 2) as cnt from company c inner join employment e on e.c_no = c.c_no group by c.c_no");
			while (rs.next()) {
				String skill = NoToStr(rs.getString("c_category"));
	            if (rs.getInt("cnt")==rs.getInt("e_people")) {
	               continue;
	            }
	            if (!jt.getText().equals("") && !rs.getString("e_title").contains(jt.getText())) {
	               continue;
	            }
	            if (jc[0].getSelectedIndex()!=0 && !rs.getString("c_address").contains(jc[0].getSelectedItem().toString())) {
	               continue;
	            }
	            if (jc[1].getSelectedIndex()!=0 && rs.getInt("e_graduate") != jc[1].getSelectedIndex()-1) {
	               continue;
	            }
	            if (jc[2].getSelectedIndex()!=0 && rs.getInt("e_gender") != jc[2].getSelectedIndex()) {
	               continue;
	            }
	            if (!VQ.jt.getText().equals("") && Arrays.stream(skill.split(",")).allMatch(x -> VQ.jt.getText().indexOf(x) == -1)) {
	               continue;
	            }
	            
	            dtm.addRow(new Object[] { rs.getInt("c_no"), rs.getInt("e_no"), jl = new JLabel(), rs.getString("e_title"), String.format("%s/%s", rs.getInt("cnt"), rs.getInt("e_people")), 
	            		won.format(rs.getInt("e_pay")), NoToStr(rs.getString("c_categry")), rs.getString("c_address"), VQ.graduatelist[rs.getInt("e_graduate")], VQ.genderlist[rs.getInt("e_gender")]});
	            jl.setIcon(new ImageIcon(new ImageIcon("datafiles/기업/" + rs.getString("c_name") + "1.jpg").getImage().getScaledInstance(50, 50, 4)));
			}
			if (dtm.getRowCount() == 0) {
				wmsg("검색결과가 없습니다.");
				jt.setText("");
				jc[0].setSelectedIndex(0);
				jc[1].setSelectedIndex(0);
				jc[2].setSelectedIndex(0);
				up();
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			new d_info();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
