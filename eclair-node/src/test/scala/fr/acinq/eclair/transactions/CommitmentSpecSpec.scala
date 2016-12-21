package fr.acinq.eclair.transactions

import fr.acinq.bitcoin.Crypto
import fr.acinq.eclair.wire.{UpdateAddHtlc, UpdateFailHtlc, UpdateFulfillHtlc}
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CommitmentSpecSpec extends FunSuite {
  test("add, fulfill and fail htlcs") {
    val spec = CommitmentSpec(Set(), 1000, 2000 * 1000, 0)
    val R1 = Crypto.sha256("foo".getBytes())
    val H1 = Crypto.sha256(R1)
    val R2 = Crypto.sha256("bar".getBytes())
    val H2 = Crypto.sha256(R2)

    val ours1 = UpdateAddHtlc(0, 1, 1000, 400, H1, "")
    val spec1 = CommitmentSpec.reduce(spec, ours1 :: Nil, Nil)
    assert(spec1.htlcs.size == 1 && spec1.htlcs.head.add.id == 1 && spec1.htlcs.head.add.paymentHash == H1)
    assert(spec1.toLocal == spec.toLocal - ours1.amountMsat)
    assert(spec1.toRemote == spec.toRemote)
    assert(spec1.totalFunds == spec.totalFunds)

    val theirs1 = UpdateFulfillHtlc(0, ours1.id, R1)
    val spec2 = CommitmentSpec.reduce(spec1, Nil, theirs1 :: Nil)
    assert(spec2.htlcs.isEmpty && spec2.toRemote == 1000 && spec2.totalFunds == spec.totalFunds)

    val theirs2 = UpdateAddHtlc(0, 2, 1000, 400, H2, "")
    val spec3 = CommitmentSpec.reduce(spec2, Nil, theirs2 :: Nil)
    assert(spec3.htlcs.size == 1)
    assert(spec3.toLocal == spec2.toLocal)
    assert(spec3.toRemote == spec2.toRemote - theirs2.amountMsat)
    assert(spec3.totalFunds == spec.totalFunds)

    val ours2 = UpdateFailHtlc(0, theirs2.id, "")
    val spec4 = CommitmentSpec.reduce(spec3, ours2 :: Nil, Nil)
    assert(spec4 == spec2)
  }
}