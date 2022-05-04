package form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class f_info extends aframe {
	
	String sno;
	JButton b;
	JTextArea jtx;
	JLabel pl, l[] = new JLabel[1000];
	
	public f_info() {
		fs("지점소개");
		
		np.add(jsp = new JScrollPane(ap = new JPanel(new FlowLayout())), BorderLayout.WEST);
		size(jsp, 173, 80);
		
		np.add(p = new JPanel(new FlowLayout(2)));
		p.add(b = new JButton("예약하기"));
		
		try {
			int k = 0;
			rs = DB.rs("select * from cafe c where c.c_name = '" + VQ.c_name + "'");
			if (rs.next()) {
				String tno[] = rs.getString("t_no").split(",");
				for (int i = 0; i < tno.length; i++) {
					rs1 = DB.rs("select * from theme t where t_no = " + tno[i]);
					if (rs1.next()) {
						ap.add(p = new JPanel(new BorderLayout()));
						p.add(l[k] = new JLabel(new ImageIcon(new ImageIcon("datafiles/테마/" + rs1.getString("t_no") + ".jpg").getImage().getScaledInstance(50, 50, 4))));
						l[k].setEnabled(false);
						p.setToolTipText(rs1.getString("t_name"));
						ToolTipManager tool = ToolTipManager.sharedInstance();
						tool.setInitialDelay(10);
						int n = k;
						String no = rs1.getString("t_no");
						p.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseReleased(MouseEvent e) {
								// TODO Auto-generated method stub
								for (int j = 0; j < tno.length; j++) {
									l[j].setEnabled(false);
								}
								l[n].setEnabled(true);
								sno = no;
								up();
								repaint();
								revalidate();
							}
						});
						k++;
					}
				}
				sno = tno[0];
				l[0].setEnabled(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		cp.setBackground(Color.black);
		cp.add(jl = new JLabel(VQ.c_name, 2), BorderLayout.NORTH);
		jl.setForeground(Color.orange);
		font(jl, 0, 30);
		
		cp.setBorder(new EmptyBorder(5, 5, 5, 5));
		cp.add(pl = new JLabel(), BorderLayout.WEST);
		cp.add(jp1 = new JPanel(null), BorderLayout.EAST);
		
		jp1.setBorder(new EmptyBorder(5, 5, 5, 5));
		jp1.setBackground(Color.black);
		size(jp1, 450, 500);
		
		b.addActionListener(e -> {
			stack.add(this);
			dispose();
			new g_reserv();
		});
		
		up();
		sh();
	}
	
	void up() {
		pl.setIcon(new ImageIcon(new ImageIcon("datafiles/테마/" + sno + ".jpg").getImage().getScaledInstance(450, 500, 4)));
		jp1.removeAll();
		try {
			rs = DB.rs("select * from theme t inner join cafe c inner join genre g on g.g_no = t.g_no"
					+ " where t.t_no = " + sno + " and c.c_name = '" + VQ.c_name + "'");
			if (rs.next()) {
				jp1.add(jl = new JLabel(rs.getString("t_name")));
				jl.setBounds(30, 0, 500, 50);
				jl.setForeground(Color.white);
				VQ.c_no = rs.getString("c_no");
				VQ.c_price = rs.getInt("c_price");
				VQ.t_personnel = rs.getInt("t_personnel");
				VQ.t_no = rs.getString("t_no");
				font(jl, 0, 28);
				
				jp1.add(jtx = new JTextArea());
				jtx.setText(rs.getString("t_explan"));
				jtx.setLineWrap(true);
				jtx.setSelectionColor(Color.black);
				jtx.setSelectedTextColor(Color.black);
				color(jtx);
				font(jtx, 0, 20);
				jtx.setBounds(30, 100, 400, 200);
				
				String s[] = "장르 : , 최대 인원 : , 시간 : , 가격 : ".split(", ");
				String ss[] = { rs.getString("g_name"), rs.getString("t_personnel"), rs.getString("t_time"), won.format(rs.getInt("c_price")) };
				String sss[] = ",명,분,원".split(",");
				for (int i = 0; i < sss.length; i++) {
					jp1.add(jl = new JLabel(s[i] + ss[i] + sss[i]));
					jl.setForeground(Color.white);
					jl.setBounds(30, 30 * i + 360, 500, 50);
					font(jl, 0, 16);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			new f_info();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
