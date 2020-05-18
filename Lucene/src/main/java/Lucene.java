import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import java.io.IOException;

public class Lucene{

    static int maxHitsPerPage = 10;
    static StandardAnalyzer analyzer = new StandardAnalyzer();
    static IndexWriterConfig config = new IndexWriterConfig(analyzer);
    static Directory index = new RAMDirectory();
    static IndexWriter w;

    static {
        try {
            w = new IndexWriter(index, config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void addDoc(String title, String body) throws IOException {
        System.out.println("A New Document Added Successfully!");
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new StringField("body", body, Field.Store.YES));
        w.addDocument(doc);
    }


    public static void searchQuery(String field,String queryString) throws ParseException, IOException {
        System.out.println("Query field " +field+", Query String: "+queryString);
        // Build a lucene query for the given string
        Query q = new QueryParser(field, analyzer).parse(queryString);

        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, maxHitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs; // returns the docIds

        System.out.println("Found " + hits.length + " hits!");
        for(int i=0;i<hits.length;++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("title") + "\t" + d.get("body"));
        }
    }

    public static void main(String args[]) throws IOException, ParseException {


        addDoc("Doc1","Nothing is here");
        addDoc("Doc2","Again Nothing is here");
        addDoc("Doc3","Again Bro, Again Nothing is here");
        addDoc("Doc4","Are you expecting Anything?");

        addDoc("Lucene in Action", "193398817");
        addDoc("Lucene for Dummies", "55320055Z");
        addDoc("Managing Gigabytes", "55063554A");
        addDoc("The Art of Computer Science", "9900333X");
        w.commit();

        searchQuery("title","doc");
        searchQuery("title","doc2");
        searchQuery("title","Lucene");
        searchQuery("title","Computer");
        searchQuery("body","here"); // Cant search in the values! only keys

//        Scanner sc=new Scanner(System.in);
//        int q = sc.nextInt();
//        while(q>1){
//            q--;
//            String query= sc.next();
//            String field= sc.next();
//            searchQuery(field,query);
//        }

        w.close();
    }
}