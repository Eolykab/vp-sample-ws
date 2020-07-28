package za.co.virtualpostman.samplevpws;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class ConvertDateToXMLGregorianCalendar {

    public ConvertDateToXMLGregorianCalendar() {
    }

    public static XMLGregorianCalendar dateToXMLGregorianCalendar(Date date, TimeZone zone) {

        XMLGregorianCalendar xmlGregorianCalendar = null;
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        gregorianCalendar.setTimeZone(zone);
        try {
            DatatypeFactory dataTypeFactory = DatatypeFactory.newInstance();
            xmlGregorianCalendar = dataTypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            System.out.println("Exception in conversion of Date to XMLGregorianCalendar" + e);
        }
        return xmlGregorianCalendar;
    }
}
