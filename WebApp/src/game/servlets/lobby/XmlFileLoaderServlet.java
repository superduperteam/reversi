//package game.servlets.lobby;
//
//import com.sun.media.sound.InvalidDataException;
//import game.handlers.ServletContextHandler;
//import game.json.MessageJson;
//import game.handlers.JsonManager;
//import game.webLogic.Room;
//
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.MultipartConfig;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.Part;
//import javax.xml.bind.JAXBException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Scanner;
//
//@MultipartConfig
//public class XmlFileLoaderServlet extends HttpServlet {
//
//    protected synchronized void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        response.setContentType("application/json");
//
//        ServletContextHandler servletContextHandler = new ServletContextHandler();
////        XmlLoader xmlLoader;
////        GameDescriptor gameDescriptor;
////        RoomsManager roomsManager;
//        StringBuilder xmlContent = new StringBuilder();
//        JsonManager jsonManager = servletContextHandler.getJsonHandler(getServletContext());
//
//        for (Part part : request.getParts()) {
//            xmlContent.append(readFromInputStream(part.getInputStream()));
//        }
//
//        try {
//           // xmlLoader = new XmlLoader(); ## call our own check
////            roomsManager = servletContextHandler.getRoomsManager(getServletContext());
////            gameDescriptor = xmlLoader.getGameDescriptorFromXml(xmlContent);
////            xmlLoader.validateXml(gameDescriptor, roomsManager);
//
//           // Room room = new Room(gameDescriptor);
//        //    jsonManager.sendJsonOut(response, room);
//
//        } catch (JAXBException | InvalidDataException e) {
//            MessageJson messageJson = new MessageJson(false, e.getMessage());
//            jsonManager.sendJsonOut(response, messageJson);
//        }
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
//        processRequest(req, resp);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
//        processRequest(req, resp);
//    }
//
//    @Override
//    public String getServletInfo() {
//        return "Servlet that loads the xml file and checks if it is a valid file";
//    }
//
//    private String readFromInputStream(InputStream inputStream) {
//        return new Scanner(inputStream).useDelimiter("\\Z").next();
//    }
//}
