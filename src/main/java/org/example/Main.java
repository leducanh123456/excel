package org.example;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        XMLInputFactory factory = XMLInputFactory.newInstance();

        try (FileInputStream fis = new FileInputStream("C:\\Users\\Admin\\Desktop\\myanmar-latest.osm")) {
            XMLStreamReader reader = factory.createXMLStreamReader(fis);

            int indentLevel = 0;

            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        // In tên phần tử với mức thụt lề
                        printIndent(indentLevel);
                        System.out.println("<" + reader.getLocalName() + ">");

                        // In tất cả các thuộc tính của phần tử
                        for (int i = 0; i < reader.getAttributeCount(); i++) {
                            printIndent(indentLevel + 1);
                            System.out.println("@" + reader.getAttributeLocalName(i) + "=\"" + reader.getAttributeValue(i) + "\"");
                        }
                        indentLevel++;
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        indentLevel--;
                        // In tên phần tử đóng với mức thụt lề
                        printIndent(indentLevel);
                        System.out.println("</" + reader.getLocalName() + ">");
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        if (reader.hasText() && !reader.getText().trim().isEmpty()) {
                            printIndent(indentLevel + 1);
                            System.out.println(reader.getText().trim());
                        }
                        break;

                    default:
                        break;
                }
            }

            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printIndent(int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
    }
}