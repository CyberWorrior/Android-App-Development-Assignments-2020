import java.lang.Integer.parseInt

class Coffee():Alpha() {
    var sugar:Int = 0 //Specify number of spoons
    var typeOfCoffee = arrayOf<String>("Black With Milk", "Black Without Milk", "iced coffee", "Tea", "Vanilla with cream")
    //I am making a one time timetable input Here
    

    fun makesCoffee(){
        this.display()
        this.selectCoffee()
        this.Sugar()
    }

    fun display() {
        println("Menu For Coffee")
        var i=1
        for (x in typeOfCoffee) {
            println("${i}.${x}")
            i++
        }
    }

    fun selectCoffee(){
        var sel = parseInt(readLine())

        print("Selected Coffee is: ${this.typeOfCoffee[sel-1]}")
    }

    fun Sugar(){
        print("\nEnter the Amount of Sugar in TableSpoons: ")
        this.sugar = parseInt(readLine())
    }
}
