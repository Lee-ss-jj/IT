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
		
		fs("�α���");
		
		np.setBorder(new EmptyBorder(5, 5, 5, 5));
		np.add(jl = new JLabel("��ɸ���", JLabel.CENTER));
		jl.setFont(new Font("HY�߰��", 1, 24));
		
		cp.setLayout(new GridLayout(0, 1));
		String tn[] = "���̵� ��й�ȣ".split(" ");
		for (int i = 0; i < tn.length; i++) {
			cp.add(p = new JPanel(new FlowLayout(FlowLayout.LEFT)));
			p.add(setcomp(new JLabel(tn[i], JLabel.RIGHT), 55, 25));
			p.add(jt[i] = new JTextField(13));
		}
		
		ep.setBorder(new EmptyBorder(5, 5, 5, 5));
		ep.add(b = new JButton("�α���"));
		
		sp.setLayout(new FlowLayout(FlowLayout.RIGHT));
		sp.add(l = new JLabel("ȸ������"));
		
		b.addActionListener(e -> {
			if(jt[0].getText().trim().equals("") || jt[1].getText().trim().equals("")) {
				wmsg("��ĭ�� �����մϴ�.");
				return;
			} if(jt[0].getText().trim().toLowerCase().equals("admin") && jt[1].getText().trim().equals("1234")) {
				imsg("�����ڷ� �α��� �Ǿ����ϴ�.");
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
					imsg(VQ.name + "�� ȯ���մϴ�.");
					stack.add(this);
					dispose();
					new c_main();
				} else {
					wmsg("ȸ�������� ��ġ���� �ʽ��ϴ�.");
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
