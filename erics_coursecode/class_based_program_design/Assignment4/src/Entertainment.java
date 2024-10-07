import tester.*;

interface IEntertainment {
    //compute the total price of this Entertainment
    double totalPrice();
    //computes the minutes of entertainment of this IEntertainment
    int duration();
    //produce a String that shows the name and price of this IEntertainment
    String format();
    //is this IEntertainment the same as that one?
    boolean sameEntertainment(IEntertainment that);
    boolean sameMagazine(Magazine that);
    boolean samePodcast(Podcast that);
    boolean sameTVSeries(TVSeries that);
    boolean isMagazine();
    boolean isPodcast();
    boolean isTVSeries();
}

class Magazine implements IEntertainment {
    String name;
    double price; //represents price per issue
    String genre;
    int pages;
    int installments; //number of issues per year
    
    Magazine(String name, double price, String genre, int pages, int installments) {
        this.name = name;
        this.price = price;
        this.genre = genre;
        this.pages = pages;
        this.installments = installments;
    }
    
    //computes the price of a yearly subscription to this Magazine
    public double totalPrice() {
        return this.price * this.installments;
    }
    
    //computes the minutes of entertainment of this Magazine, (includes all installments)
    //We assume a magazine provides 5 minutes of entertainment per page and 
    public int duration() {
        return 5*pages*installments;
    }
    
    //is this Magazine the same as that IEntertainment?
    public boolean sameEntertainment(IEntertainment that) {
      if (that.isMagazine()) {
        return that.sameMagazine(this);
      } else {
        return false;
      }
    }
    public boolean isMagazine() { return true; }
    public boolean isPodcast() { return false; }
    public boolean isTVSeries() {return false; }
    public boolean sameMagazine(Magazine that) { 
      return ((this.name == that.name) && 
          (this.price - that.price < 0.01) && 
          (this.genre == that.genre) &&
          (this.pages == that.pages) &&
          (this.installments == that.installments));
    }
    public boolean samePodcast(Podcast that) { return false; }
    public boolean sameTVSeries(TVSeries that) { return false; }    
    //produce a String that shows the name and price of this Magazine
    public String format() {
        return this.name + ", " + "$" + this.price + ".";
    }
}

class TVSeries implements IEntertainment {
    String name;
    double price; //represents price per episode
    int installments; //number of episodes of this series
    String corporation;
    
    TVSeries(String name, double price, int installments, String corporation) {
        this.name = name;
        this.price = price;
        this.installments = installments;
        this.corporation = corporation;
    }
    
    //computes the price of a yearly subscription to this TVSeries
    public double totalPrice() {
        return this.price * this.installments;
    }
    
    //computes the minutes of entertainment of this TVSeries
    //TV series and podcasts provide 50 minutes of entertainment per episode.
    public int duration() {
        return 50 * this.installments;
    }
    
    //is this TVSeries the same as that IEntertainment?
    public boolean sameEntertainment(IEntertainment that) {
        if (that.isTVSeries()) {
          return that.sameTVSeries(this);
        } else {
          return false;
        }
    }
    public boolean isMagazine() { return false; }
    public boolean isPodcast() { return false; }
    public boolean isTVSeries() {return true; }
    public boolean sameMagazine(Magazine that) { return false; }
    public boolean samePodcast(Podcast that) { return false; }
    public boolean sameTVSeries(TVSeries that) { 
        return ((this.name == that.name) && 
            (this.price - that.price < 0.01) && 
            (this.corporation == that.corporation) &&
            (this.installments == that.installments));
    }
    
    //produce a String that shows the name and price of this Magazine
    public String format() {
        return this.name + ", " + "$" + this.price + ".";
    }
}

class Podcast implements IEntertainment {
    String name;
    double price; //represents price per issue
    int installments; //number of episodes in this Podcast
    
    Podcast(String name, double price, int installments) {
        this.name = name;
        this.price = price;
        this.installments = installments;
    }
    
    //computes the price of a yearly subscription to this Podcast
    public double totalPrice() {
        return this.price * this.installments;
    }
    
    //computes the minutes of entertainment of this Podcast
    public int duration() {
        return 50 * this.installments;
    }
    
    //is this Podcast the same as that IEntertainment?
    public boolean sameEntertainment(IEntertainment that) {
      if (that.isPodcast()) {
        return that.samePodcast(this);
      } else {
        return false;
      }
    }
    public boolean isMagazine() { return false; }
    public boolean isPodcast() { return true; }
    public boolean isTVSeries() {return false; }
    
    //produce a String that shows the name and price of this Podcast
    public String format() {
        return this.name + ", " + "$" + this.price + ".";
    }

    @Override
    public boolean sameMagazine(Magazine that) {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public boolean samePodcast(Podcast that) {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public boolean sameTVSeries(TVSeries that) {
      // TODO Auto-generated method stub
      return false;
    }
}

class ExamplesEntertainment {
    IEntertainment rollingStone = new Magazine("Rolling Stone", 2.55, "Music", 60, 12);
    IEntertainment houseOfCards = new TVSeries("House of Cards", 5.25, 13, "Netflix");
    IEntertainment serial = new Podcast("Serial", 0.0, 8);
    IEntertainment jump = new Magazine("Weekly Jump", 2.01, "Manga", 100, 52);
    IEntertainment archer = new TVSeries("Archer", 4.20, 13, "FX");
    IEntertainment sawbones = new Podcast("Sawbones", 0.0, 6);
    // Make one more example of data for each of the three classes and add more tests for the 
    // totalPrice method (that is already defined) using them.
    
    //testing total price method
    boolean testTotalPrice(Tester t) {
        return t.checkInexact(this.rollingStone.totalPrice(), 2.55*12, .0001) 
        && t.checkInexact(this.houseOfCards.totalPrice(), 5.25*13, .0001)
        && t.checkInexact(this.serial.totalPrice(), 0.0, .0001);
    }
    
    //testing duration method
    boolean testDuration(Tester t) {
        return t.checkExpect(this.rollingStone.duration(), 5*12*60) 
        && t.checkExpect(this.houseOfCards.duration(), 50*13)
        && t.checkExpect(this.serial.duration(), 50*8);
    }
}

