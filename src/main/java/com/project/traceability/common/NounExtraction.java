package com.project.traceability.common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.project.traceability.model.ArtefactElement;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.ArrayCoreMap;
import edu.stanford.nlp.util.CoreMap;

/**1 Dec 2014
 * @author K.Kamalan
 *
 */
public class NounExtraction {
	public static List<String> nounWords = new ArrayList<String>();
	
	public static void getClassName(String str) {
	//public static void main(String[] args){
		//String str = "Bank Details";
		StanfordCoreNLP pipeline = new StanfordCoreNLP();
		Annotation annotation = new Annotation(str);
		pipeline.annotate(annotation);
		List<CoreMap> sentences = annotation
				.get(CoreAnnotations.SentencesAnnotation.class);

		if (sentences != null && sentences.size() > 0) {
			ArrayCoreMap sentence = (ArrayCoreMap) sentences.get(0);
			Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
			List<Tree> leaves = tree.getLeaves();
			for (int i = 0; i < leaves.size(); i++) {
				Tree leaf = leaves.get(i);
				Tree preLeaf = null;
				Tree preParent = null;
				if (i != 0)
					preLeaf = leaves.get(i - 1);
				Tree parent = leaf.parent(tree);
				if (i != 0)
					preParent = preLeaf.parent(tree);

				if (parent.label().value().equals("IN")) {
					leaves.remove(leaf);
					i--;
				}
				if (i != 0 && preParent.label().value().equals("JJ")
						&& parent.label().value().equals("NN")) {
					System.out.println(preLeaf.label().value().toLowerCase());
					nounWords.add(preLeaf.label().value().toLowerCase());
				} else if (i != 0 && preParent.label().value().equals("NN")
						&& parent.label().value().equals("NN")) {
					System.out.println(preLeaf.label().value()
							.toLowerCase()
							+ " " + leaf.label().value().toLowerCase());
					nounWords.add(preLeaf.label().value()
							.toLowerCase()
							+ " " + leaf.label().value().toLowerCase());
					//artefactElement.setArtefactElementId(id);
				} else if (parent.label().value().equals("NN")
						|| parent.label().value().equals("NNP")) {
					System.out.println(leaf.label().value().toLowerCase());
					nounWords.add(leaf.label().value().toLowerCase());
					
				}
			}
		}
		//return artefactElement;
	}

}
