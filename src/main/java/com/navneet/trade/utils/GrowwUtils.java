package com.navneet.trade.utils;

import com.navneet.trade.constants.GrowwConstants;
import com.navneet.trade.models.request.TokenRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author navneet.prabhakar
 */
@Component
public class GrowwUtils {

  @Autowired private GrowwConstants constants;
  private static final String SHA_256 = "SHA-256";
  private static final String APPROVAL = "approval";

  /**
   * Generates a TokenRequest object with the required fields.
   */
  public TokenRequest generateTokenRequest() {
    String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
    return TokenRequest.builder()
        .key_type(APPROVAL)
        .timestamp(timestamp)
        .checksum(generateChecksum(constants.getSecret(), timestamp))
        .build();
  }


  /**
   * Generates SHA-256 checksum for the given input string.
   */
  public static String generateChecksum(String secret, String timestamp) {
    try {
      String input = secret + timestamp;
      MessageDigest digest = MessageDigest.getInstance(SHA_256);
      byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(hash);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("SHA-256 algorithm not found", e);
    }
  }

}
