package controllers

import java.io.File
import javax.inject.Inject

import org.apache.poi.ss.usermodel.{Cell, DataFormatter}
import play.api.mvc._
import utils.Relation
import utils.dtos.UploadRowDTO
import utils.services.AddFilterService

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer


class Upload @Inject()(webJarAssets: WebJarAssets, addFilterService: AddFilterService) extends Controller {

  def index = Action {
    Results.Redirect(routes.Application.index())
  }

  def uploadFilters = Action(parse.multipartFormData) { request =>
    var tmpFile = new File("/tmp/tmpfile")
    try {
      request.body.file("file").map { picture =>
        val file = picture.ref.moveTo(tmpFile)
        import org.apache.poi.ss.usermodel.WorkbookFactory
        val wb = WorkbookFactory.create(file)
        val sheet = wb.getSheetAt(0)
        val rows = ArrayBuffer[UploadRowDTO]()

        def getCellString(cell: Cell) = {
          if (cell != null) {
            cell.getCellType match {
              case Cell.CELL_TYPE_NUMERIC =>
                new DataFormatter().formatCellValue(cell)
              case Cell.CELL_TYPE_STRING =>
                cell.getStringCellValue
              case _ => ""
            }
          } else {
            ""
          }
        }

        //omitting first row
        val rowIterator = sheet.rowIterator()
        rowIterator.next()
        for (row <- rowIterator) {
          var tabName = getCellString(row.getCell(0))
          var filterName = getCellString(row.getCell(1))
          var variantColumnName = getCellString(row.getCell(2))
          var relation = getCellString(row.getCell(3))
          var defaultValue = getCellString(row.getCell(4))
          var options = getCellString(row.getCell(5))
          val field = new UploadRowDTO(tabName, filterName, variantColumnName, Relation.toSqlValue(relation), defaultValue, options)
          rows += field
        }

        if (rows.nonEmpty) {
          addFilterService.save(rows)
        }

      }
      tmpFile.delete()
      Ok("File uploaded")
    }
    catch {
      case e: Exception => {
        tmpFile.delete()
        BadRequest("Something went wrong\n Error: " + e.getMessage)
      }
    }

  }
}
