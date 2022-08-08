package parking

// this class will contain information about the parking spot
data class ParkingSpace(var isFree: Boolean = true, var car: Car? = null)