package pl.edu.agh.infrastructure

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, FileInputStream}

import javax.crypto.spec.SecretKeySpec
import javax.crypto.{Cipher, CipherOutputStream}

object FileEncryptor {

  def encrypt(file: String, password: String): String = {

    val fis = new ByteArrayInputStream(file.getBytes)
    // This stream write the encrypted text. This stream will be wrapped by another stream.
    val fos = new ByteArrayOutputStream()

    val sks = new SecretKeySpec(password.getBytes(), "AES")
    // Create cipher
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.ENCRYPT_MODE, sks)
    // Wrap the output stream
    val cos = new CipherOutputStream(fos, cipher)
    // Write bytes
    val d = new Array[Byte](8)
    var b = fis.read(d)
    while (b != -1) {
      cos.write(d, 0, b)
      b = fis.read(d)
    }

    cos.flush()
    cos.close()
    fis.close()

    new String(fos.toByteArray)
  }

}
