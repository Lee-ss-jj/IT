package DB;

import javax.swing.JButton;
import javax.swing.JTextField;

public class VQ {
	public static String categorylist[] = "편의점 영화관 화장품 음식점 백화점 의류점 커피전문점 은행".split(" ");
	public static String genderlist[] = ",남자,여자,무관".split(",");
	public static String graduatelist[] = "대학교 졸업, 고등학교 졸업, 중학교 졸업, 무관".split(", ");
	public static String applylist[] = "심사중 합격 불합격".split(" ");
	public static boolean log;
	public static String u_name;
	public static JButton b;
	public static JTextField jt = new JTextField(), jtt = new JTextField();
	public static int u_no, u_gender, u_graduate, from;
}
