import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    Map<String, List<PageEntry>> answers = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {

        for (File file : Objects.requireNonNull(pdfsDir.listFiles())) {
            var doc = new PdfDocument(new PdfReader(file));
            int docNumberOfPages = doc.getNumberOfPages();

            for (int i = 1; docNumberOfPages >= i; i++) {
                var text = PdfTextExtractor.getTextFromPage(doc.getPage(i));
                var words = text.split("\\P{IsAlphabetic}+");
                Map<String, Integer> freqs = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    freqs.put(word.toLowerCase(), freqs.getOrDefault(word.toLowerCase(), 0) + 1);
                }
                for (var word : freqs.keySet()) {
                    List<PageEntry> list;
                    if (!answers.containsKey(word)) {
                        list = new ArrayList<>();
                    } else {
                        list = answers.get(word);
                    }
                    list.add(new PageEntry(file.getName(), i, freqs.get(word.toLowerCase())));
                    answers.put(word, list);
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        if (answers.get(word) == null) {
            return null;
        }
        List<PageEntry> list = answers.get(word);
        list.sort(Collections.reverseOrder());
        return list;
    }
}

