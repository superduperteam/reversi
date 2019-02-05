package game.handlers;

import game.webLogic.OnlinePlayersManager;
import game.webLogic.RoomsManager;

import javax.servlet.ServletContext;

public class ServletContextHandler {

    private final String SESSION_HANDLER_ATT = "sessionHandler";
    private final String PLAYERS_MANAGER_ATT = "playersManager";
    private final String ROOMS_MANAGER_ATT = "roomsManager";
    private final String JSON_HANDLER_ATT = "jsonHandler";

    public SessionHandler getSessionHandler(ServletContext servletContext) {
        if(servletContext.getAttribute(SESSION_HANDLER_ATT) == null) {
            servletContext.setAttribute(SESSION_HANDLER_ATT, new SessionHandler());
        }

        return (SessionHandler) servletContext.getAttribute(SESSION_HANDLER_ATT);
    }

    public OnlinePlayersManager getPlayersManager(ServletContext servletContext) {
        if(servletContext.getAttribute(PLAYERS_MANAGER_ATT) == null) {
            servletContext.setAttribute(PLAYERS_MANAGER_ATT, new OnlinePlayersManager());
        }

        return (OnlinePlayersManager) servletContext.getAttribute(PLAYERS_MANAGER_ATT);
    }

    public RoomsManager getRoomsManager(ServletContext servletContext) {
        if(servletContext.getAttribute(ROOMS_MANAGER_ATT) == null) {
            servletContext.setAttribute(ROOMS_MANAGER_ATT, new RoomsManager());
        }

        return (RoomsManager) servletContext.getAttribute(ROOMS_MANAGER_ATT);
    }

    public JsonManager getJsonHandler(ServletContext servletContext) {
        if(servletContext.getAttribute(JSON_HANDLER_ATT) == null) {
            servletContext.setAttribute(JSON_HANDLER_ATT, new JsonManager());
        }

        return (JsonManager) servletContext.getAttribute(JSON_HANDLER_ATT);
    }
}
