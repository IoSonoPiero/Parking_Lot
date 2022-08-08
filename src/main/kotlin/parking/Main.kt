package parking

// create empty parking
var parkingLot = mutableListOf<ParkingSpace>()

fun main() {

    while (true) {
        // get command string
        val command: List<String> = readln().split(" ")

        // parse the first element
        when (command[0].lowercase()) {
            // create the parking
            "create" -> createParking(command)

            // park the car
            "park" -> parkCar(command)

            // leave from parking
            "leave" -> leaveCar(command)

            // check parking status
            "status" -> parkingStatus()

            // search into parking for specific cars
            "reg_by_color", "spot_by_color", "spot_by_reg" -> printCarInfo(command)

            // exit program
            "exit" -> break
        }
    }
}

fun printCarInfo(command: List<String>) {
    // check if parking has been created
    if (parkingLot.isEmpty()) {
        println("Sorry, a parking lot has not been created.")
        return
    }

    // get the command and the parameter to start search cars
    val searchFor = command[0]
    val searchParameter = command[1]

    // contains a string identifying the result type of search "color" or "registration number"
    var kindOfSearch: String? = null

    // contains the search results
    val searchResults = mutableListOf<String>()

    // check if something has been found
    var isFound = false

    // iterate all spots and print info about the cars found
    for (index in parkingLot.indices) {
        // check if the spot is not free
        if (!parkingLot[index].isFree) {

            when (searchFor) {
                "reg_by_color" -> {
                    kindOfSearch = "color"
                    if (parkingLot[index].car?.color?.uppercase() == searchParameter.uppercase()) {
                        searchResults.add(parkingLot[index].car?.licensePlate.toString())
                        isFound = true
                    }
                }

                "spot_by_color" -> {
                    kindOfSearch = "color"
                    if (parkingLot[index].car?.color?.uppercase() == searchParameter.uppercase()) {
                        searchResults.add((index + 1).toString())
                        isFound = true
                    }
                }

                "spot_by_reg" -> {
                    kindOfSearch = "registration number"
                    if (parkingLot[index].car?.licensePlate?.uppercase() == searchParameter.uppercase()) {
                        searchResults.add((index + 1).toString())
                        isFound = true
                    }
                }
            }
        }
    }

    // the parking is empty
    if (!isFound) {
        println("No cars with $kindOfSearch $searchParameter were found.")
    } else {
        // print results
        println(searchResults.joinToString())
    }
}

fun parkingStatus() {
    // check if parking has been created
    if (parkingLot.isEmpty()) {
        println("Sorry, a parking lot has not been created.")
        return
    }

    // track if the parking is empty
    var isEmpty = true

    // iterate for each spot and print ONLY used spot
    for (index in parkingLot.indices) {
        // check if the spot is not free
        if (!parkingLot[index].isFree) {
            isEmpty = false
            // print data about the car in the spot
            println("${index + 1} ${parkingLot[index].car?.licensePlate} ${parkingLot[index].car?.color}")
        }
    }

    // the parking is empty
    if (isEmpty) {
        println("Parking lot is empty.")
    }
}

fun createParking(command: List<String>) {
    // get the number of spots
    val elements: Int = command[1].toInt()

    // create a parking of n elements
    parkingLot = MutableList(elements) { ParkingSpace() }

    // remove this line
    println("Created a parking lot with $elements spots.")
}

// park the car
fun parkCar(command: List<String>) {
    // check if parking has been created
    if (isParkingCreated()) {

        // find a free spot
        val freeSpot = findFreeSpot(parkingLot)

        // if no free spots print a message
        if (freeSpot == -1) {
            println("Sorry, the parking lot is full.")
        } else {
            // get the license plate of the car
            val licensePlate: String = command[1]

            // get the color of the car
            val color = command[2]

            // create a new car
            parkingLot[freeSpot].car = Car(licensePlate, color)

            // mark the spot as not free
            parkingLot[freeSpot].isFree = false

            println("$color car parked in spot ${freeSpot + 1}.")
        }
    } else {
        println("Sorry, a parking lot has not been created.")
    }
}

fun findFreeSpot(parkingLot: MutableList<ParkingSpace>): Int {
    // iterates into parking lots
    for (index in parkingLot.indices) {
        // check if the spot is free
        if (parkingLot[index].isFree) {
            // return the index of the free spot
            return index
        }
    }
    // no spot free
    return -1
}

// check if the parking has been created
fun isParkingCreated(): Boolean {
    return parkingLot.size != 0
}

// leave the car
fun leaveCar(command: List<String>) {
    // check if parking has been created
    if (isParkingCreated()) {

        // get the spot to check
        val spot: Int = command[1].toInt() - 1

        // check the spot
        if (!parkingLot[spot].isFree) {
            // no, it's not free, leave the car and mark the spot as free
            parkingLot[spot].isFree = true
            parkingLot[spot].car = null
            println("Spot ${spot + 1} is free.")
        } else {
            // the spot is free
            println("There is no car in spot ${spot + 1}.")
        }
    } else {
        println("Sorry, a parking lot has not been created.")
    }
}