
package Task1xml;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XMLTagLister {
    public static void main(String[] args) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                private int elementCount = 0;
                private final int MAX_ELEMENTS = 52; // Максимальна кількість тегів для обробки

                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if (elementCount >= MAX_ELEMENTS) {
                        throw new SAXException("Досягнуто ліміту елементів");
                    }
                    System.out.println("Тег: " + qName);
                    if (attributes.getLength() > 0) {
                        System.out.println("Атрибути:");
                        for (int i = 0; i < attributes.getLength(); i++) {
                            System.out.println(" - " + attributes.getQName(i) + ": " + attributes.getValue(i));
                        }
                    }
                    elementCount++;
                }

                public void endDocument() throws SAXException {
                    System.out.println("Завершення обробки документа після " + elementCount + " елементів.");
                }
            };

            saxParser.parse("Popular_Baby_Names_NY.xml", handler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
               