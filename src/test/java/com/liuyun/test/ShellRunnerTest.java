package com.liuyun.test;

import com.liuyun.builder.api.ShellRunner;

public class ShellRunnerTest {

	public static void main(String[] args) {
		
		String[] args1 = {"-configfile", "src/test/resources/MySqlConfig.xml"};
		String[] args2 = {"-configfile", "src/test/resources/OracleConfig.xml"};
		ShellRunner.main(args1);
		
	}

}
