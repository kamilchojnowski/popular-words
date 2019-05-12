package pl.sii;

import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class PopularWordsTest {
    private static final PopularWords testee = new PopularWords();

    @Test
    public void shouldReturnOneThousandMostPopularWords() {
        //given
        Map<String, Long> wordsFrequencyListCreatedByAdamKilgarriff = getWordsFrequencyListCreatedByAdamKilgarriff();

        //when
        Map<String, Long> result = testee.findOneThousandMostPopularWords();

        //then
        assertFalse(result.isEmpty());
        assertEquals(1000, result.size());
        compareWordListsFrequency(wordsFrequencyListCreatedByAdamKilgarriff, result);
    }

    private void compareWordListsFrequency(Map<String, Long> wordsFrequencyListCreatedByAdamKilgarriff, Map<String, Long> result) {
        long totalFrequencyByKilgarriff = wordsFrequencyListCreatedByAdamKilgarriff.values().stream().reduce(0L, Long::sum);
        long totalFrequencyInAResult = result.values().stream().reduce(0L, Long::sum);
        System.out.println("totalFrequencyByKilgarriff = " + totalFrequencyByKilgarriff);
        System.out.println("totalFrequencyInAResult = " + totalFrequencyInAResult);

        result.forEach((key, value) -> {
            BigDecimal valueUsagePercentage = calculatePercentage(value, totalFrequencyInAResult);
            BigDecimal kilgarriffUsagePercentage = calculatePercentage(wordsFrequencyListCreatedByAdamKilgarriff.get(key), totalFrequencyByKilgarriff);
            BigDecimal diff = kilgarriffUsagePercentage.subtract(valueUsagePercentage);
            System.out.println(key + "," + valueUsagePercentage + "%," + kilgarriffUsagePercentage + "%," + (new BigDecimal(0.5).compareTo(diff.abs()) > 0) + " " + diff);
        });
    }

    private BigDecimal calculatePercentage(double obtained, double total) {
        return new BigDecimal(obtained * 100 / total).setScale(4, RoundingMode.HALF_UP);
    }

    private Map<String, Long> getWordsFrequencyListCreatedByAdamKilgarriff() {
        List<String> wordsFrequencyListCreatedByAdamKilgarriff = null;
        Map<String, Long> map = new HashMap<>();

        try {
            Path path = Paths.get(getClass().getClassLoader().getResource("all.num").toURI());
            Stream<String> lines = Files.lines(path);
            wordsFrequencyListCreatedByAdamKilgarriff = lines.collect(Collectors.toList());
            lines.close();
        } catch (IOException eIO){
            eIO.getStackTrace();
        } catch (URISyntaxException eURISyntax){
            eURISyntax.getStackTrace();
        }

        int i = 1;
        while(i<wordsFrequencyListCreatedByAdamKilgarriff.size()){
            map.put(wordsFrequencyListCreatedByAdamKilgarriff.get(i).split(" ")[1], Long.parseLong(wordsFrequencyListCreatedByAdamKilgarriff.get(i).split(" ")[0]));
            i++;
        }

        return map;
    }
}
