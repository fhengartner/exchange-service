package biz.hengartner.euroexchange.ecb;

import biz.hengartner.euroexchange.ecb.model.CubeWithTime;
import biz.hengartner.euroexchange.ecb.model.Envelope;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

/**
 * Parses the xml format as used by:
 * <ul>
 * <li>http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml</li>
 * <li>http://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml</li>
 * </ul>
 */
public class EurofxXmlParser {

    public List<CubeWithTime> parseXml(String xml) throws IOException {
        XmlMapper xmlMapper = createXmlMapper();
        return envelopeToCubeWithTimeList(xmlMapper.readValue(xml, Envelope.class));
    }

    public List<CubeWithTime> parseXml(File file) throws IOException {
        XmlMapper xmlMapper = createXmlMapper();
        return envelopeToCubeWithTimeList(xmlMapper.readValue(file, Envelope.class));
    }

    public List<CubeWithTime> parseXml(URL url) throws IOException {
        XmlMapper xmlMapper = createXmlMapper();
        return envelopeToCubeWithTimeList(xmlMapper.readValue(url, Envelope.class));
    }

    public static void main(String[] args) throws IOException {
        String url = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
        String url2 = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml\n";

        EurofxXmlParser parser = new EurofxXmlParser();
        String out = new Scanner(new URL(url2).openStream(), "UTF-8").useDelimiter("\\A").next();

        System.out.println(parser.parseXml(out).get(0).getCubes().size());
    }

    /**
     * Configure Jackson XmlMapper.
     */
    private XmlMapper createXmlMapper() {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        return new XmlMapper(module);
    }

    private List<CubeWithTime> envelopeToCubeWithTimeList(Envelope envelope) {
        if (envelope != null && envelope.getCubeWrapper() != null) {
            return envelope.getCubeWrapper().getCubeWithTime();
        }

        throw new IllegalStateException("XML parser did not create expected structure.");
    }

}