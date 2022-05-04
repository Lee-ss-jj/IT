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
		stmt.execute("use 2021지방_1");
		return stmt.executeQuery(t);
	}

	public static void DBC() throws SQLException {
		con = DriverManager.getConnection("jdbc:mysql://localhost/?serverTimezone=UTC&allowLoadLocalInfile = true", "root", "1234");
		stmt = con.createStatement();
	}

	public static void DBS() throws SQLException {
		try {
			stmt.execute("drop database 2021지방_1");
			stmt.execute("drop user 'user'@'localhost'");
		} catch (Exception e) {
			// TODO: handle exception
		}
		stmt.execute("create database 2021지방_1");
		stmt.execute("use 2021지방_1");
		stmt.execute("set global local_infile = 1");
		try {
			stmt.execute("create user 'user'@'localhost' identified by '1234'");
			stmt.execute("grant select, insert, update, delete on 2021지방_1.* to 'user'@'localhost'");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		stmt.execute(
				"create table user(u_no int primary key not null auto_increment, u_id varchar(20), u_pw varchar(20), u_name varchar(15), u_phone varchar(20), u_age date, u_10percent int, u_30percent int)");
		stmt.execute("create table category(c_no int primary key not null auto_increment, c_name varchar(10))");
		stmt.execute(
				"create table product(p_no int primary key not null auto_increment, c_no int, p_name varchar(20), p_price int, p_stock int, p_explanation varchar(150), constraint foreign key(c_no) references category(c_no))");
		stmt.execute(
				"create table purchase(pu_no int primary key not null auto_increment, p_no int, pu_price int, pu_count int, coupon int, u_no int, pu_date date, constraint foreign key(p_no) references product(p_no), constraint foreign key(u_no) references user(u_no))");
		stmt.execute(
				"create table attendance(a_no int primary key not null auto_increment, u_no int, a_date date, constraint foreign key(u_no) references user(u_no))");
		stmt.execute(
				"create table coupon(c_no int primary key not null auto_increment, u_no int, c_date varchar(15), c_10percent int, c_30percent int, constraint foreign key(u_no) references user(u_no))");

		String s[] = "user category product purchase attendance coupon".split(" ");
		for (int i = 0; i < s.length; i++) {
			String path = "datafile2/" + s[i] + ".txt";
			stmt.execute("load data local infile '" + path + "' into table " + s[i] + " ignore 1 lines");
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			DBC();
			DBS();
			JOptionPane.showMessageDialog(null, "셋팅 성공", "정보", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "셋팅 실패", "경고", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}
