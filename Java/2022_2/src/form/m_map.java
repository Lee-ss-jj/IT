package form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;

import DB.DB;
import aframe.aframe;

public class m_map extends aframe {

	int click = 0, select = -1;
	int count[] = new int[1000];
	float cnt = 0, temp = 270;
	String cname[] = new String[1000];
	JLabel ping[] = new JLabel[17];
	JLabel map[] = new JLabel[17];
	Color list[] = { Color.red, Color.orange, Color.yellow, Color.green, Color.cyan, Color.blue, Color.pink,
			Color.magenta, Color.LIGHT_GRAY, Color.gray, Color.DARK_GRAY, Color.black, Color.white };

	public m_map() {
		fs("지역별 예약 현황");

		np.setBorder(new EmptyBorder(5, 5, 5, 5));
		np.add(jl = new JLabel("지역별 예약 현황", 0));
		jl.setFont(new Font("HY헤드라인M", 0, 26));

		cp.add(jl = new JLabel("C H A R T", 0));
		jl.setFont(new Font("HY헤드라인M", 0, 15));

		sp.setLayout(null);
		size(sp, 1200, 700);

		MapUp();
		sh();
	}

	void MapUp() {
		sp.removeAll();

		try {
			int k = 0;
			rs = DB.rs("select * from area a inner join ping p on p.a_no = a.a_no");
			while (rs.next()) {
				BufferedImage img;
				ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
				ColorConvertOp op = new ColorConvertOp(cs, null);
				int n = k;
				if (select == n) {
					img = ImageIO.read(new File("datafiles/마커.png"));
				} else {
					img = op.filter(ImageIO.read(new File("datafiles/마커.png")), null);
				}
				sp.add(ping[k] = new JLabel());
				ImageIcon ic = new ImageIcon(img.getScaledInstance(20, 25, 4));
				ping[k].setIcon(ic);
				ping[k].setBounds(rs.getInt("p_x"), rs.getInt("p_y"), 50, 50);
				ping[k].setToolTipText(rs.getString("a_name"));
				ToolTipManager tool = ToolTipManager.sharedInstance();
				tool.setInitialDelay(0);
				ping[k].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
						if (click == 0 || select != n) {
							select = n;
							MapUp();
							ChartUp(select + 1);
							click = 1;
						} else {
							select = -1;
							MapUp();
							click = 0;
						}
					}
				});
				k++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			int k = 0;
			rs = DB.rs("select * from area a inner join map m on m.a_no = a.a_no");
			while (rs.next()) {
				BufferedImage img;
				ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
				ColorConvertOp op = new ColorConvertOp(cs, null);
				int n = k;
				if (select == n) {
					img = ImageIO.read(new File("datafiles/지도/" + rs.getString("a_name") + ".png"));
				} else {
					img = op.filter(ImageIO.read(new File("datafiles/지도/" + rs.getString("a_name") + ".png")), null);
				}
				ImageIcon ic = new ImageIcon(img);
				sp.add(map[k] = new JLabel());
				map[k].setIcon(ic);
				map[k].setBounds(rs.getInt("m_x"), rs.getInt("m_y"), img.getWidth(), img.getHeight());

				k++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		repaint();
		revalidate();
	}

	void ChartUp(int selection) {
		int k = 0;
		cnt = 0;
		try {
			rs = DB.rs("select substring_index(c.c_name, ' ', 1) as cname, count(c.c_name) as cnt from reservation r inner join cafe c on c.c_no = r.c_no"
					+ " and c.a_no = " + selection + " group by cname order by cnt desc, c.c_no asc");
			while (rs.next()) {
				cnt = cnt + rs.getInt("cnt");
				count[k] = rs.getInt("cnt");
				cname[k] = rs.getString("cname");
				k++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int n = k;
		sp.add(ap = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				temp = 270;
				for (int i = 0; i < n; i++) {
					int per = (int) Math.round((double) ((count[i] / cnt) * 360));
					g.setColor(list[i]);
					g.fillArc(0, 0, 300, 300, (int) -(temp), -per);
					g.fillRect(350, i * 30 + 10, 20, 20);
					temp += per;
					g.setColor(Color.black);
					g.drawString(cname[i] + ":" + count[i] + "개", 380, i * 30 + 25);
				}
				if (n - 1 >= 0) {
					g.setColor(list[n - 1]);
					g.fillArc(0, 0, 300, 300, (int) -(temp), (int) -(360 - (temp - 270)));
				}
			}
		});
		ap.repaint();
		ap.revalidate();
		size(ap, 100, 100);
		ap.setBounds(600, 150, 600, 500);
		
		if(k == 0) {
			wmsg("예약현황이 없습니다.");
			return;
		}
	}

	public static void main(String[] args) {
		try {
			new m_map();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
