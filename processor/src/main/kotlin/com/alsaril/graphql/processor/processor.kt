package com.alsaril.graphql.processor

import com.alsaril.graphql.annotations.GraphQLRoot
import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.tools.Diagnostic

@AutoService(Processor::class)
class ListTypeAnnotationProcessor : AbstractProcessor() {
    private fun print(a: Any) {
        processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, a.toString())
    }

    fun gType(type: TypeMirror) = when {
        type.kind == TypeKind.BOOLEAN -> "Scalars.GraphQLBoolean"
        type.kind == TypeKind.INT -> "Scalars.GraphQLInt"
        type.kind == TypeKind.DOUBLE -> "Scalars.GraphQLFloat"
        else -> null
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)

        processingEnv.filer.createSourceFile("com.alsaril.graphql.generated.G\$H").openWriter().use {
            it.write("package com.alsaril.graphql.generated;\n\n")
            it.write("import graphql.schema.*;\n")
            it.write("import graphql.*;\n\n")
            it.write("public class G\$H {\n")
            it.write("    public static GraphQLFieldDefinition.Builder bF(String name, GraphQLOutputType type, DataFetcher<?> dataFetcher) {\n")
            it.write("        return GraphQLFieldDefinition.newFieldDefinition().name(name).type(type).dataFetcher(dataFetcher);\n")
            it.write("    }\n")
            it.write("}")
        }
    }

    private val map = mutableMapOf<Element, String>()

    private fun generateObject(element: Element): String {
        return map[element] ?: run {
            val name = element.simpleName
            val file = processingEnv.filer.createSourceFile("com.alsaril.graphql.generated.Generated$$name")
            file.openWriter().use {
                it.write("package com.alsaril.graphql.generated;\n\n")
                it.write("import graphql.schema.*;\n")
                it.write("import graphql.*;\n")
                it.write("import $element;\n\n")

                it.write("public class Generated$$name {\n")
                it.write("    public static final GraphQLObjectType TYPE = ")
                it.write("GraphQLObjectType\n            .newObject()\n            .name(\"${element.simpleName}\")\n")
                (element.asType() as DeclaredType).asElement().enclosedElements.forEach { el ->
                    if (el.simpleName.startsWith("get") && el.kind == ElementKind.METHOD) {
                        val method = el as ExecutableElement
                        val fieldName = el.simpleName.substring(3).let { it[0].toLowerCase() + it.substring(1) }
                        val type = method.returnType
                        val gType = gType(type) ?: generateObject((type as DeclaredType).asElement())
                        it.write("            .field(G\$H.bF(\"$fieldName\", $gType, env -> ((${element.simpleName}) env.getSource()).${el.simpleName}()))\n")
                    }
                }
                it.write("            .build();\n")
                it.write("}")
            }
            map[element] = "Generated\$$name.TYPE"
            "Generated\$$name.TYPE"
        }
    }

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        roundEnv.getElementsAnnotatedWith(GraphQLRoot::class.java).forEach {
            generateObject(it)
        }
        return true
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(GraphQLRoot::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }
}