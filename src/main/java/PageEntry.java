import java.util.Objects;

public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final Integer count;

    @Override
    public String toString() {
        return "PageEntry{" + "pdfName='" + pdfName + '\'' +
                ", page=" + page +
                ", count=" + count +
                '}' +
                "\n";
    }

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    @Override
    public int compareTo(PageEntry o) {
        return this.count.compareTo(o.count);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !o.getClass().equals(PageEntry.class)) return false;
        PageEntry pageEntry = (PageEntry) o;
        return this.pdfName.equals(pageEntry.pdfName)
                && this.page == pageEntry.page;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pdfName, page);
    }
}
