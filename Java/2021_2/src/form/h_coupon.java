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

		fs("����");

		cp.add(p = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				ImageIcon ii = new ImageIcon("datafile2/�̹���/����.jpg");
				Image ig = ii.getImage();

				g.drawImage(ig, 0, 0, 600, 250, this);
			}
		});
		size(p, 600, 250);

		p.setLayout(null);

		String ss[] = "10% ���� ����,,,10%, 30% ���� ����".split(",,,");
		p.add(setcomp(jl = new JLabel(ss[VQ.coupon], JLabel.CENTER), 0, 50, 600, 50));
		jl.setFont(new Font("HY�߰��", 1, 45));

		p.add(setcomp(jl = new JLabel("������ �߱޵Ǿ����ϴ�.", JLabel.CENTER), 0, 100, 600, 25));
		jl.setFont(new Font("", 1, 25));
		jl.setForeground(Color.red);

		p.add(setcomp(jl = new JLabel("���� : " + VQ.name), 100, 160, 200, 25));

		Calendar cal = Calendar.getInstance();

		p.add(setcomp(jl = new JLabel("�߱޳�¥ : " + ymd.format(now)), 400, 160, 200, 25));
		cal.add(Calendar.MONTH, 1);
		p.add(setcomp(jl = new JLabel("������ : " + ymd.format(cal.getTime())), 400, 200, 200, 25));

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
