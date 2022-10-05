package com.example.kotlinzaklad

fun main(){
    //string
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
}

fun funkce(a: Int, b: Int) : Int{
    return a+b
}

class Person (firstName: String, lastName: String){

    init {
        println("Person created: $firstName $lastName")
    }
}

//TODO: 3:54:00