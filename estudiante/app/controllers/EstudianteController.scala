package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._

import java.sql._
import java.util.Properties

case class Estudiante(rut: String, nombre_completo: String, edad: Int, curso: String)
object Estudiante {
  implicit val estudianteFormat: OFormat[Estudiante] = Json.format[Estudiante]
}

object DatabaseHandler {
  val url: String = "jdbc:postgresql://localhost:9003/ejemplo"
  val props: Properties = new Properties()
  props.setProperty("user", "ejemplo")
  props.setProperty("password", "ejemplo")
  val conn: Connection = DriverManager.getConnection(url, props)

  def crearEstudiante(rut: String, nombre_completo: String, edad: Int, curso: String): Unit = {
    val query = "INSERT INTO Estudiante (rut, nombre_completo, edad, curso) VALUES (?, ?, ?, ?)"
    val st = conn.prepareStatement(query)
    st.setString(1, rut)
    st.setString(2, nombre_completo)
    st.setInt(3, edad)
    st.setString(4, curso)
    st.executeUpdate()
    st.close()
  }

  def obtenerEstudiantes(): List[Estudiante] = {
    val st = conn.prepareStatement("SELECT rut, nombre_completo, edad, curso FROM Estudiante")
    val rs = st.executeQuery()
    val buffer = scala.collection.mutable.ListBuffer[Estudiante]()
    while (rs.next()) {
      buffer += Estudiante(
        rs.getString("rut"),
        rs.getString("nombre_completo"),
        rs.getInt("edad"),
        rs.getString("curso")
      )
    }
    rs.close()
    st.close()
    buffer.toList
  }

  def obtenerEstudiante(rut: String): Option[Estudiante] = {
    val st = conn.prepareStatement("SELECT rut, nombre_completo, edad, curso FROM Estudiante WHERE rut = ?")
    st.setString(1, rut)
    val rs = st.executeQuery()
    val estudiante = if (rs.next()) {
      Some(Estudiante(
        rs.getString("rut"),
        rs.getString("nombre_completo"),
        rs.getInt("edad"),
        rs.getString("curso")
      ))
    } else None
    rs.close()
    st.close()
    estudiante
  }
}

@Singleton
class EstudianteController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def crearEstudiante() = Action { implicit request: Request[AnyContent] =>
    val query = request.queryString
    val rut = query.get("rut").flatMap(_.headOption).getOrElse("")
    val nombre = query.get("nombre_completo").flatMap(_.headOption).getOrElse("")
    val edad = query.get("edad").flatMap(_.headOption).map(_.toInt).getOrElse(0)
    val curso = query.get("curso").flatMap(_.headOption).getOrElse("")

    if (rut.nonEmpty && nombre.nonEmpty && curso.nonEmpty && edad > 0) {
      DatabaseHandler.crearEstudiante(rut, nombre, edad, curso)
      Ok("Estudiante creado")
    } else {
      BadRequest("Faltan campos o son inválidos")
    }
  }

  def obtenerEstudiantes() = Action {
    val estudiantes = DatabaseHandler.obtenerEstudiantes()
    Ok(Json.toJson(estudiantes))
  }

  def obtenerEstudiante() = Action { implicit request =>
    request.getQueryString("rut") match {
      case Some(rut) =>
        DatabaseHandler.obtenerEstudiante(rut) match {
          case Some(est) => Ok(Json.toJson(est))
          case None => NotFound(Json.obj("error" -> "Estudiante no encontrado"))
        }
      case None => BadRequest(Json.obj("error" -> "Debe proporcionar el parámetro 'rut'"))
    }
  }
}
