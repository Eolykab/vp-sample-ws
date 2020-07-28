package za.co.virtualpostman.samplevpws;

import java.util.List;
import za.co.virtualpostman.samplevpws.wsdl.archivefile.InputOutputFault;
import za.co.virtualpostman.samplevpws.wsdl.authenticate.AuthenticationFault;
import za.co.virtualpostman.samplevpws.wsdl.authenticate.AuthorizationFault;
import za.co.virtualpostman.samplevpws.wsdl.document.IndexValue;
import za.co.virtualpostman.samplevpws.wsdl.externalpoll.InvalidArgumentFault;
import za.co.virtualpostman.samplews.wsclient.VirtualPostmanClient;

public class ExternalPollDocument {

    public static void main(String[] args) {

        /**
         * Getting authentication to VP services. Type in your username and
         * password to authenticate to eDocs. Please note that the user needs to
         * have access to the node you want to access.
         */
        VirtualPostmanClient vpc = new VirtualPostmanClient("https://poc.virtualpostman.co.za/external", "admin", "admin");

        /**
         * Getting document queued for external poll on VP.
         */
        long externalPollDocId;
        try {
            externalPollDocId = vpc.getExternalPollWebService().getExternalPollDocumentNew(vpc.getSessionId(), "THE_NODE_YOU_WANT TO_GET_THE_DOCUMENT_FROM");
        } catch (AuthorizationFault | AuthenticationFault | InvalidArgumentFault | za.co.virtualpostman.samplevpws.wsdl.externalpoll.AuthenticationFault | za.co.virtualpostman.samplevpws.wsdl.externalpoll.AuthorizationFault ex) {
            throw new RuntimeException("Unable to get external poll document ", ex);
        }

        if (externalPollDocId <= 0) {
            throw new RuntimeException("No external poll document available");
        }

        try {
            /**
             * Get Index values services.
             *
             */
            List<IndexValue> documentsIndexes = vpc.getDocumentsWebService().getDocument(vpc.getSessionId(), externalPollDocId).getIndexValues();
            documentsIndexes.stream().map((IndexValue documentsIndexe) -> {
                //Node index name
                String indexName = documentsIndexe.getIndexName();
                System.out.println("Index name: " + indexName);
                //Node index value
                String indexValue = documentsIndexe.getIndexValue();
                return indexValue;
            }).forEachOrdered((String indexValue) -> {
                System.out.println("Index value: " + indexValue);
            });
        } catch (AuthorizationFault | AuthenticationFault | za.co.virtualpostman.samplevpws.wsdl.document.AuthorizationFault | za.co.virtualpostman.samplevpws.wsdl.document.AuthenticationFault | za.co.virtualpostman.samplevpws.wsdl.document.InvalidArgumentFault ex) {
            throw new RuntimeException("Error getting document indexes for externl poll doc with id: " + externalPollDocId, ex);
        }

        byte[] pdfDocContentByte;

        try {

            pdfDocContentByte = vpc.getDocumentWebService().getDocumentContentAsPdf(vpc.getSessionId(), externalPollDocId);
        } catch (AuthorizationFault | AuthenticationFault | za.co.virtualpostman.samplevpws.wsdl.archivefile.AuthorizationFault | InputOutputFault | za.co.virtualpostman.samplevpws.wsdl.archivefile.AuthenticationFault | za.co.virtualpostman.samplevpws.wsdl.archivefile.InvalidArgumentFault ex) {
            throw new RuntimeException("Error geting pdf document for externl poll with id: " + externalPollDocId, ex);
        }

        if (pdfDocContentByte == null) {
            throw new RuntimeException("Unable to get PDF document content");
        }

        ByteToPdf.convertPdfToByte(pdfDocContentByte, "/tmp/x.pdf");
    }
}
