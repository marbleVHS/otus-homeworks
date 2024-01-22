package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.servlet.model.ErrorMessage;

import java.io.IOException;
import java.util.Optional;

@SuppressWarnings({"squid:S1948"})
public class ClientsApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final Gson gson;
    private final DBServiceClient dbServiceClient;

    public ClientsApiServlet(
            Gson gson,
            DBServiceClient dbServiceClient
    ) {
        this.gson = gson;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<Client> client = dbServiceClient.getClient(extractIdFromRequest(request));

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        if (client.isPresent()) {
            out.print(gson.toJson(client.get()));
        } else {
            out.print(gson.toJson(new ErrorMessage("Client not found")));
        }
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1) ? path[ID_PATH_PARAM_POSITION] : String.valueOf(-1);
        return Long.parseLong(id);
    }
}
