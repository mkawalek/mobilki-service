package pl.edu.agh.infrastructure

import java.math.BigInteger


object FileEncryptor {

  def encrypt(number: Long): String = {
    var n = new BigInteger(number.toString)

    var result = BigInteger.ONE

    while ( {
      !n.equals(BigInteger.ZERO)
    }) {
      result = result.multiply(n)
      n = n.subtract(BigInteger.ONE)
    }

    new String(result.toString())
  }

}
