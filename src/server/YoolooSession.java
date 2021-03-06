// History of Change
// vernr    |date  | who | lineno | what
//  V0.106  |200107| cic |    -   | add history of change

package server;

import common.YoolooKarte;
import common.YoolooKartenspiel;
import common.YoolooStich;
import server.YoolooServer.GameMode;
import utils.YoolooLogger;

public class YoolooSession {

	private int anzahlSpielerInRunde;
	private int botspielerinRunde;
	private GameMode gamemode = GameMode.GAMEMODE_NULL;
	private YoolooKarte[][] spielplan;
	public YoolooKartenspiel aktuellesSpiel;
	private YoolooStich[] ausgewerteteStiche;

	public YoolooSession(int anzahlSpielerInRunde,GameMode gameMode2) {
		super();
		if (gameMode2 == GameMode.GAMEMODE_R2D2_Game) {
			botspielerinRunde = 8 - anzahlSpielerInRunde;
			anzahlSpielerInRunde = 8;
		}
		this.anzahlSpielerInRunde = anzahlSpielerInRunde;
		gamemode = gameMode2;
		spielplan = new YoolooKarte[YoolooKartenspiel.maxKartenWert][anzahlSpielerInRunde];
		ausgewerteteStiche = new YoolooStich[YoolooKartenspiel.maxKartenWert];
		for (int i = 0; i < ausgewerteteStiche.length; i++) {
			ausgewerteteStiche[i] = null;
		}
		aktuellesSpiel = new YoolooKartenspiel();
		if (gameMode2 == GameMode.GAMEMODE_R2D2_Game) {
			for (int i = 0; i < botspielerinRunde; i++) {
				aktuellesSpiel.spielerRegistrieren("Bot" + i);
			}
		}
	}

	//public YoolooSession(int anzahlSpielerInRunde, GameMode gamemode) {
		//this(anzahlSpielerInRunde);
		//this.gamemode = gamemode;
	//}

	public synchronized void spieleKarteAus(int stichNummer, int spielerID, YoolooKarte karte) {
		spielplan[spielerID][stichNummer] = karte;
	}

	public synchronized YoolooStich stichFuerRundeAuswerten(int stichNummer) {
		if (ausgewerteteStiche[stichNummer] == null) {
			YoolooStich neuerStich = null;
			YoolooKarte[] karten = spielplan[stichNummer];
			for (int spielernummer = 0; spielernummer < spielplan[stichNummer].length; spielernummer++) {
				if (spielplan[stichNummer][spielernummer] == null) {
					karten = null;
				}
			}
			if (karten != null) {
				neuerStich = new YoolooStich(karten);
				neuerStich.setStichNummer(stichNummer);
				neuerStich.setSpielerNummer(aktuellesSpiel.berechneGewinnerIndex(karten));
				ausgewerteteStiche[stichNummer] = neuerStich;
				YoolooLogger.info("Stich ausgewertet:" + neuerStich.toString());
			}
		}
		return ausgewerteteStiche[stichNummer];

	}

	public YoolooKartenspiel getAktuellesSpiel() {
		return aktuellesSpiel;
	}

	public void setAktuellesSpiel(YoolooKartenspiel aktuellesSpiel) {
		this.aktuellesSpiel = aktuellesSpiel;
	}

	public int getAnzahlSpielerInRunde() {
		return anzahlSpielerInRunde;
	}

	public void setAnzahlSpielerInRunde(int anzahlSpielerInRunde) {
		this.anzahlSpielerInRunde = anzahlSpielerInRunde;
	}

	public GameMode getGamemode() {
		return gamemode;
	}

	public void setGamemode(GameMode gamemode) {
		this.gamemode = gamemode;
	}

	public YoolooKarte[][] getSpielplan() {
		return spielplan;
	}

	public void setSpielplan(YoolooKarte[][] spielplan) {
		this.spielplan = spielplan;
	}

	public String getErgebnis() {
		// TODO mit Funktion fuellen
		String ergebnis = "Ergebnis:\n blabla";
		return ergebnis;
	}

}
