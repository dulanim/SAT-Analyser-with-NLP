//package com.project.traceability.utils;
//
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.util.*;
//
//import opennlp.tools.cmdline.parser.ParserTool;
//import opennlp.tools.parser.Parse;
//import opennlp.tools.parser.Parser;
//import opennlp.tools.parser.ParserFactory;
//import opennlp.tools.parser.ParserModel;
//
//public class ParserTest {
//
//    static Set<String> nounPhrases = new HashSet<>();
//    static Set<String> adjectivePhrases = new HashSet<>();
//    static Set<String> verbPhrases = new HashSet<>();
//    private static String line = "The system shall record customer details, such as name, dob, address, telephone number and account number";
//
//   /* public void getClassName(){
//        String[] requirementPart= line.split("such as");
//        System.out.println(requirementPart[0]);
//    }*/
//
//    public void getNounPhrases(Parse p) {
//        if (p.getType().equals("NN") || p.getType().equals("NNS") ||  p.getType().equals("NNP") || p.getType().equals("NNPS")) {
//            nounPhrases.add(p.getCoveredText());
//        }
//
//        if (p.getType().equals("JJ") || p.getType().equals("JJR") || p.getType().equals("JJS")) {
//            adjectivePhrases.add(p.getCoveredText());
//        }
//
//        if (p.getType().equals("VB") || p.getType().equals("VBP") || p.getType().equals("VBG")|| p.getType().equals("VBD") || p.getType().equals("VBN")) {
//            verbPhrases.add(p.getCoveredText());
//        }
//
//        for (Parse child : p.getChildren()) {
//            getNounPhrases(child);
//        }
//    }
//    public void parserAction() throws Exception {
//        InputStream is = new FileInputStream("./src/en-parser-chunking.zip");
//        ParserModel model = new ParserModel(is);
//        Parser parser = ParserFactory.create(model);
//        String[] requirementPart= line.split("such as");
//        System.out.println(requirementPart[0]);
//        Parse topParses[] = ParserTool.parseLine(requirementPart[0], parser, 1);
//        for (Parse p : topParses){
//            //p.show();
//            getNounPhrases(p);
//        }
//    }
//
//
//
//    public void removeDefaultWord(Set<String> parse){
//        String result;
//        String[] terms = {"system", "details", "company", "information", "requirement", "organization", "database"};
//        List<Set<String>> resultList = Arrays.asList(parse);
//        //resultList.removeAll(Arrays.asList(terms));
//        ArrayList<String> termArrayList = new ArrayList<String>(Arrays.asList(terms));
//        /*if (resultList.size()>0){
//            System.out.println(resultList);
//        }*/
//
//        for (int i=0;i<resultList.size();i++){
//            for (int j=0;i<terms.length;j++)
//                if (resultList.get(i).contains(terms[j])) {
//                    resultList.remove(i);
//                    break;
//                }
//
//        }
//        System.out.println(resultList.size());
//    }
//
//    public static void main(String[] args) throws Exception {
//        new ParserTest().parserAction();
//       // new ParserTest().getClassName();
//        System.out.println("List of Noun Parse : "+nounPhrases);
//        new ParserTest().removeDefaultWord(nounPhrases);
//        System.out.println("List of Adjective Parse : "+adjectivePhrases);
//        System.out.println("List of Verb Parse : "+verbPhrases);
//    }
//}
//
//
