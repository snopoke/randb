package com.nomsic.randb.test;

import java.io.File;

public class TestUtils {
	
	public static final String TEST_DATA_FOLDER = "target/testDataFolder";

	public static void prepare() {
		System.out.println("####### Deleting data folder ########");
		File activeMqTempDir = new File(TEST_DATA_FOLDER);
		deleteDir(activeMqTempDir);
		
	}

	private static void deleteDir(File directory){
		if (directory.exists()){
			String[] children = directory.list();
			if (children != null){
				 for (int i=0; i < children.length; i++) {
			         deleteDir(new File(directory, children[i]));
			     }
			}
		}
		directory.delete();
	}
}
