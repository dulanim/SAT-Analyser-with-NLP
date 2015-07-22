package com.project.traceability.manager;

import java.util.ArrayList;
import java.util.List;

import com.project.traceability.model.ParameterModel;

public class ParameterManager {

	public static List<ParameterModel> listParameters(String parameterString){
		String[] parameters = parameterString.split(",");
		List<ParameterModel> extractedParametrs = new ArrayList<ParameterModel>();
		for(int i = 0; i < parameters.length; i++){
			String[] temp = parameters[i].split(":");
			ParameterModel parameter = new ParameterModel(temp[1], temp[0]);
			extractedParametrs.add(parameter);
		}
		return extractedParametrs;
	}
}
