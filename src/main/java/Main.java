import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Employee> list = parseXML("data.xml");

        // Используем метод из предыдущей задачи
        String json = listToJson(list);

        // И тоже из предыдущей задачи
        writeString(json, "data2.json");

        System.out.println("Конвертация завершена → data2.json создан");
    }

    public static List<Employee> parseXML(String fileName) {
        List<Employee> employees = new ArrayList<>();

        try {
            // Создаём фабрику и билдер
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Парсим файл в Document
            Document document = builder.parse(new File(fileName));

            // Получаем корневой элемент <staff>
            Element root = document.getDocumentElement();

            // Получаем все дочерние узлы
            NodeList nodeList = root.getChildNodes();

            // Перебираем узлы
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                // Пропускаем текстовые узлы (переносы строк, отступы)
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                // Теперь точно элемент <employee>
                Element employeeElement = (Element) node;

                // Извлекаем значения тегов
                long id = Long.parseLong(getTextContent(employeeElement, "id"));
                String firstName = getTextContent(employeeElement, "firstName");
                String lastName = getTextContent(employeeElement, "lastName");
                String country = getTextContent(employeeElement, "country");
                int age = Integer.parseInt(getTextContent(employeeElement, "age"));

                employees.add(new Employee(id, firstName, lastName, country, age));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return employees;
    }

    // Вспомогательный метод — безопасно достаём текст из тега
    private static String getTextContent(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent().trim();
        }
        return "";
    }

    public static String listToJson(List<Employee> list) {
        // Твой код из задачи 1 (ручная сборка JSON-строки)
        // Пример ниже — если вдруг потерял:
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < list.size(); i++) {
            Employee e = list.get(i);
            sb.append("  {\n");
            sb.append("    \"id\": ").append(e.id).append(",\n");
            sb.append("    \"firstName\": \"").append(e.firstName).append("\",\n");
            sb.append("    \"lastName\": \"").append(e.lastName).append("\",\n");
            sb.append("    \"country\": \"").append(e.country).append("\",\n");
            sb.append("    \"age\": ").append(e.age).append("\n");
            sb.append("  }");
            if (i < list.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

    public static void writeString(String str, String fileName) {
        try (var writer = new java.io.FileWriter(fileName)) {
            writer.write(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}