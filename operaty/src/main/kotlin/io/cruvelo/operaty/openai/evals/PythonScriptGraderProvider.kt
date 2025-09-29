package io.cruvelo.operaty.openai.evals

fun interface PythonScriptGraderProvider {

	suspend fun provide(srcPath: String): String
}
