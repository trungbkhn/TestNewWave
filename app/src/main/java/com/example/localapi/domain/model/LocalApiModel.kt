
import com.google.gson.annotations.SerializedName

data class LocalApiModel(
    @SerializedName("items")
    val items: List<Item?>?
) {
    data class Item(
        @SerializedName("access")
        val access: List<Position?>?,
        @SerializedName("address")
        val address: Address?,
        @SerializedName("houseNumberType")
        val houseNumberType: String?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("mapView")
        val mapView: MapView?,
        @SerializedName("position")
        val position: Position?,
        @SerializedName("resultType")
        val resultType: String?,
        @SerializedName("scoring")
        val scoring: Scoring?,
        @SerializedName("title")
        val title: String?
    ) {

        data class Address(
            @SerializedName("city")
            val city: String?,
            @SerializedName("countryCode")
            val countryCode: String?,
            @SerializedName("countryName")
            val countryName: String?,
            @SerializedName("county")
            val county: String?,
            @SerializedName("district")
            val district: String?,
            @SerializedName("houseNumber")
            val houseNumber: String?,
            @SerializedName("label")
            val label: String?,
            @SerializedName("postalCode")
            val postalCode: String?,
            @SerializedName("state")
            val state: String?,
            @SerializedName("stateCode")
            val stateCode: String?,
            @SerializedName("street")
            val street: String?
        )

        data class MapView(
            @SerializedName("east")
            val east: Double?,
            @SerializedName("north")
            val north: Double?,
            @SerializedName("south")
            val south: Double?,
            @SerializedName("west")
            val west: Double?
        )

        data class Position(
            @SerializedName("lat")
            val lat: Double?,
            @SerializedName("lng")
            val lng: Double?
        )

        data class Scoring(
            @SerializedName("fieldScore")
            val fieldScore: FieldScore?,
            @SerializedName("queryScore")
            val queryScore: Double?
        ) {
            data class FieldScore(
                @SerializedName("city")
                val city: Double?,
                @SerializedName("houseNumber")
                val houseNumber: Double?,
                @SerializedName("streets")
                val streets: List<Double?>?
            )
        }
    }
}