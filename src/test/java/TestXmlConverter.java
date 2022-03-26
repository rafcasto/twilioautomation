import dto.XMLContentResponse;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import repositories.TwilioRepository;
import repositories.TwilioRepositoryImpl;
import utils.CallUtils;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.jcabi.matchers.RegexMatchers.matchesPattern;
import static org.junit.Assert.assertThat;

public class TestXmlConverter
{
    private String expectedDollar = "(\\$[0-9]+(\\.[0-9]+)?) (\\$[0-9]+(\\.[0-9]+)?) To pay the amount due, say balance or press 1. To pay your past due amount, say past due or press 2. To go to the main menu, say menu or press 9.";
    private String expectedDigits = "To make a payment using the Credit card profile on your account ending in (\\d{4}) say profile or press 1. To make a payment using the e-cheque profile on your account ending in. (\\d{4}) say, check profile or press 2. To make a payment by entering your credit card details, say card or press 3 . To make a payment by entering your bank account details, say cheque or press 4 .\n";
    private String xmlDollar = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<Response>\n" +
            "    <Gather input=\"speech dtmf\" timeout=\"5\" numDigits=\"1\" actionOnEmptyResult=\"true\" speechTimeout=\"5\" speechModel=\"phone_call\" enhanced=\"true\" hints=\"menu balance pastdue amount\" action=\"/balanceworkflow/gatherAmtForPayment?voice=&amp;Called=%2B17622093993&amp;language=en-US&amp;ToState=GA&amp;CallerCountry=US&amp;Direction=inbound&amp;amountdue=316&amp;CallerState=GA&amp;ToZip=30071&amp;isFromPayment=false&amp;mbrsep=110759002&amp;CallSid=CA804ab0c65a0aa05a560ddc44711e628e&amp;To=%2B17622093993&amp;CallerZip=30071&amp;ToCountry=US&amp;workflowid=1&amp;isfromMemberPage=true&amp;ApiVersion=2010-04-01&amp;CalledZip=30071&amp;currentbal=157.99&amp;pastdue=158.01&amp;CallStatus=in-progress&amp;CalledCity=NORCROSS&amp;From=%2B17622306531&amp;AccountSid=ACfc710591c3f748ad48a8d1e3c0dcbee2&amp;CalledCountry=US&amp;CallerCity=NORCROSS&amp;Caller=%2B17622306531&amp;FromCountry=US&amp;ToCity=NORCROSS&amp;FromCity=NORCROSS&amp;acctstatus=A&amp;CalledState=GA&amp;budgetbal=&amp;mbrnbr=110759&amp;ppmbal=0&amp;FromZip=30071&amp;FromState=GA\">\n" +
            "        <Play>https://client9850.s3.us-east-2.amazonaws.com/Balance.wav</Play>\n" +
            "        <Say voice=\"Polly.Joanna\">\n" +
            "            <say-as interpret-as=\"currency\">$316</say-as>\n" +
            "        </Say>\n" +
            "        <Pause length=\"1\"/>\n" +
            "        <Play>https://client9850.s3.us-east-2.amazonaws.com/Balance_1a.wav</Play>\n" +
            "        <Say voice=\"Polly.Joanna\">\n" +
            "            <say-as interpret-as=\"currency\">$158.01</say-as>\n" +
            "        </Say>\n" +
            "        <Pause length=\"1\"/>\n" +
            "        <Say voice=\"Polly.Joanna\">To pay the amount due, say balance or press 1. To pay your past due amount, say past due or press 2. To go to the main menu, say menu or press 9.</Say>\n" +
            "    </Gather>\n" +
            "</Response>";

