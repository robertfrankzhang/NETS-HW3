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
import java.util.Collections;
import java.util.Comparator;
import java.net.URLDecoder;

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

  private static String yearToOscarNum(int year) {
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
    return oscarNum + suffix;
  }

  private static String removeBrackets(String s) {
    return s.replaceAll("\\[[^\\]]*\\]|\\([^\\)]*\\)", "");
  }

  private static String removeParentheses(String s) {
    return s.replaceAll("\\([^()]*\\)", "");
  }

  private static List<String> getAwardName() {
    return Arrays.asList("Best Picture", "Best Director", "Best Actor", "Best Actress",
        "Best Cinematography", "Best Production Design", "Best Adapted Screenplay", "Best Sound",
        "Best Animated Short Film", "Best Live Action Short Film", "Best Film Editing", "Best Original Score",
        "Best Original Song", "Best Supporting Actor", "Best Supporting Actress", "Best Visual Effects",
        "Best Original Screenplay", "Best Documentary Short Subject", "Best Documentary Feature Film",
        "Best International Feature Film", "Best Costume Design", "Best Makeup and Hairstyling",
        "Best Animated Feature Film", "Best Assistant Director", "Best Director, Comedy Picture",
        "Best Director, Dramatic Picture", "Best Dance Direction", "Best Original Musical or Comedy Score",
        "Best Original Story", "Best Short Subject - 1 Reel", "Best Short Subject - 2 Reel",
        "Best Short Subject - Color", "Best Short Subject - Comedy", "Best Short Subject - Novelty",
        "Best Sound Editing", "Best Title Writing", "Best Unique and Artistic Production");
  }

  private static void printAwardNames() {
    System.out.println("1. Best Picture");
    System.out.println("2. Best Director");
    System.out.println("3. Best Actor");
    System.out.println("4. Best Actress");
    System.out.println("5. Best Cinematography");
    System.out.println("6. Best Production Design");
    System.out.println("7. Best Adapted Screenplay");
    System.out.println("8. Best Sound");
    System.out.println("9. Best Animated Short Film");
    System.out.println("10. Best Live Action Short Film");
    System.out.println("11. Best Film Editing");
    System.out.println("12. Best Original Score");
    System.out.println("13. Best Original Song");
    System.out.println("14. Best Supporting Actor");
    System.out.println("15. Best Supporting Actress");
    System.out.println("16. Best Visual Effects");
    System.out.println("17. Best Original Screenplay");
    System.out.println("18. Best Documentary Short Subject");
    System.out.println("19. Best Documentary Feature Film");
    System.out.println("20. Best International Feature Film");
    System.out.println("21. Best Costume Design");
    System.out.println("22. Best Makeup and Hairstyling");
    System.out.println("23. Best Animated Feature Film");
    System.out.println("24. Best Assistant Director");
    System.out.println("25. Best Director, Comedy Picture");
    System.out.println("26. Best Director, Dramatic Picture");
    System.out.println("27. Best Dance Direction");
    System.out.println("28. Best Original Musical or Comedy Score");
    System.out.println("29. Best Original Story");
    System.out.println("30. Best Short Subject - 1 Reel");
    System.out.println("31. Best Short Subject - 2 Reel");
    System.out.println("32. Best Short Subject - Color");
    System.out.println("33. Best Short Subject - Comedy");
    System.out.println("34. Best Short Subject - Novelty");
    System.out.println("35. Best Sound Editing");
    System.out.println("36. Best Title Writing");
    System.out.println("37. Best Unique and Artistic Production");
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

      Element root = connectToURL("https://en.wikipedia.org/wiki/" + yearToOscarNum(year) + "_Academy_Awards");

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

      // Select Award
      int award = 0;
      while (true) {
        System.out.println("Please type the number of the award you want to search for.");
        printAwardNames();
        try {
          award = scanner.nextInt();
          if (award < 1 || award > 37) {
            System.out.println("Invalid choice!");
          } else {
            break;
          }
        } catch (Exception e) {
          System.out.println("Invalid input!");
          scanner.nextLine(); // clear the input buffer
        }
      }

      List<String> awardNameList = getAwardName();
      String awardName = awardNameList.get(award - 1);

      Element root = connectToURL("https://en.wikipedia.org/wiki/" + yearToOscarNum(year) + "_Academy_Awards");

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

      // For each element inside the Table Context check if the award name matches
      boolean foundMatch = false;
      for (Element element : tableContent) {
        Elements boldElements = element.select("b");
        if (boldElements.get(0).text().equals(awardName)) {
          System.out.println(boldElements.get(1).select("i").text());
          foundMatch = true;
          break;
        }
      }

      if (foundMatch == false) {
        System.out.println("No winner found!");
      }

    } else if (choice == 5) {
      // Select Award
      int award = 0;
      while (true) {
        System.out.println("Please type the number of the award you want to search for.");
        printAwardNames();
        try {
          award = scanner.nextInt();
          if (award < 1 || award > 37) {
            System.out.println("Invalid choice!");
          } else {
            break;
          }
        } catch (Exception e) {
          System.out.println("Invalid input!");
          scanner.nextLine(); // clear the input buffer
        }
      }

      List<String> awardNameList = getAwardName();
      String awardName = awardNameList.get(award - 1);

      Element root = connectToURL("https://en.wikipedia.org/wiki/" + yearToOscarNum(2022) + "_Academy_Awards");

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

      // For each element inside the Table Context check if the award name matches
      boolean foundMatch = false;
      for (Element element : tableContent) {
        Elements boldElements = element.select("b");
        if (boldElements.get(0).text().equals(awardName)) {
          String moviePageLink = boldElements.get(1).select("i").select("a").attr("href");
          Element moviePage = connectToURL("https://en.wikipedia.org" + moviePageLink);

          // Get the first table element
          Element infoTable = moviePage.select("table").first();
          List<List<Element>> infoTableContent = getTableContent(infoTable);
          for (List<Element> infoTableElement : infoTableContent) {
            if (infoTableElement.get(0).text().contains("Budget")) {
              System.out.println("Budget: " + removeBrackets(infoTableElement.get(1).text()));
            }
            if (infoTableElement.get(0).text().contains("Box office")) {
              System.out.println("Box office: " + removeBrackets(infoTableElement.get(1).text()));
            }
          }
          foundMatch = true;
          break;
        }
      }

      if (foundMatch == false) {
        System.out.println("No winner found!");
      }

    } else if (choice == 6) {
      // Select Award
      int award = 0;
      while (true) {
        System.out.println("Please type the number of the award you want to search for.");
        System.out.println("1. Best Director");
        System.out.println("2. Best Actor");
        System.out.println("3. Best Actress");
        System.out.println("4. Best Supporting Actor");
        System.out.println("5. Best Supporting Actress");
        try {
          award = scanner.nextInt();
          if (award < 1 || award > 5) {
            System.out.println("Invalid choice!");
          } else {
            break;
          }
        } catch (Exception e) {
          System.out.println("Invalid input!");
          scanner.nextLine(); // clear the input buffer
        }
      }

      Element root;
      if (award == 1) {
        root = connectToURL("https://en.wikipedia.org/wiki/Academy_Award_for_Best_Director");
      } else if (award == 2) {
        root = connectToURL("https://en.wikipedia.org/wiki/Academy_Award_for_Best_Actor");
      } else if (award == 3) {
        root = connectToURL("https://en.wikipedia.org/wiki/Academy_Award_for_Best_Actress");
      } else if (award == 4) {
        root = connectToURL("https://en.wikipedia.org/wiki/Academy_Award_for_Best_Supporting_Actor");
      } else {
        root = connectToURL("https://en.wikipedia.org/wiki/Academy_Award_for_Best_Supporting_Actress");
      }

      // Find Winners and Nominees H2
      List<String> headerKeys = Arrays.asList("winners", "and", "nominees");
      Element winNomHeader = findElementContaining(headerKeys, root, "h2");

      // Get the tables under the winners and nominees header
      List<Element> h2Content = geth2Content(winNomHeader);
      Map<String, String> linkDict = new HashMap<>();
      for (Element element : h2Content) {
        if (findElementWith("", element, "table") != null) {
          List<List<Element>> tableContent = getTableContent(element);
          if (tableContent.get(0).get(0).text().contains("Year")) {
            for (int i = 1; i < tableContent.size(); i++) {
              Element interestingText;
              if (tableContent.get(i).size() == 5 || (tableContent.get(i).size() == 4 && award == 1)) {
                interestingText = tableContent.get(i).get(1);
              } else {
                interestingText = tableContent.get(i).get(0);
              }
              Elements interestingElements = interestingText.select("a");
              for (Element linkElement : interestingElements) {
                String link = linkElement.attr("href");
                String text = linkElement.text();
                linkDict.putIfAbsent(text, link);
              }
            }
          }
        }
      }

      Map<String, Integer> freqMap = new HashMap<>();
      for (String key : linkDict.keySet()) {
        try {
          String link = URLDecoder.decode(linkDict.get(key), "UTF-8");
          Element personPage = connectToURL("https://en.wikipedia.org" + link);
          // Get the first table element
          Element infoTable = personPage.select("table").first();
          List<List<Element>> infoTableContent = getTableContent(infoTable);
          Set<String> eduSet = new HashSet<>();
          for (List<Element> infoTableElement : infoTableContent) {
            if (infoTableElement.get(0).text().contains("Education")) {
              Elements schools = infoTableElement.get(1).select("a");
              for (Element school : schools) {
                if (school.text().length() > 3) {
                  eduSet.add(removeParentheses(school.text()));
                }

              }
            }
            if (infoTableElement.get(0).text().contains("Alma mater")) {
              Elements schools = infoTableElement.get(1).select("a");
              for (Element school : schools) {
                if (school.text().length() > 3) {
                  eduSet.add(removeParentheses(school.text()));
                }
              }
            }
          }
          List<String> eduList = new ArrayList<>(eduSet);
          for (String school : eduList) {
            if (freqMap.containsKey(school)) {
              int count = freqMap.get(school);
              freqMap.put(school, count + 1);
            } else {
              freqMap.put(school, 1);
            }
          }
        } catch (Exception e) {
          System.out.println("Error: " + e);
        }
      }

      // Sort the dictionary by values
      List<Map.Entry<String, Integer>> entries = new ArrayList<>(freqMap.entrySet());
      Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
        @Override
        public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
          return e2.getValue().compareTo(e1.getValue());
        }
      });
      // Create a list of keys in descending order
      List<String> keys = new ArrayList<>();
      for (Map.Entry<String, Integer> entry : entries) {
        keys.add(entry.getKey());
      }
      // Print the list of keys
      // for (String key : keys) {
      // System.out.println(key + ": " + freqMap.get(key));
      // }
      System.out
          .println("The most common school is " + keys.get(0) + " with " + freqMap.get(keys.get(0)) + " nominations.");

    } else if (choice == 7) {

    } else if (choice == 8) {

    }
  }
}
