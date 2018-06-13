package ra.sumbayak.ranalyzer.util;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMIDocument {
    
    public static Document create () {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
            DocumentBuilder builder = factory.newDocumentBuilder ();
            return builder.newDocument ();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace ();
            return null;
        }
    }
    
    public static Document open (File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
            DocumentBuilder builder = factory.newDocumentBuilder ();
            Document doc = builder.parse (file);
            doc.getDocumentElement ().normalize ();
            return doc;
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace ();
            return null;
        }
    }
}
