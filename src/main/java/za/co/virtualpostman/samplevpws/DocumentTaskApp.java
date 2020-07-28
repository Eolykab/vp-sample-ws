package za.co.virtualpostman.samplevpws;

import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import za.co.virtualpostman.samplevpws.wsdl.authenticate.AuthenticationFault;
import za.co.virtualpostman.samplevpws.wsdl.authenticate.AuthorizationFault;
import za.co.virtualpostman.samplevpws.wsdl.document.Document;
import za.co.virtualpostman.samplevpws.wsdl.documenttasks.DocumentQueueEntryInformation;
import za.co.virtualpostman.samplevpws.wsdl.documenttasks.InvalidArgumentFault;
import za.co.virtualpostman.samplews.wsclient.VirtualPostmanClient;

/**
 * Documentation about this Sample App can found on
 * https://poc.virtualpostman.co.za/external/help/webservices/soap/api/index.html?za/co/virtualpostman/vp/svr/webservices/soap/api/documenttasks/v1/DocumentTasks_1_0.html
 * WSDL File can found on :
 * https://poc.virtualpostman.co.za/external/webservices/SOAP/DocumentTasks-v1.0?wsdl
 *
 * @author eolykab
 */
public class DocumentTaskApp {

    public static void main(String[] args) {
        try {
            System.out.println("This is a test for Document Task");
            VirtualPostmanClient vpc = new VirtualPostmanClient("https://poc.virtualpostman.co.za/external", "admin", "admin");
            String session = vpc.getSessionId();
            System.out.println("Loggin Successfully : " + session);
            try {
                DocumentQueueEntryInformation nextDocumentInTaskQueue = vpc.getDocumentTasks().getNextDocumentInTaskQueue(session, "INDEXING");
                if (nextDocumentInTaskQueue == null) {
                    System.out.println("No documents on the queue");
                } else {
                    System.out.println("Document Queue Entry ID: " + nextDocumentInTaskQueue.getDocumentQueueEntryId());
                    System.out.println("Document Queue ID: " + nextDocumentInTaskQueue.getQueueId());
                    System.out.println("Document ID: " + nextDocumentInTaskQueue.getDocumenId());
                    try {
                        List<Document> derivedDocuments = vpc.getDocumentsWebService().
                                getDerivedDocuments(session, nextDocumentInTaskQueue.getDocumenId());
                        derivedDocuments.stream().forEach((Document d) -> {
                            System.out.println("Derived Document ID : " + d.getId());

                            try {
                                /**
                                 * Do AI with the derived Documents. Whatever
                                 * actions that needs to be done on the document
                                 * Indexing or extracting ...
                                 */
                                za.co.virtualpostman.samplevpws.wsdl.archivefile.Document document = vpc.getDocumentWebService().getDocument(session, d.getId());
                                System.out.println("Document Content Type : " + document.getContentType());
                                System.out.println("Document Orginal Name : " + document.getOriginalFileName());
                                System.out.println("Document Archive Date : " + document.getArchiveDate());
                                System.out.println("Document Node Id : " + document.getNodeId());

                                document.getIndexValues().stream().forEach((iv) -> {
                                    System.out.println(iv.getIndexName() + " " + iv.getIndexValue());
                                });
                                
                                /**
                                 * You can get the content of the document like this.
                                 * 
                                 * byte[] documentContent = vpc.getDocumentWebService().getDocumentContent(session, document.getId());
                                 * 
                                 */


                            } catch (za.co.virtualpostman.samplevpws.wsdl.archivefile.AuthorizationFault | za.co.virtualpostman.samplevpws.wsdl.archivefile.AuthenticationFault | za.co.virtualpostman.samplevpws.wsdl.archivefile.InvalidArgumentFault ex) {
                                Logger.getLogger(DocumentTaskApp.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    } catch (za.co.virtualpostman.samplevpws.wsdl.document.AuthorizationFault | za.co.virtualpostman.samplevpws.wsdl.document.AuthenticationFault | za.co.virtualpostman.samplevpws.wsdl.document.InvalidArgumentFault ex) {
                        Logger.getLogger(DocumentTaskApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("Processing the document");
                    /**
                     * Setting Task Queue Success
                     */
                    vpc.getDocumentTasks().setDocumentProcessedInTaskQueueSuccess(session, nextDocumentInTaskQueue.getDocumentQueueEntryId());
                    /**
                     * Setting Task Queue Fail
                     */
                    //vpc.getDocumentTasks().setDocumentProcessedInTaskQueueFail(session,
                    //      nextDocumentInTaskQueue.getDocumentQueueEntryId(), "Unable to process document");
                    System.out.println("Processed successfully");

                }
                System.out.println("Done.");

            } catch (za.co.virtualpostman.samplevpws.wsdl.documenttasks.AuthenticationFault | InvalidArgumentFault | za.co.virtualpostman.samplevpws.wsdl.documenttasks.AuthorizationFault | MalformedURLException ex) {
                Logger.getLogger(DocumentTaskApp.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (AuthorizationFault | AuthenticationFault ex) {
            Logger.getLogger(DocumentTaskApp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
