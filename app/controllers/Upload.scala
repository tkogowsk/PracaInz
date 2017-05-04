package controllers

import java.io.File
import javax.inject.Inject

import org.apache.poi.ss.usermodel.{Cell, DataFormatter, WorkbookFactory}
import play.api.mvc._
import repository.FieldsRepository
import utils.FieldSaveDTO

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer


class Upload @Inject()(fieldsRepository: FieldsRepository) extends Controller {


  def upload = Action(parse.multipartFormData) { request =>
    try {
      request.body.file("file").map { picture =>
        var tmpFile = new File("/tmp/2")
        val file = picture.ref.moveTo(tmpFile)
        val wb = WorkbookFactory.create(file)
        val sheet = wb.getSheetAt(0)
        val fields = ArrayBuffer[FieldSaveDTO]()

        def getCellString(cell: Cell) = {
          cell.getCellType match {
            case Cell.CELL_TYPE_NUMERIC =>
              new DataFormatter().formatCellValue(cell)
            case Cell.CELL_TYPE_STRING =>
              cell.getStringCellValue
            case Cell.CELL_TYPE_FORMULA =>
              val evaluator = wb.getCreationHelper.createFormulaEvaluator()
              new DataFormatter().formatCellValue(cell, evaluator)
            case _ => " "
          }
        }

        //ommiting first row
        val rowIterator = sheet.rowIterator()
        rowIterator.next()
        for (row <- rowIterator) {

          var columnName = row.getCell(0).toString
          var feName = row.getCell(1).toString
          var relation = row.getCell(2).toString
          if (columnName.length > 0 && feName.length > 0 && relation.length > 0) {
            val field = new FieldSaveDTO(
              columnName = columnName,
              feName = feName,
              relation = relation)
            fields += field
          }
        }

        if (fields.nonEmpty) {
          //   fieldsRepository.save(fields)
        }
        tmpFile.delete()
      }
      Ok("File uploaded")
    }
    catch {
      case e: Exception => {
        Ok("Something went wrong\n Error: " + e.getMessage)
      }
    }

  }
}
