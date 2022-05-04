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
	JButton b[] = new JButton[2];
	
	public b_login() {
		fs("로그인");
		
		np.setBorder(new EmptyBorder(5, 5, 5, 5));
		np.add(jl = new JLabel("방탈출", 0));
		font(jl, 1, 28);
		
		cp.setLayout(new GridLayout(0, 1));
		cp.setBorder(new EmptyBorder(0, 5, 0, 5));
		String s[] = "아이디 비밀번호".split(" ");
		for (int i = 0; i < s.length; i++) {
			cp.add(p = new JPanel(new FlowLayout(0)));
			p.add(setcomp(new JLabel(s[i]), 65, 25));
			p.add(jt[i] = new JTextField(12));
		}
		
		sp.setBorder(new EmptyBorder(5, 5, 5, 5));
		sp.setLayout(new FlowLayout());
		String bn[] = "로그인 회원가입".split(" ");
		for (int i = 0; i < bn.length; i++) {
			sp.add(setcomp(b[i] = new JButton(bn[i]), 100, 30));
			int n = i;
			b[i].addActionListener(e -> {
				if (n == 0) {
					if (jt[0].getText().equals("") || jt[1].getText().equals("")) {
						wmsg("빈칸이 있습니다.");
						return;
					}
					try {
						rs = prs("select * from user where u_id = ? and u_pw = ?", jt[0].getText(), jt[1].getText());
						if (rs.next()) {
							VQ.log = 1;
							VQ.u_no = rs.getInt(1);
							VQ.u_id = rs.getString(2);
							VQ.u_name = rs.getString(4);
							imsg(VQ.u_name + "님 환영합니다.");
							dispose();
							stack.pop().setVisible(true);
						} else {
							throw new Exception();
						}
					} catch (Exception e2) {
						wmsg("회원정보가 일치하지 않습니다.");
						return;
					}
				} else {
					stack.add(this);
					dispose();
					new c_member();
				}
			});
		}
		
		sh();
	}
	
	public static void main(String[] args) {
		try {
			new b_login();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
