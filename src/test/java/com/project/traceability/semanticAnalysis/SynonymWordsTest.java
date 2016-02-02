/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.project.traceability.semanticAnalysis;

import com.project.traceability.model.WordsMap;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author K.Kamalan
 */
public class SynonymWordsTest {

	public SynonymWordsTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of checkSymilarity method, of class SynonymWords.
	 */
	@Test
	public void testCheckSymilarity_3args() {
		System.out.println("checkSymilarity");
		String term1 = "Account";
		String term2 = "account";
		String type = "class";
		boolean expResult = true;
		WordsMap w1 = new WordsMap();
		w1 = SynonymWords.checkSymilarity(term1, term2, type);
		boolean result = w1.isIsMatched();
		assertEquals(expResult, result);
	}

	/**
	 * Test of checkSymilarity method, of class SynonymWords.
	 */
	@Test
	public void testCheckSymilarity_4args() {
		System.out.println("checkSymilarity");
		String term1 = "AccountNo";
		String term2 = "AccountHolder";
		String type1 = "attribute";
		String type2 = "attribute";
		List<String> classNames = new ArrayList<>();
		classNames.add("account");
		classNames.add("customer");
		boolean expResult = false;
		WordsMap w2 = new WordsMap();
		w2 = SynonymWords.checkSymilarity(term1, term2, type1, type2,
				classNames);
		boolean result = w2.isIsMatched();
		assertEquals(expResult, result);
		WordsMap w3 = new WordsMap();
		w3 = SynonymWords.checkSymilarity("getAccount", "getAccountNo",
				"Field", "field", classNames);
		WordsMap w4 = new WordsMap();
		w4 = SynonymWords.checkSymilarity("airplane", "plane", "Attribute",
				"attribute", classNames);
		WordsMap w5 = new WordsMap();
		w5 = SynonymWords.checkSymilarity("isPaid", "isAc", "Attribute",
				"Attribute", classNames);
		WordsMap w6 = new WordsMap();
		w6 = SynonymWords.checkSymilarity("telNo", "telephone number",
				"Attribute", "Attribute", classNames);
		assertEquals(false, w3.isIsMatched());
		assertEquals(true, w4.isIsMatched());
		assertEquals(false, w5.isIsMatched());
		assertEquals(true, w6.isIsMatched());
	}

	/**
	 * Test of isFirstletterChanged method, of class SynonymWords.
	 */
	@Test
	public void testIsFirstletterChanged() {
		System.out.println("isFirstletterChanged");
		String term1 = "getName";
		String term2 = "setName";
		boolean expResult = true;
		boolean result = SynonymWords.isFirstletterChanged(term1, term2);
		assertEquals(expResult, result);
	}

}
