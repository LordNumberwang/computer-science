class BagelRecipe {
  double malt; //in oz / ts
  double water; //in oz / cups
  double flour; //in oz / cups
  double yeast; //in oz / ts
  double salt; //in oz / ts

  //  the weight of the flour should be equal to the weight of the water
  //  the weight of the yeast should be equal the weight of the malt
  //  the weight of the salt + yeast should be 1/20th the weight of the flour
  BagelRecipe(double flour, double yeast, double salt, double malt, double water) {
    if (flour - water > 0.001 ) {
      throw new IllegalArgumentException("Invalid water quantity: " + Double.toString(water));
    }
    this.flour = flour;
    this.water = water;

    if (malt - yeast > 0.001) {
      throw new IllegalArgumentException("Invalid malt quantity: " + Double.toString(malt) + ", expected - " + Double.toString(yeast));
    }
    this.yeast = yeast;
    this.malt = malt;
    if ((salt+yeast)-(flour / 20) > 0.001) {
      throw new IllegalArgumentException("Invalid salt quantity: " + Double.toString(salt) + ", expected - " + Double.toString((flour/20)-yeast));
    }
    this.salt = salt;
  }

  BagelRecipe(double flour, double yeast) {
    this(flour, yeast, (flour / 20) - yeast, yeast,flour);
  }

  BagelRecipe(double flourCups, double yeastTs, double saltTs) {
    this(flourCups * 4.25, 
        (yeastTs/48) * 5,
        (saltTs / 4.8),
        (yeastTs/48) * 5,
        flourCups * 4.25);
  }

  public boolean sameRecipe(BagelRecipe other) {
    return (this.flour - other.flour <= 0.001) &&
        (this.yeast - other.yeast <= 0.001) &&
        (this.salt - other.salt <= 0.001) &&
        (this.malt - other.malt <= 0.001) && 
        (this.water - other.water <= 0.001);
  }
}