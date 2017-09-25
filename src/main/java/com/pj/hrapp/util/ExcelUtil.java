package com.pj.hrapp.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import com.pj.hrapp.gui.component.ShowDialog;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ExcelUtil {

	public static FileChooser getSaveExcelFileChooser(String initialFileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialDirectory(Paths.get(System.getProperty("user.home"), "Desktop").toFile());
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Excel files", "*.xlsx"));
        fileChooser.setInitialFileName(initialFileName);
        return fileChooser;
	}
	
	public static void openExcelFile(File file) {
		String[] cmdarray = new String[]{"cmd.exe", "/c", file.getAbsolutePath()}; 
		try {
			Runtime.getRuntime().exec(cmdarray);
		} catch (IOException e) {
			ShowDialog.unexpectedError();
		} 	
	}
	
}
