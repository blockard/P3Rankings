import java.util.ArrayList;
import java.util.Collections;

public class Player implements Comparable<Player> {
	private double rank;
	private int id;
	private String name;
	private ArrayList<Integer> wins = new ArrayList<Integer>(Collections.nCopies(100, 0));

	public Player() {
		this.id = -1;
	}

	public Player(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public double getRank() {
		return rank;
	}

	public void setRank(double rank) {
		this.rank = rank;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Integer> getWins() {
		return wins;
	}

	public void setWins(ArrayList<Integer> wins) {
		this.wins = wins;
	}

	public int getWinsAgainst(int index) {
		return this.wins.get(index);
	}

	public void wonAgainst(int index) {
		this.wins.set(index, this.wins.get(index) + 1);
	}

	public void setMatchup(int index, int wins) {
		this.wins.set(index, wins);
	}

	@Override
	public int compareTo(Player otherPlayer) {
		return (int) ((otherPlayer.getRank() - this.rank) * 1000);
	}

	public String getPlayerInfo() {
		return this.name + "#" + this.id;
	}
}
