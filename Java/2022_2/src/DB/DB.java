package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class DB {
	public static Statement stmt;
	public static Connection con;

	public static ResultSet rs(String t) throws SQLException {
		DBC();
		stmt.execute("use 2022지방_1");
		return stmt.executeQuery(t);
	}

	public static void DBC() throws SQLException {
		con = DriverManager.getConnection("jdbc:mysql://localhost/?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
		stmt = con.createStatement();
	}

	public static void DBS() throws SQLException {
		try {
			stmt.execute("drop database 2022지방_1");
			stmt.execute("drop user 'user'@'localhost'");
		} catch (Exception e) {
			// TODO: handle exception
		}
		stmt.execute("create database 2022지방_1");
		stmt.execute("use 2022지방_1");
		stmt.execute("set global local_infile = 1");
		try {
			stmt.execute("create user 'user'@'localhost' identified by '1234'");
			stmt.execute("grant select, insert, update, delete on 2022지방_1.* to 'user'@'localhost'");
		} catch (Exception e) {
			// TODO: handle exception
		}
		stmt.execute("create table area(a_no int primary key not null auto_increment, a_name varchar(2))");
		stmt.execute("create table cafe(c_no varchar(10) primary key, c_name varchar(10), t_no varchar(100), c_tel varchar(15), a_no int, c_address varchar(50), c_price int, constraint foreign key(a_no) references area(a_no))");
		stmt.execute("create table genre(g_no int primary key not null auto_increment, g_name varchar(10))");
		stmt.execute("create table map(a_no int, m_x int, m_y int, constraint foreign key(a_no) references area(a_no))");
		stmt.execute("create table ping(a_no int, p_x int, p_y int, constraint foreign key(a_no) references area(a_no))");
		stmt.execute("create table quiz(q_no int primary key not null auto_increment, q_answer varchar(10))");
		stmt.execute("create table user(u_no int primary key not null auto_increment, u_id varchar(10), u_pw varchar(10), u_name varchar(10), u_date date)");
		stmt.execute("create table theme(t_no int primary key not null auto_increment, t_name varchar(30), g_no int, t_explan varchar(200), t_personnel int, t_time int, constraint foreign key(g_no) references genre(g_no))");
		stmt.execute("create table notice(n_no int primary key not null auto_increment, u_no int, n_date date, n_title varchar(20), n_content varchar(150), n_viewcount int, n_open int, constraint foreign key(u_no) references user(u_no))");
		stmt.execute("create table reservation(r_no int primary key not null auto_increment, u_no int, c_no varchar(10), t_no int, r_date date, r_time varchar(20), r_people int, r_attend int, constraint foreign key(u_no) references user(u_no), constraint foreign key(c_no) references cafe(c_no), constraint foreign key(t_no) references theme(t_no))");
		
		String s[] = "area cafe genre map ping quiz user theme notice reservation".split(" ");
		for (int i = 0; i < s.length; i++) {
			String path = "datafiles/" + s[i] + ".txt";
			stmt.execute("load data local infile '" + path + "' into table " + s[i] + " ignore 1 lines");
		}
	}
	
	public static void main(String[] args) {
		try {
			DBC();
			DBS();
			JOptionPane.showMessageDialog(null, "셋팅 성공", "정보", 1);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "셋팅 실패", "경고", 0);
		}
	}
}
