/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *..
 */
package com.project.NLP.Requirement;

import edu.stanford.nlp.trees.Tree;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author S. Shobiga
 */
public class MainClass {

    public static void main(String args[]) {

        String document1 = "The  bank client must be able to deposit an amount to \n"
                + "and withdrawing an amount from his or her account\n"
                + "using the bank application from Moratuwa branch before 10pm";

        //WORKING FINE..        
        /*String document2 = "The  bank client must be able to deposit an amount to \n"
         + "and withdrawing an amount from user account\n"
         + "using the bank application from Moratuwa branch before 10pm";
         */
        /*String document2 = "Each transaction must be\n"
         + "recorded";
         */
        //WORKING FINE...
        //PROBLEM- APPEARING 2 CLASSES AND HAVE TO REMOVE ONE -removing 2nd class would be better
        /*String document2="the client must have the ability to\n"
         + "review all transactions performed against a given\n"
         + "account. ";
         */
        //PROBLEM....
        /*      String document2 = "Recorded transactions must include the\n"
         + "date, time, transaction type, amount and account\n"
         + "balance after the transactions.";
         */
        //String document2="A bank client can have two types of accounts";
        //PROMBLEM.. when executing the next line
        //String document2 = "A checking account and a saving account";
        //PROBLEM...
        //String document2="For each checking account one related saving account can exists.";
        //WORKING FINE...
        //PROMBLEM.. WHAT TO WHEN THE NN COME WITHIN PARANTHESIS.. AND ACCESS IS TAKEN AS ATTRIBUTE
        /*String document2 = "The application must verify that a client can\n"
         + "gain access to his or her account by identification via\n"
         + "a personal identification number (PIN) code.";
         */
        /*Needs to remove the hyphens before doing NLP */
        /*DO NOT EXECUTE THIS SENTENCE - HAVE TO REMOVE THE NEGATIVE PART*/
        /*String document2 = "Neither a checking account nor a saving-account can\n"
         + "have a negative balance.";
         */
        /*String document2 = "The application should\n"
         + "automatically withdraw funds from a related saving account\n"
         + "if the requested withdrawal amount on the\n"
         + "checking account is more than its current balance.";
         */
        //WORKING FINE...
        /*String document2 = "If\n"
         + "the saving account balance is insufficient to cover the\n"
         + "requested withdrawal amount, the application should\n"
         + "inform the user and terminate the transaction.";
         */
        ArrayList sentence = readFromTextFile();
        HashSet classList = new HashSet();
        HashSet attrList = new HashSet();
        StringBuffer sbf = new StringBuffer();
        StringBuffer sb = new StringBuffer();

        BufferedWriter bwr;
        try {
            bwr = new BufferedWriter(new FileWriter(new File("ReqText.txt")));
            if (!sentence.isEmpty()) {
                for (int i = 0; i < sentence.size(); i++) {
                    StanfordCoreNLPModified stanford = new StanfordCoreNLPModified(sentence.get(i).toString());
                    Tree[] tree = stanford.generateTreeAnnotation();

                    /*noun pharase identification*/
                    ClassIdentification np = new ClassIdentification(tree);
                    classList = np.getClasses();
                    ArrayList attributesFromClass = np.getAttributeFromClass();
                    System.out.println("CLASS LIST:" + classList);

                    /*attributes*/
                    AttributeIdentification attr = new AttributeIdentification(tree, attributesFromClass, classList);
                    attrList = attr.getAttributes();
                    System.out.println("ATTRIBUTE LIST: " + attrList);

                    sb = writeToStringBuffer(classList, attrList, sbf);

                }
                writeResultToTextFile(sb, bwr);
                //readFromTextFile();
            }
            //flush the stream
            bwr.flush();

            //close the stream
            bwr.close();

        } catch (Exception e) {

        }

    }

    private static StringBuffer writeToStringBuffer(HashSet classList, HashSet attrList, StringBuffer sbf) {
        //StringBuffer sbf = new StringBuffer();

        if (!classList.isEmpty()) {
            //if (!attrList.isEmpty()) {
            for (Iterator cl = classList.iterator(); cl.hasNext();) {
                String s = cl.next().toString();
                sbf.append(s + ", ");

            }
            sbf.append("  :");
            if (attrList.isEmpty()) {
                sbf.append("NULL ");
            }
            for (Iterator at = attrList.iterator(); at.hasNext();) {
                String a = at.next().toString();
                sbf.append(a + ", ");

            }
            sbf.append(System.lineSeparator());
            //sbf.append(System.getProperty("line.separtor"));
            //}

        }
        return sbf;
    }

