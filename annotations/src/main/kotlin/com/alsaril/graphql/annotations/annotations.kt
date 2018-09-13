package com.alsaril.graphql.annotations

import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchema
import java.lang.IllegalStateException
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GraphQLRoot(
        val name: String = "",
        val description: String = "",
        val implements: Array<KClass<*>> = []
)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GraphQLInterface(
        val name: String = "",
        val description: String = "",
        val implements: Array<KClass<*>> = []
)

fun createSchema(klass: Class<out Any>): GraphQLSchema {
    val clazz = Class.forName("com.alsaril.graphql.generated.Generated$${klass.simpleName}")
    val field = clazz.declaredFields.firstOrNull { it.name == "TYPE" } ?: throw IllegalStateException()
    return GraphQLSchema.newSchema().query(field.get(null) as GraphQLObjectType).build()
}


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class GraphQLField(
        val name: String = "",
        val description: String = "",
        val nullable: Boolean = false
)
/*
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@MustBeDocumented
@Retention
annotation class GraphQLListField(
        val name: String = "",
        val description: String = "",
        val nullable: Boolean = false,
        val depth: Int = 1,
        val type: KClass<*>
)

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@MustBeDocumented
@Retention
annotation class S(
        val name: String = "",
        val description: String = "",
        val nullable: Boolean = false,
        val depth: Int = 1,
        val type: KClass<*>
)

@Target(AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
@Retention
annotation class GraphQLArgument(
        val name: String,
        val description: String = "",
        val nullable: Boolean = false
)

@Target(AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
@Retention
annotation class GraphQLListArgument(
        val name: String,
        val description: String = "",
        val nullable: Boolean = false,
        val depth: Int = 1,
        val type: KClass<*>
)

@Target(AnnotationTarget.CONSTRUCTOR)
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class GraphQLInputObject(
        val name: String = "",
        val description: String = ""
)

@Target(AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
@Retention
annotation class GraphQLInputField(
        val name: String,
        val description: String = "",
        val nullable: Boolean = false,
        val of: KClass<*> = Void::class,
        val ofNullable: Boolean = false
)

@Target(AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
@Retention
annotation class GraphQLContext

@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
@Retention
annotation class GraphQLUnion(
        val name: String = "",
        val description: String = "",
        val nullable: Boolean = false,
        val types: Array<KClass<out Any>> = []
*/
