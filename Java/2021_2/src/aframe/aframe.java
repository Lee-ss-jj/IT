package aframe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

public class aframe extends JFrame implements ActionListener, MouseListener, Runnable {

	public JPanel p, pp, ppp, jp1, jp2, np, sp, cp, wp, ep, ap;
	public JLabel l, ll, jl;
	public JScrollPane jsp;
	public JTable jta;
	public DefaultTableModel dtm;
	public DefaultTableCellRenderer cell = new DefaultTableCellRenderer();
	public SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
	public DecimalFormat zz = new DecimalFormat("00"), won = new DecimalFormat("#,##0");
	public ResultSet rs;
	public String bl = "";
	public Date now = new Date();
	public static Stack<JFrame> stack = new Stack<JFrame>();
	
	public void fs(String t) {
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
	
	public void imsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "정보", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void wmsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "경고", JOptionPane.ERROR_MESSAGE);
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
	
	public void sqlup(String t) {
		try {
			DB.stmt.execute("set foreign_key_checks = 0");
			DB.stmt.execute(t);
			DB.stmt.execute("set foreign_key_checks = 1");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public int rei(String t) {
		try {
			return Integer.parseInt(t.replace(",", ""));
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	@Override
	public void run() {
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
