package form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class g_buy extends aframe {

	JButton b;
	JLabel l[] = new JLabel[6];

	public g_buy() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if (stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}
		});
		
		fs("계산서");

		np.setLayout(new FlowLayout());
		np.add(jl = new JLabel("계산서"));
		jl.setFont(new Font("", 1, 18));

		cp.setBorder(new EmptyBorder(5, 5, 5, 5));
		cp.setLayout(new GridLayout(0, 2));

		String ln[] = "웨딩홀명, 항목, 홀사용료, 식사비용, 청첩장, 총금액".split(", ");
		for (int i = 0; i < ln.length; i++) {
			cp.add(p = new JPanel(new FlowLayout(FlowLayout.LEFT)));
			p.add(new JLabel(ln[i]));
			size(p, 150, 40);

			if (i == 1) {
				p.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.black));
			} else if (i == 4) {
				p.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.black));
			} else if (i == 0) {
				p.setBackground(Color.white);
			}

			cp.add(p = new JPanel(new FlowLayout(FlowLayout.RIGHT)));
			p.add(l[i] = new JLabel("", JLabel.RIGHT));

			if (i == 1) {
				p.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.black));
			} else if (i == 4) {
				p.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.black));
			} else if (i == 0) {
				p.setBackground(Color.white);
			}
		}

		l[0].setText(VQ.wname);
		l[1].setText("금액");
		try {
			rs = DB.rs("select wprice, mprice from 2021지방_2.sm where wno = '" + VQ.index[VQ.in] + "'");
			if (rs.next()) {
				l[2].setText(won.format(rs.getInt(1)));
				l[3].setText(won.format(VQ.wpeople * rs.getInt(2)));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (VQ.pan_num == 0) {
			l[4].setText("0");
			l[4].setForeground(Color.lightGray);
		} else {
			l[4].setText("150,000");
		}

		l[5].setText(won.format(rei(l[3].getText().replace(",", "")) + rei(l[4].getText().replace(",", ""))
				+ rei(l[2].getText().replace(",", ""))));

		sp.setLayout(new FlowLayout());
		sp.add(b = new JButton("결제"));

		b.addActionListener(e -> {
			stack.add(this);
			dispose();
			sign();
		});

		sh();
	}

	int sign = 0;
	BufferedImage buf = new BufferedImage(200, 240, BufferedImage.TYPE_INT_ARGB);
	Brush br = new Brush();

	void sign() {
		sign = 0;
		var jf = new JFrame();

		jf.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if (stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}
		});

		jf.setTitle("서명");
		
		JPanel p, pp;
		var ll = new JLabel();
		var bb = new JButton();

		jf.add(p = new JPanel(new BorderLayout()));
		p.add(pp = new JPanel(null));
		size(pp, 200, 240);
		pp.add(setcomp(ll = new JLabel(new ImageIcon(buf)), 0, 0, 200, 240));
		ll.setOpaque(true);
		ll.setBackground(Color.white);
		pp.add(setcomp(br, 0, 0, 200, 240));

		ll.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDragged(MouseEvent e) {
				sign = 1;
				// TODO Auto-generated method stub
				br.xx = e.getX();
				br.yy = e.getY();
				br.repaint();
				br.paintAll(buf.getGraphics());
			}
		});

		p.add(pp = new JPanel(new FlowLayout()), BorderLayout.SOUTH);
		pp.add(bb = new JButton("확인"));

		bb.addActionListener(e -> {
			if (sign == 0) {
				wmsg("서명을 하지 않았습니다.");
				return;
			}
			
			var dd = new DecimalFormat("0000");
			var r = new Random();
			
			for (int i = 0; i < 20; i++) {
				VQ.p_no = dd.format(r.nextInt(9999));
				try {
					rs = DB.rs("select * from 2021지방_2.payment where p_no = '" + VQ.p_no + "'");
					if (!rs.next()) {
						break;
					}
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}

			int wty_no = 0;
			int m_no = 0;
			
			try {
				rs = DB.rs("select d.wty_no ,d.m_no from 2021지방_2.sm s inner join division d on s.wno =d.wh_no where s.wno = '" + VQ.index[VQ.in] + "'");
				if (rs.next()) {
					wty_no = rs.getInt(1);
					m_no = rs.getInt(2);
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
			sqlup("insert into payment values('" + VQ.p_no + "','" + VQ.index[VQ.in] + "','" + VQ.wpeople + "','" + wty_no + "','" + m_no + "','" + VQ.pan_num + "','" + VQ.wdate + "','" + VQ.no + "')");
			imsg("결제가 완료되었습니다.\n결제번호 : " + VQ.p_no);
			int re = JOptionPane.showConfirmDialog(null, "청첩장을 보내시겠습니까?", "메시지", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			stack.pop();
			stack.pop();
			if (re == 0) {
				jf.dispose();
				new h_friend();
			} else {
				jf.dispose();
				new b_main();
			}
		});

		jf.setVisible(true);
		jf.pack();
		jf.setLocationRelativeTo(null);
	}

	class Brush extends JLabel {
		public int xx, yy;

		@Override
		public void paint(Graphics g) {
			// TODO Auto-generated method stub
			g.setColor(Color.black);
			g.fillOval(xx - 4, yy - 4, 8, 8);
		}
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
			new g_buy();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
