package dromard.generator;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		new TestGenerator().generateCode();
	}
	/*
	public static void main(String[] args) throws IOException {
		PHProject project = new PHProject();
		project.setGenerateIn(new File("TestGenerationProject").getAbsolutePath());
		project.setName("TestGenerationProject");
		project.setOveridable(true);
		project.setOverrideConfig(true);
		project.setOverrideDefaultResource(true);
		project.setPath("test");
		project.setPkg("test");
		new ProjectGenerator(project).generateCode();
	}
	*/
}
