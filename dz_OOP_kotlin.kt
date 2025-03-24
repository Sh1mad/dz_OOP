package com.example.dz_oop_part1

fun main() {
    fun main() {
        val library = Library()

        // Создаем объекты для тестирования в одну строку
        val books = listOf(Book(id = 1, name = "Маугли", isAvailable = true, pageCount = 202, author = "Джозеф Киплинг"),
            Book(id = 2, name = "Война и мир", isAvailable = false, pageCount = 1225, author = "Лев Толстой"))
        val newspapers = listOf(Newspaper(id = 3, name = "Сельская жизнь", isAvailable = true, paperNumber = 794, month = Month.AUGUST),
            Newspaper(id = 4, name = "Правда", isAvailable = false, paperNumber = 1234, month = Month.MAY))
        val disks = listOf(Disk(id = 5, name = "Дэдпул и Росомаха", isAvailable = true, type = DiskType.DVD),
            Disk(id = 6, name = "Музыкальный альбом", isAvailable = false, type = DiskType.CD))

        // Добавляем все предметы в библиотеку
        library.addItems(books)
        library.addItems(newspapers)
        library.addItems(disks)

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
                1 -> library.showItems(ItemType.BOOK)
                2 -> library.showItems(ItemType.NEWSPAPER)
                3 -> library.showItems(ItemType.DISK)
                4 -> running = false
                else -> println("Неверный выбор. Попробуйте снова.")
            }
        }
    }
}

// Реализация системы библиотеки
class Library {
    private val items = mutableListOf<LibraryItem>()

    fun addItems(newItems: List<LibraryItem>) {
        items.addAll(newItems)
    }

    fun showItems(itemType: ItemType) {
        val filteredItems = items.filter { it.getType() == itemType }
        if (filteredItems.isEmpty()) {
            println("В библиотеке нет $itemType.")
            return
        }

        println("\nСписок $itemType:")
        filteredItems.forEachIndexed { index, item ->
            println("${index + 1}. ${item.getShortInfo()}")
        }

        print("Выберите номер элемента (или '0' для возврата): ")
        val choice = readlnOrNull()?.toIntOrNull()
        if (choice == null || choice < 0 || choice > filteredItems.size) {
            println("Неверный выбор.")
            return
        }

        if (choice == 0) return

        val selectedItem = filteredItems[choice - 1]
        showItemMenu(selectedItem)
    }

    private fun showItemMenu(item: LibraryItem){
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

    private fun takeHome(item: LibraryItem) {
        if (item is TakeHomeable && item.canTakeHome()) {
            item.isAvailable = false
            println("${item.getType()} ${item.id} взята домой.")
        } else {
            println("Этот объект нельзя взять домой.")
        }
    }

    private fun readInReadingRoom(item: LibraryItem) {
        if (item is ReadableInReadingRoom && item.canReadInReadingRoom()) {
            item.isAvailable = false
            println("${item.getType()} ${item.id} взята в читальный зал.")
        } else {
            println("Этот объект нельзя читать в читальном зале.")
        }
    }

    private fun returnItem(item: LibraryItem) {
        if (item is Returnable && item.canReturn()) {
            item.isAvailable = true
            println("${item.getType()} ${item.id} возвращена.")
        } else {
            println("Этот объект нельзя вернуть.")
        }
    }
}

// Базовый абстрактный класс для реализации объектов библиотеки
abstract class LibraryItem(
    open val id: Int,
    open var isAvailable: Boolean,
    open val name: String
) {
    fun getShortInfo(): String = "$name доступна: ${if (isAvailable) "Да" else "Нет"}"
    abstract fun getDetailedInfo(): String
    abstract fun getType(): ItemType
}

// Все доступные объекты в библиотеке
enum class ItemType{
    BOOK, NEWSPAPER, DISK
}

// Класс реализации книг
class Book(
    override val id: Int,
    override var isAvailable: Boolean,
    override val name: String,
    val pageCount: Int,
    val author: String
) : LibraryItem(id, isAvailable, name), TakeHomeable, ReadableInReadingRoom, Returnable {
    override fun getDetailedInfo(): String =
        "книга: $name ($pageCount стр.) автора: $author с id: $id доступна: ${if (isAvailable) "Да" else "Нет"}"

    override fun getType(): ItemType = ItemType.BOOK
    override fun canTakeHome(): Boolean = isAvailable
    override fun canReadInReadingRoom(): Boolean = isAvailable
    override fun canReturn(): Boolean = !isAvailable
}

// Месяцы выпуска газет
enum class Month(val month: String){
    JANUARY("Январь"),
    FEBRUARY("Февраль"),
    MARCH("Март"),
    APRIL("Апрель"),
    MAY("Март"),
    JUNE("Июнь"),
    JULY("Июль"),
    AUGUST("Август"),
    SEPTEMBER("Сентябрь"),
    OCTOBER("Октябрь"),
    NOVEMBER("Ноябрь"),
    DECEMBER("Декабрь")
}

// Класс реализации газет
class Newspaper(
    override val id: Int,
    override var isAvailable: Boolean,
    override val name: String,
    val paperNumber: Int,
    val month: Month
) : LibraryItem(id, isAvailable, name), ReadableInReadingRoom, Returnable {
    override fun getDetailedInfo(): String =
        "выпуск: $paperNumber газеты $name с id: $id, месяц выпуска ${month.month} доступен: ${if (isAvailable) "Да" else "Нет"}"

    override fun getType(): ItemType = ItemType.NEWSPAPER
    override fun canReadInReadingRoom(): Boolean = isAvailable
    override fun canReturn(): Boolean = !isAvailable
}

// Доступные типы дисков
enum class DiskType{CD, DVD}

// Класс реализации дисков
class Disk(
    override val id: Int,
    override var isAvailable: Boolean,
    override val name: String,
    val type: DiskType
) : LibraryItem(id, isAvailable, name), TakeHomeable, Returnable {
    override fun getDetailedInfo(): String =
        "${type} $name доступна: ${if (isAvailable) "Да" else "Нет"}"

    override fun getType(): ItemType = ItemType.DISK
    override fun canTakeHome(): Boolean = isAvailable
    override fun canReturn(): Boolean = !isAvailable

}

// Интерфейс для проверки возможности забрать предмет домой
interface TakeHomeable{
    fun canTakeHome(): Boolean
}

interface ReadableInReadingRoom{
    fun canReadInReadingRoom(): Boolean
}

interface Returnable{
    fun canReturn(): Boolean
}