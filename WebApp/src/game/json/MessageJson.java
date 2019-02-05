package game.json;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MessageJson {

    private boolean isActionSucceeded;
    private String message;

    public MessageJson(boolean isActionSucceeded, String message) {
        this.isActionSucceeded = isActionSucceeded;
        this.message = message;
    }
}
