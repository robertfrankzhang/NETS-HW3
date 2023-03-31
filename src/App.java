import java.util.Scanner;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.List;
import java.util.ArrayList;

public class App {

  private static Element findElementWith(String text, Element element, String type) {
    if (element.tagName().equals(type) && element.text().contains(text)) {
      // System.out.print(element.tagName() + ": " + element.text());
      // System.out.println();
      return element;
    }
    Elements children = element.children();
    for (Element child : children) {
      Element childElement = findElementWith(text, child, type);
      if (childElement != null) {
        return childElement;
      }
    }
    return null;
  }

  private static List<Element> geth3Content(Element h3) {
    Elements siblings = h3.nextElementSiblings();

    // Create a list to store the elements
    List<Element> elements = new ArrayList<>();

    // Loop through the siblings and add them to the list until the next h3 element
    // is found
    for (Element sibling : siblings) {
      if (sibling.tagName().startsWith("h")) {
        break; // Stop at the next h3, h2, or h1 element
      }
      elements.add(sibling);
    }

    // Print the text of each element in the list
    // for (Element element : elements) {
    // System.out.println(element.text());
    // System.out.println("\n");
    // }

    return elements;
  }

  private static List<String> getListContent(Element list) {
    // Get all the li elements that are children of the ul element
    Elements liElements = list.select("li");

    // Create a list to store the list items
    List<String> listItems = new ArrayList<>();

    // Loop through the li elements and add their text content to the list
    for (Element li : liElements) {
      listItems.add(li.text());
    }

    return listItems;
  }

  private static List<List<String>> getTableContent(Element table) {
    // Get all the tr elements that are children of the table element
    Elements trElements = table.select("tr");

    // Create a list to store the 2D list of table items
    List<List<String>> tableItems = new ArrayList<>();

    // Create a list to store the header row
    List<String> headerRow = new ArrayList<>();

    // Loop through the tr elements and add their td/th elements to the 2D list
    for (Element tr : trElements) {
      List<String> row = new ArrayList<>();
      Elements tdElements = tr.select("td, th");
      for (Element td : tdElements) {
        row.add(td.text());
      }
      if (row.size() > 0) {
        if (headerRow.size() == 0 && tdElements.get(0).tagName().equals("th")) {
          headerRow = row;
        } else {
          tableItems.add(row);
        }
      }
    }

    return tableItems;
  }

