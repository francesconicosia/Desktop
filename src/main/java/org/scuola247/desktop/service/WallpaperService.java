package org.scuola247.desktop.service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import ch.ralscha.extdirectspring.annotation.ExtDirectMethodType;

import com.google.common.collect.ImmutableList;

@Service
public class WallpaperService {

	private ImmutableList<Wallpaper> wallpapers;

	@Autowired
    private ServletContext servletContext;
	
	@PostConstruct
	private void init() {
		Yaml y = new Yaml();
		ImmutableList.Builder<Wallpaper> builder = ImmutableList.builder();
		builder.add(new Wallpaper("blank", null, null, null));
		try (InputStream is = getClass().getResourceAsStream("/wallpapers.yaml")) {
			for (String wp : (List<String>) y.load(is)) {
				String[] splitted = wp.split(",");
				builder.add(new Wallpaper(splitted[0], servletContext.getContextPath() + splitted[1], Integer.valueOf(splitted[2]), Integer
						.valueOf(splitted[3])));
			}
		} catch (IOException e) {
			LoggerFactory.getLogger(MethodHandles.lookup().lookupClass()).error("reading wallpapers.yaml", e);
		}
		wallpapers = builder.build();
	}

	@ExtDirectMethod(value = ExtDirectMethodType.STORE_READ)
	public ImmutableList<Wallpaper> read() {
		return wallpapers;
	}
}
