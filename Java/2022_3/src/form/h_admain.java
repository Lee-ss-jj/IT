package form;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import DB.DB;
import aframe.aframe;

public class h_admain extends aframe {

	String focus;
	JButton b[] = new JButton[5];
	JLabel l[] = new JLabel[25];
	String name[] = new String[25];

	public h_admain() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				up();
			}
		});
		fs("관리자 메인");

		cp.setBorder(new EmptyBorder(10, 10, 10, 10));
		cp.setLayout(new GridLayout(0, 5, 10, 10));
		cp.setBackground(Color.white);

		sp.setBorder(new EmptyBorder(10, 10, 10, 10));
		sp.setLayout(new FlowLayout());
		sp.setBackground(Color.white);

		String s[] = "채용 정보, 지원자 목록, 광고 등록, 지원자 분석, 닫기".split(", ");
		for (int i = 0; i < s.length; i++) {
			sp.add(b[i] = new JButton(s[i]));
		}
		b[0].addActionListener(e -> {
			stack.add(this);
			dispose();
			new j_adinfo();
		});
		b[1].addActionListener(e -> {
			stack.add(this);
			dispose();
			new l_people();
		});
		b[2].addActionListener(e -> {
			stack.add(this);
			dispose();
			new k_noti(0, "");
		});
		b[3].addActionListener(e -> {
			stack.add(this);
			dispose();
			new m_chart();
		});
		b[4].addActionListener(e -> {
			dispose();
			stack.pop();
			stack.pop().setVisible(true);
		});

		up();
		sh();
	}

	void up() {
		cp.removeAll();
		
		try {
			int k = 0;
			rs = DB.rs("select * from company");
			while (rs.next()) {
				cp.add(p = new JPanel(new BorderLayout()));
				name[k] = rs.getString("c_name");
				int n = k;
				p.add(l[k] = new JLabel(new ImageIcon(new ImageIcon("datafiles/기업/" + rs.getString("c_name") + "1.jpg").getImage().getScaledInstance(100, 100, 4))) {
					@Override
					protected void paintComponent(Graphics g) {
						// TODO Auto-generated method stub
						Graphics2D g2 = (Graphics2D) g;
						g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, focus == name[n] ? 1 : 0.2f));
						super.paintComponent(g);
					}
				});
				l[k].setToolTipText(rs.getString("c_name"));
				ToolTipManager tool = ToolTipManager.sharedInstance();
				tool.setInitialDelay(10);
				l[k].setBorder(new LineBorder(Color.black));
				l[k].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						stack.add(h_admain.this);
						dispose();
						new i_company(n + 1, 1);
					}
					@Override
					public void mouseEntered(MouseEvent e) {
						focus = name[n];
						l[n].repaint();
					}
					@Override
					public void mouseExited(MouseEvent e) {
						focus = null;
						l[n].repaint();
					}
				});
				size(p, 100, 100);
				k++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			new h_admain();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
