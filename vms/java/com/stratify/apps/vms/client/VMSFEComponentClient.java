/*
 * VMSFEComponentClient.java
 *
 * Created on January 8, 2008, 7:03 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.client;

import com.stratify.apps.vms.client.VMSFEComponentServiceVMSFEComponentServiceSOAP11PortStub;
import com.stratify.apps.vms.common.VMSStaticParams;
import com.stratify.apps.vms.common.exceptions.VMSSysException;
import com.stratify.apps.vms.common.vos.VMSComponent;
import java.util.ArrayList;

import com.stratify.common.logging.Logger;


/**
 *
 * @author ravikishores
 */
public class VMSFEComponentClient {
    String casename = null;
    String servername = null;
    
    static Logger logger = Logger.getLogger(VMSFEComponentClient.class.getName());

    public VMSFEComponentClient(String casename, String servername) {
        this.casename = casename;
        this.servername = servername;
        logger.debug("VMSFEComponentClient: CASE: " + casename + " SERVER: " + servername);
    }

    public ArrayList getComponentObjects() throws VMSSysException {
        ArrayList retList = new ArrayList();
       try{
            VMSFEComponentServiceVMSFEComponentServiceSOAP11PortStub stub = new VMSFEComponentServiceVMSFEComponentServiceSOAP11PortStub
                    (VMSStaticParams.FE_WEB_SERVICE_URL_APAC);
            VMSFEComponentServiceVMSFEComponentServiceSOAP11PortStub.FetchComponents fc = new VMSFEComponentServiceVMSFEComponentServiceSOAP11PortStub.FetchComponents();
            fc.setParam0(casename);
            fc.setParam1(servername);
            
            logger.debug("getComponentObjects: WEBSERVICE URL : " + VMSStaticParams.FE_WEB_SERVICE_URL_APAC);
            
            VMSFEComponentServiceVMSFEComponentServiceSOAP11PortStub.FetchComponentsResponse res = stub.fetchComponents(fc);
            VMSFEComponentServiceVMSFEComponentServiceSOAP11PortStub.VMSComponent[] becs = res.get_return();
            
            logger.debug("getComponentObjects: NO OF COMPONENTS OBTD : " + becs.length);
            
            for(int i=0; i< becs.length;i++ ){
//              System.out.println(becs[i].getVersion());
//              System.out.println(becs[i].getIsActive());
//              System.out.println(becs[i].getServerType());
//              System.out.println(becs[i].getServerURL());

              VMSComponent component = new VMSComponent();

              component.setIsActive(becs[i].getIsActive());
              component.setServerType(becs[i].getServerType());
              component.setServerURL(becs[i].getServerURL());
              component.setVersion(becs[i].getVersion());
              
              logger.debug("ISACTIVE: " + becs[i].getIsActive());
              logger.debug("SERVERTYPE: " + becs[i].getServerType());
              logger.debug("SEVRERURL: " + becs[i].getServerURL());
              logger.debug("VERSION: " + becs[i].getVersion());
              
              retList.add(component);
              
              logger.debug("getComponentObjects: COMPONENT : " + becs[i].getServerType() + "ADDED.");
              
            }


        } catch(Exception e){
            e.printStackTrace();
           logger.error("getComponents: ", e);
           throw new VMSSysException(VMSStaticParams.SEVERITY_2,VMSStaticParams.CRITICAL, "Problem getting FE components for case", casename);
        }


        return retList;
    }


}
