package controllers

import javax.inject.Inject
import java.io.File

import play.api.mvc._
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.DataFormatter
import repository.FieldsRepository
import utils.FieldSaveDTO

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer


class Upload @Inject()(fieldsRepository: FieldsRepository) extends Controller {


  def upload = Action(parse.multipartFormData) { request =>
    try {
      request.body.file("file").map { picture =>

        val file = picture.ref.moveTo(new File("/tmp/file"))
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

        val text = rowIterator.map(row => {
          val field = new FieldSaveDTO(
            columnName = row.getCell(0).toString,
            feName = row.getCell(1).toString,
            relation = row.getCell(2).toString)

          fields += field
          row.cellIterator.map(getCellString).mkString("\t")
        }).mkString("\n")
        if (fields.size > 0) {
          fieldsRepository.save(fields)
        }
      }
      Ok("File uploaded")
    }
    catch {
      case e: Exception => {
        println(e)
        Ok("Something went wrong")
      }
    }

  }
}
