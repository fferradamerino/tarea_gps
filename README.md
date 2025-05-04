# Documentación API de Gestión Académica

Esta documentación describe los endpoints disponibles en los microservicios solicitados.

## API de Estudiantes

## Crear Estudiante

Crea un nuevo registro de estudiante en la base de datos.

**URL**: `/estudiante/crear`

**Método**: `POST`

**Parámetros de consulta (Query Params)**:

| Parámetro | Tipo | Requerido | Descripción |
|-----------|------|-----------|-------------|
| rut | String | Sí | Identificador único del estudiante (formato RUT chileno) |
| nombre_completo | String | Sí | Nombre completo del estudiante |
| edad | Integer | Sí | Edad del estudiante (debe ser mayor que 0) |
| curso | String | Sí | Curso al que pertenece el estudiante |

**Respuestas**:

| Código | Descripción | Contenido |
|--------|-------------|-----------|
| 200 OK | Estudiante creado exitosamente | Texto: "Estudiante creado" |
| 400 Bad Request | Parámetros faltantes o inválidos | Texto: "Faltan campos o son inválidos" |

**Ejemplo de solicitud**:
```
POST /estudiante/crear?rut=12345678-9&nombre_completo=Juan%20Pérez&edad=20&curso=Ingeniería%20Informática
```

## Obtener Estudiante

Recupera la información de un estudiante específico mediante su RUT.

**URL**: `/estudiante/obtener`

**Método**: `GET`

**Parámetros de consulta (Query Params)**:

| Parámetro | Tipo | Requerido | Descripción |
|-----------|------|-----------|-------------|
| rut | String | Sí | Identificador único del estudiante a consultar |

**Respuestas**:

| Código | Descripción | Contenido |
|--------|-------------|-----------|
| 200 OK | Estudiante encontrado | JSON con los datos del estudiante |
| 404 Not Found | Estudiante no encontrado | JSON: `{"error": "Estudiante no encontrado"}` |
| 400 Bad Request | Parámetro RUT no proporcionado | JSON: `{"error": "Debe proporcionar el parámetro 'rut'"}` |

**Ejemplo de solicitud**:
```
GET /estudiante/obtener?rut=12345678-9
```

**Ejemplo de respuesta exitosa**:
```json
{
  "rut": "12345678-9",
  "nombre_completo": "Juan Pérez",
  "edad": 20,
  "curso": "Ingeniería Informática"
}
```

# API de Estudiantes

## Obtener Todos los Estudiantes

Recupera la lista completa de todos los estudiantes registrados.

**URL**: `/estudiante/obtenerTodos`

**Método**: `GET`

**Parámetros**: Ninguno

**Respuestas**:

| Código | Descripción | Contenido |
|--------|-------------|-----------|
| 200 OK | Lista de estudiantes | Array JSON con todos los estudiantes |

**Ejemplo de solicitud**:
```
GET /estudiante/obtenerTodos
```

**Ejemplo de respuesta exitosa**:
```json
[
  {
    "rut": "12345678-9",
    "nombre_completo": "Juan Pérez",
    "edad": 20,
    "curso": "Ingeniería Informática"
  },
  {
    "rut": "98765432-1",
    "nombre_completo": "María González",
    "edad": 22,
    "curso": "Ingeniería Civil"
  }
]
```

# API de Evaluaciones

## Crear Evaluación

Crea un nuevo registro de evaluación para un estudiante.

**URL**: `/evaluacion/crear`

**Método**: `POST`

**Parámetros de consulta (Query Params)**:

| Parámetro | Tipo | Requerido | Descripción |
|-----------|------|-----------|-------------|
| rut_estudiante | String | Sí | RUT del estudiante al que pertenece la evaluación |
| semestre | Integer | Sí | Número del semestre (debe ser mayor que 0) |
| asignatura | String | Sí | Nombre de la asignatura evaluada |
| evaluacion | Float | Sí | Calificación obtenida (debe ser mayor o igual a 0.0) |

**Respuestas**:

| Código | Descripción | Contenido |
|--------|-------------|-----------|
| 200 OK | Evaluación creada exitosamente | Texto: "Evaluación creada" |
| 400 Bad Request | Parámetros faltantes o inválidos | Texto: "Faltan campos o son inválidos" |

**Ejemplo de solicitud**:
```
POST /evaluacion/crear?rut_estudiante=12345678-9&semestre=2&asignatura=Matemáticas&evaluacion=6.5
```

## Obtener Evaluaciones por Estudiante

Recupera todas las evaluaciones asociadas a un estudiante específico.

**URL**: `/evaluacion/obtener`

**Método**: `GET`

**Parámetros de consulta (Query Params)**:

| Parámetro | Tipo | Requerido | Descripción |
|-----------|------|-----------|-------------|
| rut | String | Sí | RUT del estudiante cuyas evaluaciones se desean consultar |

**Respuestas**:

| Código | Descripción | Contenido |
|--------|-------------|-----------|
| 200 OK | Evaluaciones encontradas | Array JSON con las evaluaciones del estudiante |
| 404 Not Found | No se encontraron evaluaciones | JSON: `{"error": "No se encontraron evaluaciones para este estudiante"}` |
| 400 Bad Request | Parámetro RUT no proporcionado | JSON: `{"error": "Debe proporcionar el parámetro 'rut'"}` |

**Ejemplo de solicitud**:
```
GET /evaluacion/obtener?rut=12345678-9
```

**Ejemplo de respuesta exitosa**:
```json
[
  {
    "rut_estudiante": "12345678-9",
    "semestre": 1,
    "asignatura": "Programación",
    "evaluacion": 6.8
  },
  {
    "rut_estudiante": "12345678-9",
    "semestre": 1,
    "asignatura": "Matemáticas",
    "evaluacion": 5.5
  }
]
```

## Obtener Todas las Evaluaciones

Recupera la lista completa de todas las evaluaciones registradas en el sistema.

**URL**: `/evaluacion/obtenerTodos`

**Método**: `GET`

**Parámetros**: Ninguno

**Respuestas**:

| Código | Descripción | Contenido |
|--------|-------------|-----------|
| 200 OK | Lista de evaluaciones | Array JSON con todas las evaluaciones |

**Ejemplo de solicitud**:
```
GET /evaluacion/obtenerTodos
```

**Ejemplo de respuesta exitosa**:
```json
[
  {
    "rut_estudiante": "12345678-9",
    "semestre": 1,
    "asignatura": "Programación",
    "evaluacion": 6.8
  },
  {
    "rut_estudiante": "98765432-1",
    "semestre": 2,
    "asignatura": "Física",
    "evaluacion": 4.5
  }
]
```
