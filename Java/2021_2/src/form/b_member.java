package form;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import DB.DB;
import aframe.aframe;

public class b_member extends aframe {
	
	int check = 0;
	JTextField jt[] = new JTextField[6];
	JButton b, jb[] = new JButton[2];
	
	public b_member() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if(stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}
		});
		
		fs("ȸ������");
		
		cp.setLayout(new GridLayout(0, 1));
		cp.setBorder(new EmptyBorder(5, 5, 5, 5));
		String tn[] = "�̸�: ���̵�: ��й�ȣ: ��й�ȣüũ: ��ȭ��ȣ: �������:".split(" ");
		for (int i = 0; i < tn.length; i++) {
			cp.add(p = new JPanel(new FlowLayout(FlowLayout.LEFT)));
			p.add(setcomp(new JLabel(tn[i], JLabel.LEFT), 105, 25));
			p.add(jt[i] = new JTextField(14));
			if(i == 1) {
				p.add(b = new JButton("�ߺ�Ȯ��"));
				b.addActionListener(e -> {
					if(jt[1].getText().trim().equals("")) {
						wmsg("���̵� �Է��ϼ���.");
						return;
					} try {
						rs = DB.rs("select * from user where u_id = '" + jt[1].getText() + "'");
						if(rs.next()) {
							wmsg("�̹� �����ϴ� ���̵��Դϴ�.");
							jt[1].setText("");
							jt[1].requestFocus();
							jb[0].setEnabled(false);
							return;
						} else {
							check = 1;
							imsg("��밡�� �� ���̵��Դϴ�.");
						}
					} catch (Exception e2) {
						// TODO: handle exception
					}
				});
			}
			
			jt[i].addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub
					int cnt = 0;
					for (int j = 0; j < jt.length; j++) {
						if(jt[j].getText().equals("")) {
							cnt++;
						}
					}
					if(cnt == 0) {
						jb[0].setEnabled(true);
					} else {
						jb[0].setEnabled(false);
					}
				}
			});
		}
		
		jt[1].addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				check = 0;
			}
		});
		
		jt[4].addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				switch(e.getKeyCode()) {
				case KeyEvent.VK_UP:
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_ESCAPE:
				case KeyEvent.VK_BACK_SPACE:
				case KeyEvent.VK_DELETE:
					return;
				}
				
				switch(jt[4].getText().length()) {
				case 3:
				case 8:
					jt[4].setText(jt[4].getText() + "-");
				}
				
				String t = e.getKeyChar() + "";
				try {
					int d = Integer.parseInt(t);
				} catch (Exception e2) {
					// TODO: handle exception
					wmsg("���ڴ� �Է��� �Ұ��մϴ�.");
					jt[4].setText("");
					jt[4].requestFocus();
					jb[0].setEnabled(false);
					return;
				}
			}
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				if(jt[4].getText().length() == 13) {
					e.consume();
				}
			}
		});
		
		sp.setLayout(new GridLayout(0, 1));
		sp.add(p = new JPanel(new FlowLayout(FlowLayout.RIGHT)));
		sp.add(p = new JPanel(new FlowLayout(FlowLayout.RIGHT)));
		String bn[] = "ȸ������ ���".split(" ");
		for (int i = 0; i < bn.length; i++) {
			p.add(jb[i] = new JButton(bn[i]));
			int n = i;
			jb[i].addActionListener(e -> {
				if(n == 0) {
					if(check == 0) {
						wmsg("���̵� �ߺ�Ȯ���� ���ּ���.");
						return;
					} if(!jt[2].getText().equals(jt[3].getText())) {
						wmsg("��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
						return;
					}
					Pattern p1 = Pattern.compile("[A-z]");
					Pattern p2 = Pattern.compile("[0-9]");
					Pattern p3 = Pattern.compile("\\p{Punct}");
					
					Matcher m1 = p1.matcher(jt[2].getText());
					Matcher m2 = p2.matcher(jt[2].getText());
					Matcher m3 = p3.matcher(jt[2].getText());
					
					if(!m1.find() || !m2.find() || !m3.find() || jt[2].getText().length() < 4) {
						wmsg("��й�ȣ�� Ȯ�����ּ���.");
						return;
					}
					String pa = "^[0-9][0-9][0-9][0-9]\\-[0-9]{1,2}\\-[0-9]{1,2}$";
					if(!jt[5].getText().matches(pa)) {
						wmsg("��������� Ȯ�����ּ���.");
						return;
					} try {
						ymd.setLenient(false);
						Date day = ymd.parse(jt[5].getText());
						int d = day.compareTo(now);
						if(d > 0) {
							wmsg("��������� Ȯ�����ּ���.");
							return;
						}
					} catch (Exception e2) {
						// TODO: handle exception
						wmsg("��������� Ȯ�����ּ���.");
						return;
					}
					sqlup("insert into user values('0','" + jt[1].getText() + "','" + jt[2].getText() + "','" + jt[0].getText() + "','" + jt[4].getText() + "','" + jt[5].getText() + "','0','0')");
					imsg("ȸ�������� �Ϸ�Ǿ����ϴ�.");
					dispose();
					stack.pop().setVisible(true);
				} else {
					dispose();
					stack.pop().setVisible(true);
				}
			});
		}
		sp.add(p = new JPanel(new FlowLayout(FlowLayout.RIGHT)));
		
		jb[0].setEnabled(false);
		
		
		sh();
	}
	
	public static void main(String[] args) {
		try {
			new b_member();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}