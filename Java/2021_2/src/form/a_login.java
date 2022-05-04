package form;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class a_login extends aframe {
	
	JTextField jt[] = new JTextField[2];
	JButton b;
	
	public a_login() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		
		fs("로그인");
		
		np.setBorder(new EmptyBorder(5, 5, 5, 5));
		np.add(jl = new JLabel("기능마켓", JLabel.CENTER));
		jl.setFont(new Font("HY견고딕", 1, 24));
		
		cp.setLayout(new GridLayout(0, 1));
		String tn[] = "아이디 비밀번호".split(" ");
		for (int i = 0; i < tn.length; i++) {
			cp.add(p = new JPanel(new FlowLayout(FlowLayout.LEFT)));
			p.add(setcomp(new JLabel(tn[i], JLabel.RIGHT), 55, 25));
			p.add(jt[i] = new JTextField(13));
		}
		
		ep.setBorder(new EmptyBorder(5, 5, 5, 5));
		ep.add(b = new JButton("로그인"));
		
		sp.setLayout(new FlowLayout(FlowLayout.RIGHT));
		sp.add(l = new JLabel("회원가입"));
		
		b.addActionListener(e -> {
			if(jt[0].getText().trim().equals("") || jt[1].getText().trim().equals("")) {
				wmsg("빈칸이 존재합니다.");
				return;
			} if(jt[0].getText().trim().toLowerCase().equals("admin") && jt[1].getText().trim().equals("1234")) {
				imsg("관리자로 로그인 되었습니다.");
				stack.add(this);
				dispose();
				new i_list();
				return;				
			} try {
				rs = DB.rs("select * from user where u_id = '" + jt[0].getText().trim().toLowerCase() + "' and u_pw = '" + jt[1].getText().trim().toLowerCase() + "'");
				if(rs.next()) {
					VQ.name = rs.getString(4);
					VQ.no = rs.getInt(1);
					VQ.per10 = rs.getInt(7);
					VQ.per30 = rs.getInt(8);
					imsg(VQ.name + "님 환영합니다.");
					stack.add(this);
					dispose();
					new c_main();
				} else {
					wmsg("회원정보가 일치하지 않습니다.");
					return;
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		});
		
		l.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				stack.add(a_login.this);
				dispose();
				new b_member();
			}
		});
		
		sh();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		try {
			new a_login();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
