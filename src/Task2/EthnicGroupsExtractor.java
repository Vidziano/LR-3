package Task2;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class EthnicGroupsExtractor {

    static class BabyNamesHandler extends DefaultHandler {
        private Map<String, Integer> ethnicGroups = new HashMap<>();
        private boolean inEthnicityTag = false;
        private StringBuilder currentEthnicity = new StringBuilder();
        private int totalEntries = 0;

        public Map<String, Integer> getEthnicGroups() {
            return ethnicGroups;
        }

        public int getTotalEntries() {
            return totalEntries;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equalsIgnoreCase("ethcty")) {
                inEthnicityTag = true;
                currentEthnicity.setLength(0);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equalsIgnoreCase("ethcty")) {
                inEthnicityTag = false;
                String ethnicity = currentEthnicity.toString().trim();
                if (!ethnicity.isEmpty()) {
                    ethnicGroups.put(ethnicity, ethnicGroups.getOrDefault(ethnicity, 0) + 1);
                    totalEntries++;
                }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (inEthnicityTag) {
                currentEthnicity.append(ch, start, length);
            }
        }
    }

    public static void main(String[] args) {
        try {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            BabyNamesHandler handler = new BabyNamesHandler();
            xmlReader.setContentHandler(handler);
            xmlReader.parse(new InputSource(new FileInputStream("Popular_Baby_Names_NY.xml")));
            System.out.println("\n------------------------------------------------------");
            System.out.println("Еthnic groups in alphabetical order:");
            new TreeMap<>(handler.getEthnicGroups()).keySet().stream().limit(30).forEach(System.out::println);
            System.out.println("------------------------------------------------------");
            System.out.println("Ethnic groups found in the dataset and their counts:");
            handler.getEthnicGroups().forEach((ethnicity, count) -> System.out.println(ethnicity + ": " + count));
            System.out.println("------------------------------------------------------");
            System.out.println("Total entries: " + handler.getTotalEntries());
            System.out.println("------------------------------------------------------");



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
