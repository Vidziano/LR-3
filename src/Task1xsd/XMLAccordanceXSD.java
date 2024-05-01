package Task1xsd;


import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import javax.xml.transform.sax.SAXSource;

public class XMLAccordanceXSD {
    public static void cheking (String xmlFileName, String xsdFileName) {

        String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;

        SchemaFactory factory = SchemaFactory.newInstance(language);

        Schema schema = null;

        try {
            schema = factory.newSchema(new File(xsdFileName));

            Validator validator = schema.newValidator();

            SAXSource source = new SAXSource(new InputSource(new BufferedReader(new FileReader(new File(xmlFileName)))));

            System.out.println("\nСhecking document " + xmlFileName +
                    " validation in accordance with " + xsdFileName );

            validator.validate(source);

            System.out.println("The document is confirmed normally. ");

        } catch (SAXException e) {

            e.printStackTrace();

            System.exit(1);

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }


    public static void main(String[] args) {
        XMLAccordanceXSD.cheking("Popular_Baby_Names_NY.xml", "Popular_Baby_Names_NY.xsd");
    }

}