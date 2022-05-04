package aframe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Stack;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import DB.DB;
import DB.VQ;

public class aframe extends JFrame implements ActionListener, MouseListener, KeyListener, Runnable {

	public JPanel p, pp, ppp, jp1, jp2, jp3, np, sp, cp, wp, ep, ap;
	public JLabel l, ll, jl;
	public JScrollPane jsp;
	public JTable jta;
	public DefaultTableModel dtm;
	public DefaultTableCellRenderer cell = new DefaultTableCellRenderer();
	public SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd"), bir = new SimpleDateFormat("yyyyMMdd");
	public DecimalFormat zz = new DecimalFormat("00"), won = new DecimalFormat("#,##0");
	public String bl = "";
	public Date now = new Date();
	public ResultSet rs, rs1, rs2;
	public static Stack<JFrame> stack = new Stack<>();

	public void fs(String t) {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if (stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}
		});
		setTitle(t);
		setDefaultCloseOperation(2);
		add(np = new JPanel(new BorderLayout()), BorderLayout.NORTH);
		add(sp = new JPanel(new BorderLayout()), BorderLayout.SOUTH);
		add(cp = new JPanel(new BorderLayout()), BorderLayout.CENTER);
		add(wp = new JPanel(new BorderLayout()), BorderLayout.WEST);
		add(ep = new JPanel(new BorderLayout()), BorderLayout.EAST);
	}

	public void sh() {
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void imsg(String t) {
		JOptionPane.showMessageDialog(null, t, "정보", 1);
	}

	public void wmsg(String t) {
		JOptionPane.showMessageDialog(null, t, "경고", 0);
	}

	public void size(JComponent c, int x, int y) {
		c.setPreferredSize(new Dimension(x, y));
	}

	public <T extends JComponent> T setcomp(T c, int x, int y) {
		c.setPreferredSize(new Dimension(x, y));
		return c;
	}

	public <T extends JComponent> T setcomp(T c, int x, int y, int w, int h) {
		c.setBounds(x, y, w, h);
		return c;
	}

	public <T extends JComponent> T font(T c, int x, int y) {
		c.setFont(new Font("HY헤드라인M", x, y));
		return c;
	}

	public int rei(String t) {
		try {
			return Integer.parseInt(t.replace(",", ""));
		} catch (Exception e) {
			return 0;
		}
	}

	public void sqlup(String t) {
		try {
			DB.DBC();
			DB.stmt.execute("use 2022지방_2");
			DB.stmt.execute("set foreign_key_checks = 0");
			DB.stmt.execute(t);
			DB.stmt.execute("set foreign_key_checks = 1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void psqlup(String t, String... strings) {
		try {
			DB.DBC();
			DB.stmt.execute("use 2022지방_2");
			DB.stmt.execute("set foreign_key_checks = 0");
			var pstmt = DB.con.prepareStatement(t);
			for (int i = 0; i < strings.length; i++) {
				pstmt.setString(i + 1, strings[i]);
			}
			pstmt.execute();
			DB.stmt.execute("set foreign_key_checks = 1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ResultSet prs(String t, Object... args) {
		try {
			DB.DBC();
			DB.stmt.execute("use 2022지방_2");
			var prs = DB.con.prepareStatement(t);
			for (int i = 0; i < args.length; i++) {
				prs.setObject(i + 1, args[i]);
			}
			return prs.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String StrToNo(String category) {
		var tmp = new ArrayList<String>();
		if (category.length() == 0) {
			return "";
		}
		for(String s : category.split(",")) {
			tmp.add("" + (Arrays.asList(VQ.categorylist).indexOf(s)+1));
		}
		return String.join(",", tmp);
	}
	
	public static String NoToStr(String category) {
		var tmp = new ArrayList<String>();
		if (category.length() == 0) {
			return "";
		}
		for(String s : category.split(",")) {
			tmp.add(VQ.categorylist[Integer.valueOf(s) - 1]);
		}
		return String.join(",", tmp);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
