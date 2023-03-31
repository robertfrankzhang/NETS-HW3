import java.util.Scanner;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class App {

  private static Element connectToURL(String url) {
    Element root;
    while (true) {
      try {
        Document doc = Jsoup.connect(url).get();
        root = doc.body();
        break;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return root;
  }

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

  private static Element findElementContaining(List<String> text, Element element, String type) {
    if (element.tagName().equals(type)) {
      boolean contains = true;
      for (String s : text) {
        if (!element.text().toLowerCase().contains(s.toLowerCase())) {
          contains = false;
        }
      }
      if (contains) {
        return element;
      }
    }
    Elements children = element.children();
    for (Element child : children) {
      Element childElement = findElementContaining(text, child, type);
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

    return elements;
  }

  private static List<Element> geth2Content(Element h2) {
    Elements siblings = h2.nextElementSiblings();

    // Create a list to store the elements
    List<Element> elements = new ArrayList<>();

    // Loop through the siblings and add them to the list until the next h2 element
    // is found
    for (Element sibling : siblings) {
      if (sibling.tagName().startsWith("h") && !sibling.tagName().equals("h3")) {
        break; // Stop at the next h2, or h1 element
      }
      elements.add(sibling);
    }

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

  private static List<List<Element>> getTableContent(Element table) {
    // Get all the tr elements that are children of the table element
    Elements trElements = table.select("tr");

    // Create a list to store the 2D list of table items
    List<List<Element>> tableItems = new ArrayList<>();

    // Loop through the tr elements and add their td/th elements to the 2D list
    for (Element tr : trElements) {
      List<Element> row = new ArrayList<>();
      Elements tdElements = tr.select("td, th");
      for (Element td : tdElements) {
        row.add(td);
      }
      tableItems.add(row);
    }

    return tableItems;
  }

  private static List<Element> flattenTableContent(Element table) {
    // Get all the tr elements that are children of the table element
    Elements trElements = table.select("tr");

    // Create a list to store the list of table items
    List<Element> tableItems = new ArrayList<>();

    // Loop through the tr elements and add their td/th elements to the list
    for (Element tr : trElements) {
      Elements tdElements = tr.select("td, th");
      for (Element td : tdElements) {
        tableItems.add(td);
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

    // Do something with the valid choice...
    if (choice == 1) {
      Element root = connectToURL("https://en.wikipedia.org/wiki/Academy_Awards");
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
      Element root = connectToURL("https://en.wikipedia.org/wiki/Academy_Awards");

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
          List<List<Element>> tableContent = getTableContent(element);
          for (List<Element> row : tableContent) {
            String year = row.get(0).text();
            if (year.contains("Year introduced")) {
              continue;
            }
            if (year.contains("/")) {
              int firstYear = Integer.parseInt(year.substring(0, 4));
              int secondYear = Integer.parseInt(year.substring(5));
              secondYear += firstYear / 100 * 100;
              if ((firstYear >= decade * 10 && firstYear < decade * 10 + 10)
                  || (secondYear >= decade * 10 && secondYear < decade * 10 + 10)) {
                System.out.println(row.get(1).text());
                addedCounter++;
              }
            } else {
              int firstYear = Integer.parseInt(year.substring(0, 4));
              if (firstYear >= decade * 10 && firstYear < decade * 10 + 10) {
                System.out.println(row.get(1).text());
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
      // Select Year
      int year = 0;
      while (true) {
        System.out.println(
            "Please choose a year between 1928 and 2022 by typing a num between 1928 and 2022. This indicates the year in which the film was released.");
        try {
          year = scanner.nextInt();
          if (year < 1929 || year > 2022) {
            System.out.println("Invalid choice!");
          } else {
            break;
          }
        } catch (Exception e) {
          System.out.println("Invalid input!");
          scanner.nextLine(); // clear the input buffer
        }
      }

      // Select Num Awards
      int numAwards = 0;
      while (true) {
        System.out.println("Please type a number >= 1 to select the min # of awards");
        try {
          numAwards = scanner.nextInt();
          if (numAwards < 1) {
            System.out.println("Invalid choice!");
          } else {
            break;
          }
        } catch (Exception e) {
          System.out.println("Invalid input!");
          scanner.nextLine(); // clear the input buffer
        }
      }

      int oscarNum = year - 1927;
      String suffix;
      switch (oscarNum % 10) {
        case 1:
          suffix = "st";
          break;
        case 2:
          suffix = "nd";
          break;
        case 3:
          suffix = "rd";
          break;
        default:
          suffix = "th";
          break;
      }
      Element root = connectToURL("https://en.wikipedia.org/wiki/" + oscarNum + suffix + "_Academy_Awards");

      // Create an empty dict of {Film : Nomination Count}
      Map<String, Integer> nominationCounter = new HashMap<String, Integer>();

      // Find Winners and Nominees H2
      List<String> headerKeys = Arrays.asList("winners", "and", "nominees");
      Element winNomHeader = findElementContaining(headerKeys, root, "h2");

      // Find the first table under the winners and nominees header
      List<Element> h2Content = geth2Content(winNomHeader);
      List<Element> tableContent = null;
      for (Element element : h2Content) {
        if (findElementWith("", element, "table") != null) {
          tableContent = flattenTableContent(element);
          break;
        }
      }
      if (tableContent == null) {
        System.out.println("No table found!");
        return;
      }

      // For each element inside the Table Context, find the award name, map to dict,
      // update freq dict
      for (Element element : tableContent) {
        Elements italicElements = element.select("i");
        List<String> italicTextList = new ArrayList<>();
        for (Element italicElement : italicElements) {
          italicTextList.add(italicElement.text());
        }
        // Set<String> set = new HashSet<>(italicTextList);
        // List<String> uniqueTextList = new ArrayList<>(set);
        for (String s : italicTextList) {
          nominationCounter.put(s, nominationCounter.getOrDefault(s, 0) + 1);
        }
      }

      // Print films with more than numAwards
      for (String film : nominationCounter.keySet()) {
        if (nominationCounter.get(film) >= numAwards) {
          System.out.println(film + " (" + nominationCounter.get(film) + " nominations)");
        }
      }

    } else if (choice == 4) {

    } else if (choice == 5) {

    } else if (choice == 6) {

    } else if (choice == 7) {

    } else if (choice == 8) {

    }
  }
}