  private static List<String> flattenTableContent(Element table) {
    // Get all the tr elements that are children of the table element
    Elements trElements = table.select("tr");

    // Create a list to store the list of table items
    List<String> tableItems = new ArrayList<>();

    // Loop through the tr elements and add their td/th elements to the list
    for (Element tr : trElements) {
      List<String> row = new ArrayList<>();
      Elements tdElements = tr.select("td, th");
      for (Element td : tdElements) {
        tableItems.add(td.text());
      }
    }
    return tableItems;
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int choice = 0;
    boolean isValidChoice = false;

    while (!isValidChoice) {
      System.out.println("Please choose from 1 to 8:");
      System.out
          .println("1. List all Discontinued categories for awards (i.e., categories that are no longer awarded).");
      System.out.println(
          "2. How many award categories were added in the XXX0s? (please select a decade between the 1920s and 2020s)");
      System.out.println("3. List all the movies that were nominated for at least X awards in YYYY");
      System.out.println("4. Which film won 'Award Category Name' in XXXX?");
      System.out.println(
          "5. What was the budget for the 'Award Category Name' winner in 2022? How much did this movie make in the box office?");
      System.out.println(
          "6. Which academic institution (university, college, etc.) has the highest number of alumni nominated for the 'Award Category Name' award?");
      System.out.println(
          "7. For 'Award Category Name', for the countries that were nominated/won, how many times have they been nominated in the past (including this year)?");
      System.out
          .println("8. Wild card – come up with an interesting question. List the question and find the answer to it.");
      try {
        choice = scanner.nextInt();
        if (choice < 1 || choice > 8) {
          System.out.println("Invalid choice!");
        } else {
          isValidChoice = true;
        }
      } catch (Exception e) {
        System.out.println("Invalid input!");
        scanner.nextLine(); // clear the input buffer
      }
    }

    // Try to connect to the URL
    Element root;
    while (true) {
      try {
        String url = "https://en.wikipedia.org/wiki/Academy_Awards";
        Document doc = Jsoup.connect(url).get();
        root = doc.body();
        break;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // Do something with the valid choice...
    if (choice == 1) {
      System.out.println("Discontinued Categories:");
      Element discontinuedHeader = findElementWith("Discontinued categories", root, "h3");
      List<Element> h3Content = geth3Content(discontinuedHeader);
      for (Element element : h3Content) {
        if (findElementWith("", element, "ul") != null) {
          List<String> listContent = getListContent(element);
          for (String listItem : listContent) {
            System.out.println(listItem.split(":")[0].trim());
          }
        }
      }
    } else if (choice == 2) {
      int addedCounter = 0;

      // Select Decade
      int decade = 0;
      boolean isValideDecadeChoice = false;
      while (!isValideDecadeChoice) {
        System.out.println("Please choose a decade between the 1920s and 2020s by typing a num between 192 and 202:");
        try {
          decade = scanner.nextInt();
          if (decade < 192 || decade > 202) {
            System.out.println("Invalid choice!");
          } else {
            isValideDecadeChoice = true;
            break;
          }
        } catch (Exception e) {
          System.out.println("Invalid input!");
          scanner.nextLine(); // clear the input buffer
        }
      }

      System.out.println("Awards added in the " + decade
          + "0s (including discontinued awards, as long as the discontinued award wasn't discontinued in the same decade it was introduced");

      // Find the table
      Element currentCatHeader = findElementWith("Current categories", root, "h3");
      List<Element> h3Content = geth3Content(currentCatHeader);
      for (Element element : h3Content) {
        if (findElementWith("", element, "table") != null) {
          List<List<String>> tableContent = getTableContent(element);
          for (List<String> row : tableContent) {
            String year = row.get(0);
            if (year.contains("/")) {
              int firstYear = Integer.parseInt(year.substring(0, 4));
              int secondYear = Integer.parseInt(year.substring(5));
              secondYear += firstYear / 100 * 100;
              if ((firstYear >= decade * 10 && firstYear < decade * 10 + 10)
                  || (secondYear >= decade * 10 && secondYear < decade * 10 + 10)) {
                System.out.println(row.get(1));
                addedCounter++;
              }
            } else {
              int firstYear = Integer.parseInt(year.substring(0, 4));
              if (firstYear >= decade * 10 && firstYear < decade * 10 + 10) {
                System.out.println(row.get(1));
                addedCounter++;
              }
            }
          }
        }
      }

      // Count discontinued awards (that weren't discontinued in the same decade
      // introduced)
      Element discontinuedHeader = findElementWith("Discontinued categories", root, "h3");
      List<Element> h3Content2 = geth3Content(discontinuedHeader);
      for (Element element : h3Content2) {
        if (findElementWith("", element, "ul") != null) {
          List<String> listContent = getListContent(element);
          for (String listItem : listContent) {
            String years = listItem.split(":")[1].trim();
            if (years.contains("to")) {
              String first = years.split(" to ")[0];
              String second = years.split(" to ")[1];
              if (Integer.parseInt(first.substring(0, 3)) == decade
                  && Integer.parseInt(first.substring(0, 3)) != Integer.parseInt(second.substring(0, 3))) {
                System.out.println(listItem.split(":")[0].trim() + " (currently discontinued)");
                addedCounter++;
              }
            }
          }
        }
      }

      // Print total
      System.out.println("Total Awards Added: " + addedCounter);
    } else if (choice == 3) {

    } else if (choice == 4) {

    } else if (choice == 5) {

    } else if (choice == 6) {

    } else if (choice == 7) {

    } else if (choice == 8) {

    }
  }
}
