/*
 * VMSFEDAO.java
 *
 * Created on June 6, 2008, 12:51 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.fe;
import com.stratify.apps.vms.common.VMSCommonUtils;
import com.stratify.apps.vms.common.VMSStaticParams;
import com.stratify.apps.vms.common.exceptions.VMSDAOException;
import com.stratify.apps.vms.common.vos.VMSComponent;
import com.stratify.apps.vms.dao.common.VMSDAOQueries;
import com.stratify.apps.vms.dao.common.VMSDAOUtils;
import com.stratify.apps.vms.common.VMSParseHTML;
import com.stratify.apps.vms.common.VMSReadRegistry;
import com.stratify.common.logging.Logger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import com.stratify.datahub.common.util.PyEnv;
import com.stratify.datahub.common.util.PyEnvLocal;


/**
 *
 * @author RavikishoreS
 */
public class VMSFEDAO {
    static Logger logger = Logger.getLogger(VMSFEDAO.class.getName());
    Connection con = null;
    
    /** Creates a new instance of VMSFEDAO */
    public VMSFEDAO(Connection con1) {
        this.con = con1;
    }
    
    public VMSComponent getDBServer(String serverName) throws VMSDAOException{
        PreparedStatement pstmtGetDBserver = null;
        ResultSet rsGetDBserver = null;
        VMSComponent fedb = null;
        //for FE DB server info obtd from DS_DB_PKG_BUILD
        //for DS_DB_PKG_BUILD info
        try{
            String serverType = "DatabaseServer_FE";
            String major = null;
            String minor = null;
            String build_number = null;
            String patch_number = null;
            String schema_number = null;
            fedb = new VMSComponent();
            //query is same for BE and FE DB
            pstmtGetDBserver = con.prepareStatement(VMSDAOQueries.GET_DB_INFO);
            logger.info("getDBServer: Query Used to get DB info: " + VMSDAOQueries.GET_DB_INFO);
            rsGetDBserver = pstmtGetDBserver.executeQuery();
            if(rsGetDBserver.next()){
                major = "" + rsGetDBserver.getInt("MAJOR");
                minor = "" + rsGetDBserver.getInt("MINOR");
                build_number = "" + rsGetDBserver.getInt("BUILD_NUMBER");
                patch_number = "" + rsGetDBserver.getInt("PATCH_NUMBER");
                schema_number = "" + rsGetDBserver.getInt("SCHEMA_NUMBER");
                
                fedb.setServerType(serverType);
                fedb.setVersion(major + "." + minor + "." + build_number +"." + patch_number );
                fedb.setServerURL(VMSStaticParams.HTTP + serverName + ":" + VMSStaticParams.MSSQL_PORT);
                fedb.setIsActive(1);
                
            }
        }catch(SQLException sqle){
            logger.error("getDBServer: DB related error: " , sqle);
            throw new VMSDAOException(sqle.getMessage());
        }catch(Exception ex){
            logger.error("getDBServer: error: " , ex);
            throw new VMSDAOException(ex.getMessage());
        }finally{
            VMSDAOUtils.freeUp(rsGetDBserver,pstmtGetDBserver);
        }
        
        return fedb;
    }
    
    public ArrayList getKopernik() throws VMSDAOException{
        ArrayList koperniks = null;
        
        PreparedStatement pstmtGetComponents = null;
        ResultSet rsGetComponents = null;
        String servType;
        String version ;
        String serverURL;
        int active;
        try{
            pstmtGetComponents = con.prepareStatement(VMSDAOQueries.GET_BE_COMPONENTS);
            logger.info("getKopernik: Query Used for getting Kopernik info: " + VMSDAOQueries.GET_BE_COMPONENTS);
            rsGetComponents = pstmtGetComponents.executeQuery();
            
            koperniks = new ArrayList();
            
            while(rsGetComponents.next()){
                servType = null;
                version = null;
                serverURL = null;
                active = 0;
                //component type is same as VMSComponent
                VMSComponent fecompo = new VMSComponent();
                
                servType = rsGetComponents.getString("SERVER_TYPE");
                version = rsGetComponents.getString("VERSION");
                serverURL = rsGetComponents.getString("SERVER_URL");
                active = rsGetComponents.getInt("ACTIVE");
                
                fecompo.setServerType(servType);
                fecompo.setVersion(version);
                fecompo.setServerURL(serverURL);
                fecompo.setIsActive(active);
                
                koperniks.add(fecompo);
                logger.info("getKoperniks: Kopernik Compo added.");
            }
        }catch(SQLException sqle){
            logger.error("getKoperniks: DB related error: " , sqle);
            throw new VMSDAOException(sqle);
        }catch(Exception ex){
            logger.error("getKoperniks: error: " , ex);
            throw new VMSDAOException(ex);
        }finally{
            VMSDAOUtils.freeUp(rsGetComponents, pstmtGetComponents);
        }
        
        return koperniks;
    }
    
