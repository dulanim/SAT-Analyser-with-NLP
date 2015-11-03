/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *..
 */
package com.project.NLP.Requirement;

import edu.stanford.nlp.trees.Tree;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author S. Shobiga
 */
public class MainClass {

    public static void main(String args[]) {
/*
        String document1 = "The  bank client must be able to deposit an amount to \n"
                + "and withdrawing an amount from his or her account\n"
                + "using the bank application from Moratuwa branch before 10pm";

        String document = "The  bank client must be able to deposit an amount to \n"
                + "and withdrawing an amount from user account\n"
                + "using the bank application from Moratuwa branch before 10pm";

        String document2 = "The system shall record customer details, such as name, dob, address, telephone number and account number.";

//        Each transaction must be\n"
//                + "recorded, and the client must have the ability to\n"
//                + "review all transactions performed against a given \n"
//                + "account. Recorded transactions must include the\n"
//                + "date, time, transaction type, amount and account\n"
//                + "balance after the transaction.";
//        
        StanfordCoreNLPModified stanford = new StanfordCoreNLPModified(document2);
        Tree[] tree = stanford.generateTreeAnnotation();

//        stanford.generateCorefLink();
//        noun pharase identification
        ClassIdentification np = new ClassIdentification(tree);
        HashSet classList = np.getClasses();

        System.out.println("class found:" + classList);

        System.out.println("attribute found from class:" + np.getAttributeFromClass());

//        
//         ArrayList att =np.getAttributeFromClass();
//        System.out.println("Attribute found:"+att);
//        
//        attributes
        AttributeIdentification attr = new AttributeIdentification(tree, np.getAttributeFromClass());
        HashSet attrList = attr.getAttributes();
        System.out.println("Final attributes: " + attrList);
        //attr.setAttributesFromClass(np.getAttributeFromClass());
//        

        System.out.println("*********************************");
        classList.removeAll(attrList);
        System.out.println("class Found :" + classList);
        System.out.println("attributes found: " + attrList);
*/
        
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
        ClassRelationIdentifier cIden=new ClassRelationIdentifier();
        cIden.identifyCandidateRelations(req1);
    }
}
