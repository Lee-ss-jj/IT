package form;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import DB.DB;
import aframe.aframe;

public class k_noti extends aframe {
	
	int sno, acnt;
	JButton b[] = new JButton[2];
	JTextField jt[] = new JTextField[3];
	JComboBox jc[] = new JComboBox[2];
	JRadioButton jr[] = new JRadioButton[3];
	ButtonGroup g = new ButtonGroup();
	
	public k_noti(int cno, String cname) {
		if(cno == 0) fs("공고등록");
		else fs("공고수정");
		
		cp.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new TitledBorder(new LineBorder(Color.black), "모집내용")));
		cp.setLayout(new GridLayout(0, 1, 5, 5));
		String s[] = "회사명 공고내용 시급 모집인원 성별 최종학력".split(" ");
		for (int i = 0; i < s.length; i++) {
			cp.add(p = new JPanel(new FlowLayout(0)));
			p.add(setcomp(new JLabel(s[i]), 80, 30));
			if (i == 0) {
				p.add(setcomp(jc[0] = new JComboBox<>(), 150, 30));
			} else if (i == 4) {
				String ss[] = "남 여 무관".split(" ");
				for (int j = 0; j < ss.length; j++) {
					p.add(setcomp(jr[j] = new JRadioButton(ss[j]), 50, 30));
				}
			} else if (i == 5) {
				String item[] = "대학교 졸업, 고등학교 졸업, 중학교 졸업, 무관".split(", ");
				p.add(setcomp(jc[1] = new JComboBox<>(item), 150, 30));
			} else {
				p.add(setcomp(jt[i - 1] = new JTextField(30), 50, 30));
			}
			p.setBorder(new EmptyBorder(10, 10, 10, 30));
		}
		sp.setLayout(new FlowLayout(2));


		sp.setBorder(new EmptyBorder(10, 10, 10, 10));
		sp.add(setcomp(b[0] = new JButton(), 100, 30));
		sp.add(setcomp(b[1] = new JButton("삭제"), 100, 30));
		
		try {
			if (cno == 0) {
				jr[0].setSelected(true);
				rs = DB.rs("select * from company c where not exists(select * from employment e where c.c_no = e.c_no)");
				while (rs.next()) {
					jc[0].addItem(rs.getString("c_name"));
				}
				jc[0].setSelectedIndex(0);
				b[0].setText("등록");
			} else {
				rs = DB.rs("select * from company c inner join employment e where c.c_no = e.c_no");
				while (rs.next()) {
					jc[0].addItem(rs.getString("c_name"));
				}
				jc[0].setSelectedItem(cname);
				jr[0].setEnabled(false);
				jr[1].setEnabled(false);
				jr[2].setEnabled(false);
				jc[1].setEnabled(false);
				b[0].setText("수정");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		jc[0].addActionListener(e -> {
			up();
		});
		b[0].addActionListener(e -> {
			for (int i = 0; i < 3; i++) {
				if (jt[i].getText().equals("") || jc[0].getSelectedItem().equals("")) {
					wmsg("빈칸이 존재합니다.");
					return;
				}
				try {
					Integer.parseInt(jt[1].getText());
					int d = Integer.parseInt(jt[2].getText());
					if (d < acnt) {
						wmsg("모집정원이 지원자보다 적습니다.");
						return;
					}
				} catch (Exception e2) {
					wmsg("숫자로 입력하세요.");
					return;
				}
				int egender = 0;
				if (jr[0].isSelected()) {
					egender = 1;
				} else if (jr[1].isSelected()) {
					egender = 2;
				} else {
					egender = 3;
				}
				if (b[0].getText().equals("등록")) {
					psqlup("insert into employment values(0,?,?,?,?,?,?)", sno + "", jt[0].getText(), jt[1].getText(), jt[2].getText(),
							egender + "", jc[1].getSelectedIndex() + "");
					imsg("등록이 완료되었습니다.");
					dispose();
					stack.pop().setVisible(true);
				} else {
					psqlup("update employment set e_title = ?, e_pay = ?, e_people = ? where c_no = " + sno, jt[0].getText(), jt[1].getText(), jt[2].getText());
					imsg("수정이 완료되었습니다.");
					dispose();
					stack.pop().setVisible(true);
				}
			}
		});
		b[1].addActionListener(e -> {
			sqlup("delete from employment where c_no = " + sno);
			imsg("삭제가 완료되었습니다.");
			dispose();
			stack.pop().setVisible(true);
		});
		
		sno = cno;
		up();
		sh();
	}
	
	void up() {
		b[1].setVisible(false);
		try {
			rs = DB.rs("select * from company where c_name = '" + jc[0].getSelectedItem() + "'");
			if (rs.next()) {
				sno = rs.getInt("c_no");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			rs = DB.rs("select *, count(a.e_no) as cnt from company c inner join employment e inner join applicant a on a.e_no = e.e_no"
					+ " where c.c_no = e.c_no and a.a_apply != 2 and c.c_name like '" + jc[0].getSelectedItem().toString() + "' group by c.c_no");
			if (rs.next()) {
				if (rs.getInt("cnt") == 0) {
					b[1].setVisible(true);
				}
				acnt = rs.getInt("cnt");
				jt[0].setText(rs.getString("e_title"));
				jt[1].setText(rs.getString("e_pay"));
				jt[2].setText(rs.getString("e_people"));
				jr[rs.getInt("e_gender")-1].setSelected(true);
				jc[1].setSelectedIndex(rs.getInt("e_graduate"));
			} else {
				rs1 = DB.rs("select * from employment where c_no = " + sno);
				if (rs1.next()) {
					b[1].setVisible(true);
					jt[0].setText(rs1.getString("e_title"));
					jt[1].setText(rs1.getString("e_pay"));
					jt[2].setText(rs1.getString("e_people"));
					jr[rs1.getInt("e_gender")-1].setSelected(true);
					jc[1].setSelectedIndex(rs1.getInt("e_graduate"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			new k_noti(21, "스타벅스");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
