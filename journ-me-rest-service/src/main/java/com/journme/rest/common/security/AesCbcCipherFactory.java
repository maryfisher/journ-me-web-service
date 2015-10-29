/*
 * Jumio Inc.
 *
 * Copyright (C) 2010 - 2011
 * All rights reserved.
 */
package com.journme.rest.common.security;

import java.nio.charset.Charset;
import javax.crypto.Cipher;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class AesCbcCipherFactory extends BasePooledObjectFactory<Cipher> {

    public static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";

    public static final String KEYGEN_ALGORITHM = "AES";

    public static final Charset CHARSET = Charset.forName("UTF-8");

    @Override
    public Cipher create() throws Exception {
        return Cipher.getInstance(CIPHER_TRANSFORMATION);
    }

    @Override
    public PooledObject<Cipher> wrap(Cipher cipher) {
        return new DefaultPooledObject<Cipher>(cipher);
    }
}
