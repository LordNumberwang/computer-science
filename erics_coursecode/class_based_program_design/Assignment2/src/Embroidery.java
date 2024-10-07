import tester.Tester;

interface IMotif {
  double calcDiff();
  double countHelp();
  String infoHelper();
}
interface ILoMotif{
  double sumDiff();
  double motifCount(); //excludes group motifs themselves
  String concatInfo(String info);
}

class MtMotif implements ILoMotif {
  public double sumDiff() {
    return 0.0;
  }
  public double motifCount() {
    return 0.0;
  }
  public String concatInfo(String info) {
    return info;
  }
}
class ConsLoMotif implements ILoMotif {
  IMotif first;
  ILoMotif rest;
  
  ConsLoMotif(IMotif motif, ILoMotif rest) {
    this.first = motif;
    this.rest = rest;
  }
  public double sumDiff() {
    return this.first.calcDiff() + this.rest.sumDiff();
  }
  public double motifCount() {
    return this.first.countHelp() + this.rest.motifCount();
  }
  public String concatInfo(String info) {
    if (info == "") {
      return this.rest.concatInfo(this.first.infoHelper());
    } else {
      return this.rest.concatInfo(info + ", " + this.first.infoHelper());
    }
  }
}

class EmbroideryPiece {
  String name;
  IMotif motif;
  
  EmbroideryPiece(String name, IMotif motif) {
    this.name = name;
    this.motif = motif;
  }
  
  double averageDifficulty() {
    return (this.motif.calcDiff() / this.motif.countHelp());
  }
  
  String embroideryInfo() {
    return this.name + ": " + this.motif.infoHelper() + ".";
  }
}

class CrossStitchMotif implements IMotif {
  String description;
  double difficulty;

  CrossStitchMotif(String desc, double diff) {
    this.description = desc;
    this.difficulty = diff;
  }
  
  public double calcDiff() {
    return this.difficulty;
  }
  
  public double countHelp() {
    return 1.0;
  }
  
  public String infoHelper() {
    return this.description + " (cross stitch)";
  }
}

class ChainStitchMotif implements IMotif {
  String description;
  double difficulty;

  ChainStitchMotif(String desc, double diff) {
    this.description = desc;
    this.difficulty = diff;
  }

  public double calcDiff() {
    return this.difficulty;
  }

  public double countHelp() {
    return 1.0;
  }

  public String infoHelper() {
    return this.description + " (chain stitch)";
  }
}

class GroupMotif implements IMotif {
  String description;
  ILoMotif motifs;
  
  GroupMotif(String desc, ILoMotif motifs) {
    this.description = desc;
    this.motifs = motifs;
  }

  public double calcDiff() {
    return this.motifs.sumDiff();
  }
  
  public double countHelp() {
    return this.motifs.motifCount();
  }
  
  public String infoHelper() {
    return motifs.concatInfo("");
  }
}

class ExamplesEmbroidery {
  IMotif bird = new CrossStitchMotif("bird",4.5);
  IMotif tree = new ChainStitchMotif("tree",3.0);
  IMotif rose = new CrossStitchMotif("rose",5.0);
  IMotif poppy = new ChainStitchMotif("poppy",4.75);
  IMotif daisy = new CrossStitchMotif("daisy",3.2);
  ILoMotif flowermotifs = 
      new ConsLoMotif(rose,
          new ConsLoMotif(poppy, 
              new ConsLoMotif(daisy, new MtMotif())));
  IMotif flowers = new GroupMotif("flowers", flowermotifs);
  ILoMotif naturemotifs = 
      new ConsLoMotif(bird,
          new ConsLoMotif(tree,
              new ConsLoMotif(flowers, new MtMotif())));
  IMotif nature = new GroupMotif("nature", naturemotifs);
  EmbroideryPiece pillowCover = new EmbroideryPiece("Pillow Cover", nature);
  
  boolean testCalcDiff(Tester t) {
    return t.checkInexact(rose.calcDiff(), 5.0, 0.05) && 
        t.checkInexact(flowers.calcDiff(), 12.95, 0.05) &&
        t.checkInexact(nature.calcDiff(), 20.45, 0.05);
  }
  
  boolean testCounters(Tester t) {
    return t.checkInexact(rose.countHelp(), 1.0, 0.05) && 
        t.checkInexact(flowers.countHelp(), 3.0, 0.05) &&
        t.checkInexact(nature.countHelp(), 5.0, 0.05) &&
        t.checkInexact(flowermotifs.motifCount(), 3.0, 0.05) &&
        t.checkInexact(naturemotifs.motifCount(), 5.0, 0.05);
  }

  boolean testAverageDifficulty(Tester t) {
    return t.checkInexact(pillowCover.averageDifficulty(),4.09,0.005);
  }
  
  boolean testEmbroideryInfo(Tester t) {
    return t.checkExpect(pillowCover.embroideryInfo(),
        "Pillow Cover: bird (cross stitch), tree (chain stitch), rose (cross stitch), poppy (chain stitch), daisy (cross stitch).");
  }
}