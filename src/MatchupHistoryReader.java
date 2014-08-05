import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MatchupHistoryReader {

	private ArrayList<Player> players;
	private int playerCount = 0;

	@SuppressWarnings("resource")
	public ArrayList<Player> readHistory() throws IOException {
		players = new ArrayList<Player>(Collections.nCopies(100, new Player()));
		BufferedReader reader = new BufferedReader(new FileReader("bin/matchup_history_save.txt"));
		String matchup = reader.readLine();
		while (matchup != null) {
			updateMatchup(matchup);
			matchup = reader.readLine();
		}
		ArrayList<Player> p = new ArrayList<Player>();
		p.addAll(players.subList(0, playerCount));
		return p;
	}

	private void updateMatchup(String matchup) {
		String[] split = matchup.split(" ");
		int p1wins = Integer.parseInt(split[1]);
		int p2wins = Integer.parseInt(split[4]);

		int p1id = Integer.parseInt(split[0].split("#")[1]);
		int p2id = Integer.parseInt(split[3].split("#")[1]);
		Player p1 = players.get(p1id);
		Player p2 = players.get(p2id);
		if (p1.getId() == -1) {
			p1 = new Player(p1id, split[0].split("#")[0]);
			playerCount++;
		}
		if (p2.getId() == -1) {
			p2 = new Player(p2id, split[3].split("#")[0]);
			playerCount++;
		}
		p1.setMatchup(p2id, p1wins);
		p2.setMatchup(p1id, p2wins);
		players.set(p1id, p1);
		players.set(p2id, p2);
	}
}
