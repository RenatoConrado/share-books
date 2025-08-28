package com.renatoconrado.share_books.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class UriUtil {

    public static URI buildUri(String path, Object... variables) {
        return ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path(path)
            .buildAndExpand(variables)
            .toUri();
    }

}
