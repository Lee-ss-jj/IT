package aframe;

import java.awt.BorderLayout;
import java.awt.Color;
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

public class aframe extends JFrame implements ActionListener, MouseListener, KeyListener, Runnable {

	public JPanel p,pp,ppp,jp1,jp2,jp3,np,sp,cp,wp,ep,ap;
	public JLabel l,ll,jl;
	public JScrollPane jsp;
	public JTable jta;
	public DefaultTableModel dtm;
	public DefaultTableCellRenderer cell = new DefaultTableCellRenderer();
	public SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
	public DecimalFormat zz = new DecimalFormat("00"), won = new DecimalFormat("#,##0");
	public String bl = "";
	public Date now = new Date();
	public ResultSet rs, rs1;
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
	
	public void imsg(String m) {
		JOptionPane.showMessageDialog(null, m, "정보", 1);
	}
	
	public void wmsg(String m) {
		JOptionPane.showMessageDialog(null, m, "경고", 0);
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
	
	public <T extends JComponent> T color(T c) {
		c.setBackground(Color.black);
		c.setForeground(Color.white);
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
			DB.stmt.execute("use 2022지방_1");
			DB.stmt.execute("set foreign_key_checks = 0");
			DB.stmt.execute(t);
			DB.stmt.execute("set foreign_key_checks = 1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void psqlup(String t, String...args) {
		try {
			DB.DBC();
			DB.stmt.execute("use 2022지방_1");
			DB.stmt.execute("set foreign_key_checks = 0");
			var p = DB.con.prepareStatement(t);
			for (int i = 0; i < args.length; i++) {
				p.setString(i + 1, args[i]);
			}
			p.execute();
			DB.stmt.execute("set foreign_key_checks = 1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ResultSet prs(String t, Object...objects) {
		try {
			DB.DBC();
			DB.stmt.execute("use 2022지방_1");
			var pstmt = DB.con.prepareStatement(t);
			for (int i = 0; i < objects.length; i++) {
				pstmt.setObject(i + 1, objects[i]);
			}
			return pstmt.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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