    public VMSCaseShareList getCaseShareDB(int major, String caseName) throws VMSDAOException {
        PreparedStatement pstmtGetCaseShares = null;
        ResultSet rsGetCaseShares = null;
        VMSCaseShareList caseShareList = null;
        String servType ;
        String version ;
        String serverURL;
        int primary ;
        int indexOfLastSlash ;
        
        try{
            //try to get the FE caseShares
            if(major >= 9){
                pstmtGetCaseShares = con.prepareStatement(VMSDAOQueries.GET_FE_CASESHARES_9_ON);
                logger.info("getCaseShare: " + VMSDAOQueries.GET_FE_CASESHARES_9_ON);
            }else if(major > 6 && major <= 8){
                pstmtGetCaseShares = con.prepareStatement(VMSDAOQueries.GET_FE_CASESHARES_6_ON);
                logger.info("getCaseShare: " + VMSDAOQueries.GET_FE_CASESHARES_6_ON);
            }else{
                pstmtGetCaseShares = con.prepareStatement(VMSDAOQueries.GET_FE_CASESHARES_6_OFF);
                logger.info("getCaseShare: " + VMSDAOQueries.GET_FE_CASESHARES_6_OFF);
            }
            
            rsGetCaseShares = pstmtGetCaseShares.executeQuery();
            caseShareList = new VMSCaseShareList();
            
            while(rsGetCaseShares.next()){
                servType = null;
                version = "0.0.0.0";
                serverURL = null;
                primary = 0;
                indexOfLastSlash = 0;
                
                VMSComponent feCaseShare = new VMSComponent();
                
                serverURL = rsGetCaseShares.getString("CASESHARE_PATH").trim();
                //try to remove ending \ if any
                try{
                    if(serverURL.endsWith("\\")){
                        indexOfLastSlash = serverURL.lastIndexOf("\\");
                        serverURL = serverURL.substring(0, indexOfLastSlash);
                    }
                }catch(Exception ex){
                    logger.warn("getCaseShareDB: Error: " , ex);
                    continue;
                }
                
                primary = rsGetCaseShares.getInt("IS_PRIMARY");
                if(primary == 0){
                    servType = VMSStaticParams.CASESHARE_SECONDARY;
                }else{
                    servType = VMSStaticParams.CASESHARE_PRIMARY;
                }
                
                //read the version info from caseshare/scripts/common/version.txt
                try{
                    version = VMSCommonUtils.readVersion(serverURL);
                }catch(Exception fe){
                    logger.error("getCaseShare:  error: " , fe);
                }
                
                logger.info("getCaseShare: CaseShare(Original): " + serverURL);
                
                feCaseShare.setIsActive(1);
                feCaseShare.setServerType(servType);
                feCaseShare.setServerURL(serverURL);
                feCaseShare.setVersion(version);
                
                //if this case Share not exists, add
                if(!caseShareList.containsServer(feCaseShare)){
                    caseShareList.add(feCaseShare);
                }
                
                logger.info("getCaseShare: Primary CaseShare info added.");
                
                
            }
        }catch(SQLException sqle){
            logger.error("getCaseShare: DB related error: ", sqle);
            throw new VMSDAOException(sqle);
        }catch(Exception ex){
            logger.error("getCaseShare: error: " , ex);
            throw new VMSDAOException(ex);
        }finally{
            VMSDAOUtils.freeUp(rsGetCaseShares, pstmtGetCaseShares);
            
        }
        
        return caseShareList;
    }
    
