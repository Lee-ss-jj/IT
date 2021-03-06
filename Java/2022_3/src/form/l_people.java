package form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import DB.DB;
import DB.VQ;
import aframe.aframe;

public class l_people extends aframe {

	int ano[] = new int[1000], aano, pass = 0;
	JPanel bp[] = new JPanel[1000];
	JPopupMenu pop = new JPopupMenu();

	public l_people() {
		fs("지원자 정보");

		cp.add(jsp = new JScrollPane(ap = new JPanel(new GridLayout(0, 1))));
		ap.setBorder(new EmptyBorder(10, 10, 10, 10));
		size(jsp, 600, 450);

		JMenuItem it1 = new JMenuItem("합격");
		JMenuItem it2 = new JMenuItem("불합격");

		pop.add(it1);
		pop.add(it2);

		it1.addActionListener(e -> {
			pass = 1;
			apply();
		});
		it2.addActionListener(e -> {
			pass = 2;
			apply();
		});

		up();
		sh();
	}

	void up() {
		ap.removeAll();
		try {
			int k = 0;
			rs = DB.rs("SELECT a_no, u_name, c_name, u_birth, u_graduate, u_email, u_img FROM applicant a inner join user u on a.u_no = u.u_no inner join employment e on a.e_no = e.e_no "
					+ "inner join company c on e.c_no = c.c_no where a_apply = 0");
			while(rs.next()) {
				ap.add(bp[k] = new JPanel(new BorderLayout()));
				
				bp[k].setBorder(new LineBorder(Color.black));
				bp[k].add(jl = new JLabel(new ImageIcon(ImageIO.read(rs.getBlob(7).getBinaryStream()).getScaledInstance(150, 150, Image.SCALE_SMOOTH))), BorderLayout.WEST);
				jl.setBorder(new EmptyBorder(30, 30, 30, 30));
				
				bp[k].add(p = new JPanel(new GridLayout(0, 1)));
				p.add(jl = new JLabel("지원회사 : " + rs.getString(3)));
				jl.setFont(new Font("HY헤드라인M", 1, 22));
				
				p.add(jl = new JLabel("이름 : " + rs.getString(2) + "(나이 : " + (2023 - Integer.parseInt(rs.getString(4).split("-")[0])) + "세"));
				jl.setFont(new Font("HY헤드라인M", 0, 16));
				
				
				p.add(jl = new JLabel("생년월일 : " + rs.getString(4)));
				jl.setFont(new Font("HY헤드라인M", 0, 16));
				
				p.add(jl = new JLabel("최종학력 : " + VQ.graduatelist[rs.getInt(5)]));
				jl.setFont(new Font("HY헤드라인M", 0, 16));
				
				p.add(jl = new JLabel("email : " + rs.getString(6)));
				jl.setFont(new Font("HY헤드라인M", 0, 16));
				
				int i = k;
				ano[k] = rs.getInt(1);
				bp[k].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
						if(e.getButton() == 3) {
							pop.show(bp[i], e.getX(), e.getY());
							aano = ano[i];
						}
					}
				});
				
				k++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void apply() {
		sqlup("update applicant set a_apply = " + pass + " where a_no = " + aano);
		if(pass == 1) {
			sqlup("update company set c_employee = c_employee + 1");
		}
		imsg("심사가 완료되었습니다.");
		up();
	}

	public static void main(String[] args) {
		new l_people();
	}
}
