package controllers
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import java.sql._
import java.util.Properties

case class Evaluacion(rut_estudiante: String, semestre: Int, asignatura: String, evaluacion: Float)

object Evaluacion {
  implicit val evaluacionFormat: OFormat[Evaluacion] = Json.format[Evaluacion]
}

object DatabaseHandler {
  val url: String = "jdbc:postgresql://localhost:9003/ejemplo"
  val props: Properties = new Properties()
  props.setProperty("user", "ejemplo")
  props.setProperty("password", "ejemplo")
  val conn: Connection = DriverManager.getConnection(url, props)
  
  def crearEvaluacion(rut_estudiante: String, semestre: Int, asignatura: String, evaluacion: Float): Unit = {
    val query = "INSERT INTO Evaluacion (rut_estudiante, semestre, asignatura, evaluacion) VALUES (?, ?, ?, ?)"
    val st = conn.prepareStatement(query)
    st.setString(1, rut_estudiante)
    st.setInt(2, semestre)
    st.setString(3, asignatura)
    st.setFloat(4, evaluacion)
    st.executeUpdate()
    st.close()
  }
  
  def obtenerEvaluaciones(): List[Evaluacion] = {
    val st = conn.prepareStatement("SELECT rut_estudiante, semestre, asignatura, evaluacion FROM Evaluacion")
    val rs = st.executeQuery()
    val buffer = scala.collection.mutable.ListBuffer[Evaluacion]()
    while (rs.next()) {
      buffer += Evaluacion(
        rs.getString("rut_estudiante"),
        rs.getInt("semestre"),
        rs.getString("asignatura"),
        rs.getFloat("evaluacion")
      )
    }
    rs.close()
    st.close()
    buffer.toList
  }
  
  def obtenerEvaluacionesPorEstudiante(rut: String): List[Evaluacion] = {
    val st = conn.prepareStatement("SELECT rut_estudiante, semestre, asignatura, evaluacion FROM Evaluacion WHERE rut_estudiante = ?")
    st.setString(1, rut)
    val rs = st.executeQuery()
    val buffer = scala.collection.mutable.ListBuffer[Evaluacion]()
    while (rs.next()) {
      buffer += Evaluacion(
        rs.getString("rut_estudiante"),
        rs.getInt("semestre"),
        rs.getString("asignatura"),
        rs.getFloat("evaluacion")
      )
    }
    rs.close()
    st.close()
    buffer.toList
  }
}


@Singleton
class EvaluacionController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  
  def crearEvaluacion() = Action { implicit request: Request[AnyContent] =>
    val query = request.queryString
    val rut = query.get("rut_estudiante").flatMap(_.headOption).getOrElse("")
    val semestre = query.get("semestre").flatMap(_.headOption).map(_.toInt).getOrElse(0)
    val asignatura = query.get("asignatura").flatMap(_.headOption).getOrElse("")
    val evaluacion = query.get("evaluacion").flatMap(_.headOption).map(_.toFloat).getOrElse(0.0f)
    
    if (rut.nonEmpty && semestre > 0 && asignatura.nonEmpty && evaluacion >= 0.0f) {
      DatabaseHandler.crearEvaluacion(rut, semestre, asignatura, evaluacion)
      Ok("Evaluación creada")
    } else {
      BadRequest("Faltan campos o son inválidos")
    }
  }
  
  def obtenerEvaluaciones() = Action {
    val evaluaciones = DatabaseHandler.obtenerEvaluaciones()
    Ok(Json.toJson(evaluaciones))
  }
  
  def obtenerEvaluacionesPorEstudiante() = Action { implicit request =>
    request.getQueryString("rut") match {
      case Some(rut) =>
        val evaluaciones = DatabaseHandler.obtenerEvaluacionesPorEstudiante(rut)
        if (evaluaciones.nonEmpty) {
          Ok(Json.toJson(evaluaciones))
        } else {
          NotFound(Json.obj("error" -> "No se encontraron evaluaciones para este estudiante"))
        }
      case None => BadRequest(Json.obj("error" -> "Debe proporcionar el parámetro 'rut'"))
    }
  }
}
