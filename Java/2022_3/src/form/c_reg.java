package form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import DB.DB;
import aframe.aframe;

public class c_reg extends aframe {
	
	int check = 0;
	File sfile;
	JButton b;
	JTextField jt[] = new JTextField[4], et, at;
	JComboBox jc[] = new JComboBox[3];
	JRadioButton jr[] = new JRadioButton[2];
	ButtonGroup g = new ButtonGroup();
	
	public c_reg() {
		fs("회원가입");
		
		String email[] = "naver.com, outlook.com, daum.com, gmail.com, nate.com, kebi.com, yahoo.com, korea.com, empal.com, hanmail.net".split(", ");
		String graduate[] = "대학교 졸업, 고등학교 졸업, 중학교 졸업".split(", ");
		String area[] = " , 서울, 부산, 대구, 인천, 광주, 대전, 울산, 세종, 경기, 강원, 충북, 충남, 전북, 전남, 경북, 경남, 제주".split(", ");
		
		cp.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10) ,new TitledBorder(new LineBorder(Color.black), "회원가입")));
		cp.add(jp1 = new JPanel(new GridLayout(0, 1, 5, 5)), BorderLayout.WEST);
		String s[] = "이름 아이디 비밀번호 생년월일 이메일 성별 최종학력 주소".split(" ");
		for (int i = 0; i < s.length; i++) {
			jp1.add(p = new JPanel(new FlowLayout(0)));
			p.add(setcomp(new JLabel(s[i]), 80, 25));
			if (i < 4) {
				p.add(jt[i] = new JTextField(14));
			} else if (i == 4){
				p.add(et = new JTextField(10));
				p.add(new JLabel("@"));
				p.add(setcomp(jc[0] = new JComboBox<>(email), 120, 25));
			} else if (i == 5) {
				p.add(jr[0] = new JRadioButton("남"));
				p.add(jr[1] = new JRadioButton("여"));
				g.add(jr[0]);
				g.add(jr[1]);
			} else if (i == 6) {
				p.add(setcomp(jc[1] = new JComboBox<>(graduate), 120, 25));
			} else {
				p.add(setcomp(jc[2] = new JComboBox<>(area), 120, 25));
				p.add(at = new JTextField(20));
				at.setEnabled(false);
				at.setOpaque(false);
			}
		}
		jc[1].setSelectedIndex(-1);
		size(cp, 700, 500);
		cp.add(p = new JPanel(new FlowLayout()));
		p.add(setcomp(l = new JLabel(), 150, 150));
		l.setBorder(new LineBorder(Color.black));
		
		sp.setLayout(new FlowLayout(2));
		sp.setBorder(new EmptyBorder(5, 5, 5, 5));
		sp.add(setcomp(b = new JButton("가입"), 100, 30));
		l.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.getClickCount() != 2) {
					return;
				}
				l.removeAll();
				JFileChooser ch = new JFileChooser();
				ch.setAcceptAllFileFilterUsed(false);
				ch.setFileFilter(new FileNameExtensionFilter("JPG Images", "jpg"));
				int re = ch.showOpenDialog(null);
				if (re != 1) {
					try {
						sfile = ch.getSelectedFile();
						l.setIcon(new ImageIcon(new ImageIcon(sfile.toString()).getImage().getScaledInstance(150, 150, 4)));
						repaint();
						revalidate();
						check = 1;
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
				
			}
		});
		jc[2].addActionListener(e -> {
			if (jc[2].getSelectedIndex() != 0) {
				at.setEnabled(true);
				at.setOpaque(true);
				at.requestFocus();
			} else {
				at.setEnabled(false);
				at.setOpaque(false);
			}
		});
		b.addActionListener(e -> {
			int cnt = 0;
			for (int i = 0; i < jt.length; i++) {
				if (jt[i].getText().equals("")) {
					cnt ++;
				}
			}
			if (cnt > 0 || check == 0 || jc[1].getSelectedIndex() == -1 || jc[2].getSelectedIndex() == 0) {
				wmsg("빈칸이 존재합니다.");
				return;
			}
			try {
				rs = prs("select * from user where u_id = ?", jt[1].getText());
				if (rs.next()) {
					wmsg("이미 존재하는 아이디입니다.");
					jt[1].setText("");
					jt[1].requestFocus();
					return;
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			String pw = jt[2].getText();
			String date = jt[3].getText();
			if (!pw.matches("(.*)[0-9a-zA-z](.*)") || !pw.matches("(.*)[$#@!](.*)") || pw.length() < 4) {
				wmsg("비밀번호 형식이 일치하지 않습니다.");
				return;
			}
			try {
				Date day = ymd.parse(date);
				int d = day.compareTo(now);
				if (d > 0) {
					throw new Exception();
				}
			} catch (Exception e3) {
				wmsg("생년월일 형식이 맞지 않습니다.");
				jt[3].setText("");
				jt[3].requestFocus();
				return;
			}
			int k = 1;
			try {
				rs = DB.rs("select * from user");
				while (rs.next()) {
					k++;
				}
			} catch (Exception e3) {
				e3.printStackTrace();
			}
			psqlup("insert user values('0',?,?,?,?,?,?,?,?,'')", jt[0].getText(), jt[1].getText(), jt[2].getText(), jt[3].getText(), String.format("%s@%s", et.getText(), jc[0].getSelectedItem().toString()),
					jr[0].isSelected() ? "1" : "2", (jc[1].getSelectedIndex()) + "", String.format("%s %s", jc[2].getSelectedItem().toString(), at.getText()));
			try {
				Files.copy(sfile.toPath(), Paths.get("./datafiles/회원사진/" + k + ".jpg"));
				var ps = DB.con.prepareStatement("update user set u_img = ? where u_no = " + k);
				ps.setBlob(1, new FileInputStream("datafiles/회원사진/" + k + ".jpg"));
				ps.executeUpdate();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			imsg("회원가입이 완료되었습니다.");
			dispose();
			stack.pop().setVisible(true);
		});
		
		sh();
	}

	void up() {
		
	}
	
	public static void main(String[] args) {
		try {
			new c_reg();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
