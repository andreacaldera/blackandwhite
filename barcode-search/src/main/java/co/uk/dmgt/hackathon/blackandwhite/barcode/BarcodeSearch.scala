package co.uk.dmgt.hackathon.blackandwhite.barcode

import co.uk.dmgt.hackathon.blackandwhite.barcode.google.GoogleProductSearch

object BarcodeSearch extends Application {

  override def main(args: Array[String]) {
    if (args.isEmpty) println("welcome to the blackandwhite barcode search api, please provide a barcode number")
    else {
      // println("welcome to the blackandwhite barcode search api, looking for barcode " + args(0))
      val googleFeed = new GoogleProductSearch(args(0));
      println(googleFeed.getProductInformation());
    }
  }

}