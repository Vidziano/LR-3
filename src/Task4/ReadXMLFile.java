package Task4;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class ReadXMLFile {
    public static void main(String[] args) {

        try {
            File xmlFile = new File("Sorted_Top_Names.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            System.out.println("Кореневий елемент: " + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("Name");

            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String gender = getTagValue("Gender", eElement);
                    String name = getTagValue("Name", eElement);
                    String count = getTagValue("Count", eElement);
                    String rating = getTagValue("Rating", eElement);

                    // Перевірка на пусті значення перед виведенням
                    if (!gender.isEmpty() && !name.isEmpty() && !count.isEmpty() && !rating.isEmpty()) {
                        System.out.println("Стать : " + gender);
                        System.out.println("Ім'я : " + name);
                        System.out.println("Кількість : " + count);
                        System.out.println("Рейтинг : " + rating);
                        System.out.println("----------------------------");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList != null && nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        } else {
            return "";
        }
    }
}
