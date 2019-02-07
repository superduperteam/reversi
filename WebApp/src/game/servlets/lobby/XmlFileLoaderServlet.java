package game.servlets.lobby;

import Exceptions.*;
import GameEngine.GameManager;
import GameEngine.GameSettingsReader;
import com.sun.media.sound.InvalidDataException;
import game.handlers.ServletContextHandler;
import game.json.MessageJson;
import game.handlers.JsonManager;
import game.webLogic.Room;
import game.webLogic.RoomsManager;
import jaxb.schema.generated.GameDescriptor;


import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

@MultipartConfig
public class XmlFileLoaderServlet extends HttpServlet {

    protected synchronized void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        GameDescriptor gameDescriptor;
        RoomsManager roomsManager;
        StringBuilder xmlContent = new StringBuilder();
        JsonManager jsonManager = servletContextHandler.getJsonHandler(getServletContext());
//

        for (Part part : request.getParts()) {
            xmlContent.append(readFromInputStream(part.getInputStream()));
        }
        System.out.println(xmlContent);
        InputStream inputStream = new ByteArrayInputStream(xmlContent.toString().getBytes());
        try {
        GameSettingsReader gameSettingsReader = new GameSettingsReader();
            System.out.println(request.getInputStream());
            GameManager gameManager = gameSettingsReader.extractGameSettings(inputStream);
            roomsManager = servletContextHandler.getRoomsManager(getServletContext());
//            gameDescriptor = xmlLoader.getGameDescriptorFromXml(xmlContent);
//            xmlLoader.validateXml(gameDescriptor, roomsManager);
//
            Room room = new Room(gameManager, "My Room1", "Menash");
            if(roomsManager.isRoomWithNameExists(room.getRoomName())){
                MessageJson messageJson = new MessageJson(false, "There's a room with that name already");
                jsonManager.sendJsonOut(response, messageJson);
            }
            else {
                jsonManager.sendJsonOut(response, room);
            }
            roomsManager.addRoom(room);

        }
         catch (PlayersInitPositionsOutOfRangeException e) {
            MessageJson messageJson = new MessageJson(false, e.toString());
            jsonManager.sendJsonOut(response, messageJson);
        } catch (RowsNotInRangeException e) {
            MessageJson messageJson = new MessageJson(false, e.toString());
            jsonManager.sendJsonOut(response, messageJson);
        } catch (ColumnsNotInRangeException e) {
            MessageJson messageJson = new MessageJson(false, e.toString());
            jsonManager.sendJsonOut(response, messageJson);
        } catch (ThereAreAtLeastTwoPlayersWithSameID e) {
            MessageJson messageJson = new MessageJson(false, e.toString());
            jsonManager.sendJsonOut(response, messageJson);
        } catch (OutOfRangeNumberOfPlayersException e) {
            MessageJson messageJson = new MessageJson(false, e.toString());
            jsonManager.sendJsonOut(response, messageJson);
        } catch (TooManyInitialPositionsException e) {
            MessageJson messageJson = new MessageJson(false, e.toString());
            jsonManager.sendJsonOut(response, messageJson);
        } catch (BoardSizeDoesntMatchNumOfPlayersException e) {
            MessageJson messageJson = new MessageJson(false, e.toString());
            jsonManager.sendJsonOut(response, messageJson);
        } catch (IslandsOnRegularModeException e) {
            MessageJson messageJson = new MessageJson(false, e.toString());
            jsonManager.sendJsonOut(response, messageJson);
        } catch (PlayerHasNoInitialPositionsException e) {
            MessageJson messageJson = new MessageJson(false, e.toString());
            jsonManager.sendJsonOut(response, messageJson);
        } catch (PlayersInitPositionsOverrideEachOtherException e) {
            MessageJson messageJson = new MessageJson(false, e.toString());
            jsonManager.sendJsonOut(response, messageJson);
        } catch (InvalidNumberOfPlayersException e) {
            MessageJson messageJson = new MessageJson(false, e.toString());
            jsonManager.sendJsonOut(response, messageJson);        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        processRequest(req, resp);
    }

    @Override
    public String getServletInfo() {
        return "Servlet that loads the xml file and checks if it is a valid file";
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }
}
