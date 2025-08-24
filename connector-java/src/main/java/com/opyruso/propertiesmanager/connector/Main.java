package com.opyruso.propertiesmanager.connector;
import java.io.File;

import jakarta.inject.Inject;

import com.opyruso.propertiesmanager.connector.constants.CommandEnum;
import com.opyruso.propertiesmanager.connector.services.UpdatePropertiesCommandService;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main implements QuarkusApplication {
	
	@Inject
	protected UpdatePropertiesCommandService updatePropertiesCommandService;
	
	public static void main(String[] args) {
		Quarkus.run(Main.class, args);
	}

    @Override
    public int run(String... args) throws Exception {
        System.out.println("Start");
        try {
			String commandAsString = args[0];
			CommandEnum command = CommandEnum.valueOf(commandAsString.toUpperCase());
        	switch (command) {
			case UPDATE:
				String projectId = args[1];
				String version = args[2];
				String env = args[3];
				File file = new File(args[4]);
				String filenameFilter = ".*";
				if (args.length >= 6 && args[5] != null) {
					filenameFilter = args[5];
				}
				System.out.println("projectId: " + projectId);
				System.out.println("version: " + version);
				System.out.println("env: " + env);
				System.out.println("starting filesPath: " + file.getAbsolutePath());
				updatePropertiesCommandService.process(projectId, version, env, file, filenameFilter);
				break;
			}
			Quarkus.asyncExit(0);
            System.out.println("End");
		} catch (Exception e) {
			Quarkus.asyncExit(99);
            System.out.println("error");
            e.printStackTrace();
		}
        Quarkus.asyncExit(0);
        Quarkus.waitForExit();
		return 0;
    }
}