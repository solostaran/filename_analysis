package fr.solostaran.filename_analysis;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public class MainController {

	private Stage stage;
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public MainController() {
//		txtDirectory.textProperty().addListener((observable, oldValue, newValue) -> {
//			if (!newValue.equals(oldValue)) {
//				onChangeDirectory(new File(newValue));
//			}
//		});
	}

	@FXML
	private TextField txtDirectory;

	@FXML
	private TextArea centralText;

	@FXML
	private TextField txtDelimiters;

	@FXML
	private CheckBox chkSubdir;

	@FXML
	private CheckBox chkTest;

	public void onChooseDirectoryClick(ActionEvent actionEvent) {
		DirectoryChooser dir_chooser = new DirectoryChooser();
		String initialDir = txtDirectory.getText();
		dir_chooser.setInitialDirectory(initialDir.isEmpty() ? new File(".") : new File(initialDir));
		File dir = dir_chooser.showDialog(stage);
		if (dir != null) {
			txtDirectory.setText(dir.getAbsolutePath());
			try {
				onChangeDirectory(dir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void onChangeDirectory(File directory) throws IOException {
		//final String [] delimiters = {"_#", "_@", "."};
		String strDelim = txtDelimiters.getText();
		boolean subdir = chkSubdir.isSelected();
		String [] delimiters = strDelim.split("[|]");
		// get the file list (with or without sub-directories)
		// from https://stackoverflow.com/questions/2056221/recursively-list-files-in-java
		List<File> fileList = new ArrayList<>();
		if (chkTest.isSelected()) {
			// for test purpose
			final String [] fileArray = {"FPjAxF4aIAMyMTs_Spring_@2GONG_02.jpg","FMvy3dnaMAEAkS4_#HuTao_#GenshinImpact_@POISE_21.jpg","FPfXafmaQAEHRUS_@POISE_21.jpg","FPfXYsCaIAIbBtv_@POISE_21.jpg","FPKq-zTVUAEHQZG_#BlueArchive_@hipanan2222.jpg","FPP5r62aUA0Kh6W_Ninja_Pekora_@Sco_ttie.jpg","FPP6dDnaMAYtj3M_@_HaeO.jpg","FPPm169agAQNSFF_Late_@EunYooo_.jpg","FPPOkuJVQAEnUEA_@Nannung_mdr.jpg","FPPqE19VEAQYCNG_@Tess88884.jpg","FPQAoyqVIAkksrk_Yeon[CherryBlossom]_@rmflawha12.jpg","FPQEUZTaUAc-EGd_Spring_@ozzingo.jpg","FPQKCt6aUAQfnkq_Spring_@_NknHm.jpg","FPR8bqhVIAQMACj_#Yelan_@retty9349.jpg","FPT0M6fVQAg2aeh_Ookami_Mio_@Yozora1902.jpg","FPT8Rz9UYAQYDIq_@hd_1735.jpg","FPUZpSkaQAEZlUJ_Mars_@patch_oxxo.jpg"};
			Arrays.stream(fileArray).forEach(s -> fileList.add(new File(s)));
		} else {
			// different code between listing files or listing subdirectories + files
			if (subdir)
				Files.walk(directory.toPath()).filter(Files::isRegularFile).forEach(p -> fileList.add(p.toFile()));
			else {
				Arrays.stream(directory.listFiles()).sequential().filter(File::isFile).forEach(f -> fileList.add(f));
			}
		}

		// segmentation on the filelist
		Map<String, Map<String, Integer>> splitMap = StringSegmentation.initializeMap(delimiters);
		for (File file : fileList) {
			//System.out.println(file.getName());
			try {
				StringSegmentation.segmentation(file.getName(), splitMap);
			} catch (StringIndexOutOfBoundsException e) {}
		}

		// display (and sort) in a text area
		centralText.setText("");
		splitMap.forEach( (delim, map) -> {
			centralText.appendText(delim+"\n");
			// sort : https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values
			map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
							.forEach( (e) -> centralText.appendText("\t"+e.getValue()+"\t"+e.getKey()+"\n"));
		});
	}
}
