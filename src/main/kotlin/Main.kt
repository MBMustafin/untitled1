import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.DriverManager

fun uploadMatrix(
    Description: String,
    matrix: Array<Array<Int>>
){
    transaction {
        // Создание таблиц
        transaction {
            // Создание таблиц
            SchemaUtils.create(Matrices, MatrixElements)

            // Вставка данных в таблицу Matrices и получение ID
            val matrixId = Matrices.insert {
                it[description] = Description
            } get Matrices.id // Получаем идентификатор вставленной записи

            // Проверка что ID был получен
            if (matrixId != null) {
                val matrix = matrix

                // Вставка элементов матрицы в таблицу MatrixElements
                matrix.forEachIndexed { rowIndex, row ->
                    row.forEachIndexed { colIndex, value ->
                        MatrixElements.insert {
                            it[MatrixElements.matrixId] = matrixId
                            it[MatrixElements.row] = rowIndex
                            it[MatrixElements.col] = colIndex
                            it[MatrixElements.value] = value
                        }
                    }
                }
            } else {
                println("Не удалось получить идентификатор матрицы")
            }
        }
}}

fun downloadMatrix(matrixNum: Int){
    transaction {
        // Выбираем все элементы матрицы с данным matrixId
        val matrixElements = MatrixElements.select { MatrixElements.matrixId eq matrixNum }

        // Определяем размер матрицы с учетом максимального индекса строки и столбца
        val maxRow = matrixElements.maxOf { it[MatrixElements.row] }
        val maxCol = matrixElements.maxOf { it[MatrixElements.col] }
        val matrix = Array(maxRow + 1) { Array(maxCol + 1) { 0 } }

        // Заполняем массив значениями из базы данных
        matrixElements.forEach {
            val row = it[MatrixElements.row]
            val col = it[MatrixElements.col]
            val value = it[MatrixElements.value]
            matrix[row][col] = value
        }
        println("Матрица $matrixNum :")
        for(i in 0..maxRow){
            for(j in 0..maxCol){
                var temp = matrix[i][j]
                print("$temp \t")

            }
            println("\n")
        }
        // Теперь у вас есть матрица, восстановленная из базы данных
    }
}

fun downloadAllMatrices(){
    transaction{
        val id = Matrices.select(Matrices.id)
        id.forEach {
            downloadMatrix(it[Matrices.id])
        }
    }

}


fun main(args: Array<String>) {

    Database.connect({
        DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/exam_matrix",
            "root",
            "root")
    })

    uploadMatrix("Пример Матрицы", arrayOf(
        arrayOf(1, 2, 3, 3),
        arrayOf(4, 5, 6, 3),
        arrayOf(7, 8, 9, 3)
    ))

    downloadAllMatrices()
}
