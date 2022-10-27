package controller;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.wltea.analyzer.lucene.IKAnalyzer;
import pojo.Book;
import service.BookService;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private BookService bookService;

    @RequestMapping("searchBook.do")
    public ModelAndView searchBook(Book book) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {
        ModelAndView mav = new ModelAndView("searchBook");

        String keyword = book.getName();
        System.out.println(keyword);

        IKAnalyzer analyzer = new IKAnalyzer();

        Directory index = createIndex(analyzer);

        Query query=new QueryParser("name",analyzer).parse(keyword);

        IndexReader reader= DirectoryReader.open(index);
        IndexSearcher searcher=new IndexSearcher(reader);
        int numberPerPage=10;
        ScoreDoc[] hits=searcher.search(query,numberPerPage).scoreDocs;
        List<Book>books=new ArrayList<>();
        for(int i=0;i<hits.length;i++){
            ScoreDoc scoreDoc=hits[i];
            int docId=scoreDoc.doc;
            Document document=searcher.doc(docId);
            Book tmpBook=bookService.get(Integer.parseInt(document.get("id")));
            books.add(tmpBook);
        }
        mav.addObject("books",books);
        return mav;
    }



    private Directory createIndex(IKAnalyzer analyzer)throws IOException{
        Directory index=new RAMDirectory();
        IndexWriterConfig config=new IndexWriterConfig(analyzer);
        IndexWriter writer=new IndexWriter(index,config);
        List<Book>books=bookService.listByBookType(1);
        for (Book book:books){
            addDoc(writer,book);
        }
        writer.close();
        return index;
    }

    private void addDoc(IndexWriter writer,Book book)throws IOException{
        Document document=new Document();
        document.add(new TextField("id",book.getId()+"", Field.Store.YES));
        document.add(new TextField("name",book.getName(),Field.Store.YES));
        writer.addDocument(document);
    }
}
