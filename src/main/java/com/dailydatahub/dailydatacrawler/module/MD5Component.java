package com.dailydatahub.dailydatacrawler.module;

import java.security.MessageDigest;

import org.springframework.stereotype.Service;

import jakarta.xml.bind.DatatypeConverter;

@Service
public class MD5Component {
    
    /**
     * convert plainText to MD5 Statement
     * @param plainText
     * @return
     * @throws Exception
     */
	public String md5AndHex(String plainText) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(plainText.getBytes());
		return DatatypeConverter.printHexBinary(md.digest());
	}
}
