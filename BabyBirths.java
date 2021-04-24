/**
 * Print out total number of babies born, as well as for each gender, in a given CSV file of baby name data.
 * 
 * @author Duke Software Team 
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;

public class BabyBirths {
    public void printNames () {
        FileResource fr = new FileResource();
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            if (numBorn <= 100) {
                System.out.println("Name " + rec.get(0) +
                           " Gender " + rec.get(1) +
                           " Num Born " + rec.get(2));
            }
        }
    }

    public void totalBirths (FileResource fr) {
        int totalBirths = 0;
        int totalNames = 0;
        int totalNumberOfBoys = 0;
        int totalNumberOfGirls = 0;
        int totalBoysNames = 0;
        int totalGirlsNames = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirths += numBorn;
            totalNames++;
            if (rec.get(1).equals("M")) {
                totalNumberOfBoys += numBorn;
                totalBoysNames++;
            }
            else {
                totalNumberOfGirls += numBorn;
                totalGirlsNames++;
            }
        }
        System.out.println("Total births = " + totalBirths);
        System.out.println("Total Girls = " + totalNumberOfGirls);
        System.out.println("Total Boys = " + totalNumberOfBoys);
        
        System.out.println("Total different names = " + totalNames);
        System.out.println("Total Girls names = " + totalGirlsNames);
            System.out.println("Total Boys names = " + totalBoysNames);
    }

    public int getRank(int year, String name, String gender){
        int rank = -1;
        int totalCount = 0;
        int girlsCount = 0;
        FileResource fr = new FileResource("data\\yob" + year + ".csv");
        for (CSVRecord rec : fr.getCSVParser(false)) {
            totalCount++;
            if (rec.get(1).equals("F")) girlsCount++;
            if (rec.get(1).equals(gender) && rec.get(0).equals(name)) {
                rank = totalCount == girlsCount ? girlsCount : totalCount - girlsCount;
            }
        }
        return rank;
    }
    
    public int getRank(FileResource fr, String name, String gender){
        int rank = -1;
        int totalCount = 0;
        int girlsCount = 0;
        
        for (CSVRecord rec : fr.getCSVParser(false)) {
            totalCount++;
            if (rec.get(1).equals("F")) girlsCount++;
            if (rec.get(1).equals(gender) && rec.get(0).equals(name)) {
                rank = totalCount == girlsCount ? girlsCount : totalCount - girlsCount;
            }
        }
        return rank;
    }
    
    public String getName(int year, int rank, String gender){
        int boyCounter = 0;
        int totalCounter = 1;
        FileResource fr = new FileResource("data\\yob" + year + ".csv");
        for (CSVRecord rec : fr.getCSVParser(false)) {
            if (gender.equals("F") && rec.get(1).equals("F") && totalCounter == rank) {
                return rec.get(0);
            } 
            if (gender.equals("M") && rec.get(1).equals("M")){
                boyCounter++;
                if (boyCounter == rank) return rec.get(0);
            }
            totalCounter++;
        }
        return "NO NAME";
    }
    
    public void whatIsNameInYear(String name, int year, int newYear, String gender){
        int rankOfName = getRank(year, name, gender);
        String nameInNewYear = getName(newYear, rankOfName, gender);
        System.out.println(name + " born in " + year + " would be " + nameInNewYear + " if she was born in " + newYear);
    }
    
    public int highestRankInManyFiles(String name, String gender){
        // return the highest a name has ranked in any of multiple files.
        int highestRankTotal = 0;
        boolean flagFirstRank = true;
        
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            int currRank = getRank(fr, name, gender);
            if (flagFirstRank){
                highestRankTotal = currRank;
            } else {
                if(highestRankTotal > currRank && currRank != -1) highestRankTotal = currRank;
            }
            flagFirstRank = false;
        }
        System.out.println(highestRankTotal);
        return highestRankTotal;
    }
    
    public double getAverageRank(String name, String gender){
        // return the average a name has ranked in any of multiple files.
        double averageRank = 0;
        int sumOfRanks = 0;
        int counter = 0;
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            int currRank = getRank(fr, name, gender);
            if (currRank != -1){
                sumOfRanks += currRank;
            }
            counter++;
        }
        averageRank = (double)sumOfRanks / counter;
        if (sumOfRanks == 0) return -1;
        return averageRank;
    }
    
    public int yearOfHighestRank(String name, String gender){
        // return the year number a name has ranked higher in.
        int highestRankTotal = -1;
        boolean flagFirstRank = true;
        String fileNameHighRank = null;
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            int currRank = getRank(fr, name, gender);
            //System.out.println(currRank);
            if (flagFirstRank && currRank != -1){
                highestRankTotal = currRank;
                fileNameHighRank = f.getName();
                flagFirstRank = false;
            } else {
                if(highestRankTotal > currRank && currRank != -1) {
                    highestRankTotal = currRank;
                    fileNameHighRank = f.getName();
                }
            }
            //System.out.println(highestRankTotal);
        }
        if (highestRankTotal == -1) {
            return -1;
        } else {
            return Integer.parseInt(fileNameHighRank.substring(fileNameHighRank.indexOf("yob") + 3, fileNameHighRank.indexOf("yob") + 7));
        }
    }
    
    public int getTotalBirthsRankedHigher(int year, String name, String gender){
        int result = 0;
        int totalBirths = 0;
        int girlsBirths = 0;
        FileResource fr = new FileResource("data\\yob" + year + ".csv");
        for (CSVRecord rec : fr.getCSVParser(false)) {
            if (rec.get(1).equals(gender) && rec.get(0).equals(name)) {
                result = totalBirths == girlsBirths ? girlsBirths : totalBirths - girlsBirths;
            }
            totalBirths += Integer.parseInt(rec.get(2));
            if (rec.get(1).equals("F")) girlsBirths += Integer.parseInt(rec.get(2));
        }
        return result;
    }
    
    public void testGetTotalBirthsRankedHigher() {
        int totalBirths = getTotalBirthsRankedHigher(1990, "Drew", "M");
        System.out.println("Total Births Ranked Higher: " + totalBirths);
    }
    
    public void testGetAverageRank(){
        System.out.println(getAverageRank("Genevieve", "F"));
    }
    
    public void testYearOfHighestRank(){
        System.out.println(yearOfHighestRank("Genevieve", "F"));
    }
    
    public void testTotalBirths () {
        FileResource fr = new FileResource("data/yob1905.csv");
        totalBirths(fr);
    }
    
    public void testgetRank() {
        int rank = getRank(1971, "Frank", "M");
        System.out.println("Rank is: " + rank);
    }
    
    public void testgetName() {
        int rank = 350 ;
        System.out.println("Name of rank " + rank + " is: " + getName(1980, rank, "F"));
    }
    
    public void testWhatIsNameInYear(){
        whatIsNameInYear("Owen", 1974, 2014, "M");
    }
}
