/*
 * VMSMailReport.java
 *
 * Created on June 3, 2008, 2:34 AM
 *
 * Stratify Inc P Ltd
 */

package com.stratify.apps.vms.mail;

import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import com.stratify.apps.common.html.HtmlTable;
import com.stratify.apps.common.mail.HtmlMailImpl;

public class VMSMailReport {
    private static HtmlMailImpl mail = new HtmlMailImpl();
    private String mailHeader = "";
    private String htmlMail;
    private String subject = null;
    private static String htmlPrefix = "<html><head><style>body,td,th,div,input,textarea,select," +
            "option{font-family:Verdana,Arial,Helvetica,sans-serif;font-size:11px;}.dataTable" +
            "{border: 1px solid #808080;border-collapse: collapse;empty-cells: show}.dataTable td, " +
            ".dataTable th{padding: 2px;border: 1px solid #808080;}.dataTable th{background: #EEEEEE;" +
            "color: #666666;font-weight: bold;padding-right: 5px;}.dataTable td.number{text-align: right;}" +
            "</style></head><body>";
    private ArrayList<HtmlTable> htmlTables = new ArrayList<HtmlTable>();
    
    private static String FOOTER = "<br><br>For any comments or suggestion mail to <a href=\"mailto:VMS-Admin@stratify.com?subject=[VMS Feedback]\">VMS ADMIN</a><br><br>Accuracy disclaimer: This  email alert provides only indicative incompatible components for a case, please <br>" +
            "verify the data provided in this mail against actual components and take necessary actions. <br>" +
            "------------------------------------------------------------------------<br>" +
            "N.B. This is an automatically generated email message. Please do not reply to it.<br><br>" +
            "The information transmitted is intended only for the person or entity to which it is addressed and<br>" +
            "may contain confidential and/or privileged.Any review, retransmission, dissemination or other use of,<br>" +
            "or taking of any action in reliance upon, this information by persons or entities other than the intended <br>" +
            "recipient is prohibited. If you received this in error, please contact the sender immediately and delete this <br>" +
            "message and any attachments from any computer.<br>";
    
    public void setMailHeader(String header){
        mailHeader = "<Strong><font size=2.5><U>" + header + "</U><BR><BR></font></strong>";
    }
    
    public void finalizeMail(){
        htmlMail = htmlPrefix + mailHeader;
        for(int i = 0;i < htmlTables.size();i++)
            htmlMail += htmlTables.get(i).getContent();
        htmlMail += FOOTER + "</body></html>";
    }
    
    public void addTable(HtmlTable ht){
        htmlTables.add(ht);
    }
    
    public boolean sendMailReport(Properties props){
        boolean sentFlag = false;
        String pop_recepients[] = null;
        String weeklyReportSubject = "";
        String mailFrom = "";
        
        /* Send POP3 mail report. */
        String ReportViewers = props.getProperty("ReportViewers", "ravikishores@stratify.com");       
        mailFrom = props.getProperty("mailFrom", "VMS-ADMIN@stratify.com");
        StringTokenizer st = new StringTokenizer(ReportViewers,",");
        pop_recepients = new String[st.countTokens()];
        if(null == subject || "".equals(subject))
            weeklyReportSubject = props.getProperty("weeklyReportSubject"); // "SOS : WeeklyStatus report for the week ending : " + (new java.sql.Date(System.currentTimeMillis()).toString());
        else
            weeklyReportSubject = subject;
        for(int i = 0;i < pop_recepients.length;i++){
            pop_recepients[i] = st.nextToken().trim();
        }
        try{
            mail.setMailHost(props.getProperty("MAILHOST", "corpmail01.calpurnia.com"));
            mail.setMailFormat(props.getProperty("MAILFORMAT", "text/html"));
            mail.postMail(pop_recepients, weeklyReportSubject, htmlMail, mailFrom);
            sentFlag = true;
        } catch(Exception e){
            e.printStackTrace();
        }
        
        return sentFlag;
    }
    
    public String getHtmlMail() {
        return htmlMail;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
}

