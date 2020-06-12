import java.lang.Integer.parseInt

fun main() {
    var person = Alpha()
    val days = arrayOf(
        "Sunday",
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday"
    ) //this is the days the alarm will ring
    print("Alram Hour: ")
    var hour: Int = parseInt(readLine()) //Alarm time in hours
    println("Alarm Min: ")
    val min: Int = parseInt(readLine()) // Alarm time in minutes
    val currentDay = "Saturday" //Considering current day starting day so that next day the alarm will ring next day
    var temp: Int

    if (person.ringTheAlarmOnTime(days, hour, min, currentDay)) {
        person.makeCoffee()
        print("Enter Temperature for bathing water")
        temp = parseInt(readLine())
        person.heatWater(temp, days)
        person.packBag(days)
        person.breakFastLunch()
        person.ironClothes()
    }

}