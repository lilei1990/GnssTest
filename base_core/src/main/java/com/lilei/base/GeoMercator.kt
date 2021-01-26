package com.lilei.base

fun main() {
//    116.397564,39.90694
//    var a0 = latLng2WebMercator(114.32894, 30.585748)
//    var a00 = latLng2WebMercator2(114.32894, 30.585748)
//    var a000 = webMercator2LngLat(114.32894, 30.585748)
    var a1 = webMercator2LngLat(116.397564, 39.90694)
    var a2 = webMercator2LngLat(121.955074, 30.897295)
    val x = Math.abs(a1[0]*1000000 - a2[0]*1000000)
    val y = Math.abs(a1[1]*1000000 - a2[1]*1000000)
    println("[$x,$y]")
    val sqrt = Math.sqrt(x * x + y * y)
    println(sqrt)
}

//方法一
fun latLng2WebMercator(lng: Double, lat: Double): ArrayList<Double> {
    var earthRad = 6378137.0;
    var x = lng * Math.PI / 180 * earthRad;
    var a = lat * Math.PI / 180;
    var y = earthRad / 2 * Math.log((1.0 + Math.sin(a)) / (1.0 - Math.sin(a)));
//    println("[$x,$y]")
    val arrayListOf = arrayListOf<Double>()
    arrayListOf.add(x)
    arrayListOf.add(y)

    return arrayListOf
    //    [x, y]; //[12727039.383734727, 3579066.6894065146]
}

//方法一
fun latLng2WebMercator2 (lng: Double, lat: Double): ArrayList<Double> {
    var x = lng *20037508.34/180;
    var y = Math.log(Math.tan((90+lat)*Math.PI/360))/(Math.PI/180);
    y = y *20037508.34/180;
//    println("[$x,$y]")
    val arrayListOf = arrayListOf<Double>()
    arrayListOf.add(x)
    arrayListOf.add(y)

    return arrayListOf
    //    [x, y]; //[12727039.383734727, 3579066.6894065146]
}

//方法一
fun webMercator2LngLat (lng: Double, lat: Double): ArrayList<Double> {
    var x = lng / 20037508.34 * 180;
    var y = lat / 20037508.34 * 180;
    y = 180 / Math.PI * (2 * Math.atan(Math.exp(y * Math.PI / 180)) - Math.PI / 2);
//    println("[$x,$y]")
    val arrayListOf = arrayListOf<Double>()
    arrayListOf.add(x)
    arrayListOf.add(y)

    return arrayListOf
    //    [x, y]; //[12727039.383734727, 3579066.6894065146]
}
class GeoMercator {



}