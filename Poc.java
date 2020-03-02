import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Poc {

	Integer numberOfPlayer = 4; // Input

	Map<Integer, String> eachPlayersHolding = new HashMap<Integer, String>();

	public static void main(String args[]) {
		Poc poc = new Poc();
		Set<String> deck = poc.fillUpTheDeck(); // I have used set because we don't
											// want same sequence in the deck
											// every time
		System.out.println("Normal deck");
		poc.printTheDeck(deck);
		deck = poc.shuffleTheDeck(deck);
		System.out.println("Shuffled deck");
		poc.printTheDeck(deck);
		poc.startTheGame(deck);
		System.out.println("Cards that each player holds");
		poc.printTheCardEachPlayerIsHolding(poc.eachPlayersHolding);
		Integer winner = poc.findTheWinner(poc.eachPlayersHolding);
		System.out.println("Winner is Player " +winner);
		poc.finishTheGame(poc.eachPlayersHolding, deck);
	}

	/**
	 * This method finishes the game by returning all the cards back to the deck
	 * 
	 * @param eachPlayersHolding
	 * @param deck
	 */
	private void finishTheGame(Map<Integer, String> eachPlayersHolding, Set<String> deck) {
		deck.addAll(eachPlayersHolding.values());
	}

	/**
	 * This method prints what each player is holding
	 * 
	 * @param eachPlayersHolding
	 */
	private void printTheCardEachPlayerIsHolding(Map<Integer, String> eachPlayersHolding) {
		System.out.println(eachPlayersHolding);
	}

	/**
	 * This method starts the game by distributing the cards amongst players
	 * 
	 * @param deck
	 */
	private void startTheGame(Set<String> deck) {
		for (int i = 1; i <= numberOfPlayer; i++) {
			eachPlayersHolding.put(i, getRandomElement(deck));
		}
	}

	/**
	 * This method finds the winner
	 * 
	 * @param eachPlayersHolding
	 * @return
	 */
	private Integer findTheWinner(Map<Integer, String> eachPlayersHolding) {
		String previousBig = null;
		Integer winner = null;
		boolean first =  true;
		for(Map.Entry<Integer, String> entry : eachPlayersHolding.entrySet()) {
			if(first) {
			previousBig = entry.getValue();
			winner = entry.getKey();
			first = false;
			continue;
			} 
			
			if(!first) {
				Integer index1 = previousBig.indexOf("-");
				Integer previousNumber = Integer.parseInt(previousBig.substring(0,index1));
				Integer index2 = entry.getValue().indexOf("-");
				Integer currentNumber = Integer.parseInt(entry.getValue().substring(0,index2));
				if(currentNumber > previousNumber) {
					previousBig = entry.getValue();
					winner = entry.getKey();
				}
				if(currentNumber == previousNumber) {
					String previousType = previousBig.substring(index1+1,previousBig.length());
					String currentType = entry.getValue().substring(index2+1,entry.getValue().length());
					switch(currentType) {
					case "Spades" : previousBig = entry.getValue();
					                winner = entry.getKey();
					                break;
					                
					case "Heart" : if(!previousType.equals("Spades")) {
						           previousBig = entry.getValue();
	                               winner = entry.getKey();
					               }
	                               break;
					case "Club" : if(previousType.equals("Diamonds")) {
						          previousBig = entry.getValue();
                                  winner = entry.getKey();
					              }
                                  break;
					}
				}
			}
		}
		return winner;
	}

	/**
	 * This method prints the deck
	 * 
	 * @param deck
	 */
	private void printTheDeck(Set<String> deck) {
		System.out.println(deck);
	}

	/**
	 * This method adds required players to the game
	 * 
	 * @param numberOfPlayersToAdd
	 */
	private void addPlayer(Integer numberOfPlayersToAdd) {
		numberOfPlayer = numberOfPlayersToAdd + numberOfPlayer;
	}

	/**
	 * This method removes required players to the game
	 * 
	 * @param numberOfPlayersToRemove
	 */
	private void removePlayer(Integer numberOfPlayersToRemove) {
		numberOfPlayer = numberOfPlayer - numberOfPlayersToRemove;
	}

	/**
	 * This method fills up the deck
	 * 
	 * @return
	 */
	private Set<String> fillUpTheDeck() {
		Set<String> deck = new HashSet<String>();
		for (int i = 1; i <= 13; i++) {
			deck.add(String.valueOf(i) + "-Spades");
		}
		for (int i = 1; i <= 13; i++) {
			deck.add(String.valueOf(i) + "-Heart");
		}
		for (int i = 1; i <= 13; i++) {
			deck.add(String.valueOf(i) + "-Club");
		}
		for (int i = 1; i <= 13; i++) {
			deck.add(String.valueOf(i) + "-Diamonds");
		}
		return deck;
	}

	/**
	 * This method shuffles the deck
	 * 
	 * @param deck
	 * @return
	 */
	private Set<String> shuffleTheDeck(Set<String> deck) {
		Set<String> shuffledDeck = new HashSet<String>();
		Iterator<String> itr = deck.iterator();
		Map<String, String> shuffledMap = new HashMap<String, String>();
		while (itr.hasNext()) {
			shuffledMap.put(itr.next(), itr.next());
		}
		shuffledDeck.addAll(shuffledMap.values());
		return shuffledDeck;
	}

	/**
	 * This method distributes the cards amongst players
	 * 
	 * @param deck
	 * @return
	 */
	private String getRandomElement(Set<String> deck) {
		Random random = new Random();
		int randomNumber = random.nextInt(deck.size());
		Iterator<String> iterator = deck.iterator();
		int currentIndex = 0;
		String randomElement = null;
		while (iterator.hasNext()) {
			randomElement = iterator.next();
			if (currentIndex == randomNumber) {
				deck.remove(randomElement); // removing the card from the deck
											// so that it won't get served to
											// other player
				return randomElement;
			}
			currentIndex++;
		}
		return randomElement;
	}
}
