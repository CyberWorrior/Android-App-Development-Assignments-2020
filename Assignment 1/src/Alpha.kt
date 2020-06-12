import java.lang.Integer.parseInt

//Named My Robot As Alpha
open class Alpha {

    val Sunday = false;//Holiday
    val Monday = arrayOf("Micro Controller","Operating Systems")
    val Tuesday = arrayOf("Physics","Chemistry","Maths")
    val Wednesday = arrayOf("Operating Systems","Chemistry","Maths")
    val Thursday = arrayOf("Design And Analysis","Data Communication")
    val Friday  = arrayOf("Designing Lab")
    val Saturday = arrayOf("Physics","Maths","Maths")
    var countdays:Int= 0
    fun ringTheAlarmOnTime(days:Array<String>, hour:Int, min:Int ,currentDay:String):Boolean{
        for(x in days) {
            if(x == currentDay){
                break
            }
            countdays++
        }
            println("Alarm Rings on ${days[countdays]}")
            println("Time ${hour}:${min}")
            print("Wake Up")
            println(" ")
            println(" ")
        return days[countdays] == currentDay
    }

    open fun makeCoffee(){
        println("preparing Coffee")
        var c = Coffee()
        c.makesCoffee()
    }

    fun heatWater(Temp:Int, days: Array<String>){
        println("Heating Water")
        //Bathing EveryDay Is important
        println("Water is Heating at ${Temp} deg Celsius")
    }

    fun packBag(days:Array<String>){
        println("Packing Bags According to timetable for ${days[countdays]}")
        when(countdays){
            0 -> println("Today is holiday, ENJOY!")
            1 -> for (x in Monday)
                print("$x ")
            2 -> for (x in Tuesday)
                print("$x ")
            3 -> for (x in Wednesday)
                print("$x ")
            4 -> for (x in Thursday)
                print("$x ")
            5 -> for (x in Friday)
                print("$x ")
            6 -> for (x in Saturday)
                print("$x ")
        }
    }

    fun breakFastLunch(){
        println("\nBreakfast")
        var b = Breakfast();
        println("Do you want to choose breakfast and lunch \n1.Manully \n2.Auto")
        var ch:Int = parseInt(readLine());
        when(ch){
            1 -> b.Manually()
            2 -> b.randomChoice()
        }

    }

    fun ironClothes(){
        println("Iron the clothes")
    }
}