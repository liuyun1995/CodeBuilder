package test;

import com.liuyun.builder.api.ShellRunner;

public class ShellRunnerTest {

	public static void main(String[] args) {
		String[] args1 = {"-configfile", "src/test/resources/CodeBuidler.xml"};
		ShellRunner.main(args1);
	}

}
