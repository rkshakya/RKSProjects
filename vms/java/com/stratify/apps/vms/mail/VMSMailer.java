/*
 * VMSMailer.java
 *
 * Created on June 2, 2008, 10:54 PM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.mail;

import com.stratify.apps.vms.common.VMSStaticParams;
import com.stratify.apps.vms.common.exceptions.VMSDAOException;
import com.stratify.apps.vms.common.exceptions.VMSMailException;
import com.stratify.apps.vms.common.vos.VMSCaseComponent;
import com.stratify.apps.vms.dao.VMSConnectionFactory;
import com.stratify.apps.vms.dao.VMSDAOFactory;
import com.stratify.apps.vms.dao.VMSMyDAO;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Properties;
import com.stratify.common.logging.Logger;
import com.stratify.apps.common.html.HtmlTable;

/**
 *
 * @author RavikishoreS
 */
public class VMSMailer {
    static Logger logger = Logger.getLogger(VMSMailer.class.getName());
    private static Properties mailerProps = null;
    private VMSMyDAO mydao = null;
    private Connection conMy = null;
    
    
    /** Creates a new instance of VMSMailer */
    public VMSMailer(Properties props) {
        this.mailerProps = props;
    }
    
    public void runMailer(ArrayList data) throws VMSMailException{
        //create DAO reqd for sendMail
        try{
            createDAO();
        }catch(VMSDAOException vde){
            logger.error("runMailer : Error: ", vde);
            throw new VMSMailException(vde.getMessage());
        }
        //for each element of Data, prepare and send mail
        for(int count = 0; count < data.size(); count++){
            boolean sentFlag = false;
            sentFlag = sendMail((ArrayList)data.get(count), mydao);
        }
        
    }
    
    private boolean sendMail(ArrayList arrayList, VMSMyDAO mydao) {
        
        //contruct componets table for each case
        HtmlTable componentsTable = new HtmlTable();
        componentsTable.addTitle("Incompatible components list for the case: " + ((VMSCaseComponent)arrayList.get(0)).getCaseName());
        
        componentsTable.addHeader(new String[]{"CASE" ,
        "COMPONENT", "VERSION", "SERVER", "PORT/DETAILS"});
        
        for(int counter = 0 ; counter < arrayList.size(); counter++){
            VMSCaseComponent component = (VMSCaseComponent)arrayList.get(counter);
            componentsTable.addRow(new String[]{component.getCaseName(),
            component.getComponent(), component.getVersion(), component.getServerName(), component.getPort()});
        }
        componentsTable.commit();
        
        //mail constituents construction
        VMSMailReport report = new VMSMailReport();
        report.setSubject("VMS[Beta] :Incompatible Components for case: " + ((VMSCaseComponent)arrayList.get(0)).getCaseName());
        report.setMailHeader("Incompatible Components");
        report.addTable(componentsTable);
        report.finalizeMail();
        if(report.sendMailReport(mailerProps)){
            int insFlag = 0;
            try{
                insFlag = mydao.recordHistory(((VMSCaseComponent)arrayList.get(0)).getCaseGid(), VMSStaticParams.MSG_INCOMPATIBILITY);
            }catch(VMSDAOException vde){
                logger.error("sendMail: Error: " , vde);
            }
            if(insFlag > 0){
                logger.info("sendMail: Mail sent/Event Captured for case " + ((VMSCaseComponent)arrayList.get(0)).getCaseName());
            }
        }
        
        return true;
        
    }
    
    public void sendMail4MultipleInstallations(ArrayList multipleInstallationData) {
        //contruct componets table for each case
        HtmlTable componentsTable = new HtmlTable();
        componentsTable.addTitle("Redundant Component Installations list");
        
        componentsTable.addHeader(new String[]{"CASE" ,
        "COMPONENT", "VERSION", "SERVER", "PORT/DETAILS"});
        
        for(int counter = 0 ; counter < multipleInstallationData.size(); counter++){
            VMSCaseComponent component = (VMSCaseComponent)multipleInstallationData.get(counter);
            componentsTable.addRow(new String[]{component.getCaseName(),
            component.getComponent(), component.getVersion(), component.getServerName(), component.getPort()});
        }
        componentsTable.commit();
        
        //mail constituents construction
        VMSMailReport report = new VMSMailReport();
        report.setSubject("VMS[Beta] :Redundant Components Detection List");
        report.setMailHeader("Redundant Components Information");
        report.addTable(componentsTable);
        report.finalizeMail();
        if(report.sendMailReport(mailerProps)){
            int insFlag = 0;
            try{
                insFlag = mydao.recordRedundantHistory( VMSStaticParams.MSG_REDUNDANCY);
            } catch(VMSDAOException vde){
                logger.error("sendMail4MultipleInstallations: Error: " , vde);
            }
            if(insFlag > 0){
                logger.info("sendMail4MultipleInstallations: Mail sent/Event Captured for case redundant components.");
            }
        }
        
    }
    
    private void createDAO() throws VMSDAOException{
        //get connection to dest MySQL DB(VMS MYSQL DB) - param 1 for it
        try{
            this.conMy = VMSConnectionFactory.getMyConnection(1);
            this.mydao = VMSDAOFactory.getVerMyDAO(conMy);
        }catch(Exception ex){
            logger.error("createDAO: Error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }
    }
    
    
}
