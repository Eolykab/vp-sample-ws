package za.co.virtualpostman.samplews.wsclient;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.ws.BindingProvider;
import za.co.virtualpostman.samplevpws.wsdl.archivefile.VirtualPostman;
import za.co.virtualpostman.samplevpws.wsdl.archivefile.VirtualPostmanService;
import za.co.virtualpostman.samplevpws.wsdl.authenticate.Auth;
import za.co.virtualpostman.samplevpws.wsdl.authenticate.AuthService;
import za.co.virtualpostman.samplevpws.wsdl.authenticate.AuthenticationFault;
import za.co.virtualpostman.samplevpws.wsdl.authenticate.AuthorizationFault;
import za.co.virtualpostman.samplevpws.wsdl.document.Documents;
import za.co.virtualpostman.samplevpws.wsdl.document.DocumentsService;
import za.co.virtualpostman.samplevpws.wsdl.documenttasks.DocumentTasks;
import za.co.virtualpostman.samplevpws.wsdl.documenttasks.DocumentTasksService;
import za.co.virtualpostman.samplevpws.wsdl.externalpoll.ExternalPoll;
import za.co.virtualpostman.samplevpws.wsdl.externalpoll.ExternalPollService;
import za.co.virtualpostman.samplevpws.wsdl.node.Nodes;
import za.co.virtualpostman.samplevpws.wsdl.node.NodesService;

public class VirtualPostmanClient {

    private final String serverURL;
    private final String username;
    private final String password;
    private String sessionId = null;
    private Auth authWebService = null;
    private VirtualPostman vpWebService = null;
    private Documents docWebService = null;
    private ExternalPoll externalPollWebService = null;
    private Nodes nodeService = null;
    private DocumentTasks documentTasksService = null;

    public VirtualPostmanClient(String serverURL, String username, String password) {
        this.serverURL = serverURL;
        this.username = username;
        this.password = password;
    }

    private static String normaliseServerUrl(String url) {
        if (!url.endsWith("/")) {
            url += "/";
        }
        return url;
    }

    public Auth getAuthWebService() {
        if (authWebService == null) {
            authWebService = new AuthService(VirtualPostmanClient.class.getResource("/webservices/wsdl/Auth-v3.0.wsdl")).getAuthPort();
            ((BindingProvider) authWebService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, normaliseServerUrl(serverURL) + "webservices/SOAP/Auth-v3.0?wsdl");
        }

        return authWebService;
    }

    public VirtualPostman getDocumentWebService() {
        if (vpWebService == null) {
            vpWebService = new VirtualPostmanService(VirtualPostmanClient.class.getResource("/webservices/wsdl/VirtualPostman-v3.3.wsdl")).getVirtualPostmanPort();
            ((BindingProvider) vpWebService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, normaliseServerUrl(serverURL) + "webservices/SOAP/VirtualPostman-v3.3?wsdl");
        }
        return vpWebService;
    }

    public Documents getDocumentsWebService() {
        if (docWebService == null) {
            docWebService = new DocumentsService(VirtualPostmanClient.class.getResource("/webservices/wsdl/Documents-v1.6.wsdl")).getDocumentsPort();
            ((BindingProvider) docWebService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, normaliseServerUrl(serverURL) + "webservices/SOAP/Documents-v1.5?wsdl");
        }
        return docWebService;
    }

    public ExternalPoll getExternalPollWebService() {
        if (externalPollWebService == null) {
            externalPollWebService = new ExternalPollService(VirtualPostmanClient.class.getResource("/webservices/wsdl/ExternalPoll-v1.0.wsdl")).getExternalPollPort();
            ((BindingProvider) externalPollWebService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, normaliseServerUrl(serverURL) + "webservices/SOAP/ExternalPoll-v1.0?wsdl");
        }
        return externalPollWebService;
    }

    public Nodes getNode() {
        if (nodeService == null) {
            nodeService = new NodesService(VirtualPostmanClient.class.getResource("/webservices/SOAP/Nodes-v1.1.wsdl")).getNodesPort();
            ((BindingProvider) nodeService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, normaliseServerUrl(serverURL) + "webservices/SOAP/Nodes-v1.1?wsdl");

        }
        return nodeService;
    }

    public DocumentTasks getDocumentTasks() throws MalformedURLException {
        if (documentTasksService == null) {
            documentTasksService = new DocumentTasksService(new URL(normaliseServerUrl(serverURL) + "webservices/SOAP/DocumentTasks-v1.0?wsdl")).getDocumentTasksPort();
        }
        return documentTasksService;
    }

    public String getSessionId()
            throws AuthorizationFault, AuthenticationFault {
        if (sessionId == null) {
            sessionId = getAuthWebService().login(username, password);
        }

        return sessionId;
    }
}
