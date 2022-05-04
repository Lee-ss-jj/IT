package form;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import DB.VQ;
import aframe.aframe;

public class f_pan extends aframe {

	public f_pan() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if (stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}
		});

		fs("청첩장");

		VQ.pan_num = 1;

		np.setLayout(new FlowLayout());
		np.add(l = new JLabel(VQ.pan_num + "번 이미지"));

		cp.add(p = new JPanel(null) {
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				ImageIcon ii = new ImageIcon("datafile3/청첩장/청첩장" + VQ.pan_num + ".jpg");
				Image ig = ii.getImage();

				g.drawImage(ig, 0, 0, 400, 600, this);
			}
		});
		size(p, 400, 600);

		JPopupMenu pop = new JPopupMenu();
		JMenuItem t = new JMenuItem("1번");
		JMenuItem tt = new JMenuItem("2번");
		JMenuItem ttt = new JMenuItem("3번");
		JMenuItem tttt = new JMenuItem("결정");

		pop.add(t);
		pop.add(tt);
		pop.add(ttt);
		pop.add(tttt);

		t.addActionListener(e -> {
			VQ.pan_num = 1;
			l.setText(VQ.pan_num + "번 이미지");
			repaint();
		});
		tt.addActionListener(e -> {
			VQ.pan_num = 2;
			l.setText(VQ.pan_num + "번 이미지");
			repaint();
		});
		ttt.addActionListener(e -> {
			VQ.pan_num = 3;
			l.setText(VQ.pan_num + "번 이미지");
			repaint();
		});
		tttt.addActionListener(e -> {
			imsg("디자인" + VQ.pan_num + "번으로 결정되었습니다.");
			VQ.jtt.setText(VQ.pan_num + "");
			dispose();
			stack.pop().setVisible(true);
		});

		p.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.getButton() == 3) {
					pop.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		p.add(setcomp(new JLabel("<html><center>오랜 기다림 속에서<br>저희 두 사람, 한 마음이 되어<br>참된 사랑의 결실을 맺게 되었습니다.<br>오셔서 축복해 주시면 감사하겠습니다.</center></html>" ,JLabel.CENTER), 0, 150, 400, 100));
		p.add(setcomp(new JLabel("워딩홀명 : " + VQ.wname), 110, 370, 300, 30));
		p.add(setcomp(new JLabel("장소 : " + VQ.wadd), 110, 410, 300, 30));
		p.add(setcomp(new JLabel("날짜 : " + VQ.wdate), 110, 450, 300, 30));

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
			new f_pan();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
