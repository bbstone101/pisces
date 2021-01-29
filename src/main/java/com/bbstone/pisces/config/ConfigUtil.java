package com.bbstone.pisces.config;

import com.bbstone.pisces.util.BFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class ConfigUtil {

    private static final String CLASSPATH_PREFIX = "classpath:";

    public static File readClasspathFile(String path) {
        File file = null;
        if (StringUtils.isNotBlank(path) && path.startsWith(CLASSPATH_PREFIX)) {
            String classpath = path.substring(CLASSPATH_PREFIX.length());
            classpath = BFileUtil.convertToLocalePath(classpath);
            log.debug("path: {}, classpath: {}", path, classpath);
            URI uri = null;
            try {
                uri = Thread.currentThread().getContextClassLoader().getResource(classpath).toURI();
                log.debug("uri: {}", uri.toString());
            } catch (URISyntaxException e) {
                log.error("read classpath file error.", e);
            }
            file = new File(uri);
        }
        return file;
    }
}
