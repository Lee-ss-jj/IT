package form;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class a_login extends aframe {

	JTextField jt[] = { new JTextField(14), new JPasswordField(14) };
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
		np.add(l = new JLabel("WEDDING", JLabel.CENTER));
		l.setFont(new Font("HY견고딕", 1, 28));

		cp.setLayout(new GridLayout(0, 1));
		String tn[] = "ID :, PW :".split(", ");
		for (int i = 0; i < tn.length; i++) {
			cp.add(p = new JPanel(new FlowLayout(FlowLayout.LEFT)));
			p.add(setcomp(l = new JLabel(tn[i], JLabel.LEFT), 55, 25));
			p.add(jt[i]);
		}
		sp.add(p = new JPanel());
		
		ep.setBorder(new EmptyBorder(5, 5, 5, 5));
		ep.add(b = new JButton("로그인"));
		
		b.addActionListener(e -> {
			if(jt[0].getText().equals("") || jt[1].getText().equals("")) {
				wmsg("빈칸이 존재합니다.");
				return;
			}
			try {
				rs = DB.rs("select * from user where u_id = '" + jt[0].getText() + "' and u_pw = '" + jt[1].getText() + "'");
				if(rs.next()) {
					stack.add(this);
					VQ.no = rs.getInt(1);
					rs = DB.rs("select u_name, datediff(p.p_date, current_date()) from 2021지방_2.user u inner join payment p on p.u_no = u.u_no inner join invitation i on i.p_no = p.p_no where i.i_to = '" + VQ.no + "' order by p.p_date asc");
					if(rs.next()) {
						imsg(rs.getString(1) + "님의 결혼식이 D-" + rs.getString(2) + "일 남았습니다.");
					}
					stack.add(this);
					dispose();
					new b_main();
				} else {
					wmsg("ID 또는 PW가 일치하지 않습니다.");
					return;
				}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		});
		
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
			new a_login();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
