package io.cruvelo.operaty.db

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.jetbrains.exposed.sql.transactions.experimental.withSuspendTransaction

interface TransactionalRunner {

	suspend fun <T> transaction(
        isolation: Int? = null,
        readOnly: Boolean = false,
        block: suspend () -> T,
    ): T
}

open class ExposedTransactionalRunner(
    private val database: Database,
) : TransactionalRunner {

	override suspend fun <T> transaction(isolation: Int?, readOnly: Boolean, block: suspend () -> T): T {
		val current: Transaction? = TransactionManager.currentOrNull()
		return if (current != null) {
			current.withSuspendTransaction { block() }
		} else {
			suspendedTransactionAsync(
				Dispatchers.IO,
				db = database,
				transactionIsolation = isolation
			) { block() }.await()
		}
	}
}
