package GameEngine;

import jaxb.*;
import jdk.internal.util.xml.impl.Input;

import jaxb.schema.generated.GameDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.lang.String;


public class GameSettingsReader {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "GameEngine.jaxb.schema.generated";

    // TODO: Check path
    
    public static void readGameSettings() {
        System.out.println("Please enter a XML path:");
        Scanner reader = new Scanner(System.in);
        String filePathString;
        
        filePathString = reader.nextLine();
        Path filePath = Paths.get(filePathString);
        // check path
        
        InputStream inputSteam = GameSettingsReader.class.getResourceAsStream(filePath.toAbsolutePath().toString());

    }

    public void extractGameSettings(InputStream xmlStream) throws JAXBException
    {
        try{
            GameDescriptor gamedDescriptor = deserializeFrom(xmlStream);



        } catch (JAXBException e)
        {
            e.printStackTrace();
        }
    }

        private static GameDescriptor  deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (GameDescriptor) u.unmarshal(in);
    }
}
//    public static void main(String[] args) {
//        InputStream inputStream = SchemaBasedJAXBMain.class.getResourceAsStream("/resources/world.xml");
//        try {
//            Countries countries = deserializeFrom(inputStream);
//            System.out.println("name of first country is: " + countries.getCountry().get(0).getName());
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static Countries deserializeFrom(InputStream in) throws JAXBException {
//        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
//        Unmarshaller u = jc.createUnmarshaller();
//        return (Countries) u.unmarshal(in);
//    }

