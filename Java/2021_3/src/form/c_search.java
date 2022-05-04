package form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class c_search extends aframe{
	
	JButton jb[] = new JButton[2], b[] = new JButton[1000];
	JTextField jt[] = new JTextField[4];
	JPanel xp[] = new JPanel[3];
	
	public c_search() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if (stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}
		});
		
		fs("�˻�");
		
		add(jsp = new JScrollPane(jp1 = new JPanel(new BorderLayout())));
		size(jsp, 900, 600);
		
		jp1.add(p = new JPanel(new GridLayout(0, 1)), BorderLayout.NORTH);
		
		p.add(pp = new JPanel(new FlowLayout(FlowLayout.LEFT)));
		pp.add(setcomp(jl = new JLabel("����"), 65, 25));
		pp.add(xp[0] = new JPanel(new GridLayout(0, 6, 5, 5)));
		Arrays.stream("�����, ���ı�, ������, �߱�, ������, ���ʱ�, ��������, ���α�".split(", "))
		.forEach(x -> xp[0].add(setcomp(new JCheckBox(x), 90, 25)));
		
		p.add(pp = new JPanel(new FlowLayout(FlowLayout.LEFT)));
		pp.add(setcomp(jl = new JLabel("��������"), 65, 25));
		pp.add(xp[1] = new JPanel(new GridLayout(0, 6, 5, 5)));
		Arrays.stream("�Ϲݿ���Ȧ, ����, �Ͽ콺, ȣ�ڿ���Ȧ, �߿ܿ���, ������, �������, ȸ��, ����, ��ȸ".split(", "))
		.forEach(x -> xp[1].add(setcomp(new JCheckBox(x), 90, 25)));
		
		p.add(pp = new JPanel(new FlowLayout(FlowLayout.LEFT)));
		pp.add(setcomp(jl = new JLabel("�Ļ�����"), 65, 25));
		pp.add(xp[2] = new JPanel(new GridLayout(0, 6, 5, 5)));
		Arrays.stream("���, ����, �ѽ�".split(", "))
		.forEach(x -> xp[2].add(setcomp(new JCheckBox(x), 90, 25)));
		
		jp1.add(p = new JPanel(new GridLayout(0, 2)), BorderLayout.CENTER);
		
		p.add(pp = new JPanel(new FlowLayout(FlowLayout.LEFT)));
		pp.add(new JLabel("�����ο�"));
		pp.add(jt[0] = new JTextField(10));
		pp.add(new JLabel("~"));
		pp.add(jt[1] = new JTextField(10));
		
		p.add(pp = new JPanel());
		p.add(pp = new JPanel(new FlowLayout(FlowLayout.LEFT)));
		pp.add(new JLabel("Ȧ����"));
		pp.add(jt[2] = new JTextField(10));
		pp.add(new JLabel("~"));
		pp.add(jt[3] = new JTextField(10));
		
		p.add(pp = new JPanel(new FlowLayout(FlowLayout.RIGHT)));
		String bn[] = "�˻� �ʱ�ȭ".split(" ");
		for (int i = 0; i < bn.length; i++) {
			pp.add(jb[i] = new JButton(bn[i]));
			int n = i;
			jb[i].addActionListener(e -> {
				if(n == 0) {
					up();
				} else {
					jt[0].setText("");
					jt[1].setText("");
					jt[2].setText("");
					jt[3].setText("");
					Arrays.stream(xp[0].getComponents())
					.map(x -> (JCheckBox) x)
					.forEach(x -> x.setSelected(false));
					Arrays.stream(xp[1].getComponents())
					.map(x -> (JCheckBox) x)
					.forEach(x -> x.setSelected(false));
					Arrays.stream(xp[2].getComponents())
					.map(x -> (JCheckBox) x)
					.forEach(x -> x.setSelected(false));
					up();
				}
			});
		}
		jp1.add(ap = new JPanel(new GridLayout(0, 1, 5, 5)), BorderLayout.SOUTH);
		ap.setBorder(new EmptyBorder(5, 5, 5, 5));
		up();
		sh();
	}
	
	void up() {
		int peo1 = 0, peo2 = Integer.MAX_VALUE, min = 0, max = Integer.MAX_VALUE;
		if (!jt[0].getText().equals("")) {
			peo1 = rei(jt[0].getText());
		}
		if (!jt[1].getText().equals("")) {
			peo2 = rei(jt[1].getText());
		}
		if (!jt[2].getText().equals("")) {
			min = rei(jt[2].getText());
		}
		if (!jt[3].getText().equals("")) {
			max = rei(jt[3].getText());
		}
		for (int i = 0; i < jt.length; i++) {
			if (jt[i].getText().equals("")) continue;
			try {
				int d = Integer.parseInt(jt[i].getText());
			} catch (Exception e) {
				// TODO: handle exception
				wmsg("�����ο��� Ȧ����� ���ڸ� �Է� �����մϴ�.");
				return;
			}
		}
		if (peo1 > peo2 || min > max) {
			wmsg("���ڸ� �ùٸ��� �Է����ּ���.");
			return;
		}
		ap.removeAll();

		ArrayList<String> local = new ArrayList<String>();
		ArrayList<String> wty = new ArrayList<String>();
		ArrayList<String> meal = new ArrayList<String>();

		Arrays.stream(xp[0].getComponents())
		.map(x -> (JCheckBox) x)
		.filter(x -> x.isSelected())
		.forEach(x -> local.add(x.getText()));
		Arrays.stream(xp[1].getComponents())
		.map(x -> (JCheckBox) x)
		.filter(x -> x.isSelected())
		.forEach(x -> wty.add(x.getText()));
		Arrays.stream(xp[2].getComponents())
		.map(x -> (JCheckBox) x)
		.filter(x -> x.isSelected())
		.forEach(x -> meal.add(x.getText()));

		int k = 0;
		try {
			rs = DB.rs("select * from 2021����_2.sm where wpeople between '" + peo1 + "' and '" + peo2 + "' and wprice between '" + min + "' and '" + max + "'");
			while (rs.next()) {
				String lo = rs.getString(3);
				String w = rs.getString(6);
				String m = rs.getString(7);

				if (local.size() > 0 && local.stream().anyMatch(x -> lo.contains(x)) == false) {
					continue;
				}
				if (wty.size() > 0 && wty.indexOf(w.trim()) == -1) {
					continue;
				}
				if (meal.size() > 0 && meal.indexOf(m.trim()) == -1) {
					continue;
				}
				VQ.index[k] = rs.getInt(1);

				ap.add(p = new JPanel(new BorderLayout()));
				p.setBorder(new LineBorder(Color.black));
				p.add(pp = new JPanel(new BorderLayout()), BorderLayout.WEST);
				pp.setBorder(new EmptyBorder(5, 5, 5, 5));
				pp.add(jl = new JLabel());
				jl.setIcon(new ImageIcon(new ImageIcon("datafile3/����Ȧ/" + rs.getString(2) + "/" + rs.getString(2) + "1.jpg").getImage().getScaledInstance(200, 120, Image.SCALE_SMOOTH)));

				p.add(pp = new JPanel(new GridLayout(0, 1)), BorderLayout.CENTER);
				pp.add(new JLabel(rs.getString(2)));
				pp.add(new JLabel());
				pp.add(new JLabel("�ּ� : " + rs.getString(3) + " / "));
				pp.add(new JLabel("�������� : " + rs.getString(6) + " / �Ļ����� : " + rs.getString(7) + " / "));
				pp.add(new JLabel("�����ο� : " + rs.getString(4) + " / Ȧ���� : " + won.format(rs.getInt(5)) + "��"));

				p.add(pp = new JPanel(new BorderLayout()), BorderLayout.EAST);
				pp.setBorder(new EmptyBorder(5, 5, 5, 5));
				pp.add(b[k] = new JButton("����"));
				size(b[k], 80, 120);

				int n = k;
				b[k].addActionListener(e -> {
					VQ.in = n;
					stack.add(this);
					dispose();
					new d_weddinghall();
				});
				k++;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		VQ.max = k;
		for (int i = 0; i < 3; i++) {
			ap.add(p = new JPanel());
			size(p, 10, 120);
		}
		revalidate();
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
