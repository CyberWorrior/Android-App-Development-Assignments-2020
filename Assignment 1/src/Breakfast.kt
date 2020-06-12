
import java.lang.Integer.parseInt
class Breakfast():Alpha() {
    var LikesBreakFast = arrayListOf<String>("Idli Chutney","Masala Dosa","Soda","Poori","Pallav","Rice Bath")
    var LikesLunch = arrayListOf<String>("Kolhapuri Vegetables","Low Fat Dahi Chicken","Butter Chicken Masala","Paneer Achaari","Egg Fried Rice","Normal Rice")
    var choiceBreakFast:Int = 0
    var choiceLunch:Int = 0


    fun randomChoice(){
        println("Randomizing Breakfast and Lunch as per your wish")
        println(LikesBreakFast.random())
        println(LikesLunch.random())
    }

    fun Manually(){
        println("Enter your choice of Breakfast")
        var i=1;
        for(x in LikesBreakFast){
            println("${i}.${LikesBreakFast}")
            i++
        }
        choiceBreakFast = parseInt(readLine());
        println("Enter Choice of Lunch")
        var j=1;
        for (x in LikesLunch){
            println("${j}.${LikesLunch}")
            j++
        }
        choiceLunch = parseInt(readLine());
    }
}