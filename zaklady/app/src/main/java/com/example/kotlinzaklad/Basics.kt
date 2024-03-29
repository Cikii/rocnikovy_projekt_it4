package com.example.kotlinzaklad

import kotlinx.coroutines.flow.callbackFlow
data class User(val id: Long, val name: String)

//-------------
//TODO: 5:39:36
//-------------

fun main() {

    val numbers = arrayOf(1,2,3,4,5,6) //IntArray -- typ promenne
    println(numbers.contentToString())  // array by ukazalo jen lokaci v pameti
    for (element in numbers){
        println(element)
    }
    println("--------")
    println(numbers[0])  // pozice v arrayi []
    numbers[0] = 6
    println("zmenene cislo na pozici 0 v arrayi: ${numbers[0]} ")
    println("upraveny array: ${numbers.contentToString()} ")
    println("--------")

    val months = listOf("January", "February")
    val m1 = months.toMutableList()   // jinak nelze do listof pridavat -> musim z nej udelat mutablelist (array)
    val monthsN = arrayOf("March","April", "May", "June")
    m1.addAll(monthsN)
    //m1.removeAt(1)  // smaze na indexu 1
    println(m1)
    println("--------")

    val daysOfTheWeek = mapOf(1 to "Monday", 2 to "Tuesday", 3 to "Wendesday")
    println(daysOfTheWeek.toSortedMap()) //seradi abecedne

    /*  val stringList: List<String> = listOf("dsdf", "sfsdf", "dfsdfs")

    //string
    val user1 = User(1,"Martin")

    val name = user1.name
    println(name)
    println("\n")


    var myName = "Viktor"


    var myAge = 18
    var bub = 16

    var jes = true


    println("Hello $myName your name has ${myName.length} characters")

    val isEqual = 5>3
    println("isEqual is $isEqual")
    println("is 5 greater than 3 --> ${5>3}")

    var season = 3
    when(season){
        1 -> println("Spring")
        2 -> println("Summer")
        3 ->{
            println("Fall")
            println("Autumn")
        }
        4 -> println("Winter")
        else -> println("Invalid season")
    }
    println("\n")
    var month = 3
    when(month){
        in 3..5 -> println("Spring")
        in 6..8 -> println("Summer")
        in 9..11 -> println("Fall")
        12,1,2 -> println("Winter")

    }


    var x : Any = 13.37
    when(x){
        is Int -> println("$x is an Int")
        else -> println("$x is not an Int")
    }


    var cislo = 1
    while (cislo <= 10){
        println("${cislo++}")
    }

    println("\n-------------")

   for(num in 1..10){
       println("$num")
   }
    println("\n-------------")

   for(n in 1 until 10){
       print("$n ")
   }
    println("\n-------------")

   for(m in 10 downTo 1 step 2){
       println("$m")
   }
    println("\n-------------")


    funkce(3,1)
    println("${funkce(3,1)}")

    var cl = Person("Viktor", "Chudoba")
    println("\n-------------")

    var myCar = Car()
    println(myCar.myBrand)
    myCar.maxSpeed=350
    println(myCar.maxSpeed)

}

fun funkce(a: Int, b: Int) : Int{
    return a+b
}

class Person (firstName: String, lastName: String){

    init {
        println("Person created: $firstName $lastName")
    }
}

class Car(){
    lateinit var owner : String
    val myBrand: String = "BMW"
    get(){
        return field.lowercase()
    }
    var maxSpeed:Int=250
    set(value) {
        field = if (value>0) value else throw IllegalArgumentException("Max speed cannot be less than 0") //pokud je na radku 82 nastavena 0 nebo mensi
    }
    var myModel : String = "MS"
        private set // nemuze se prepsat jinde nez uvnitr class

    init {
        this.owner = "Frank"

    }
*/
}



