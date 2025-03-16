package com.example.dz_oop_part1

fun main() {
    val library = Library()

    // Добавление объектов для тестирования
    library.addBook(Book(id = 1, name = "Маугли", isAvailable = true, pageCount =  202, author = "Джозеф Киплинг"))
    library.addBook(Book(id = 2, name = "Война и мир", isAvailable = false, pageCount =  1225, author =  "Лев Толстой"))
    library.addNewspaper(Newspaper(id = 3, name = "Сельская жизнь", isAvailable = true, paperNumber = 794))
    library.addNewspaper(Newspaper(id = 4, name = "Правда", isAvailable =  false, paperNumber = 1234))
    library.addDisk(Disk(id = 5, name = "Дэдпул и Росомаха", isAvailable = true, type = DiskType.DVD))
    library.addDisk(Disk(id = 6, name = "Музыкальный альбом", isAvailable =  false, type = DiskType.CD))

    // Главное меню
    var running = true
    while (running) {
        println("\n--- Библиотечная система ---")
        println("1. Показать книги")
        println("2. Показать газеты")
        println("3. Показать диски")
        println("4. Выход")

        print("Выберите действие: ")
        when (readlnOrNull()?.toIntOrNull()) {
            1 -> library.showItems(library.books, "книг")
            2 -> library.showItems(library.newspapers, "газет")
            3 -> library.showItems(library.disks, "дисков")
            4 -> running = false
            else -> println("Неверный выбор. Попробуйте снова.")
        }
    }
}

// Реализация системы библиотеки
class Library {
    val books = mutableListOf<Book>()
    val newspapers = mutableListOf<Newspaper>()
    val disks = mutableListOf<Disk>()

    fun addBook(book: Book) = books.add(book)
    fun addNewspaper(newspaper: Newspaper) = newspapers.add(newspaper)
    fun addDisk(disk: Disk) = disks.add(disk)

    fun showItems(items: List<LibraryItem>, itemType: String) {
        if (items.isEmpty()){
            println("В библиотеке нет $itemType.")
            return
        }

        println("\nСписок $itemType:")
        items.forEachIndexed { index, item ->
            println("${index + 1}. ${item.getShortInfo()}")
        }

        print("Выберите номер элемента (или '0' для возврата): ")
        val choice = readLine()?.toIntOrNull()
        if (choice == null || choice < 0 || choice > items.size) {
            println("Неверный выбор.")
            return
        }

        if (choice == 0) return

        val selectedItem = items[choice - 1]
        showItemMenu(selectedItem)
    }

    fun showItemMenu(item: LibraryItem){
        while(true) {
            println("\n--- Меню для '${item.name}' ---")
            println("1. Взять домой")
            println("2. Читать в читальном зале")
            println("3. Показать подробную информацию")
            println("4. Вернуть")
            println("5. Назад к списку")

            print("Выберите действие: ")
            when (readlnOrNull()?.toIntOrNull()) {
                1 -> takeHome(item)
                2 -> readInReadingRoom(item)
                3 -> println("\n${item.getDetailedInfo()}")
                4 -> returnItem(item)
                5 -> return
                else -> println("Неверный выбор.")
            }
        }
    }

    private fun takeHome(item:LibraryItem){
        if (item is Newspaper) {
            println("Газеты нельзя взять домой.")
        } else if (!item.isAvailable){
            println("Этот объект недоступен в данный момент.")
        } else {
            item.isAvailable = false
            println("${item.getType()} ${item.id} взяли домой.")
        }
    }

    private fun readInReadingRoom(item: LibraryItem){
        if (item is Disk) {
            println("Диском нельзя воспользоваться в читальном зале.")
        } else if (!item.isAvailable) {
            println("Этот объект недоступен в данный момент.")
        } else {
            item.isAvailable = false
            println("${item.getType()} ${item.id} взяли в читальный зал.")
        }
    }

    private fun returnItem(item: LibraryItem) {
        if (item.isAvailable){
            println("Объект уже находится в библиотеке.")
        } else {
            item.isAvailable = true
            println("${item.getType()} ${item.id} возвращена.")
        }
    }
}

// Базовый интерфейс для реализации объектов библиотеки
interface  LibraryItem{
    val id: Int
    var isAvailable: Boolean
    val name: String

    fun getShortInfo(): String
    fun getDetailedInfo(): String
    fun getType(): String

}

// Класс реализации книг
class Book(
    override val id: Int,
    override var isAvailable: Boolean,
    override val name: String,
    val pageCount: Int,
    val author: String
    ) : LibraryItem {
    override fun getShortInfo(): String = "$name доступна: ${if (isAvailable) "Да" else "Нет"}"

    override fun getDetailedInfo(): String =
        "книга: $name ($pageCount стр.) автора: $author с id: $id доступна: ${if (isAvailable) "Да" else "Нет"}"

    override fun getType(): String = "Книга"
}

// Класс реализации газет
class Newspaper(
    override val id: Int,
    override var isAvailable: Boolean,
    override val name: String,
    val paperNumber: Int
) : LibraryItem{
    override fun getShortInfo(): String = "$name доступна: ${if (isAvailable) "Да" else "Нет"}"

    override fun getDetailedInfo(): String =
        "выпуск: $paperNumber газеты $name с id: $id доступен: ${if (isAvailable) "Да" else "Нет"}"

    override fun getType(): String = "Газета"
}

// Доступные типы дисков
enum class DiskType{CD, DVD}

// Класс реализации дисков
class Disk(
    override val id: Int,
    override var isAvailable: Boolean,
    override val name: String,
    val type: DiskType
) : LibraryItem{
    override fun getShortInfo(): String = "$name доступна: ${if (isAvailable) "Да" else "Нет"}"

    override fun getDetailedInfo(): String =
        "${type} $name доступна: ${if (isAvailable) "Да" else "Нет"}"

    override fun getType(): String = "Диск"
}