interface IMenuItem {
  //Empty
}

class Soup implements IMenuItem {
  String name;
  int price;
  boolean isVegetarian;

  Soup(String name, int price, boolean isVegetarian) {
    this.name = name;
    this.price = price;
    this.isVegetarian = isVegetarian;
  }
}

class Salad implements IMenuItem {
  String name;
  int price;

  boolean isVegetarian;
  String dressing;
  
  Salad (String name, int price, boolean isVegetarian) {
    this.name = name;
    this.price = price;
    this.isVegetarian = isVegetarian;
  }
}

class Sandwich implements IMenuItem {
  String name;
  int price;
  String bread;
  String filling1;
  String filling2;
  
  Sandwich (String name, int price, String bread, String filling1, String filling2) {
    this.name = name;
    this.price = price;
    this.bread = bread;
    this.filling1 = filling1;
    this.filling2 = filling2;
  }
}

class ExamplesMenuItem {
  Soup borscht = new Soup("Borscht", 750, true);
  Salad caesar = new Salad("Caesar", 1200, false);
  Sandwich pbnj = new Sandwich("PB&J", 900, "Sourdough", "Peanut Butter", "Jelly");
}