    public VMSComponent getSearchServer() throws VMSDAOException{
        PreparedStatement pstmtGetDTSearch = null;
        ResultSet rsGetDTSearch = null;
        VMSComponent dtSearch = null;
        try{
            //lds/ftSearchServer
            pstmtGetDTSearch = con.prepareStatement(VMSDAOQueries.GET_BE_DTSEARCH);
            logger.info("getSearchServer: " + VMSDAOQueries.GET_BE_DTSEARCH);
            
            rsGetDTSearch = pstmtGetDTSearch.executeQuery();
            
            String servURL = null;
            String servVersion = null;
            String serverDT = null;
            String serverDTMod = null;
            String numPart = null;
            String subKey = null;
            String serverType = VMSStaticParams.SEARCH_SERVER;
            int indexOfLastSlash = 0;
            
            if(rsGetDTSearch.next()){
                servURL = rsGetDTSearch.getString("VALUE").trim();
            }
            
            //logic revised - May 21, 2008
            servVersion = new VMSParseHTML(servURL, VMSStaticParams.SEARCH_SERVER_ALIAS.toLowerCase()).getText().trim();
            
            logger.info("getSearchServer: DTS search version obtd: " + servVersion);
            
            dtSearch = new VMSComponent();
            //set the component values
            dtSearch.setIsActive(1);
            dtSearch.setServerType(serverType);
            dtSearch.setVersion(servVersion);
            dtSearch.setServerURL(servURL);
        }catch(SQLException sqle){
            logger.error("getSearchServer: DB related error: " , sqle);
            throw new VMSDAOException(sqle);
        }catch(Exception ex){
            logger.error("getSearchServer: error: ", ex);
            throw new VMSDAOException(ex);
        }finally{
            VMSDAOUtils.freeUp(rsGetDTSearch, pstmtGetDTSearch);
            
        }
        
        return dtSearch;
        
    }
    
    public VMSComponent getAnnotationServer() throws VMSDAOException{
        PreparedStatement pstmtGetAnnotationSearchServer = null;
        ResultSet rsGetAnnotationSearchServer = null;
        VMSComponent annotationserver = null;
        try{
            //lds/ftSearchServer
            pstmtGetAnnotationSearchServer = con.prepareStatement(VMSDAOQueries.GET_ANNOTATION_SEARCH_SERVER);
            logger.info("getAnnotationServer: " + VMSDAOQueries.GET_ANNOTATION_SEARCH_SERVER);
            
            rsGetAnnotationSearchServer = pstmtGetAnnotationSearchServer.executeQuery();
            
            String servURL = null;
            String servVersion = null;
            String serverAnnotation = null;
            String serverAnnotationMod = null;
            String numPart = null;
            String subKey = null;
            String serverType = VMSStaticParams.ANNOTATION_SERVER;
            int indexOfLastSlash = 0;
            
            if(rsGetAnnotationSearchServer.next()){
                servURL = rsGetAnnotationSearchServer.getString("SERVER_URL").trim();
            }
            
            //logic revised - May 21, 2008
            servVersion = new VMSParseHTML(servURL, VMSStaticParams.SEARCH_SERVER_ALIAS.toLowerCase()).getText().trim();
            
            logger.info("getAnnotationServer: Annotation search version obtd: " + servVersion);
            
            annotationserver = new VMSComponent();
            //set the component values
            annotationserver.setIsActive(1);
            annotationserver.setServerType(serverType);
            annotationserver.setVersion(servVersion);
            annotationserver.setServerURL(servURL);
        }catch(SQLException sqle){
            logger.error("getAnnotationServer: DB related error: " , sqle);
            throw new VMSDAOException(sqle);
        }catch(Exception ex){
            logger.error("getAnnotationServer: error: ", ex);
            throw new VMSDAOException(ex);
        }finally{
            VMSDAOUtils.freeUp(rsGetAnnotationSearchServer, pstmtGetAnnotationSearchServer);
            
        }
        
        return annotationserver;
        
    }
    
