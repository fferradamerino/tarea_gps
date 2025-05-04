CREATE TABLE Estudiante (
	rut VARCHAR(20),
	nombre_completo VARCHAR(20),
	edad INT,
	curso VARCHAR(20),
	PRIMARY KEY(rut)
);

CREATE TABLE Evaluacion (
	rut_estudiante VARCHAR(20),
	semestre INT,
	asignatura VARCHAR(20),
	evaluacion FLOAT,
	FOREIGN KEY(rut_estudiante) REFERENCES Estudiante(rut)
);

INSERT INTO Estudiante (rut, nombre_completo, edad, curso)
	VALUES ("20.629.178-8", "Fabián Ferrada", 24, 6);
	
INSERT INTO Evaluacion (rut_estudiante, semestre, asignatura, evaluacion)
	VALUES ("20.629.178-8", 1, "Gestión de proyectos de software", 7.0);
