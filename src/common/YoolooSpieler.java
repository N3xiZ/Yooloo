// History of Change
// vernr    |date  | who | lineno | what
//  V0.106  |200107| cic |    -   | add history of change

package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import common.YoolooKartenspiel.Kartenfarbe;

public class YoolooSpieler implements Serializable {

	private static final long serialVersionUID = 376078630788146549L;
	private String name;
	private Kartenfarbe spielfarbe;
	private int clientHandlerId = -1;
	private int punkte;
	private YoolooKarte[] aktuelleSortierung;
	ArrayList<YoolooSpieler> spielerliste = new ArrayList<YoolooSpieler>();

	public YoolooSpieler(String name, int maxKartenWert) {
		this.name = name;
		this.punkte = 0;
		this.spielfarbe = null;
		this.aktuelleSortierung = new YoolooKarte[maxKartenWert];
	}

	// Sortierung wird zufuellig ermittelt
	public void sortierungFestlegen() {
		YoolooKarte[] neueSortierung = new YoolooKarte[this.aktuelleSortierung.length];
		for (int i = 0; i < neueSortierung.length; i++) {
			int neuerIndex = (int) (Math.random() * neueSortierung.length);
			while (neueSortierung[neuerIndex] != null) {
				neuerIndex = (int) (Math.random() * neueSortierung.length);
			}
			neueSortierung[neuerIndex] = aktuelleSortierung[i];
			// System.out.println(i+ ". neuerIndex: "+neuerIndex);
		}
		aktuelleSortierung = neueSortierung;
	}


	public void sortierungHochzählen() {
		int neuerIndex = -1;
		YoolooKarte[] neueSortierung = new YoolooKarte[this.aktuelleSortierung.length];
		for (int i = 0; i < neueSortierung.length; i++) {
			if (i < 10) {
				neuerIndex += 1;
			}
			neueSortierung[neuerIndex] = aktuelleSortierung[i];
			// System.out.println(i+ ". neuerIndex: "+neuerIndex);
		}
		aktuelleSortierung = neueSortierung;
	}

	public void sortierungRunterzählen() {
		int neuerIndex = 10;
		YoolooKarte[] neueSortierung = new YoolooKarte[this.aktuelleSortierung.length];
		for (int i = 0; i < neueSortierung.length; i++) {
			if (i < 10) {
				neuerIndex -= 1;
			}
			neueSortierung[neuerIndex] = aktuelleSortierung[i];
			// System.out.println(i+ ". neuerIndex: "+neuerIndex);
		}
		aktuelleSortierung = neueSortierung;
	}

	public void sortierungRandom() {
		YoolooKarte[] neueSortierung = new YoolooKarte[this.aktuelleSortierung.length];
		int neuerIndex = 0;
		for (int i = 0; i < neueSortierung.length; i++) {
			if (i < 3) {
				neuerIndex = (int) (Math.random()* 4);
			} else if (i > 3) {
				neuerIndex = (int) (Math.random()* 3)+4;
			} else if (i < 7) {
				neuerIndex = (int) (Math.random()* 2)+8;
			}
				neueSortierung[neuerIndex] = aktuelleSortierung[i];

		}
		aktuelleSortierung = neueSortierung;
	}

	// Sortierung wird zufuellig den größten und den kleinsten wert ermitteln
	public void sortierungGroßMittelKlein() {
		YoolooKarte[] neueSortierung = new YoolooKarte[this.aktuelleSortierung.length];
		aktuelleSortierung = neueSortierung;
	}

	// Sortierung wird zufuellig den größten und den kleinsten wert ermitteln
	public void sortierungMittelGroßKlein() {
		YoolooKarte[] neueSortierung = new YoolooKarte[this.aktuelleSortierung.length];
		aktuelleSortierung = neueSortierung;
	}

	public void sortierungMittelKleinGroß() {
		YoolooKarte[] neueSortierung = new YoolooKarte[this.aktuelleSortierung.length];
		aktuelleSortierung = neueSortierung;
	}

	public void sortierungKleinGroßMittel() {
		YoolooKarte[] neueSortierung = new YoolooKarte[this.aktuelleSortierung.length];
		aktuelleSortierung = neueSortierung;
	}
	public void sortierungGroßKleinMittel() {
		YoolooKarte[] neueSortierung = new YoolooKarte[this.aktuelleSortierung.length];
		aktuelleSortierung = neueSortierung;
	}


	public int erhaeltPunkte(int neuePunkte) {
		System.out.print(name + " hat " + punkte + " P - erhaelt " + neuePunkte + " P - neue Summe: ");
		this.punkte = this.punkte + neuePunkte;
		System.out.println(this.punkte);
		return this.punkte;
	}

	@Override
	public String toString() {
		return "YoolooSpieler [name=" + name + ", spielfarbe=" + spielfarbe + ", puntke=" + punkte
				+ ", altuelleSortierung=" + Arrays.toString(aktuelleSortierung) + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Kartenfarbe getSpielfarbe() {
		return spielfarbe;
	}

	public void setSpielfarbe(Kartenfarbe spielfarbe) {
		this.spielfarbe = spielfarbe;
	}

	public int getClientHandlerId() {
		return clientHandlerId;
	}

	public void setClientHandlerId(int clientHandlerId) {
		this.clientHandlerId = clientHandlerId;
	}

	public int getPunkte() {
		return punkte;
	}

	public void setPunkte(int puntke) {
		this.punkte = puntke;
	}

	public YoolooKarte[] getAktuelleSortierung() {
		return aktuelleSortierung;
	}

	public void setAktuelleSortierung(YoolooKarte[] aktuelleSortierung) {
		this.aktuelleSortierung = aktuelleSortierung;
	}

	public void stichAuswerten(YoolooStich stich) {
		System.out.println(stich.toString());

	}

}