    public ArrayList getPDFRedactServer() throws VMSDAOException{
        ArrayList pdfredactservers = null;
        PreparedStatement pstmtGetRedactPDF = null;
        ResultSet rsGetRedactPDF = null;
        String servURL;
        String servName;
        String servVersion;
        String serverDT;
        String serverType;
        String subKey;
        
        try{
            //now for Redact, PDF Servers
            //redact/redactionSrv
            //redact/pdfconMSFEvertSrv
            pdfredactservers = new ArrayList();
            pstmtGetRedactPDF = con.prepareStatement(VMSDAOQueries.GET_FE_REDACTPDF);
            rsGetRedactPDF = pstmtGetRedactPDF.executeQuery();
            logger.info("getPDFRedactServer: Query for getting PDF/REDACT server: " + VMSDAOQueries.GET_FE_REDACTPDF );
            
            while(rsGetRedactPDF.next()){
                servURL = null;
                servName = null;
                servVersion = null;
                serverDT = null;
                serverType = null;
                subKey = null;
                
                VMSComponent pdfRed = new VMSComponent();
                
                servURL = rsGetRedactPDF.getString("VALUE").trim();
                servName = rsGetRedactPDF.getString("NAME").trim();
                
                //logic revised 21 May 2008
                if(servName.equalsIgnoreCase(VMSStaticParams.REDACT_SERVER_KEY)){
                    serverType = VMSStaticParams.REDACTION_SERVER_ALIAS;
                    try{
                        servVersion = new VMSParseHTML(servURL, serverType).getText().trim();
                    }catch(Exception ex){
                        logger.warn("getPDFRedactServer: Problem getting Redaction Server version: " , ex);
                    }
                }else if(servName.equalsIgnoreCase(VMSStaticParams.PDF_SERVER_KEY)){
                    serverType = VMSStaticParams.PDF_SERVER_ALIAS;
                    try{
                        servVersion = new VMSParseHTML(servURL, serverType).getText().trim();
                    } catch(Exception ex){
                        logger.warn("getPDFRedactServer: Problem getting PDF Server version: " , ex);
                    }
                }
                logger.info("getPDFRedactServer: Server version obtd: " + servVersion);
                
                //set the component values
                pdfRed.setIsActive(1);
                pdfRed.setServerType(serverType);
                pdfRed.setVersion(servVersion);
                pdfRed.setServerURL(servURL);
                
                pdfredactservers.add(pdfRed);
                
            }
        }catch(SQLException sqle){
            logger.error("getPDFRedactServer: DB related error: ", sqle);
            throw new VMSDAOException(sqle);
        }catch(Exception ex){
            logger.error("getPDFRedactServer: error: " , ex);
            throw new VMSDAOException(ex);
        }finally{
            VMSDAOUtils.freeUp(rsGetRedactPDF, pstmtGetRedactPDF);
        }
        return pdfredactservers;
    }
    
    ArrayList getSecondaryCaseShares(String caseName) throws VMSDAOException{
        ArrayList secCaseShares = null;
        PreparedStatement pstmtGetSecCaseShares = null;
        ResultSet rsGetSecCaseShares = null;
        String servURL;
        
        try{
            
            secCaseShares = new ArrayList();
            pstmtGetSecCaseShares = con.prepareStatement(VMSDAOQueries.GET_FE_SECCASESHARES);
            rsGetSecCaseShares = pstmtGetSecCaseShares.executeQuery();
            logger.info("getSecondaryCaseShares: Query for getting secondary caseshares 9: " + VMSDAOQueries.GET_FE_SECCASESHARES );
            
            String seccaseshare;
            
            caseName = caseName.toLowerCase();
            
            while(rsGetSecCaseShares.next()){
                seccaseshare = rsGetSecCaseShares.getString("PREFIX").trim().toLowerCase();
                if((seccaseshare.startsWith("\\")) && (seccaseshare.indexOf(caseName) > -1)){
                    seccaseshare = seccaseshare.substring(0, seccaseshare
                            .indexOf(caseName) - 1);
                    seccaseshare += "\\" + caseName;
                    
                    if(!secCaseShares.contains(seccaseshare)){
                        secCaseShares.add(seccaseshare);
                        logger.info("getSecondaryCaseShares: Sec caseshares added " + seccaseshare);
                    }
                }
                
                
            }
        } catch(SQLException sqle){
            logger.error("getSecondaryCaseShares: DB related error: ", sqle);
            throw new VMSDAOException(sqle);
        }catch(Exception ex){
            logger.error("getSecondaryCaseShares: error: " , ex);
            throw new VMSDAOException(ex);
        }finally{
            VMSDAOUtils.freeUp(rsGetSecCaseShares, pstmtGetSecCaseShares);
        }
        return secCaseShares;
    }
    
    
    
}
