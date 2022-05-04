package form;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import aframe.aframe;

public class c_member extends aframe {

	JButton b;
	LocalDate cal = LocalDate.now();
	JTextField jt[] = new JTextField[4];
	JComboBox jc[] = new JComboBox[3];
	JComponent c[] = { jc[0] = new JComboBox<>(), jc[1] = new JComboBox<>(), jc[2] = new JComboBox<>() };

	public c_member() {
		fs("회원가입");

		cp.setLayout(new GridLayout(0, 1));
		cp.setBorder(new EmptyBorder(10, 10, 10, 10));
		String s[] = "이름, 아이디, 비밀번호, 비밀번호 확인, 생년월일".split(", ");
		for (int i = 0; i < s.length; i++) {
			cp.add(p = new JPanel(new FlowLayout()));
			p.add(setcomp(new JLabel(s[i]), 100, 30));
			if (i == 4) {
				String ss[] = "년 월 일".split(" ");
				for (int j = 0; j < ss.length; j++) {
					p.add(c[j]);
					p.add(setcomp(new JLabel(ss[j]), 15, 25));
					if (j == 0)
						size(jc[j], 55, 25);
					else
						size(jc[j], 40, 25);
				}
			} else {
				p.add(jt[i] = new JTextField(18));
			}
			p.setBorder(new EmptyBorder(5, 5, 5, 5));
		}

		sp.setLayout(new FlowLayout());
		sp.add(b = new JButton("회원가입"));

		for (int i = 1900; i <= cal.getYear(); i++) {
			jc[0].addItem(i);
		}

		jc[0].setSelectedItem(cal.getYear());
		upMon();
		jc[1].setSelectedItem(zz.format(cal.getMonthValue()));
		upDay();
		jc[2].setSelectedItem(zz.format(cal.getDayOfMonth()));

		jc[0].addActionListener(e -> {
			upMon();
			upDay();
		});
		jc[1].addActionListener(e -> {
			upDay();
		});
		b.addActionListener(e -> {
			int cnt = 0;
			for (int i = 0; i < jt.length; i++) {
				if (jt[i].getText().equals("")) {
					cnt++;
				}
			}
			if (cnt > 0) {
				wmsg("빈칸이 있습니다.");
				return;
			}
			try {
				rs = prs("select * from user where u_id = ?", jt[1].getText());
				if (rs.next() || jt[1].getText().length() < 4 || jt[1].getText().length() > 8) {
					wmsg("사용할 수 없는 아이디입니다.");
					return;
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			String id = jt[1].getText();
			String pw = jt[2].getText();
			for (int i = 0; i < pw.length() - 3; i++) {
				if (id.contains(pw.subSequence(i, i + 4))) {
					wmsg("비밀번호는 아이디와 4글자 이상 연속으로 겹쳐질 수 없습니다.");
					return;
				}
			}
			if (!jt[2].getText().equals(jt[3].getText())) {
				wmsg("비밀번호가 일치하지 않습니다.");
				return;
			}
			String date = (String.format("%d-%s-%s", jc[0].getSelectedItem(), jc[1].getSelectedItem(), jc[2].getSelectedItem()));
			psqlup("insert into user values(?,?,?,?,?)", "0", jt[1].getText(), jt[2].getText(), jt[0].getText(), date);
			imsg(jt[0].getText() + "님 가입을 환영합니다.");
			dispose();
			stack.pop().setVisible(true);
		});

		sh();
	}

	void upMon() {
		jc[1].removeAllItems();

		int y = rei(jc[0].getSelectedItem().toString());
		int max = y == cal.getYear() ? cal.getMonthValue() : 12;

		for (int i = 1; i <= max; i++) {
			jc[1].addItem(zz.format(i));
		}
		jc[1].setSelectedItem("01");
	}

	void upDay() {
		jc[2].removeAllItems();

		if (jc[1].getSelectedItem() == null) {
			return;
		}
		
		int y = rei(jc[0].getSelectedItem().toString());
		int m = rei(jc[1].getSelectedItem().toString());
		int max = y == cal.getYear() && m == cal.getMonthValue() ? cal.getDayOfMonth() : LocalDate.of(y, m, 1).lengthOfMonth();

		for (int i = 1; i <= max; i++) {
			jc[2].addItem(zz.format(i));
		}
		jc[2].setSelectedItem("01");
	}

	public static void main(String[] args) {
		try {
			new c_member();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
