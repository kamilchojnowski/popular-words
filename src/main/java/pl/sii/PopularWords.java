package pl.sii;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PopularWords {

    public static void main(String[] args) {
        PopularWords popularWords = new PopularWords();
        Map<String, Long> result = popularWords.findOneThousandMostPopularWords();
        result.entrySet().forEach(System.out::println);
    }

    public Map<String, Long> findOneThousandMostPopularWords() {
        List<String> wordsToLookThrough = new ArrayList<>();
        List<String> mostPopularWords = new ArrayList<>();
        Map<String, Long> map = new HashMap<>();

        //I made an assumption I cannot change the structure of the project. However, if I could, I would place file all.num in the src/main/resources,
        //then I could erase method {@code readFromTestResourcesFile(String resource)} and use {@code mostPopularWords = readDataFromResourcesFile("all.num");
        try {
            wordsToLookThrough = readDataFromResourcesFile("3esl.txt");
            mostPopularWords = readDataFromTestResourcesFile("all.num");
        } catch (IOException eUI){
            eUI.getStackTrace();
        } catch (URISyntaxException eURISyntax){
            eURISyntax.getStackTrace();
        } catch (NullPointerException eNullPointer){
            eNullPointer.getStackTrace();
        }

        int i = 1;

        //second condition here handles changes is file 3esl.txt - in case there are less than 1000 words
        //method {@code findOneThousandMostPopularWords()} will return a valid map with no exceptions. I assumed that's how it should work, it will fail the tests anyway, no need to,
        //double checking it. However the name of the method can be misleading because it will return less words.
        while(map.size() != 1000 && i < mostPopularWords.size()){
            String[] wordWithFrequency = mostPopularWords.get(i).split(" ");
            if(wordsToLookThrough.contains(wordWithFrequency[1])){
                map.put(wordWithFrequency[1],Long.parseLong(wordWithFrequency[0]));
            }
            i++;
        }

        return map;
    }

    private List<String> readDataFromResourcesFile(String resource) throws IOException, URISyntaxException {
        Path path = Paths.get(getClass().getClassLoader().getResource(resource).toURI());
        Stream<String> lines = Files.lines(path);
        List<String> data = lines.collect(Collectors.toList());
        lines.close();

        return data;
    }

    private List<String> readDataFromTestResourcesFile(String resource) throws IOException {
        Path path = Paths.get(System.getProperty("user.dir").concat("\\target\\test-classes\\" + resource));

        //other way, without using target path
        //{@code Path path = Paths.get(System.getProperty("user.dir").toString().concat("\\src\\test\\resources\\" + resource));}

        Stream<String> lines = Files.lines(path);
        List<String> data = lines.collect(Collectors.toList());
        lines.close();

        return data;
    }


}
