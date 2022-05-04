package form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import DB.VQ;
import aframe.aframe;

public class e_select extends aframe {
	
	JButton b;
	JTextField jt;
	JCheckBox cb[] = new JCheckBox[8];
	ArrayList<String> slist = new ArrayList<>();
	
	public e_select(int from) {
		fs("직종선택");
		
		VQ.from = 1;
		VQ.jt.setText("");
		setBackground(Color.white);
		
		cp.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new TitledBorder(new LineBorder(Color.black), "직종선택", 0, 0, new Font("HY헤드라인M", 0, 22))));
		cp.setBackground(Color.white);
		cp.setLayout(new GridLayout(0, 2));
		size(cp, 300, 300);
		
		for (int i = 0; i < VQ.categorylist.length; i++) {
			int id = i;
			
			cp.add(setcomp(cb[i] = new JCheckBox(VQ.categorylist[i]), 100, 25));
			cb[i].setOpaque(false);
			cb[i].addActionListener(e -> {
				if (cb[id].isSelected()) {
					slist.add(VQ.categorylist[id]);
				} else {
					slist.remove(VQ.categorylist[id]);
				}
				jt.setText(String.join(",", slist));
			});
		}
		
		sp.add(p = new JPanel(new FlowLayout()), BorderLayout.NORTH);
		p.add(jl = new JLabel("선택직종명"));
		p.add(jt = new JTextField(16));
		jt.setEnabled(false);
		p.setBackground(Color.white);
		font(jl, 0, 18);
		
		if (from == 1) {
			String s[] = VQ.jtt.getText().split(",");
			for (int i = 0; i < s.length; i++) {
				for (int j = 0; j < cb.length; j++) {
					if (cb[j].getText().equals(s[i])) {
						cb[j].setSelected(true);
						slist.add(VQ.categorylist[j]);
					}
				}
			}
			jt.setText(String.join(",", slist));
		}
		
		sp.add(p = new JPanel(new FlowLayout()));
		p.add(setcomp(b = new JButton("선택"), 80, 25));
		p.setBackground(Color.white);
		
		b.addActionListener(e -> {
			if (jt.getText().equals("")) {
				wmsg("직종을 선택하세요.");
				return;
			}
			if (from == 0) {
				VQ.jt.setText(jt.getText());
			} else {
				VQ.jtt.setText(jt.getText());
			}
			dispose();
			stack.pop().setVisible(true);
		});
		
		sh();
	}
	
	public static void main(String[] args) {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
