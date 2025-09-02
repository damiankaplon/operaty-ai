package io.cruvelo.operaty.openai.evals

object ResourcesPythonScriptGraderProvider : PythonScriptGraderProvider {

	override suspend fun provide(srcPath: String): String =
		this::class.java.classLoader.getResourceAsStream(srcPath)
			?.use { inputStream -> inputStream.buffered().readAllBytes() }
			?.toString(Charsets.UTF_8)
			?: error("Failed to python grader file")
}
