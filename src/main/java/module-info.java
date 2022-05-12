module fr.solostaran.filename_analysis {
	requires javafx.controls;
	requires javafx.fxml;

	requires org.controlsfx.controls;

	opens fr.solostaran.filename_analysis to javafx.fxml;
	exports fr.solostaran.filename_analysis;
}
