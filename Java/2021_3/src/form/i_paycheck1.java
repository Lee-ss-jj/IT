package form;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class i_paycheck1 extends aframe{
	
	JButton b, jb[] = new JButton[12];
	JTextField jt;
	
	public i_paycheck1() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if (stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}
		});
		
		fs("결제번호 확인");
		
		np.setLayout(new GridLayout(0, 2));
		np.setBorder(new EmptyBorder(5, 5, 5, 5));
		np.add(new JLabel("결제번호를 입력하세요"));
		
		np.add(p = new JPanel(new FlowLayout(FlowLayout.RIGHT)));
		p.setBorder(new EmptyBorder(5, 5, 5, 5));
		p.add(b = new JButton("확인"));
		
		cp.setBorder(new EmptyBorder(5, 5, 5, 5));
		cp.add(jt = new JTextField());
		size(jt, 10, 40);
		jt.setBorder(new LineBorder(Color.black));
		jt.setHorizontalAlignment(0);
		
		sp.setBorder(new EmptyBorder(5, 5, 5, 5));
		sp.setLayout(new GridLayout(0, 3, 5, 5));
		for (int i = 0; i < jb.length; i++) {
			sp.add(jb[i] = new JButton());
			size(jb[i], 100, 50);
			int n = i;
			jb[i].addActionListener(e -> {
				switch (n) {
				case 9: {
					up();
					break;
				}
				case 11: {
					try {
						jt.setText(jt.getText().substring(0, jt.getText().length() - 1));
					} catch (Exception e2) {
						// TODO: handle exception
						wmsg("삭제할 번호가 없습니다.");
						return;
					}
					break;
				}
				default: {
					jt.setText(jt.getText() + jb[n].getText());					
				}
				}
			});
		}
		
		jt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				e.consume();
			}
		});
		
		b.addActionListener(e -> {
			if(jt.getText().equals("")) {
				wmsg("결제번호가 없습니다.");
				return;
			}
			try {
				rs = DB.rs("select * from 2021지방_2.payment where p_no = '" + jt.getText() + "'");
				if(rs.next()) {
					VQ.p_no = jt.getText();
					stack.add(this);
					dispose();
					new i_paycheck2();
				} else {
					wmsg("일치하는 결제번호가 없습니다.");
					jt.setText("");
					up();
					return;
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		});
		
		up();
		sh();
	}
	
	void up() {
		ArrayList<String> arr = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			arr.add(i + "");
		}
		
		Collections.shuffle(arr);
		
		for (int i = 0; i < 9; i++) {
			jb[i].setText(arr.get(i));
		}
		
		jb[9].setText("재배열");
		jb[10].setText(arr.get(9));
		jb[11].setText("←");
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
			new i_paycheck1();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
