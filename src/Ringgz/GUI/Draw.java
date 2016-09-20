package Ringgz.GUI;

import javax.swing.*;

import Ringgz.Spel.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Draw extends JPanel {

	private List<Ring> ringenList;

	public Draw(List<Ring> list) {
		ringenList = new ArrayList<Ring>();
		for(int i = 0; i<list.size(); i++){
			ringenList.add(list.get(i));
		}
		setBackground(Color.BLACK);

	}

	public void voegRingToe(Ring r) {
		boolean toegevoegd = false;
		if (r != null) {
			if (ringenList.size() == 0) {
				ringenList = new ArrayList<Ring>();
				ringenList.add(r);
			} else {
				for (int i = 0; i < ringenList.size() && !toegevoegd; i++) {
					if (r.getType() > ringenList.get(i).getType()) {
						ringenList.add(i, r);
						toegevoegd = true;
					}
				}
				if (!toegevoegd) {
					ringenList.add(r);
					toegevoegd = true;
				}
			}
		}
	}

	public Draw(Ring r) {
		ringenList = new ArrayList<Ring>();

		ringenList.add(r);
		setBackground(Color.BLACK);

	}


	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		for (int i = 0; i < ringenList.size(); i++) {
			int type = ringenList.get(i).getType();
			int kleur = ringenList.get(i).getKleur();
			Color RGBkleur = null;
			if (kleur == 0) {
				RGBkleur = Color.RED;
			}
			if (kleur == 1) {
				RGBkleur = Color.GREEN;
			}
			if (kleur == 2) {
				RGBkleur = Color.YELLOW;
			}
			if (kleur == 3) {
				RGBkleur = new Color(151, 0, 178);
			}
			g.setColor(RGBkleur);
			if (type == 0) {
				g.fillOval(24, 24, 32, 32);
			} else if (type == 1) {
				g.fillOval(16, 16, 48, 48);
				g.setColor(Color.BLACK);
				g.fillOval(23, 23, 34, 34);
			} else if (type == 2) {
				g.fillOval(8, 8, 64, 64);
				g.setColor(Color.BLACK);
				g.fillOval(15, 15, 50, 50);
			} else if (type == 3) {
				g.fillOval(0, 0, 80, 80);
				g.setColor(Color.BLACK);
				g.fillOval(7, 7, 66, 66);
			} else if (type == 4) {
				g.fillOval(0, 0, 80, 80);
			}

		}

	}

}
