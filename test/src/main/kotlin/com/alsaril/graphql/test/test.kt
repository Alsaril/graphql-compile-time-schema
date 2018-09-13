package com.alsaril.graphql.test

import com.alsaril.graphql.annotations.GraphQLRoot
import com.alsaril.graphql.annotations.createSchema
import graphql.schema.GraphQLObjectType

class Point(val x: Double, val y: Double)

@GraphQLRoot
class Area(val p1: Point, val p2: Point)

fun main(args: Array<String>) {
    val schema = createSchema(Area::class.java)
    println(schema)
}