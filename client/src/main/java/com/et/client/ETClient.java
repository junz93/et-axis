package com.et.client;

import com.exacttarget.wsdl.partnerapi.*;

import java.rmi.RemoteException;

import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContextFactory;


public class ETClient {

    public ETClient() {
    }

    public static org.apache.xmlbeans.XmlObject getAPIObject(Class type) throws Exception {
        java.lang.reflect.Method creatorMethod = null;
        if (org.apache.xmlbeans.XmlObject.class.isAssignableFrom(type)) {
            Class[] declaredClasses = type.getDeclaredClasses();
            for (int i = 0; i < declaredClasses.length; i++) {
                Class declaredClass = declaredClasses[i];
                if (declaredClass.getName().endsWith("$Factory")) {
                    creatorMethod = declaredClass.getMethod("newInstance", null);
                    break;
                }

            }
        }
        if (creatorMethod != null) {
            return (org.apache.xmlbeans.XmlObject) creatorMethod.invoke(null, null);
        } else {
            throw new Exception("Creator not found!");
        }

    }

    /**
     * sample to retrieve email from account
     *
     * @param args
     */
//    public static void main1(String args[]) {
//        org.apache.axis2.context.ConfigurationContext configurationContext = null;
//        try {
//            EndpointReference endPointReference = new EndpointReference("https://webservice.exacttarget.com/Service.asmx");
//            configurationContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(PropertiesUtil.getInstance().getProperty("repositoryFolder"), PropertiesUtil.getInstance().getProperty("axis2File"));
//            PartnerAPIStub stub = new PartnerAPIStub(configurationContext, "https://webservice.exacttarget.com/Service.asmx");
//            ServiceClient serviceClient = stub._getServiceClient();
//            Options options = new Options();
//            options.setTo(endPointReference);
//            serviceClient.setOptions(options);
//            RetrieveRequestMsgDocument retrieveRequestMsgDocument = com.exacttarget.wsdl.partnerapi.RetrieveRequestMsgDocument.Factory.newInstance();
//            RetrieveRequest rreq = com.exacttarget.wsdl.partnerapi.RetrieveRequest.Factory.newInstance();
//            rreq.setObjectType("Email");
//            String rreqProperties[] = {
//                    "ID", "Name", "Subject", "HTMLBody"
//            };
//            rreq.setPropertiesArray(rreqProperties);
//            SimpleFilterPart sfp = com.exacttarget.wsdl.partnerapi.SimpleFilterPart.Factory.newInstance();
//            sfp.setProperty("Name");
//            sfp.setSimpleOperator(SimpleOperators.EQUALS);
//            String sfpValues[] = {
//                    "Test"
//            };
//            sfp.setValueArray(sfpValues);
//            rreq.setFilter(sfp);
//            com.exacttarget.wsdl.partnerapi.RetrieveRequestMsgDocument.RetrieveRequestMsg retrieveRequestMsg = retrieveRequestMsgDocument.addNewRetrieveRequestMsg();
//            retrieveRequestMsg.setRetrieveRequest(rreq);
//            RetrieveResponseMsgDocument retrieveResponseMsgDocument = stub.retrieve(retrieveRequestMsgDocument);
//            com.exacttarget.wsdl.partnerapi.RetrieveResponseMsgDocument.RetrieveResponseMsg retrieveResponseMsg = retrieveResponseMsgDocument.getRetrieveResponseMsg();
//            System.out.println((new StringBuilder()).append("Response ::: ").append(retrieveResponseMsg.getOverallStatus()).toString());
//            APIObject apiObject = retrieveResponseMsg.getResultsArray(0);
//            Email email = (Email) apiObject;
//            System.out.println("Subject ::: " + email.getHTMLBody());
//            System.out.println((new StringBuilder()).append("Email  Id  ::: ").append(apiObject.getObjectID()).toString());
//
//        }
//        catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    //Create Call
    public static void main(String args[]) {
        org.apache.axis2.context.ConfigurationContext configurationContext = null;
        try {

            EndpointReference endPointReference = new EndpointReference("https://webservice.exacttarget.com/Service.asmx");
            System.out.println(ETClient.class.getResource("/axis2client.xml").getPath());
            configurationContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem("D:/repo/", ETClient.class.getResource("/axis2client.xml").getPath().substring(1));
            PartnerAPIStub stub = new PartnerAPIStub(configurationContext, "https://webservice.exacttarget.com/Service.asmx");
            ServiceClient serviceClient = stub._getServiceClient();
            Options options = new Options();
            options.setTo(endPointReference);
            serviceClient.setOptions(options);

            TriggeredSendDefinition Tdef = TriggeredSendDefinition.Factory.newInstance();
            Tdef.setCustomerKey("SUGAR_TEST_TRIGGER");

            TriggeredSend triggeredSend = TriggeredSend.Factory.newInstance();


            Subscriber subscriber1 = Subscriber.Factory.newInstance();

            subscriber1.setEmailAddress("youremail@example.com");
            subscriber1.setSubscriberKey("password");

            Attribute attribute1 = Attribute.Factory.newInstance();

            //Send-time attribute name
            attribute1.setName("HTML__BODY");

            // Value of the attribute
            attribute1.setValue("<html><b>THIS IS A TEST</b></html>");

            Attribute attribute1a = Attribute.Factory.newInstance();

            //Send-time attribute name
            attribute1a.setName("Subject");

            // Value of the attribute
            attribute1a.setValue("Japanese: 犬はいつの日か地球を支配する");

            subscriber1.setAttributesArray(new Attribute[]{attribute1, attribute1a});

            triggeredSend.setSubscribersArray(new Subscriber[]{subscriber1});
            triggeredSend.setTriggeredSendDefinition(Tdef);


            CreateOptions createOptions = CreateOptions.Factory.newInstance();

            CreateRequestDocument createRequestDocument = CreateRequestDocument.Factory.newInstance();

            CreateRequestDocument.CreateRequest createRequest = CreateRequestDocument.CreateRequest.Factory.newInstance();

            createRequest.setObjectsArray(new APIObject[]{triggeredSend});
            createRequest.setOptions(createOptions);

            createRequestDocument.setCreateRequest(createRequest);
            CreateResponseDocument responseDoc = stub.create(createRequestDocument);

            System.out.println("TriggeredSend :::  " + responseDoc.getCreateResponse());


        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
