package form;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class i_paycheck2 extends aframe {

	JCheckBox jch[] = new JCheckBox[3];
	JTextField jt[] = new JTextField[12];

	public i_paycheck2() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if (stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}
		});

		fs("결제확인");
		
		np.add(p = new JPanel(new FlowLayout()));
		np.setBorder(new EmptyBorder(5, 5, 5, 5));
		p.add(jl = new JLabel("결제확인"));
		jl.setFont(new Font("", 1, 24));
		p.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.black));

		cp.setBorder(new EmptyBorder(5, 5, 5, 5));
		cp.setLayout(new GridLayout(0, 1));
		String ln[] = "웨딩홀명, 날짜, 주소, 예식형태, 식사종류, 식사비용, 인원수, 청첩장, 홀사용료, 총식사비용, 청첩장금액, 총금액".split(", ");
		for (int i = 0; i < ln.length; i++) {
			cp.add(p = new JPanel(new FlowLayout(FlowLayout.LEFT)));
			p.add(setcomp(new JLabel(ln[i], JLabel.LEFT), 75, 25));
			if (i == 7) {
				for (int j = 0; j < jch.length; j++) {
					p.add(jch[j] = new JCheckBox(j + 1 + ""));
					jch[j].setEnabled(false);
					size(jch[j], 85, 25);
				}
			} else {
				p.add(jt[i] = new JTextField(28));
				jt[i].setEnabled(false);
				System.out.println(i);
			}

			if (i > 7 && i < 11) {
				p.setBackground(Color.LIGHT_GRAY);
			}
		}

		try {
			rs = DB.rs(
					"select s.wname, p.p_date , s.wadd, s.wtyname, s.mname, s.mprice, p.p_people, s.wprice, (p.p_people * s.mprice) , p.i_no from 2021지방_2.payment p inner join sm s on s.wno = p.wh_no where p_no = '"
							+ VQ.p_no + "' and p.u_no = '" + VQ.no + "'");
			if (rs.next()) {
				jt[0].setText(rs.getString(1));
				jt[1].setText(rs.getString(2));
				jt[2].setText(rs.getString(3));
				jt[3].setText(rs.getString(4));
				jt[4].setText(rs.getString(5));
				jt[5].setText(rs.getString(6));
				jt[6].setText(rs.getString(7));
				jt[8].setText(won.format(rs.getInt(8)));
				jt[9].setText(won.format(rs.getInt(9)));
				if (rs.getInt(10) > 0) {
					jch[rs.getInt(10) - 1].setSelected(true);
					jt[10].setText("150,000");
				} else {
					jt[10].setText("0");
				}
				jt[11].setText(won.format(rei(jt[8].getText()) + rei(jt[9].getText()) + rei(jt[10].getText())));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		sh();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		super.actionPerformed(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		super.mouseClicked(e);
	}

	public static void main(String[] args) {
		try {
			new i_paycheck2();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
