package util

import java.security.MessageDigest

import org.apache.commons.codec.binary.Hex

object StringUtils {
  def sha256(value: String): String = {
    val md = MessageDigest.getInstance("SHA-256")
    md.update(value.getBytes("Shift_JIS"))
    Hex.encodeHexString(md.digest)
  }
}