    private static void writeResultToTextFile(StringBuffer sbf, BufferedWriter bwr) {
        //create StringBuffer object
        try {

            /*
             * To write contents of StringBuffer to a file, use
             * BufferedWriter class.
             */
            //BufferedWriter bwr = new BufferedWriter(new FileWriter(new File("ReqText.txt")));
            //write contents of StringBuffer to a file
            //bwr.write(sbf.toString());
            bwr.append(sbf.toString());
            //bwr.newLine();
            //flush the stream
            //bwr.flush();
            //close the stream
            //bwr.close();
            System.out.println("Content of StringBuffer written to File.");
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    private static ArrayList readFromTextFile() {
        BufferedReader br = null;
        String[] sen;
        ArrayList l = new ArrayList();
        try {

            String sCurrentLine;
            br = new BufferedReader(new FileReader("OrderRequirement.txt"));

            while ((sCurrentLine = br.readLine()) != null) {
                //System.out.println(sCurrentLine);
                sen = sCurrentLine.split("\\.");
                for (int i = 0; i < sen.length; i++) {
                    l.add(sen[i]);
                }
            }

            for (int j = 0; j < l.size(); j++) {
                System.out.println("sen:   " + l.get(j));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return l;
    }

    /*
    
     String req1="The bank client must be able to deposit an amount to\n" +
     "and withdraw an amount from his or her accounts\n" +
     "using the bank application. Each transaction must be\n" +
     "recorded, and the client must have the ability to\n" +
     "review all transactions performed against a given\n" +
     "account. Recorded transactions must include the\n" +
     "date, time, transaction type, amount and account\n" +
     "balance after the transaction.\n" +
     "A bank client can have two types of accounts. A\n" +
     "checking-account and a saving-account. For each\n" +
     "checking account, one related saving-account can\n" +
     "exists. The application must verify that a client can\n" +
     "gain access to his or her account by identification via\n" +
     "a personal identification number (PIN) code.\n" +
     "Neither a checking-account nor a saving-account can\n" +
     "have a negative balance. The application should\n" +
     "automatically withdraw funds from a related saving account\n" +
     "if the requested withdrawal amount on the\n" +
     "checking-account is more than its current balance. If\n" +
     "the saving-account balance is insufficient to cover the\n" +
     "requested withdrawal amount, the application should\n" +
     "inform the user and terminate the transaction.";
        
     String req2="Candidate will register with system to hire services. Candidate\n" +
     "can provide information about his academic, work experience\n" +
     "and referee details. Candidates may make verification request\n" +
     "during registration or some time later. Candidate updates his\n" +
     "details at any time. System will inform service seeker about\n" +
     "approximate time period required to provide service. System\n" +
     "records details of candidate. System shall keep status of each\n" +
     "request up-to-date. System shall interact with education institute\n" +
     "systems to verify originality of degree. System asks type of service\n" +
     "required. Service may be Standard, Silver or Gold. System enters\n" +
     "request as a record in system. System informs about outcome to\n" +
     "the candidate. Referees send recommendation letters using system\n" +
     "on behalf of students. Customers pay fee for each type of service.\n" +
     "Customer may be candidate or employer. Standard service\n" +
     "verifies details of education. Silver service verifies details of\n" +
     "education and profession. Gold service verifies details of\n" +
     "education, profession and recommendation letters from referees.\n" +
     "Employer accesses system to hire services of system for\n" +
     "verification purposes. Employer registers with system. After,\n" +
     "employer should provide system with information on type of\n" +
     "service required along with candidate unique identification\n" +
     "number. System records details of employer. Verification officer\n" +
     "accesses system to retrieve verification and inquiry requests.\n" +
     "Verification officer performs all types of verifications requested\n" +
     "by candidate and employers upon retrieving requests. Verification\n" +
     "officer also verifies authenticity of referee. Verification officer\n" +
     "uses system to send to referees a request for a recommendation\n" +
     "letter when candidate requests gold service.";
        
     /*Call Method identifier */
        //MethodIdentifier mIden=new MethodIdentifier();
    //mIden.identifyCandidateMethods(req2);
    /*Call Relations Identifer */
       // ClassRelationIdentifier cIden=new ClassRelationIdentifier();
    // cIden.identifyCandidateRelations(req1);
}
