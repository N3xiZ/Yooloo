// History of Change
// vernr    |date  | who | lineno | what
//  V0.106  |200107| cic |    -   | add history of change 

package server;

import common.YoolooKartenspiel;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import common.YoolooKartenspiel;
import utils.YoolooLogger;

public class YoolooServer {

	// Server Standardwerte koennen ueber zweite Konstruktor modifiziert werden!
	private int port = 44137;
	private int spielerProRunde = 8; // min 1, max Anzahl definierte Farben in Enum YoolooKartenSpiel.KartenFarbe)
	private GameMode serverGameMode = GameMode.GAMEMODE_SINGLE_GAME;
	private GameMode serverGameModeBot = GameMode.GAMEMODE_R2D2_Game;

	public GameMode getServerGameMode() {
		return serverGameMode;
	}

	public void setServerGameMode(GameMode serverGameMode) {
		this.serverGameMode = serverGameMode;
	}

	private ServerSocket serverSocket = null;
	private boolean serverAktiv = true;

	// private ArrayList<Thread> spielerThreads;
	private ArrayList<YoolooClientHandler> clientHandlerList;

	private ExecutorService spielerPool;

	/**
	 * Serverseitig durch ClientHandler angebotenen SpielModi. Bedeutung der
	 * einzelnen Codes siehe Inlinekommentare.
	 * 
	 * Derzeit nur Modus Play Single Game genutzt
	 */
	public enum GameMode {
		GAMEMODE_NULL, // Spielmodus noch nicht definiert
		GAMEMODE_SINGLE_GAME, // Spielmodus: einfaches Spiel
		GAMEMODE_R2D2_Game, // Spielmodus: Bot Game
		GAMEMODE_PLAY_ROUND_GAME, // noch nicht genutzt: Spielmodus: Eine Runde von Spielen
		GAMEMODE_PLAY_LIGA, // noch nicht genutzt: Spielmodus: Jeder gegen jeden
		GAMEMODE_PLAY_POKAL, // noch nicht genutzt: Spielmodus: KO System
		GAMEMODE_PLAY_POKAL_LL // noch nicht genutzt: Spielmodus: KO System mit Lucky Looser
	};

	public YoolooServer(int port, int spielerProRunde, GameMode gameMode) {
		this.port = port;
		this.spielerProRunde = spielerProRunde;
		this.serverGameMode = gameMode;
	}

	public void startServer() {
		try {
			// Init
			serverSocket = new ServerSocket(port);
			spielerPool = Executors.newCachedThreadPool();
			clientHandlerList = new ArrayList<YoolooClientHandler>();
			YoolooLogger.info("Server gestartet - warte auf Spieler");
			long starttime = System.currentTimeMillis();

			while (serverAktiv) {
				Socket client = null;

				// Neue Spieler registrieren
				try {
					client = serverSocket.accept();
					YoolooClientHandler clientHandler = new YoolooClientHandler(this, client);
					clientHandlerList.add(clientHandler);
					YoolooLogger.info("[YoolooServer] Anzahl verbundene Spieler: " + clientHandlerList.size());
				} catch (IOException e) {
					YoolooLogger.error("Client Verbindung gescheitert");
					e.printStackTrace();
				}

				long endtime = System.currentTimeMillis();
				long elapsed = endtime - starttime;
				if (elapsed > 18000) {
					// Neue Session starten wenn nicht ausreichend Spieler verbunden sind und 1
					// minute vergangen ist!
					if (clientHandlerList.size() < Math.min(spielerProRunde,
							YoolooKartenspiel.Kartenfarbe.values().length) && clientHandlerList.size() > 0) {
						// Init Session
						YoolooSession yoolooSession = new YoolooSession(clientHandlerList.size(), serverGameModeBot);

						// Starte pro Client einen ClientHandlerTread
						for (int i = 0; i < clientHandlerList.size(); i++) {
							YoolooClientHandler ch = clientHandlerList.get(i);
							ch.setHandlerID(i);
							ch.joinSession(yoolooSession);
							spielerPool.execute(ch); // Start der ClientHandlerThread - Aufruf der Methode run()
						}

						// nuechste Runde eroeffnen
						clientHandlerList = new ArrayList<YoolooClientHandler>();
					}
				}

				// Neue Session starten wenn ausreichend Spieler verbunden sind!
				if (clientHandlerList.size() >= Math.min(spielerProRunde,
						YoolooKartenspiel.Kartenfarbe.values().length)) {
					// Init Session
					YoolooSession yoolooSession = new YoolooSession(clientHandlerList.size(), serverGameMode);

					// Starte pro Client einen ClientHandlerTread
					for (int i = 0; i < clientHandlerList.size(); i++) {
						YoolooClientHandler ch = clientHandlerList.get(i);
						ch.setHandlerID(i);
						ch.joinSession(yoolooSession);
						spielerPool.execute(ch); // Start der ClientHandlerThread - Aufruf der Methode run()
					}

					// nuechste Runde eroeffnen
					clientHandlerList = new ArrayList<YoolooClientHandler>();
				}
			}
		} catch (IOException e1) {
			YoolooLogger.error("ServerSocket nicht gebunden");
			serverAktiv = false;
			e1.printStackTrace();
		}

	}

	// TODO Dummy zur Serverterminierung noch nicht funktional
	public void shutDownServer(int code) {
		if (code == 543210) {
			this.serverAktiv = false;
			YoolooLogger.info("Server wird beendet");
			spielerPool.shutdown();
		} else {
			YoolooLogger.info("Servercode falsch");
		}
	}
}
