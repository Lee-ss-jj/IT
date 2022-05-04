package form;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import DB.VQ;
import aframe.aframe;

public class h_coupon extends aframe {

	public h_coupon() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if(stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}
		});

		fs("쿠폰");

		cp.add(p = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				ImageIcon ii = new ImageIcon("datafile2/이미지/쿠폰.jpg");
				Image ig = ii.getImage();

				g.drawImage(ig, 0, 0, 600, 250, this);
			}
		});
		size(p, 600, 250);

		p.setLayout(null);

		String ss[] = "10% 할인 쿠폰,,,10%, 30% 할인 쿠폰".split(",,,");
		p.add(setcomp(jl = new JLabel(ss[VQ.coupon], JLabel.CENTER), 0, 50, 600, 50));
		jl.setFont(new Font("HY견고딕", 1, 45));

		p.add(setcomp(jl = new JLabel("쿠폰이 발급되었습니다.", JLabel.CENTER), 0, 100, 600, 25));
		jl.setFont(new Font("", 1, 25));
		jl.setForeground(Color.red);

		p.add(setcomp(jl = new JLabel("고객명 : " + VQ.name), 100, 160, 200, 25));

		Calendar cal = Calendar.getInstance();

		p.add(setcomp(jl = new JLabel("발급날짜 : " + ymd.format(now)), 400, 160, 200, 25));
		cal.add(Calendar.MONTH, 1);
		p.add(setcomp(jl = new JLabel("사용기한 : " + ymd.format(cal.getTime())), 400, 200, 200, 25));

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
			new h_coupon();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
