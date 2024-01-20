package dev.rdh.minesweeper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config implements Serializable {

	@Serial
	private static final long serialVersionUID = -2394573435L;

	private static final Path CONFIG_PATH = Path.of(System.getProperty("user.home")).resolve(".rdh").resolve("minesweeper").resolve("config.ser");

	public boolean useWASD = true;
	public char chord = 'c';
	public char flag = 'f';
	public char reveal = ' ';
	public char reset = 'r';

	public static Config load() {
		if(!CONFIG_PATH.toFile().exists()) {
			return new Config();
		}

		try(ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(CONFIG_PATH))) {
			return (Config) stream.readObject();
		} catch(IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public void save() {
		try {
			Files.createDirectories(CONFIG_PATH.getParent());
			Files.deleteIfExists(CONFIG_PATH);
			Files.createFile(CONFIG_PATH);
			try(ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(CONFIG_PATH))) {
				out.writeObject(this);
			}
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
}
