package Task3;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class PopularNamesExtractor {
    public static void main(String[] args) {
        try {
            int topNamesCount = 500;

            File xmlFile = new File("Popular_Baby_Names_NY.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList rowList = doc.getElementsByTagName("row");
            System.out.println("All " + topNamesCount + " names were found");

            Map<String, Babie> nameMap = new HashMap<>();

            for (int i = 0; i < rowList.getLength(); i++) {
                Element row = (Element) rowList.item(i);
                String ethnicGroup = row.getElementsByTagName("ethcty").item(0).getTextContent().trim();
                if (!"HISPANIC".equals(ethnicGroup)) continue;

                String name = row.getElementsByTagName("nm").item(0).getTextContent();
                String gender = row.getElementsByTagName("gndr").item(0).getTextContent();
                int count = Integer.parseInt(row.getElementsByTagName("cnt").item(0).getTextContent());
                int rank = Integer.parseInt(row.getElementsByTagName("rnk").item(0).getTextContent());

                Babie existing = nameMap.get(name);
                if (existing == null) {
                    nameMap.put(name, new Babie(gender, name, count, rank));
                } else {
                    existing.incrementCount(count);
                    existing.updateRating(rank);
                }
            }

            List<Babie> babies = new ArrayList<>(nameMap.values());
            Collections.sort(babies, Comparator.comparingInt(Babie::getRating).thenComparingInt(Babie::getCount).reversed());

            for (Babie babie : babies) {
                System.out.println(babie);
            }

            Document newDoc = dBuilder.newDocument();
            Element rootElement = newDoc.createElement("TopBabyNames");
            newDoc.appendChild(rootElement);

            for (Babie babie : babies) {
                Element nameElement = newDoc.createElement("Name");
                nameElement.appendChild(newDoc.createElement("Gender")).appendChild(newDoc.createTextNode(babie.getGender()));
                nameElement.appendChild(newDoc.createElement("Name")).appendChild(newDoc.createTextNode(babie.getName()));
                nameElement.appendChild(newDoc.createElement("Count")).appendChild(newDoc.createTextNode(String.valueOf(babie.getCount())));
                nameElement.appendChild(newDoc.createElement("Rating")).appendChild(newDoc.createTextNode(String.valueOf(babie.getRating())));
                rootElement.appendChild(nameElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(newDoc);
            StreamResult result = new StreamResult(new File("Sorted_Top_Names.xml"));
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Babie {
        private String gender;
        private String name;
        private int count;
        private int rating;

        public Babie(String gender, String name, int count, int rating) {
            this.gender = gender;
            this.name = name;
            this.count = count;
            this.rating = rating;
        }

        public void incrementCount(int additionalCount) {
            this.count += additionalCount;
        }

        public void updateRating(int newRating) {
            if (newRating < this.rating) {
                this.rating = newRating;
            }
        }

        public String getGender() {
            return gender;
        }

        public String getName() {
            return name;
        }

        public int getCount() {
            return count;
        }

        public int getRating() {
            return rating;
        }

        @Override
        public String toString() {
            return String.format("Babie{gender='%s', name='%s', nums=%d, rating=%d}", gender, name, count, rating);
        }
    }
}
