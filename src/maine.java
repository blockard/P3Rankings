import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

public class maine {

	public static ArrayList<Player> players;
	public static ArrayList<Player> sorted = new ArrayList<Player>();
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static MatchupHistoryReader mhr = new MatchupHistoryReader();

	public static void main(String[] args) throws IOException {
		players = new ArrayList<Player>();
		players = mhr.readHistory();
		run();
	}

	private static Player addPlayer() throws IOException {
		int id = players.size();
		System.out.print("Enter player name: ");
		String name = br.readLine();
		Player newPlayer = new Player(id, name);
		return newPlayer;
	}

	public static void run() throws IOException {
		System.out.print("Action: ");
		String input = br.readLine();
		while (!input.equals("q!")) {
			if (input.equals("play")) {
				recordGame();
			} else if (input.equals("Matchups")) {
				displayMatchups();
			} else if (input.equals("My Matchups")) {
				displayMatchup();
			} else if (input.equals("%")) {
				displayWinPercentage();
			} else if (input.equals("Register")) {
				players.add(addPlayer());
			} else if (input.equals("Player list")) {
				displayPlayers();
			} else if (input.equals("Rankings")) {
				displayRankings();
			} else if (input.equals("Save")) {
				save();
			}
			System.out.print("Action: ");
			input = br.readLine();
		}
	}

	private static void displayMatchup() throws IOException {
		System.out.print("Enter player name: ");
		String name = br.readLine();
		int id = getPlayerId(name);
		if (id == -1) {
			System.out.println("Invalid player name");
		} else {
			Player p = players.get(id);
			for (int i = 0; i < p.getWins().size(); i++) {
				
			}
		}
	}

	private static void save() throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("bin/matchup_history_save.txt", "UTF-8");
		for (int x = 0; x < players.size(); x++) {
			for (int y = x + 1; y < players.size(); y++) {
				if (x != y) {
					int wins = players.get(x).getWinsAgainst(y);
					int losses = players.get(y).getWinsAgainst(x);
					if (wins != 0 || losses != 0) {
						writer.println(players.get(x).getPlayerInfo() + " " + wins + " - " + players.get(y).getPlayerInfo() + " "
								+ losses);
					}
				}
			}
		}
		writer.close();
	}

	private static void displayPlayers() {
		for (int i = 0; i < players.size(); i++) {
			System.out.println(players.get(i).getPlayerInfo());
		}
	}

	private static void displayMatchups() {
		for (int x = 0; x < players.size(); x++) {
			for (int y = x + 1; y < players.size(); y++) {
				if (x != y) {
					int wins = players.get(x).getWinsAgainst(y);
					int losses = players.get(y).getWinsAgainst(x);
					if (wins != 0 || losses != 0) {
						System.out.println(players.get(x).getName() + " " + wins + " - " + players.get(y).getName() + " " + losses);
					}
				}
			}
		}
	}

	private static void displayWinPercentage() {
		for (int x = 0; x < players.size(); x++) {
			for (int y = x + 1; y < players.size(); y++) {
				if (x != y) {
					float wins = players.get(x).getWinsAgainst(y);
					float losses = players.get(y).getWinsAgainst(x);
					if (wins != 0 || losses != 0) {
						int win = (int) ((wins / (wins + losses)) * 100);
						int loss = (int) ((losses / (wins + losses)) * 100);
						System.out.println(players.get(x).getName() + " " + win + "% - " + players.get(y).getName() + " " + loss
								+ "%");
					}
				}
			}
		}
	}

	private static void displayRankings() {
		double[][] m = new double[players.size()][players.size()];
		double[] b = new double[players.size()];
		float wins;
		float losses;
		for (int x = 0; x < players.size(); x++) {
			b[x] = 1;
			m[x][x] = 0;
			for (int y = x + 1; y < players.size(); y++) {
				wins = players.get(x).getWinsAgainst(y);
				losses = players.get(y).getWinsAgainst(x);
				if (wins == 0 && losses == 0) {
					m[x][y] = 1;
					m[y][x] = 1;
				} else {
					m[x][y] = 1 + (wins / (wins + losses));
					m[y][x] = 1 + (losses / (wins + losses));
				}
			}
		}
		NonNegativeLeastSquares nnls = new NonNegativeLeastSquares(players.size(), players.size(), m, b);
		nnls.solve();
		double[] ranks = nnls.x;
		Player p;
		double score;
		for (int i = 0; i < players.size(); i++) {
			p = players.get(i);
			score = 0;
			for (int j = 0; j < players.size(); j++) {
				wins = p.getWinsAgainst(j);
				losses = players.get(j).getWinsAgainst(i);
				if (wins != 0 || losses != 0) {
					score += (wins / (wins + losses)) * ranks[j];
				}
			}
			players.get(i).setRank(score);
		}

		sorted.clear();
		sorted.addAll(players);
		Collections.sort(sorted);
		for (int i = 0; i < sorted.size(); i++) {
			System.out.println(sorted.get(i).getName() + ": " + (int) (sorted.get(i).getRank() * 10000));
		}
	}

	private static void recordGame() throws IOException {
		System.out.print("Enter winner: ");
		String winner = br.readLine();
		System.out.print("Enter loser: ");
		String loser = br.readLine();
		int winnerId = getPlayerId(winner);
		int loserId = getPlayerId(loser);
		if (winnerId != -1 && loserId != -1) {
			players.get(winnerId).wonAgainst(loserId);
		} else {
			System.out.println("Invalid player name");
		}
		save();
	}

	private static int getPlayerId(String name) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getName().equals(name)) {
				return players.get(i).getId();
			}
		}
		return -1;
	}
}
