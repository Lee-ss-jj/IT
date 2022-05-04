package form;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


import DB.DB;
import DB.VQ;
import aframe.aframe;

public class f_apply extends aframe {
	
	int gra = 0;
	JButton b;
	JTextField jt[] = new JTextField[5];
	
	public f_apply(int cno) {
		fs("지원가능여부");
		
		VQ.from = 1;
		cp.setLayout(new GridLayout(0, 1));
		String s[] = "기업이름 대표자 주소 모집성별 모집최종학력".split(" ");
		for (int i = 0; i < s.length; i++) {
			cp.add(p = new JPanel(new FlowLayout()));
			p.setBorder(new EmptyBorder(3, 3, 3, 3));
			p.add(setcomp(jl = new JLabel(s[i]), 100, 30));
			p.add(setcomp(jt[i] = new JTextField(), 150, 30));
			p.setBackground(Color.white);
			jt[i].setEnabled(false);
		}
		try {
			rs = DB.rs("select * from employment e inner join applicant a on e.e_no = a.e_no inner join company c on c.c_no = e.c_no"
					+ " where c.c_no = " + cno + " group by e.e_no");
			if (rs.next()) {
				jt[0].setText(rs.getString("c_name"));
				jt[1].setText("c_ceo");
				jt[2].setText("c_address");
				jt[3].setText(VQ.genderlist[rs.getInt("e_gender")]);
				jt[4].setText(VQ.graduatelist[rs.getInt("e_graduate")]);
				gra = rs.getInt("e_graduate");
				Toolkit kit = Toolkit.getDefaultToolkit();
				Image img = kit.getImage("datafiles/기업/" + rs.getString("c_name") + "1.jpg");
				this.setIconImage(img);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		cp.setBorder(new EmptyBorder(5, 5, 5, 5));
		cp.setBackground(Color.white);
		sp.setLayout(new FlowLayout());
		sp.add(b = new JButton("지원가능여부보기"));
		
		b.addActionListener(e -> {
			if (!jt[3].getText().equals("무관") && !jt[3].getText().equals(VQ.genderlist[VQ.u_gender])) {
				wmsg("지원이 불가합니다. (성별)");
				return;
			}
			if (!jt[4].getText().equals("무관") && (VQ.u_graduate > gra)) {
				wmsg("지원이 불가합니다. (학력)");
				return;
			}
			try {
				rs = DB.rs("select * from employment e inner join applicant a on e.e_no a.e_no where u_no = " + VQ.u_no + " and c_no = " + cno);
				while (rs.next()) {
					if (rs.getInt("a_apply") < 2) {
						wmsg("합격자 또는 심사중입니다.");
						return;
					}
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			imsg("지원가능한 공고입니다.");
			VQ.b.setEnabled(true);
			dispose();
			stack.pop().setVisible(true);
			return;
		});
		sp.setBackground(Color.white);
		
		sh();
	}
	
	public static void main(String[] args) {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
