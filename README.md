# КПО-ИДЗ2. Крупнов Иван, БПИ217

**Индивидуальное домашнее задание №2 по курсу Конструирование программного обеспечения. Домашнее задание выполнено на предполагаемую оценку 10.**

### Информация о решении
* С консоли считывается путь к анализируемой папке.
* Директория обходится с помощью метода `walk` в классе `DirectoryWalker`. 
Каждый файл парсится с помощью `Parser.parseFile`. Выводится список зависимостей.
* На основании зависимостей строится направленный граф `DependencyGraph`, где родительский элемент означает наличие `require 'parent.txt'` в дочернем.
* На графе выполняется топологическая сортировка глубинным обходом с автоматическим поиском циклов.
* Если цикл был найден, выводится соответствующее сообщение; иначе печатаются имена файлов в порядке компиляции, отсортированные с помощью `getCompilationOrder`.
