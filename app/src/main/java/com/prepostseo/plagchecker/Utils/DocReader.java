package com.prepostseo.plagchecker.Utils;

import java.io.File;
import java.io.FileInputStream;

//import org.apache.poi.xwpf.;
//import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
/**
 * Created by Hassan on 10/21/17.
 */

public class DocReader {
    public static StringBuilder readDocFile(String fileName) {

        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());

//            HWPFDocument doc = new HWPFDocument(fis);
//
//            WordExtractor we = new WordExtractor(doc);

            StringBuilder stringBuilder = new StringBuilder();
//            String[] paragraphs = we.getParagraphText();
//
//            System.out.println("Total no of paragraph "+paragraphs.length);
//            for (String para : paragraphs) {
//                //System.out.println(para.toString());
//                stringBuilder.append(para.toString());
//            }
//            fis.close();
            return stringBuilder;

        } catch (Exception e) {
            return  null;
        }

    }
    public static StringBuilder readDocxFile(String fileName) {

        try {

            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            XWPFDocument docx = new XWPFDocument(fis);

            StringBuilder stringBuilder=new StringBuilder();
            //using XWPFWordExtractor Class
            XWPFWordExtractor we = new XWPFWordExtractor(docx);
            stringBuilder.append( we.getText());
            return  stringBuilder;

        } catch (Exception e) {
            return  null;
        }

    }
}
