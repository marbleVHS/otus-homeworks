package ru.otus.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;
import ru.otus.servlet.model.ClientTemplateModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"squid:S1948"})
public class ClientsServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.ftl";
    private static final String TEMPLATE_ATTR_CLIENTS_LIST = "clientsList";

    private static final String PARAM_NAME = "name";
    private static final String PARAM_ADDRESS = "address";
    private static final String PARAM_PHONE = "phone";


    private final TemplateProcessor templateProcessor;
    private final DBServiceClient dbServiceClient;

    public ClientsServlet(
            TemplateProcessor templateProcessor,
            DBServiceClient dbServiceClient
    ) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        List<ClientTemplateModel> clientsTemplateModelsList = dbServiceClient
                .findAll()
                .stream()
                .map(ClientTemplateModel::new)
                .toList();
        paramsMap.put(TEMPLATE_ATTR_CLIENTS_LIST, clientsTemplateModelsList);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter(PARAM_NAME);
        String address = request.getParameter(PARAM_ADDRESS);
        String phone = request.getParameter(PARAM_PHONE);

        dbServiceClient.saveClient(
                new Client(
                        null,
                        name,
                        new Address(null, address),
                        List.of(new Phone(null, phone))
                )
        );

        response.sendRedirect("/clients");

    }
}
