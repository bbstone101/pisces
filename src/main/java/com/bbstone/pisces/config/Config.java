package com.bbstone.pisces.config;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Config {

	private static Configurations configs = new Configurations();
	
	private static Configuration config() {
		try {
			Configuration config = configs.properties(new File("config.properties"));
			return config;
		} catch (ConfigurationException cex) {
			log.error("parse config.properties error", cex);
			return null;
		}
	}

	/** ssl enabled */
	public static boolean sslEnabled = config().getBoolean("ssl.enabled", false);
	public static int port = config().getInt("server.port", 8899);
	public static String host = config().getString("server.host", "0.0.0.0");
	

	public static String serverDir = config().getString("server.dir");
	public static String clientDir = config().getString("client.dir");

	public static String tempFilePostfix = config().getString("file.temp.postfix");

	public static String serverSSLRootFile = config().getString("server.ssl.root_file", "");
	public static String serverSSLKeyFile = config().getString("server.ssl.key_file", "");
	public static String serverSSLCertChainFile = config().getString("server.ssl.cert_chain_file", "");

	public static String clientSSLRootFile = config().getString("client.ssl.root_file", "");
	public static String clientSSLKeyFile = config().getString("client.ssl.key_file", "");
	public static String clientSSLCertChainFile = config().getString("client.ssl.cert_chain_file", "");


	

}
