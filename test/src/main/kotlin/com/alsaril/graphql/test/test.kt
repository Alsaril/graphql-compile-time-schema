package com.alsaril.graphql.test

import com.alsaril.graphql.annotations.GraphQLField
import com.alsaril.graphql.annotations.GraphQLRoot
import com.alsaril.graphql.annotations.createSchema
import graphql.GraphQL

@GraphQLRoot
class Query(@Transient val address: Address) {
    @GraphQLField
    fun address() = address
}

class Address(
        val id: Long = 0,
        val lat: Double,
        val lon: Double,
        val description: String,
        val metros: List<List<AddressMetro>>
)

class AddressMetro(
        val id: Long = 0,
        val address: Address,
        val station: String,
        val line: String,
        val color: String,
        val distance: Double
)

fun main(args: Array<String>) {
    val schema = createSchema(Query::class.java)
    println(schema)

    val graphQL = GraphQL.newGraphQL(schema).build()
    val result = graphQL.execute("query { address { id } }", Query(Address(10, 0.6, 0.4, "sefe", emptyList())))
    println(result)

}