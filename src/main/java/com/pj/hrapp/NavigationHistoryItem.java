package com.pj.hrapp;

import java.util.Map;

public class NavigationHistoryItem {

	private String sceneName;
	private Map<String, Object> model;
	
	public NavigationHistoryItem(String sceneName, Map<String, Object> model) {
		this.sceneName = sceneName;
		this.model = model;
	}
	
	public String getSceneName() {
		return sceneName;
	}
	
	public Map<String, Object> getModel() {
		return model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
	}
	
}
