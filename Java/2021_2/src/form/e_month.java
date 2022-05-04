package form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class e_month extends aframe {
	
	JLabel l[] = new JLabel[12];
	JButton b;
	
	public e_month() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if(stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}
		});
		
		fs("월 선택");
		
		cp.setLayout(new GridLayout(0, 4));
		for (int i = 0; i < l.length; i++) {
			cp.add(p = new JPanel(new BorderLayout()) {
				@Override
				protected void paintComponent(Graphics g) {
					// TODO Auto-generated method stub
					g.setColor(Color.yellow);
					g.fillOval(10, 10, 55, 55);
					g.setColor(Color.black);
					g.drawOval(10, 10, 55, 55);
				}
			});
			p.add(l[i] = new JLabel(i + 1 + "월", JLabel.CENTER));
			int n = i;
			l[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					if(l[n].getForeground().equals(Color.red)) {
						l[n].setForeground(Color.black);
					} else {
						l[n].setForeground(Color.red);
					}
					repaint();
				}
			});
			size(p, 75, 75);
		}
		
		sp.setLayout(new FlowLayout());
		sp.add(b = new JButton("확인"));
		
		b.addActionListener(e -> {
			int cnt = 0;
			VQ.sus = 0;
			
			for (int i = 0; i < l.length; i++) {
				if(l[i].getForeground().equals(Color.red)) {
					cnt++;
				}
			}
			if(cnt == 0) {
				wmsg("구매 월을 선택하세요.");
				return;
			}
			
			cnt = 0;
			for (int i = 0; i < l.length; i++) {
				if(l[i].getForeground().equals(Color.red)) {
					VQ.mon[VQ.sus] = l[i].getText();
					try {
						rs = DB.rs("select * from 2021지방_1.purchase where date_format(pu_date, '%Y년%m월%d일') like '%" + l[i].getText() + "%' and u_no = '" + VQ.no + "'");
						if(rs.next()) {
							cnt++;
						}
					} catch (Exception e2) {
						// TODO: handle exception
					}
					VQ.sus++;
				}
			}
			if(cnt == 0) {
				wmsg("구매내역이 없습니다.");
				return;
			}
			
			if(VQ.ecnt == 1) 
				stack.add(this);
			dispose();
			new f_buylist();
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
			new e_month();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
