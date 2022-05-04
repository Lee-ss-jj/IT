package form;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class e_cal extends aframe {

	JButton b[] = new JButton[2], jb[] = new JButton[42];
	Calendar cal = Calendar.getInstance();
	JLabel y, m;
	int yy, mm;

	public e_cal() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if (stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}
		});

		fs("기간선택");

		yy = cal.get(Calendar.YEAR);
		mm = cal.get(Calendar.MONTH) + 1;

		np.setLayout(new FlowLayout());
		np.add(b[0] = new JButton("◁"));
		np.add(y = new JLabel(yy + ""));
		np.add(new JLabel("년"));
		np.add(m = new JLabel(mm + ""));
		np.add(new JLabel("월"));
		np.add(b[1] = new JButton("▷"));

		cp.setLayout(new GridLayout(7, 7));
		String ln[] = "일 월 화 수 목 금 토".split(" ");
		for (int i = 0; i < ln.length; i++) {
			cp.add(setcomp(jl = new JLabel(ln[i], JLabel.CENTER), 55, 30));
			if (i == 0)
				jl.setForeground(Color.red);
			else if (i == 6)
				jl.setForeground(Color.blue);
		}
		
		for (int i = 0; i < jb.length; i++) {
			cp.add(jb[i] = new JButton());
			jb[i].setBorder(new LineBorder(Color.black));
			jb[i].addActionListener(this);
			if (i % 7 == 0)
				jb[i].setForeground(Color.red);
			else if (i % 7 == 6)
				jb[i].setForeground(Color.blue);
			size(jb[i], 50, 50);
		}
		
		b[0].addActionListener(e -> {
			mm--;
			if (mm == 0) {
				yy--;
				mm = 12;
			}
			y.setText(yy + "");
			m.setText(mm + "");
			up();
		});

		b[1].addActionListener(e -> {
			mm++;
			if (mm == 13) {
				yy++;
				mm = 1;
			}
			y.setText(yy + "");
			m.setText(mm + "");
			up();
		});

		up();
		sh();
	}

	void up() {
		for (int i = 0; i < jb.length; i++) {
			jb[i].setVisible(false);
			jb[i].setEnabled(true);
		}

		b[0].setEnabled(true);
		cal = Calendar.getInstance();
		if (cal.get(Calendar.YEAR) == yy && cal.get(Calendar.MONTH) + 1 == mm) {
			b[0].setEnabled(false);
		}

		cal.set(yy, mm - 1, 0);
		int sday = cal.get(Calendar.DAY_OF_WEEK);
		cal.set(yy, mm, 0);
		int eday = cal.get(Calendar.DATE);

		for (int i = sday; i < sday + eday; i++) {
			int d = i - sday + 1;
			jb[i].setVisible(true);
			jb[i].setText(d + "");

			bl = y.getText() + "-" + zz.format(rei(m.getText())) + "-" + zz.format(rei(jb[i].getText()));
			Date day = null;
			try {
				day = ymd.parse(bl);
				int dd = day.compareTo(now);

				if (dd < 0) {
					jb[i].setEnabled(false);
				}
				
				rs = DB.rs("select * from 2021지방_2.payment where wh_no = '" + VQ.index[VQ.in] + "' and p_date = '" + bl+ "'");
				if (rs.next()) {
					jb[i].setEnabled(false);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		for (int i = 0; i < jb.length; i++) {
			if (e.getSource() == jb[i]) {
				VQ.jtt.setText("");
				bl = y.getText() + "-" + zz.format(rei(m.getText())) + "-" + zz.format(rei(jb[i].getText()));
				VQ.jt[7].setText(bl);
				dispose();
				stack.pop().setVisible(true);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		super.mouseClicked(e);
	}

	public static void main(String[] args) {
		try {
			new e_cal();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
