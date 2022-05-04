package form;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import aframe.aframe;

public class Z extends aframe {
	
	public Z() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				if(stack.isEmpty() == false) {
					stack.pop().setVisible(true);
				}
			}
		});
		
		fs("");
		
		
		
		sh();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		try {
			new Z();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
