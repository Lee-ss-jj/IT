package DB;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class DB {
	public static Statement stmt;
	public static Connection con;

	public static ResultSet rs(String t) throws SQLException {
		DBC();
		stmt.execute("use 2022지방_2");
		return stmt.executeQuery(t);
	}

	public static void DBC() throws SQLException {
		con = DriverManager.getConnection("jdbc:mysql://localhost/?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
		stmt = con.createStatement();
	}

	public static void DBS() throws SQLException {
		try {
			stmt.execute("drop database 2022지방_2");
			stmt.execute("drop user 'user'@'localhost'");
		} catch (Exception e) {
			// TODO: handle exception
		}
		stmt.execute("create database 2022지방_2");
		stmt.execute("use 2022지방_2");
		stmt.execute("set global local_infile = 1");
		try {
			stmt.execute("create user 'user'@'localhost' identified by '1234'");
			stmt.execute("grant select, insert, update, delete on 2022지방_2.* to 'user'@'localhost'");
		} catch (Exception e) {
			// TODO: handle exception
		}
		stmt.execute("create table user(u_no int primary key not null auto_increment, u_name varchar(10), u_id varchar(10), u_pw varchar(15), u_birth varchar(15), u_email varchar(30), u_gender int, u_graduate int, u_address varchar(100), u_img longblob)");
		stmt.execute("create table company(c_no int primary key not null auto_increment, c_name varchar(10), c_ceo varchar(10), c_address varchar(100), c_category varchar(15), c_employee int, c_img longblob, c_search int)");
		stmt.execute("create table employment(e_no int primary key not null auto_increment, c_no int, e_title varchar(30), e_pay int, e_people int, e_gender int, e_graduate int, constraint foreign key(c_no) references company(c_no))");
		stmt.execute("create table applicant(a_no int primary key not null auto_increment, e_no int, u_no int, a_apply int, constraint foreign key(e_no) references employment(e_no), constraint foreign key(u_no) references user(u_no))");
		
		String s[] = "user company employment applicant".split(" ");
		for (int i = 0; i < s.length; i++) {
			String path = "datafiles/" + s[i] + ".txt";
			stmt.execute("load data local infile '" + path + "' into table " + s[i] + " ignore 1 lines");
		}
		
		try {
			var rss1 = DB.rs("select * from company");
			while (rss1.next()) {
				String path = "datafiles/기업/" + rss1.getString(2) + "1.jpg";
				PreparedStatement ps = con.prepareStatement("update company set c_img = ? where c_no = " + rss1.getInt(1));
				ps.setBlob(1, new FileInputStream(new File(path)));
				ps.executeUpdate();
			}
			var rss2 = DB.rs("select * from user");
			while (rss2.next()) {
				String path = "datafiles/회원사진/" + rss2.getString(1) + ".jpg";
				PreparedStatement ps = con.prepareStatement("update user set u_img = ? where u_no = " + rss2.getInt(1));
				ps.setBlob(1, new FileInputStream(new File(path)));
				ps.executeUpdate();
			}
		} catch (Exception e) {
			// TODO: handle exception
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
