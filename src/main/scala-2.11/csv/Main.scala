package csv

import java.io.{File, FileOutputStream, OutputStreamWriter, BufferedWriter}

import com.github.tototoshi.csv.{CSVWriter, CSVReader}
import util.StringUtils._

object Main {

  def main(args: Array[String]): Unit =
    if (args.size < 2 || args.size > 3) println("Invalid argument")
    else diff(args)


  def diff(args: Array[String]): Unit = {

    val pk: Int = if (args.size == 3) args(2).toInt else 0
    val (filePath0, filePath1) = (args(0), args(1))

    val file0 = CSVReader.open(filePath0)
    val file1 = CSVReader.open(filePath1)

    val pkHashs0 = file0.iterator.zipWithIndex
      .foldLeft(List[(String, Int)]()) { (acc, row) => (sha256(row._1(pk)), row._2):: acc }

    val pkHashs1 = file1.iterator.zipWithIndex
      .foldLeft(List[(String, Int)]()) { (acc, row) => (sha256(row._1(pk)), row._2):: acc }

    file0.close()
    file1.close()

    val diffIndex = (pkHashs0 diff pkHashs1).map(_._2)

    val input = CSVReader.open(filePath0)
    val writer = CSVWriter.open("diff.csv", append = true)

    input.iterator.zipWithIndex.foreach { case (row, index) if diffIndex contains index =>
      writer.writeRow(row)
    }

    input.close()
    writer.close()

    println(s"Diff lines: ${diffIndex.size}")

  }

}
