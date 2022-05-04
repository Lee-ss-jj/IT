package form;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import DB.VQ;
import aframe.aframe;

public class b_login extends aframe {
	
	JTextField jt[] = new JTextField[2];
	JButton b;
	
	public b_login() {
		fs("로그인");
		
		np.setBorder(new EmptyBorder(5, 5, 5, 5));
		np.add(jl = new JLabel("아르바이트", 0));
		font(jl, 1, 26);
		
		cp.setLayout(new GridLayout(0, 1));
		cp.setBorder(new EmptyBorder(5, 5, 5, 5));
		String s[] = "아이디 비밀번호".split(" ");
		for (int i = 0; i < s.length; i++) {
			cp.add(p = new JPanel(new FlowLayout()));
			p.add(setcomp(new JLabel(s[i]), 65, 25));
			p.add(jt[i] = new JTextField(15));
		}
		ep.setBorder(new EmptyBorder(5, 5, 5, 5));
		ep.add(setcomp(b = new JButton("로그인"), 80, 30));
		b.addActionListener(e -> {
			if (jt[0].getText().equals("") || jt[1].getText().equals("")) {
				wmsg("빈칸이 존재합니다.");
				return;
			} else if (jt[0].getText().equals("admin") && jt[1].getText().equals("1234")) {
				imsg("관리자로 로그인하였습니다.");
				stack.add(this);
				dispose();
				new h_admain();
				return;
			}
			try {
				rs = prs("select * from user where u_id = ? and u_pw = ?", jt[0].getText(), jt[1].getText());
				if (rs.next()) {
					VQ.log = true;
					VQ.u_no = rs.getInt(1);
					VQ.u_name = rs.getString(2);
					VQ.u_gender = rs.getInt(7);
					VQ.u_graduate = rs.getInt(8);
					imsg(VQ.u_name + "님 환영합니다.");
					dispose();
					stack.pop().setVisible(true);
				} else {
					throw new Exception();
				}
			} catch (Exception e2) {
				wmsg("회원 정보가 일치하지 않습니다.");
				jt[0].setText("");
				jt[1].setText("");
				jt[0].requestFocus();
				return;
			}
		});
		
		sh();
	}
	
	void up() {
		
	}
	
	public static void main(String[] args) {
		try {
			new b_login();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
