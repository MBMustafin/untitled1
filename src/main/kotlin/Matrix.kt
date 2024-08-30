package ru.smak.chat.databaseimport

import org.jetbrains.exposed.sql.Table


object Matrices : Table() {
    val id = integer("id").autoIncrement() // Уникальный идентификатор матрицы
    val description = varchar("description", 255) // Описание матрицы
    override val primaryKey = PrimaryKey(id)
}

object MatrixElements : Table() {
    val matrixId = integer("matrix_id").references(Matrices.id) // Внешний ключ на таблицу Matrices
    val row = integer("row") // Номер строки
    val col = integer("col") // Номер столбца
    val value = integer("value") // Значение элемента матрицы
    override val primaryKey = PrimaryKey(matrixId, row, col) // Композитный первичный ключ
}