    private String xmlDigit = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<Response>\n" +
            "    <Gather input=\"speech dtmf\" timeout=\"5\" numDigits=\"1\" actionOnEmptyResult=\"true\" speechTimeout=\"5\" speechModel=\"phone_call\" enhanced=\"true\" hints=\"profile card check e-check echeck\" action=\"/paymentworkflow/gatherPaymentType?paymentAmt=158.01&amp;voice=&amp;Called=%2B17622093993&amp;language=en-US&amp;ToState=GA&amp;CallerCountry=US&amp;last4EC=6789&amp;Direction=inbound&amp;Name=BROWN%20MORTON%20L%20JR&amp;last4CC=1885&amp;accountStatus=A&amp;balance=157.99&amp;CallerState=GA&amp;ToZip=30071&amp;mbrsep=110759002&amp;CallSid=CA804ab0c65a0aa05a560ddc44711e628e&amp;To=%2B17622093993&amp;CallerZip=30071&amp;ToCountry=US&amp;ApiVersion=2010-04-01&amp;CalledZip=30071&amp;ecProfileExists=true&amp;CallStatus=in-progress&amp;CalledCity=NORCROSS&amp;From=%2B17622306531&amp;mbrAccounts=&amp;AccountSid=ACfc710591c3f748ad48a8d1e3c0dcbee2&amp;ccProfileExists=true&amp;checkCode=0&amp;CalledCountry=US&amp;CallerCity=NORCROSS&amp;ecProfile=6789&amp;Caller=%2B17622306531&amp;FromCountry=US&amp;ToCity=NORCROSS&amp;creditCardCode=0&amp;FromCity=NORCROSS&amp;CalledState=GA&amp;mbrnbr=110759&amp;memberPayment=&amp;ccProfile=1885&amp;useConvFlag=3&amp;PaymentToken=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzZWRjLnBnLmFwaS5rZXlpZCI6Ijk4OC4xLjEiLCJzZWRjLnBnLmFwaS5jdXN0b21lcmlkIjoiMDAxMTA3NTkwMDIiLCJleHAiOjE2NDcyOTY4NzQuNzU1MDU2NCwic2VkYy5wZy5hcGkudHJhY2tpbmdpZCI6bnVsbCwic2VkYy5wZy5hcGkucG9zcmVmaWQiOm51bGx9.KLQj2c3IgFv36QYSQZxnb_otCss41QzABUS3M8WPAj8&amp;FromZip=30071&amp;allowedPaymentType=All&amp;FromState=GA\">\n" +
            "        <Say voice=\"Polly.Joanna\">To make a payment using the Credit card profile on your account ending in</Say>\n" +
            "        <Say voice=\"Polly.Joanna\">\n" +
            "            <say-as interpret-as=\"number:digit\">1885</say-as>\n" +
            "        </Say>\n" +
            "        <Pause length=\"1\"/>\n" +
            "        <Say voice=\"Polly.Joanna\">say profile or press 1. To make a payment using the e-cheque profile on your account ending in.</Say>\n" +
            "        <Say voice=\"Polly.Joanna\">\n" +
            "            <say-as interpret-as=\"number:digit\">6789</say-as>\n" +
            "        </Say>\n" +
            "        <Pause length=\"1\"/>\n" +
            "        <Say voice=\"Polly.Joanna\">say, check profile or press 2. To make a payment by entering your credit card details, say card or press 3 .</Say>\n" +
            "        <Say voice=\"Polly.Joanna\"> To make a payment by entering your bank account details, say cheque or press 4 .</Say>\n" +
            "    </Gather>\n" +
            "</Response>";
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    @Test
    public void convertXMLTest()
    {

        XMLContentResponse response = convertXML(xmlDigit);
        String repString = response.getSay().replace("  "," ").trim();
        System.out.println(repString);
            MatcherAssert.assertThat(repString.trim(),matchesPattern(expectedDigits.trim()));
        System.out.println(repString);
    }

    @Test
    public void VerifyMatcher()
    {
        MatcherAssert.assertThat("test 1234 test",matchesPattern("test (\\d{4}) test"));
    }

    private XMLContentResponse convertXML(String xmlResponse){
        XMLContentResponse convertString = new XMLContentResponse();
        try
        {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlResponse));
            Document doc = db.parse(is);
            doc.getDocumentElement().normalize();
            NodeList nodeList=doc.getElementsByTagName("*");
            convertString.setSay(listSay(nodeList));

        }catch (Exception ex){}

        return convertString;
    }

    private List<String> listSay(NodeList nodeList)
    {
        List<String> listSay = new ArrayList();
        String oldInnerTag = "";
        for (int i=0; i<nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            if(element.hasChildNodes() && oldInnerTag.equals(element.getTagName())){
                NodeList innerNodeList =  element.getElementsByTagName("*");
                listSay.addAll(listInnerSay(innerNodeList));

            }
            String elementText = getElementText((Element) nodeList.item(i));
            if(elementText != null)
            {
                listSay.add(elementText);
            }
            oldInnerTag = element.getTagName();

        }
        return listSay;
    }

    private List<String> listInnerSay(NodeList nodeList)
    {
        List<String> listSay = new ArrayList();
        for (int i=0; i<nodeList.getLength(); i++) {
            String elementText = getElementText((Element) nodeList.item(i));
            if(elementText != null)
            {
                listSay.add(elementText);
            }
        }
        return listSay;
    }

    private String getElementText(Element element)
    {
        if(element.getTagName().equals("Say"))
        {
            return element.getTextContent();
        }
        return null;
    }
}
