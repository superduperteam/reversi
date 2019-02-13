package game.handlers;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonManager {

    public void sendJsonOut(HttpServletResponse response, Object data) throws IOException {
        try(PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            String json = gson.toJson(data);
            System.out.println("output - " + json);
            out.print(json); // ## was out.println
            out.flush();
        }
    }
}
