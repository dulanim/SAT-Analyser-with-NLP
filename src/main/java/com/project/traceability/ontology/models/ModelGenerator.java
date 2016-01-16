package com.project.traceability.ontology.models;

public class ModelGenerator {

	
	
	
	public static void main(String[] args) {
		
		 ModelCreator model_creator = ModelCreator.getModelInstance();
		 model_creator.setPath("/home/shiyam/");
		 if(model_creator.initModel()){
			 System.out.println("Success fully initiated model.owl file ");
		 }else{
			 System.out.println("Failled in initiation of model.owl file ");
		 }
		 

			
		 Word word = new Word("hasName","Kanapathy","Person",StaticData.OWL_CLASS);
		 if(model_creator.createNewNode("Mother", "Mom",word)){
			 System.out.println("Success fully added Person new node into model.owl file ");
			 
			 word = new Word();
			 word.setPropertyName("hasName");
			 word.setValue("SteveJob");
			 word.setWordType(StaticData.OWL_CLASS);
			 word.setParentName("Person");
			 
			 if(model_creator.createNewNode("Father", "Appa",word)){
				 System.out.println("Successfully added new node into model.owl file ");
			 }
			 
			 word = new Word();
			 word.setPropertyName("hasSize");
			 word.setValue("25cm");
			 word.setWordType(StaticData.OWL_CLASS);
			 word.setParentName("Vehicle");
			 if(model_creator.createNewNode("Car", "Mini-Bus",word)){
				 System.out.println("Successfully added Vehcile new node into model.owl file ");
			 }
			 
			 word = new Word();
			 word.setPropertyName("hasSize");
			 word.setValue("90cm");
			 word.setWordType(StaticData.OWL_CLASS);
			 word.setParentName("Car");
			 
			 if(model_creator.createNewNode("SuperCar", "DevilCar",word)){
				 System.out.println("Successfully added Car new node into model.owl file ");
			 }
			 
			 word = new Word();
			 word.setPropertyName("hasLength");
			 word.setValue("1.5meter");
			 word.setWordType(StaticData.OWL_CLASS);
			 word.setParentName("SuperCar");
			 
			 if(model_creator.createNewNode("SafariCar", "Maruthi",word)){
				 System.out.println("Successfully added SuperCar new node into model.owl file ");
			 }
		 }else{
			 System.out.println("Failled in adding new node of model.owl file ");
		 }
		 
		 if(model_creator.isMatchingWords("SafariCar", "Father")){
			 System.out.println("Matched");
		 }else{
			 System.out.println("Not Matched");
		 }
		 
//		 NavigationModel navigator = NavigationModel.getNavigatorInstane();
//		 navigator.getAllClassesProperty();
//		 
//		 System.out.println("-------------------Subclass of  " + "Classes" + "--------------------");
//		 List<String> list = navigator.getSubClassFor("Classes");
//		 System.out.println("Sizeeeeeeeee" + list.size());
//		 
//		 System.out.println("-------------------Classes Properties " + "--------------------");
//		 list = navigator.getAllProperties();
//		 
//		 System.out.println("-------------------Classes Values " + "--------------------");
//		 navigator.getAllValues1("hasSize");//("hasName");
	}
	

}
