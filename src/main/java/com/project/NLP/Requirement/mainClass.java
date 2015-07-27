/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *..
 */
package com.project.NLP.Requirement;

import edu.stanford.nlp.trees.Tree;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author S. Shobiga
 */
public class mainClass {

    public static void main(String args[]) {

        String document = "The bank client must be able to deposit an amount to \n"
                + "and withdraw an amount from his or her accounts\n"
                + "using the bank application.";
//        Each transaction must be\n"
//                + "recorded, and the client must have the ability to\n"
//                + "review all transactions performed against a given \n"
//                + "account. Recorded transactions must include the\n"
//                + "date, time, transaction type, amount and account\n"
//                + "balance after the transaction.";
//        
        stanfordCoreNLP stanford = new stanfordCoreNLP(document);
        ArrayList tree = stanford.generateTreeAnnotation();
//        System.out.println("array list result for size: "+tree.size());
//         for(int i =0;i<tree.size();i++){
//             System.out.println(i+" "+tree.get(i));
//        }
        stanford.generateCorefLink();
        /*noun pharase identification*/
        classIdentification np = new classIdentification(tree);
        ArrayList list =np.nounPharseIdentification("NP");
        System.out.println(list);
        

    }
}
