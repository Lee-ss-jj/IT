package DB;

import java.sql.SQLException;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class DB extends JFrame {

	public static Statement stmt;
	public static Connection con;

	public static ResultSet rs(String t) throws SQLException {
		DBC();
		stmt.execute("use 2021지방_2");
		return stmt.executeQuery(t);
	}

	public static void DBX() throws SQLException {
		try {
			stmt.execute("drop view sm");
		} catch (Exception e) {
			// TODO: handle exception
		}
		stmt.execute("create view sm(wno, wname, wadd, wpeople, wprice, wtyname, mname, mprice) as select wh.wh_no, wh.wh_name, wh.wh_add, wh.wh_people, wh.wh_price, wty.wty_name, m.m_name, m.m_price from weddinghall wh inner join division d on wh.wh_no = d.wh_no inner join mealtype m on m.m_no = d.m_no inner join weddingtype wty on wty.wty_no = d.wty_no");
	}

	public static void DBC() throws SQLException {
		con = DriverManager.getConnection("jdbc:mysql://localhost/?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
		stmt = con.createStatement();
	}

	Timer tm = new Timer(30, e -> {
		revalidate();
		repaint();
	});

	int sm = 0;

	public DB() {
		JPanel jp1, jp2;
		add(jp1 = new JPanel(new BorderLayout()));
		jp1.setBackground(Color.white);

		jp1.add(jp2 = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				if (sm < 100) {
					sm++;
				} else {
					tm.stop();
					JOptionPane.showMessageDialog(null, "세팅 성공", "정보", JOptionPane.INFORMATION_MESSAGE);
					dispose();
				}
				double d = 360 * (sm / 100.0);

				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.blue);
				g2.setStroke(new BasicStroke(25));
				g2.drawArc(40, 30, 200, 200, 90, (int) -d);
				
				g2.setColor(Color.black);
				g2.drawString(sm + "%", 130, 140);

			}
		});
		jp2.setOpaque(false);

		tm.start();

		setVisible(true);
		setSize(300, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(2);
	}

	public static void DBS() throws SQLException {
		try {
			stmt.execute("drop database 2021지방_2");
			stmt.execute("drop user 'user'@'localhost'");
		} catch (Exception e) {
			// TODO: handle exception
		}
		stmt.execute("create database 2021지방_2");
		stmt.execute("use 2021지방_2");
		stmt.execute("set global local_infile=1");
		try {
			stmt.execute("create user 'user'@'localhost' identified by '1234'");
			stmt.execute("grant select, insert, update, delete on 2021지방_2.* to 'user'@'localhost'");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		stmt.execute("create table mealtype(m_no int primary key not null auto_increment, m_name varchar(5), m_price int)");
		stmt.execute("create table user(u_no int primary key not null auto_increment, u_id varchar(20), u_pw varchar(20), u_name varchar(20), u_date date, u_phone varchar(50))");
		stmt.execute("create table weddinghall(wh_no int primary key not null auto_increment, wh_name varchar(20), wh_add varchar(50), wh_people int, wh_price int)");
		stmt.execute("create table weddingtype(wty_no int primary key not null auto_increment, wty_name varchar(15))");
		stmt.execute("create table division(wh_no int, wty_no int, m_no int, constraint foreign key(wh_no) references weddinghall(wh_no), constraint foreign key(wty_no) references weddingtype(wty_no), constraint foreign key(m_no) references mealtype(m_no))");
		stmt.execute("create table payment(p_no varchar(4) primary key not null, wh_no int, p_people int, wty_no int, m_no int, i_no int, p_date date, u_no int, constraint foreign key(wh_no) references weddinghall(wh_no), constraint foreign key(wty_no) references weddingtype(wty_no), constraint foreign key(m_no) references mealtype(m_no), constraint foreign key(u_no) references user(u_no))");
		stmt.execute("create table invitation(i_no int primary key not null auto_increment, p_no varchar(4), i_from int, i_to int, constraint foreign key(p_no) references payment(p_no))");

		String s[] = "mealtype user weddinghall weddingtype division payment invitation".split(" ");
		for (int i = 0; i < s.length; i++) {
			String path = "datafile3/" + s[i] + ".txt";
			stmt.execute("load data local infile '" + path + "' into table " + s[i] + " ignore 1 lines");
		}
	}

	public static void main(String[] args) {
		try {
			DBC();
			DBS();
			DBX();
			new DB();
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "세팅 실패", "경고", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

}
