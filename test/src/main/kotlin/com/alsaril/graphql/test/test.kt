package com.alsaril.graphql.test

import com.alsaril.graphql.annotations.GraphQLRoot
import com.alsaril.graphql.annotations.createSchema

@GraphQLRoot
data class Address(
        val id: Long = 0,
        val lat: Double,
        val lon: Double,
        val description: String,
        val metros: List<List<AddressMetro?>?>
)

data class AddressMetro(
        val id: Long = 0,
        val address: Address,
        val station: String,
        val line: String,
        val color: String,
        val distance: Double
)


fun main(args: Array<String>) {
    val schema = createSchema(Address::class.java)
    println(schema)
}