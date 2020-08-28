package uk.ac.reading.rt016631.game;

class HighScore {
    String name;
    int score; //TODO hcange to long ?

    /**Default constructor; creates a HighScore object with default values for attributes (0 or null)*/
    public HighScore() {
        name = null;
        score = 0;
    }

    /**
     * Creates a HighScore object with the attributes specified by the parameters
     * @param name name of player with high score
     * @param score player's score
     */
    public HighScore(String name, int score) {
        this.name = name;
        this.score = score;
    }
}
