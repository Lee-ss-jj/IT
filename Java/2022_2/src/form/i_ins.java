package form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import DB.VQ;
import aframe.aframe;

public class i_ins extends aframe {
	
	JTextArea jtx;
	JTextField jt[] = new JTextField[3];
	JRadioButton jr[] = new JRadioButton[2];
	JButton b[] = new JButton[2];
	ButtonGroup g = new ButtonGroup();
	
	public i_ins() {
		fs("등록");
		
		np.setLayout(new GridLayout(0, 1));
		String s[] = "아이디 등록일 제목".split(" ");
		for (int i = 0; i < s.length; i++) {
			np.add(p = new JPanel(new FlowLayout()));
			p.add(setcomp(new JLabel(s[i]), 60, 25));
			p.add(jt[i] = new JTextField(16));
			if (i == 2) jt[i].setBorder(new LineBorder(Color.black));
		}
		jt[0].setEnabled(false);
		jt[1].setEnabled(false);
		
		cp.setBorder(new EmptyBorder(5, 5, 5, 5));
		cp.add(setcomp(new JLabel("내용"), 65, 150), BorderLayout.WEST);
		cp.add(jtx = new JTextArea());
		jtx.setLineWrap(true);
		jtx.setBorder(new LineBorder(Color.black));
		
		cp.add(p = new JPanel(new FlowLayout(0)), BorderLayout.SOUTH);
		p.add(setcomp(new JLabel("공개여부"), 65, 25), BorderLayout.WEST);
		p.add(jr[0] = new JRadioButton("비공개"));
		p.add(jr[1] = new JRadioButton("공개"));
		jr[1].setSelected(true);
		jt[0].setText(VQ.u_id);
		jt[1].setText(ymd.format(now));
		
		sp.setLayout(new FlowLayout(3));
		String bn[] = "등록 취소".split(" ");
		for (int i = 0; i < bn.length; i++) {
			sp.add(b[i] = new JButton(bn[i]));
			int n = i;
			b[i].addActionListener(e -> {
				if (n == 0) {
					if (jt[2].getText().equals("") || jtx.getText().equals("")) {
						wmsg("빈칸이 존재합니다.");
						return;
					}
					psqlup("insert into notice values('0',?,?,?,?,'0',?)", VQ.u_no + "", jt[1].getText(), jt[2].getText(), jtx.getText(), jr[0].isSelected() ? "0" : "1");
					imsg("게시물 등록이 완료되었습니다.");
					dispose();
					stack.pop().setVisible(true);
				} else {
					dispose();
					stack.pop().setVisible(true);
				}
			});
		}
		g.add(jr[0]);
		g.add(jr[1]);
		
		sh();
	}
	
	public static void main(String[] args) {
		try {
			new i_ins();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
