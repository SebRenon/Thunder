/*
 *  Adaptation of Jake Wharton's Interceptor that you can find here:
 *  https://gist.github.com/JakeWharton/f26f19732f0c5907e1ab#file-oauth1signinginterceptor-java
 *
 *  Some of the changes concern:
 *      - deletion of some fields
 *      - check content-type of the request if === "application/x-www-form-urlencoded" before adding them to the parameters to include them to the signature
 *      - null check for body
 *
 */

package com.srenon.thunder.sdk.network;

import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.ByteString;

final class OauthInterceptor implements Interceptor {

    private static final Escaper ESCAPER = UrlEscapers.urlFormParameterEscaper();

    private static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";

    private static final String OAUTH_NONCE = "oauth_nonce";

    private static final String OAUTH_SIGNATURE = "oauth_signature";

    private static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";

    private static final String OAUTH_SIGNATURE_METHOD_VALUE = "HMAC-SHA1";

    private static final String OAUTH_TIMESTAMP = "oauth_timestamp";

    private static final String OAUTH_VERSION = "oauth_version";

    private static final String OAUTH_VERSION_VALUE = "1.0";

    private final String consumerKey;

    private final String consumerSecret;

    private final Random random;

    private OauthInterceptor(String consumerKey, String consumerSecret, Random random) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.random = random;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(signRequest(chain.request()));
    }

    private Request signRequest(Request request) throws IOException {
        byte[] nonce = new byte[32];
        random.nextBytes(nonce);
        String oauthNonce = ByteString.of(nonce).base64().replaceAll("\\W", "");
        String oauthTimestamp = String.valueOf(System.currentTimeMillis());

        String consumerKeyValue = ESCAPER.escape(consumerKey);

        SortedMap<String, String> parameters = new TreeMap<>();
        parameters.put(OAUTH_CONSUMER_KEY, consumerKeyValue);
        parameters.put(OAUTH_NONCE, oauthNonce);
        parameters.put(OAUTH_TIMESTAMP, oauthTimestamp);
        parameters.put(OAUTH_SIGNATURE_METHOD, OAUTH_SIGNATURE_METHOD_VALUE);
        parameters.put(OAUTH_VERSION, OAUTH_VERSION_VALUE);

        HttpUrl url = request.url();
        for (int i = 0; i < url.querySize(); i++) {
            parameters.put(ESCAPER.escape(url.queryParameterName(i)),
                    ESCAPER.escape(url.queryParameterValue(i)));
        }

        String contentType = request.headers().get("content-type");
        if (contentType != null && "application/x-www-form-urlencoded".equals(contentType)) {

            // only add body content to signature if specific content-type
            Buffer body = new Buffer();
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                requestBody.writeTo(body);
            }

            while (!body.exhausted()) {
                long keyEnd = body.toString().indexOf('=');
                if (keyEnd == -1) {
                    throw new IllegalStateException("Key with no value: " + body.readUtf8());
                }
                String key = body.readUtf8(keyEnd);
                body.skip(1); // Equals.

                long valueEnd = body.toString().indexOf('&');
                String value = valueEnd == -1 ? body.readUtf8() : body.readUtf8(valueEnd);
                if (valueEnd != -1) {
                    body.skip(1); // Ampersand.
                }

                parameters.put(key, value);
            }
        }

        Buffer base = new Buffer();
        String method = request.method();
        base.writeUtf8(method);
        base.writeByte('&');
        base.writeUtf8(ESCAPER.escape(request.url().newBuilder().query(null).build().toString()));
        base.writeByte('&');

        boolean first = true;
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (!first) {
                base.writeUtf8(ESCAPER.escape("&"));
            }
            first = false;
            base.writeUtf8(ESCAPER.escape(entry.getKey()));
            base.writeUtf8(ESCAPER.escape("="));
            base.writeUtf8(ESCAPER.escape(entry.getValue()));
        }

        String signingKey =
                ESCAPER.escape(consumerSecret) + "&";

        SecretKeySpec keySpec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA1");
            mac.init(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException(e);
        }
        byte[] result = mac.doFinal(base.readByteArray());
        String signature = ByteString.of(result).base64();

        String authorization = "OAuth "
                + OAUTH_CONSUMER_KEY + "=\"" + consumerKeyValue + "\", "
                + OAUTH_NONCE + "=\"" + oauthNonce + "\", "
                + OAUTH_SIGNATURE + "=\"" + ESCAPER.escape(signature) + "\", "
                + OAUTH_SIGNATURE_METHOD + "=\"" + OAUTH_SIGNATURE_METHOD_VALUE + "\", "
                + OAUTH_TIMESTAMP + "=\"" + oauthTimestamp + "\", "
                + OAUTH_VERSION + "=\"" + OAUTH_VERSION_VALUE + "\"";

        return request.newBuilder()
                .addHeader("Authorization", authorization)
                .build();
    }

    static final class Builder {

        private String consumerKey;

        private String consumerSecret;

        private Random random = new SecureRandom();

        Builder consumerKey(String consumerKey) {
            if (consumerKey == null) {
                throw new NullPointerException("consumerKey = null");
            }
            this.consumerKey = consumerKey;
            return this;
        }

        Builder consumerSecret(String consumerSecret) {
            if (consumerSecret == null) {
                throw new NullPointerException("consumerSecret = null");
            }
            this.consumerSecret = consumerSecret;
            return this;
        }

        public OauthInterceptor build() {
            if (consumerKey == null) {
                throw new IllegalStateException("consumerKey not set");
            }
            if (consumerSecret == null) {
                throw new IllegalStateException("consumerSecret not set");
            }
            return new OauthInterceptor(consumerKey, consumerSecret, random);
        }
    }
}