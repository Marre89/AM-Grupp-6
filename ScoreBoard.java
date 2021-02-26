import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreBoard {
    private List<Score> scores;

    public ScoreBoard() {
        resetScores();
    }

    private void resetScores() {
        scores = new ArrayList<>();
    }

    public void addScore(Score score) {
        getScores().add(score);
    }

    public boolean isHighScore(Score score) {
        
        for (Score existingScore : getScores()) {
            if (!score.equals(existingScore) && existingScore.getScore() > score.getScore()) {
                return false;
            }
        }
        
        return true;
    }

    public List<Score> getSortedByScore() {
        Comparator<Score> scoreComparator = Comparator.comparing(Score::getScore);
        Comparator<Score> nameComparator = Comparator.comparing(Score::getName);

        getScores().sort(scoreComparator.reversed().thenComparing(nameComparator));
        return getScores();
    }

    public List<Score> getScores() {
        return scores;
    }

    public List<Score> getSortedByScore(int limit) {
        return getSortedByScore().subList(0, limit);
    }

    String fileName = "HighScore";
    
    public ScoreBoard loadScoreBoardFromFile(String fileName) {
        ScoreBoard sb = new ScoreBoard();
        
        File file = new File(fileName);
        if(!file.exists()) {
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] splitString = line.split("\t", -1);
                sb.addScore(new Score(splitString[0], Integer.parseInt(splitString[1])));
            }
    
        } catch(IOException e) {
                e.printStackTrace();
            }
            return sb;
        }

    public void writeScoreBoardToFile(ScoreBoard sb, String fileName) {
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
        
            for(Score score : sb.getScores()) {
                bw.append(score.getName() + "\t" + score.getScore() + "\n");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}








