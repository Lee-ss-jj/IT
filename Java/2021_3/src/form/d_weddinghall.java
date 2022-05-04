package form;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class d_weddinghall extends aframe {

	JLabel l;
	JButton jb[] = new JButton[4];
	Image ig;
	Thread th = new Thread(this);
	int cnt = 0;
	int c = 0;
	static float alp;
	
	public d_weddinghall() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if (stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				if (!VQ.jt[7].getText().equals("")) {
					jb[3].setEnabled(true);
				} else {
					jb[3].setEnabled(false);
					jb[1].setEnabled(false);
				}
				if (!VQ.jt[6].getText().equals("") && !VQ.jt[7].getText().equals("")) {
					jb[1].setEnabled(true);
				} else {
					jb[1].setEnabled(false);
				}
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				th.stop();
			}

		});

		fs("웨딩홀");
		
		np.add(new JPanel());

		wp.add(p = new JPanel(new GridLayout(0, 1)), BorderLayout.NORTH);
		p.add(new JLabel("웨딩홀명", JLabel.CENTER));
		p.add(l = new JLabel("", JLabel.CENTER));
		l.setFont(new Font("", 1, 24));

		wp.add(p = new JPanel(new BorderLayout()), BorderLayout.CENTER);
		p.setBorder(new EmptyBorder(10, 10, 10, 10));

		p.add(pp = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				Graphics2D g2 = (Graphics2D) g;
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alp / 500));
				g2.drawImage(ig, 0, 0, 350, 200, this);
			}
		});
		size(pp, 350, 200);

		wp.add(p = new JPanel(new FlowLayout()), BorderLayout.SOUTH);
		size(p, 320, 70);
		String bn[] = "이전, 결제, 다음, 청첩장 선택".split(", ");
		for (int i = 0; i < bn.length; i++) {
			p.add(jb[i] = new JButton(bn[i]));
			size(jb[i], 100, 25);
			int n = i;
			jb[i].addActionListener(e -> {
				switch (n) {
				case 0: {
					VQ.in--;
					if (VQ.in == -1)
						VQ.in = VQ.max - 1;
					up();
					break;
				}
				case 1: {
					if (rei(VQ.jt[6].getText()) > rei(VQ.jt[1].getText())) {
						wmsg("수용인원보다 작게 입력하세요.");
						return;
					}
					VQ.wpeople = rei(VQ.jt[6].getText());
					VQ.wadd = VQ.jt[0].getText();
					VQ.wdate = VQ.jt[7].getText();
					VQ.wname = l.getText();
					if (VQ.jtt.getText().equals("")) {
						VQ.pan_num = 0;
					}
					stack.add(this);
					dispose();
					new g_buy();
					break;
				}
				case 2: {
					VQ.in++;
					if (VQ.in == VQ.max)
						VQ.in = 0;
					up();
					break;
				}
				case 3: {
					VQ.wadd = VQ.jt[0].getText();
					VQ.wdate = VQ.jt[7].getText();
					VQ.wname = l.getText();
					setVisible(false);
					stack.add(this);
					new f_pan();
					break;
				}
				}
			});
		}
		p.add(VQ.jtt = new JTextField(3));
		VQ.jtt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				switch (e.getKeyCode()) {
				case KeyEvent.VK_BACK_SPACE:
				case KeyEvent.VK_DELETE:
					return;
				}
				e.consume();
			}
		});
		VQ.jtt.setHorizontalAlignment(0);

		cp.setLayout(new GridLayout(0, 1));
		String ln[] = "주소, 수용인원, 홀사용료, 예식형태, 식사종류, 식사비용, 인원수, 날짜".split(", ");
		for (int i = 0; i < ln.length; i++) {
			cp.add(p = new JPanel(new FlowLayout(FlowLayout.LEFT)));
			p.add(setcomp(new JLabel(ln[i], JLabel.LEFT), 65, 25));
			p.add(VQ.jt[i] = new JTextField(28));

			if (i < 6) {
				VQ.jt[i].setEnabled(false);
			}
		}

		VQ.jt[7].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				stack.add(d_weddinghall.this);
				setVisible(false);
				new e_cal();
			}
		});

		VQ.jt[7].addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				if (VQ.jt[7].getText().equals("")) {
					jb[1].setEnabled(false);
					jb[3].setEnabled(false);
				}
				e.consume();
			}
		});

		VQ.jt[6].addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if (!VQ.jt[6].getText().equals("") && !VQ.jt[7].getText().equals("")) {
					jb[1].setEnabled(true);
				} else {
					jb[1].setEnabled(false);
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_BACK_SPACE:
				case KeyEvent.VK_DELETE:
					return;
				}

				String t = e.getKeyChar() + "";
				try {
					int d = Integer.parseInt(t);
				} catch (Exception e2) {
					// TODO: handle exception
					wmsg("인원수를 바르게 입력해주세요.");
					VQ.jt[6].setText("");
					return;
				}
			}
		});

		up();

		if (!th.isAlive()) {
			th.start();
		}

		sh();
	}

	void up() {
		try {
			rs = DB.rs("select * from 2021지방_2.sm where wno = '" + VQ.index[VQ.in] + "'");
			if (rs.next()) {
				l.setText(rs.getString(2));
				VQ.jt[0].setText(rs.getString(3));
				VQ.jt[1].setText(rs.getString(4));
				VQ.jt[2].setText(rs.getString(5));
				VQ.jt[3].setText(rs.getString(6));
				VQ.jt[4].setText(rs.getString(7));
				VQ.jt[5].setText(rs.getString(8));

				File f = new File("datafile3/웨딩홀/" + rs.getString(2));
				File files[] = f.listFiles();

				c = 1;
				cnt = 0;
				for (int i = 0; i < files.length; i++) {
					if (files[i].isFile()) {
						cnt++;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				ImageIcon ii = new ImageIcon("datafile3/웨딩홀/" + l.getText() + "/" + l.getText() + c + ".jpg");
				ig = ii.getImage();
				
				for(alp = 0; alp < 450; alp++) {
					repaint();
					th.sleep(2);
				}
				for (alp = 0; alp < 400; alp++) {
					th.sleep(1);
				}
				for (alp = 450; alp > 0; alp--) {
					repaint();
					th.sleep(2);
				}
				
				if(c == cnt) c = 0;
				c++;
			} catch (Exception e) {
				// TODO: handle exception
			}
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
			new c_search();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
