package za.co.virtualpostman.samplevpws;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import javax.xml.datatype.XMLGregorianCalendar;
import za.co.virtualpostman.samplevpws.wsdl.archivefile.AuthenticationFault;
import za.co.virtualpostman.samplevpws.wsdl.archivefile.AuthorizationFault;
import za.co.virtualpostman.samplevpws.wsdl.archivefile.InputOutputFault;
import za.co.virtualpostman.samplevpws.wsdl.archivefile.InvalidArgumentFault;
import za.co.virtualpostman.samplevpws.wsdl.document.Document;
import za.co.virtualpostman.samplevpws.wsdl.document.IndexValue;
import za.co.virtualpostman.samplews.wsclient.VirtualPostmanClient;

public class SearchSpcificDocument {

    public static void main(String[] args) {
        VirtualPostmanClient vpc = new VirtualPostmanClient("https://poc.virtualpostman.co.za/external", "admin", "admin");

        /**
         * Set the waybill number/Or barcode you want to query for below.
         * Specify the node index name, and the index value (The actual waybill
         * number you need) And store these in a list of IndexValues
         */
        IndexValue indexValue = new IndexValue();
        indexValue.setIndexName("THE_NODE_INDEX_NAME");
        indexValue.setIndexValue("THE_BARCODE_VALUE");

        List<IndexValue> indexValues = new LinkedList<>();
        indexValues.add(indexValue);

        /**
         * Set the date range. Query document for date between start date and
         * end date.
         */
        Date sDate = new Date(LocalDate.now().minusDays(100).atTime(0, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        Date eDate = new Date(LocalDate.now().atTime(13, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        TimeZone zone = TimeZone.getDefault();

        XMLGregorianCalendar startDate = ConvertDateToXMLGregorianCalendar.dateToXMLGregorianCalendar(sDate, zone);
        XMLGregorianCalendar endDate = ConvertDateToXMLGregorianCalendar.dateToXMLGregorianCalendar(eDate, zone);

        /**
         * Query using the below lines. Query returns a document that you can
         * use to get the document archive date,
         */
        List<Document> docs;
        try {
            docs = vpc.getDocumentsWebService().findDocuments(vpc.getSessionId(), "THE_NODE_YOU_WANT_TO_QUERY_FROM", indexValues, startDate, endDate, 0, 0, "THE_NODE_INDEX_NAME", true);
        } catch (za.co.virtualpostman.samplevpws.wsdl.authenticate.AuthorizationFault | za.co.virtualpostman.samplevpws.wsdl.authenticate.AuthenticationFault | za.co.virtualpostman.samplevpws.wsdl.document.AuthorizationFault | za.co.virtualpostman.samplevpws.wsdl.document.AuthenticationFault | za.co.virtualpostman.samplevpws.wsdl.document.InvalidArgumentFault ex) {
            throw new RuntimeException("Unable to find document", ex);
        }

        if (docs.isEmpty()) {
            throw new RuntimeException("No document found");
        }

        for (Document doc : docs) {
            byte[] pdf;
            try {
                pdf = vpc.getDocumentWebService().getDocumentContentAsPdf(vpc.getSessionId(), doc.getId());
            } catch (AuthorizationFault | InputOutputFault | AuthenticationFault | InvalidArgumentFault | za.co.virtualpostman.samplevpws.wsdl.authenticate.AuthorizationFault | za.co.virtualpostman.samplevpws.wsdl.authenticate.AuthenticationFault ex) {
                throw new RuntimeException("Error getting pdf content", ex);
            }

            ByteToPdf.convertPdfToByte(pdf, "/tmp/out.pdf");
        }
    }

}
